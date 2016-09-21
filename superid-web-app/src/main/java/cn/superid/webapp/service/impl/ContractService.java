package cn.superid.webapp.service.impl;

import cn.superid.utils.StringUtil;
import cn.superid.webapp.component.SortByState;
import cn.superid.webapp.controller.forms.*;
import cn.superid.webapp.enums.ContractRoleKind;
import cn.superid.webapp.model.*;
import cn.superid.webapp.service.IContractService;
import cn.superid.webapp.service.IRoleService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.service.forms.*;
import cn.superid.webapp.utils.TimeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Created by jizhenya on 16/9/18.
 */
@Service
public class ContractService implements IContractService {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;

    @Override
    public int confirm(long allianceId,long operationRoleId, long contractId) {
        //第一步,检查
        ContractEntity contract = ContractEntity.dao.findById(contractId);
        RoleEntity role = RoleEntity.dao.findById(operationRoleId,allianceId);
        ContractRoleEntity confirmer = ContractRoleEntity.dao.partitionId(contractId).eq("role_id",operationRoleId).selectOne();
        if(contract == null | role == null | confirmer == null | confirmer.getConfirmed() != 0){
            return 0;
        }

        //第二步,检查所有人的标志位从而得知是否最后一个签字的
        List<ContractRoleEntity> contractRoleList = ContractRoleEntity.dao.partitionId(contractId).selectList();
        if(contractRoleList == null | contractRoleList.size() == 0){
            return 0;
        }

        boolean isStart = true;
        for(ContractRoleEntity c : contractRoleList){
            if(c.getRoleId() == operationRoleId){
                //改标志位和时间
                c.setConfirmed(1);
                c.setConfirmedTime(TimeUtil.getCurrentSqlTime());
                c.update();

                //TODO:将成员加入讨论组,待讨论组定下来后进行编写

            }else if(c.getConfirmed() == 0){
                isStart = false ;
            }
        }

        //根据isStart的值知道是否全部同意了。
        if(isStart){
            //如果全部同意了,合同置为发起成功
            contract.setState(2);
            contract.update();
            return 2;
        }

        return 1;

    }

    @Override
    public boolean refuseConfirm(long operationRoleId, long contractId) {
        ContractEntity contract = ContractEntity.dao.findById(contractId);
        ContractRoleEntity refuser = ContractRoleEntity.dao.partitionId(contractId).eq("role_id",operationRoleId).selectOne();
        if(contract == null | refuser == null | refuser.getConfirmed() != 0){
            return false;
        }

        //取出要删的条目,包括讨论组,contractRole,contract本身
        List<ContractRoleEntity> contractRoleList = ContractRoleEntity.dao.partitionId(contractId).selectList();

        //执行删除
        contract.delete();
        for(ContractRoleEntity c : contractRoleList){
            c.delete();
        }

        //TODO:删除讨论组

        return true;
    }

    @Override
    public SignForm sign(long allianceId, long operationRoleId, long contractId) {
        ContractEntity contract = ContractEntity.dao.findById(contractId);
        ContractRoleEntity signer = ContractRoleEntity.dao.partitionId(contractId).eq("role_id",operationRoleId).selectOne();
        RoleEntity role = RoleEntity.dao.findById(operationRoleId,allianceId);
        if(contract == null | contract.getState() != 2 | signer == null | signer.getSignature() != 0 | role ==null){
            return new SignForm(0,null,null);
        }

        //第一步，将该角色确认位置为1，并且记录
        signer.setSignature(1);
        signer.setSignatureTime(TimeUtil.getCurrentSqlTime());
        signer.update();

        contract.setIsBlock(1);
        contract.update();

        //第二步，检测是否所有方都已经确认
        List<Integer> signList = ContractRoleEntity.dao.partitionId(contractId).groupBy("alliance_id").sumList("signature");
        boolean isAllSign = true;
        for(Integer i : signList){
            if(i < 1){
                isAllSign = false;
            }
        }

        //第三步，增加log
        recorcSimpleLog(contractId,roleService.getNameByRoleId(operationRoleId)+ "已确认合同并签字");

        if(isAllSign){
            //如果所有人已确认
            contract.setState(3);
            contract.setSignatureTime(TimeUtil.getCurrentSqlTime());
            contract.update();
            return new SignForm(2,roleService.getNameByRoleId(operationRoleId),signer.getSignatureTime().getTime());
        }

        return new SignForm(1,roleService.getNameByRoleId(operationRoleId),signer.getSignatureTime().getTime());
    }

    @Override
    public boolean undoSign(long allianceId, long operationRoleId, long contractId) {
        ContractEntity contract = ContractEntity.dao.findById(contractId);
        ContractRoleEntity signer = ContractRoleEntity.dao.partitionId(contractId).eq("role_id",operationRoleId).selectOne();
        RoleEntity role = RoleEntity.dao.findById(operationRoleId,allianceId);
        //如果不存在或者状态已经生效，则无法撤销
        if(contract == null | contract.getState() == 3 | signer == null | signer.getSignature() != 1 | role == null){
            return false;
        }
        //将标志位置为初始状态
        signer.setSignature(0);
        signer.setSignatureTime(null);
        signer.update();

        //第二步,检查是不是所有人都未同意,是则回到编辑态
        int isAllUnsign = ContractRoleEntity.dao.partitionId(contractId).sum("signature");
        if( isAllUnsign == 0 ){
            contract.setIsBlock(0);
            contract.update();
        }

        //第三步，增加log
        recorcSimpleLog(contractId,roleService.getNameByRoleId(operationRoleId)+ "撤销确认签字");

        return true;
    }

    @Override
    public boolean terminate(long allianceId, long operationRoleId, long contractId) {
        ContractEntity contract = ContractEntity.dao.findById(contractId);
        RoleEntity role = RoleEntity.dao.findById(operationRoleId,allianceId);
        List<ContractRoleEntity> contractRoleList = ContractRoleEntity.dao.partitionId(contractId).selectList();

        if(contract == null | contract.getState() != 3 | contractRoleList == null | role == null){
            return false;
        }

        //把合同置为正在终止
        contract.setState(4);
        contract.update();
        //把所有人的终止标志位置为0
        for(ContractRoleEntity c : contractRoleList){
            if(c.getRoleId() == operationRoleId){
                c.setTerminate(1);
            }else{
                c.setTerminate(0);
            }
            c.update();
        }

        //第三步，增加log
        recorcSimpleLog(contractId,roleService.getNameByRoleId(operationRoleId)+ "发起终止合同");

        return true;
    }

    @Override
    public SignForm agreeTerminate(long allianceId, long operationRoleId, long contractId) {
        //第一步,检查
        ContractEntity contract = ContractEntity.dao.findById(contractId);
        RoleEntity role = RoleEntity.dao.findById(operationRoleId,allianceId);
        ContractRoleEntity terminater = ContractRoleEntity.dao.partitionId(contractId).eq("role_id",operationRoleId).selectOne();
        if(contract == null | contract.getState()!=4 | role == null | terminater == null | terminater.getTerminate() != 0){
            return new SignForm(0,null,null);
        }

        terminater.setTerminate(1);
        terminater.setTerminateTime(TimeUtil.getCurrentSqlTime());
        terminater.update();

        //第二步，检查是不是最后一个同意的
        List<Integer> terminateList = ContractRoleEntity.dao.partitionId(contractId).groupBy("alliance_id").sumList("terminate");
        boolean isAllTerminated = true;
        for(Integer i : terminateList){
            if(i != 1){
                isAllTerminated = false;
            }
        }

        //第三步，增加log
        recorcSimpleLog(contractId,roleService.getNameByRoleId(operationRoleId)+ "同意终止合同");

        if(isAllTerminated){
            //如果所有人已同意
            contract.setState(0);
            contract.setTerminateTime(TimeUtil.getCurrentSqlTime());
            contract.update();
            return new SignForm(2,roleService.getNameByRoleId(operationRoleId),terminater.getTerminateTime().getTime());
        }

        return new SignForm(1,roleService.getNameByRoleId(operationRoleId),terminater.getTerminateTime().getTime());
    }

    @Override
    public SignForm refuseTerminate(long allianceId, long operationRoleId, long contractId) {
        //第一步,检查
        ContractEntity contract = ContractEntity.dao.findById(contractId);
        RoleEntity role = RoleEntity.dao.findById(operationRoleId,allianceId);
        ContractRoleEntity terminater = ContractRoleEntity.dao.partitionId(contractId).eq("role_id",operationRoleId).selectOne();
        if(contract == null | contract.getState()!=4 | role == null | terminater == null | terminater.getTerminate() != 0){
            return new SignForm(0,null,null);
        }

        //第二步,改变标志位
        terminater.setTerminate(2);
        terminater.setTerminateTime(TimeUtil.getCurrentSqlTime());
        terminater.update();
        contract.setState(3);
        contract.update();

        //第三步，增加log
        recorcSimpleLog(contractId,roleService.getNameByRoleId(operationRoleId)+ "拒绝终止合同");

        return new SignForm(1,roleService.getNameByRoleId(operationRoleId),terminater.getTerminateTime().getTime());
    }

    @Override
    public SignForm signAddition(long allianceId, long operationRoleId, long contractId, long additionId) {
        ContractEntity contract = ContractEntity.dao.findById(contractId);
        RoleEntity role = RoleEntity.dao.findById(operationRoleId,allianceId);
        UserEntity user = userService.getCurrentUser();
        ContractRoleEntity signer = ContractRoleEntity.dao.partitionId(contractId).eq("role_id",operationRoleId).selectOne();
        AdditionEntity addition = AdditionEntity.dao.findById(additionId,contractId);

        if(contract == null | role == null | signer == null | signer.getAddition() != 0 | addition == null | addition.getState() != 1 | user ==null){
            return new SignForm(0,null,null);
        }

        //第一步，将该角色确认位置为1，并且记录下签字信息
        signer.setAddition(1);
        signer.setAdditionTime(TimeUtil.getCurrentSqlTime());
        signer.update();

        String signedRole = addition.getSignedRole();
        if(signedRole == null || signedRole.equals("[]")){
            addition.setSignedRole("[{\"roleName\":\""+role.getTitle()+"\",\"kind\":\""+signer.getKind()+"\",\"signedTime\":\""+TimeUtil.getCurrentSqlTime()+"\",\"userName\":\""+user.getUsername()+"\",\"roleId\":\""+role.getId()+"\"}]");
        }else{
            HashMap<String,String> hash = new HashMap<>();
            hash.put("roleName",role.getTitle());
            hash.put("kind",signer.getKind()+"");
            hash.put("signedTime",TimeUtil.getCurrentSqlTime().toString());
            hash.put("userName",user.getUsername());
            hash.put("roleId",role.getId()+"");
            JSONArray info = JSONArray.parseArray(signedRole);
            info.add(0,hash);
            addition.setSignedRole(info.toString());
        }
        addition.setIsBlock(1);
        addition.update();

        //第二步，检测是否所有方都已经确认
        List<Integer> additionList = ContractRoleEntity.dao.partitionId(contractId).groupBy("alliance_id").sumList("addition");
        boolean isAllSigned = true;
        for(Integer i : additionList){
            if(i != 1){
                isAllSigned = false;
            }
        }

        //第三步，增加log
        recorcSimpleLog(contractId,roleService.getNameByRoleId(operationRoleId)+ "已确认附加条款并签字");

        if(isAllSigned){
            //如果所有人已确认
            addition.setState(2);
            addition.setConfirmedTime(TimeUtil.getCurrentSqlTime());
            addition.update();
            return new SignForm(2,roleService.getNameByRoleId(operationRoleId),signer.getAdditionTime().getTime());
        }

        return new SignForm(1,roleService.getNameByRoleId(operationRoleId),signer.getAdditionTime().getTime());
    }

    @Override
    public boolean undoAddition(long allianceId, long operationRoleId, long contractId, long additionId) {
        ContractEntity contract = ContractEntity.dao.findById(contractId);
        RoleEntity role = RoleEntity.dao.findById(operationRoleId, allianceId);
        ContractRoleEntity signer = ContractRoleEntity.dao.partitionId(contractId).eq("role_id", operationRoleId).selectOne();
        AdditionEntity addition = AdditionEntity.dao.findById(additionId, contractId);
        if (contract == null | role == null | signer == null | signer.getAddition() == 0 | addition == null | addition.getState() != 1) {
            return false;
        }
        signer.setAddition(0);
        signer.setAdditionTime(null);
        signer.update();

        //第二步,检查是不是所有人都未同意,是则回到编辑态
        int isAllUnsign = ContractRoleEntity.dao.partitionId(contractId).sum("addition");
        if (isAllUnsign == 0) {
            addition.setIsBlock(0);
        }

        //这一步用来把当时的签字信息从addition json记录中抹去
        JSONArray info = JSONArray.parseArray(addition.getSignedRole());
        int location = 0;
        boolean isExist = false;
        for (int i = 0; i < info.size(); i++) {
            JSONObject j = info.getJSONObject(i);
            if (((Long) j.get("roleId") == role.getId())) {
                isExist = true;
                location = i;
                break;
            }
        }
        if (isExist) {
            info.remove(location);
        }
        addition.setSignedRole(info.toString());
        addition.update();

        //第三步，增加log
        recorcSimpleLog(contractId, roleService.getNameByRoleId(operationRoleId) + "撤销附加条款签字");

        return true;
    }

    @Override
    public boolean addTemplate(long allianceId,long operationRoleId, long affairId, String title, String content, String thumbContent) {
        AffairEntity affair = AffairEntity.dao.findById(affairId,allianceId);
        RoleEntity role = RoleEntity.dao.findById(operationRoleId, allianceId);
        if(affair == null | role == null ){
            return false;
        }
        ContractTemplateEntity contractTemplate = new ContractTemplateEntity();
        contractTemplate.setAffairId(affairId);
        contractTemplate.setAllianceId(affair.getAllianceId());
        contractTemplate.setContent(content);
        contractTemplate.setTitle(title);
        contractTemplate.setThumbContent(thumbContent);
        contractTemplate.save();
        return true;
    }

    @Override
    public boolean deleteTemplate(long affairId, long operationRoleId, long id) {
        ContractTemplateEntity contractTemplate = ContractTemplateEntity.dao.findById(id,affairId);
        if(contractTemplate==null) return false;
        contractTemplate.delete();
        return true;
    }

    @Override
    public List<ContractTemplateForm> listTemplate(long operationRoleId , long allianceId) {
        RoleEntity role = RoleEntity.dao.findById(operationRoleId, allianceId);
        if(role == null ){
            return null;
        }
        List<Object> list = ContractTemplateEntity.dao.join(AffairEntity.class).on("affair_id","id").eq("alliance_id",allianceId).selectListByJoin(ContractTemplateForm.class,"a.id","b.name as affair_name","a.title","a.thumb_content");
        if(list == null){
            return null;
        }
        List<ContractTemplateForm> result = new ArrayList<>();
        for(Object o:list){
            result.add((ContractTemplateForm)o);
        }

        return result;
    }

    @Override
    public String getContentOfTemplate(long id, long affairId) {
        ContractTemplateEntity contractTemplate = ContractTemplateEntity.dao.findById(id,affairId);
        if(contractTemplate==null) return null;
        return contractTemplate.getContent();
    }

    @Override
    public boolean editTemplate(long operationRoleId, long id, long affairId, String title, String content, String thumbContent) {
        ContractTemplateEntity contractTemplate = ContractTemplateEntity.dao.findById(id,affairId);
        if(contractTemplate==null) return false;
        if(StringUtil.notEmpty(title)){
            contractTemplate.setTitle(title);
        }
        if(StringUtil.notEmpty(content)){
            contractTemplate.setContent(content);
        }

        if(StringUtil.notEmpty(thumbContent)){
            contractTemplate.setThumbContent(thumbContent);
        }
        contractTemplate.update();
        return true;

    }

    @Override
    public boolean addRole(long operationRoleId, long roleId, long contractId, long allianceId) {

        ContractRoleEntity operator = ContractRoleEntity.dao.partitionId(contractId).eq("role_id",operationRoleId).selectOne();

        if(operator == null){
            return false;
        }

        ContractRoleEntity newRole = new ContractRoleEntity();
        newRole.setRoleId(roleId);
        newRole.setAllianceId(operator.getAllianceId());
        newRole.setContractId(contractId);
        newRole.setKind(operator.getKind());
        newRole.setConfirmed(0);
        newRole.setSignature(0);
        newRole.setTerminate(0);
        newRole.setAddition(0);
        newRole.save();

        //TODO:把人拉进讨论组

        //第三步，增加log

        recorcSimpleLog(contractId, roleService.getNameByRoleId(operationRoleId) + "邀请了"+roleService.getNameByRoleId(roleId)+"加入合同");


        return true;
    }

    @Override
    public boolean removeRole(long operationRoleId, long roleId, long contractId, long allianceId) {

        ContractRoleEntity operator = ContractRoleEntity.dao.partitionId(contractId).eq("role_id",operationRoleId).selectOne();
        ContractRoleEntity role = ContractRoleEntity.dao.partitionId(contractId).eq("role_id",roleId).selectOne();
        if(operator == null | role == null | !(operator.getAllianceId() == role.getAllianceId())){
            return false;
        }
        role.delete();

        //TODO:把人从讨论组删除

        //第三步，增加log
        recorcSimpleLog(contractId, roleService.getNameByRoleId(operationRoleId) + "将"+roleService.getNameByRoleId(roleId)+"移除出合同");


        return true;
    }

    @Override
    public ContractEntity addContract(String name, long operationRoleId, String roles, long allianceId) {

        //TODO:创建讨论组

        ContractEntity contractEntity = new ContractEntity();
        contractEntity.setTitle(name);
        contractEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        contractEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        contractEntity.setState(1);
        contractEntity.save();

        //以下是成员
        RoleEntity roleEntity = RoleEntity.dao.findById(operationRoleId,allianceId);
        ContractRoleEntity contractRoleEntity = new ContractRoleEntity();
        contractRoleEntity.setAllianceId(roleEntity.getAllianceId());
        contractRoleEntity.setRoleId(operationRoleId);
        contractRoleEntity.setKind(ContractRoleKind.YIFANG.toInt());
        contractRoleEntity.setContractId(contractEntity.getId());
        //全部未签字状态
        contractRoleEntity.setConfirmed(1);
        contractRoleEntity.setConfirmedTime(TimeUtil.getCurrentSqlTime());
        contractRoleEntity.setAddition(0);
        contractRoleEntity.setSignature(0);
        contractRoleEntity.setTerminate(0);

        contractRoleEntity.save();

        //TODO:将成员加入讨论组


        List<String> strs = Arrays.asList(roles.split(","));
        List<Long> roleList = new ArrayList<>();
        for(String s : strs){
            roleList.add(Long.parseLong(s));
        }
        List<Long> allianceKinds = new ArrayList<>();
        allianceKinds.add(contractRoleEntity.getAllianceId());
        int i = 1;
        for(Long roleId : roleList){
            roleEntity = RoleEntity.dao.findById(roleId,allianceId);
            ContractRoleEntity crEntity = new ContractRoleEntity();

            crEntity.setAllianceId(roleEntity.getAllianceId());
            crEntity.setRoleId(roleId);

            boolean isExist = false;
            int index = 0;
            for(int j=0;j<allianceKinds.size();j++){
                if(crEntity.getAllianceId() == allianceKinds.get(j)){
                    isExist = true;
                    index = j;
                    break;
                }
            }
            if(isExist){
                crEntity.setKind(ContractRoleKind.YIFANG.toInt()+index);
            }
            else{
                allianceKinds.add(crEntity.getAllianceId());
                crEntity.setKind(ContractRoleKind.YIFANG.toInt()+i);
                i++;
            }


            //全部未签字状态
            crEntity.setSignature(0);
            crEntity.setAddition(0);
            crEntity.setConfirmed(0);
            crEntity.setConfirmedTime(TimeUtil.getCurrentSqlTime());
            crEntity.setTerminate(0);
            crEntity.setContractId(contractEntity.getId());
            crEntity.save();
        }




        return contractEntity;
    }

    @Override
    public ContractEntity editContract(long contractId, long operationRoleId, String content,long allianceId) {
        ContractEntity contractEntity = ContractEntity.dao.findById(contractId);
        if(contractEntity == null) return null;

        contractEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        contractEntity.setContent(content);
        contractEntity.setState(2);
        contractEntity.update();

        ContractLogEntity contractLogEntity = new ContractLogEntity();
        contractLogEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        contractLogEntity.setContractId(contractId);
        contractLogEntity.setHasDetail(1);
        contractLogEntity.setDetail(content);
        contractLogEntity.setMessage(RoleEntity.dao.findById(operationRoleId,allianceId).getTitle() + "修改了合同内容");
        contractLogEntity.save();
        return contractEntity;
    }

    @Override
    public List<OwnContractResult> checkOwnContracts(long operationRoleId, int state, long allianceId) {
        List<Long> contractIds = getContractList(operationRoleId);

        List<Long> fitContractIds = new ArrayList<>(); //符合传过来的状态的交易id
        List<OwnContractResult> results = new ArrayList<>();

        if(state == -1){
            fitContractIds = contractIds;
        }
        else {
            for(Long contractId : contractIds){
                if(ContractEntity.dao.findById(contractId).getState()==state){
                    fitContractIds.add(contractId);
                }
            }
        }

        for(Long contractId : fitContractIds){
            ContractEntity contractEntity = ContractEntity.dao.findById(contractId);
            OwnContractResult ownContractResult = new OwnContractResult();
            ownContractResult.setContractId(contractId);
            ownContractResult.setContractTitle(contractEntity.getTitle());
            ownContractResult.setState(contractEntity.getState());
            ownContractResult.setCreateTime(contractEntity.getCreateTime());
            ownContractResult.setSignatureTime(contractEntity.getSignatureTime());

            ContractInfo contractInfo = checkContractDetail(operationRoleId,contractId,state,allianceId);
            if(contractInfo == null) return null;

            ownContractResult.setAlliances(contractInfo.getAlliances());
            results.add(ownContractResult);
        }

        Collections.sort(results, new SortByState());

        return results;
    }

    @Override
    public ContractInfo checkContractDetail(long operationRoleId, long contractId, int state, long alliance) {
        ContractInfo contractInfo = new ContractInfo();
        boolean isFind = false;

        //检测是否有权限查看内容
        List<Long> contractIds = getContractList(operationRoleId);
        List<ContractEntity> result = new ArrayList<>();
        for(Long contract : contractIds){
            if(contract.longValue() == contractId) {
                isFind = true;
                break;
            }
        }
        if(!isFind) return null;

        ContractEntity contractEntity = ContractEntity.dao.findById(contractId);
        if(contractEntity == null)  return null;
        contractInfo.setId(contractId);
        contractInfo.setTitle(contractEntity.getTitle());
        contractInfo.setContent(contractEntity.getContent());
        contractInfo.setCreateTime(contractEntity.getCreateTime());
        contractInfo.setSignatureTime(contractEntity.getSignatureTime());
        contractInfo.setState(contractEntity.getState());
        contractInfo.setDgId(contractEntity.getDiscussGroupId());

        RoleEntity roleEntity = RoleEntity.dao.findById(operationRoleId,alliance);
        //alliance部分
        List<Long> allianceIds = getAllianceList(operationRoleId,contractId);
        List<AllianceSigned> allianceSigneds = new ArrayList<>();
        for(Long allianceId : allianceIds){
            boolean isConfirmed = false;
            boolean isSigned = false;
            boolean isTerminate = false;

            AllianceEntity allianceEntity = AllianceEntity.dao.findById(allianceId);

            List<ContractRoleEntity> contractRoleEntities = ContractRoleEntity.dao.partitionId(contractId).eq("alliance_id",allianceId).selectList();

            AllianceSigned allianceSigned = new AllianceSigned();
            allianceSigned.setAllianceName(allianceEntity.getName());
            allianceSigned.setAllianceKind(contractRoleEntities.get(0).getKind());

            if(roleEntity.getAllianceId() == allianceId){
                allianceSigned.setIsInKind(1);
            }
            for(ContractRoleEntity contractRoleEntity : contractRoleEntities){
                allianceSigned.setRoleId(contractRoleEntity.getRoleId());
                allianceSigned.setRoleName(roleService.getNameByRoleId(contractRoleEntity.getRoleId()));
                RoleEntity temp = RoleEntity.dao.findById(contractRoleEntity.getRoleId(),contractRoleEntity.getAllianceId());
                long userId = temp.getUserId();
                //判断是否是本盟
                if(contractRoleEntity.getRoleId() == operationRoleId){
                    allianceSigned.setIsSelf(1);
                }
                //查看全部合同
                if(state == -1){
                    if(contractRoleEntity.getConfirmed()==1){
                        allianceSigned.setConfirmed(1);
                        allianceSigned.setConfirmedTime(contractRoleEntity.getConfirmedTime());
                        isConfirmed = true;
                    }
                    if(contractRoleEntity.getSignature()==1){
                        allianceSigned.setSignature(1);
                        allianceSigned.setSignatureTime(contractRoleEntity.getSignatureTime());
                        isSigned = true;
                    }
                    if(contractRoleEntity.getTerminate()==1){
                        allianceSigned.setTerminate(1);
                        allianceSigned.setTerminateTime(contractRoleEntity.getTerminateTime());
                        isTerminate = true;
                    }
                }
                else{
                    //正在发起的合同
                    if(state == 1){
                        if(contractRoleEntity.getConfirmed()==1){
                            allianceSigned.setConfirmed(1);
                            allianceSigned.setConfirmedTime(contractRoleEntity.getConfirmedTime());
                            isConfirmed = true;
                        }
                    }
                    //等待确认的合同
                    else if(state ==2){
                        if(contractRoleEntity.getSignature()==1){
                            allianceSigned.setSignature(1);
                            allianceSigned.setSignatureTime(contractRoleEntity.getSignatureTime());
                            isSigned = true;
                        }
                    }
                    //已经生效的合同
                    else if(state ==3){
                        if(contractRoleEntity.getTerminate()==1){
                            allianceSigned.setTerminate(1);
                            allianceSigned.setTerminateTime(contractRoleEntity.getTerminateTime());
                            isTerminate = true;
                        }
                    }
                }
                if(isConfirmed||isSigned||isTerminate){
                    break;
                }
            }
            allianceSigneds.add(allianceSigned);
        }
        Collections.sort(allianceSigneds,new SortByAllianceSignedKind());
        contractInfo.setAlliances(JSON.toJSONString(allianceSigneds));

        //changeLogs部分
        List<ContractLogEntity> contractLogEntities = ContractLogEntity.dao.partitionId(contractId).desc("modify_time").selectList();
        List<ChangeLogResult> changeLogResults = new ArrayList<>();
        for(ContractLogEntity contractLogEntity : contractLogEntities){
            ChangeLogResult changeLogResult = new ChangeLogResult();
            changeLogResult.setContractId(contractLogEntity.getContractId());
            changeLogResult.setHasDetail(contractLogEntity.getHasDetail());
            changeLogResult.setId(contractLogEntity.getId());
            changeLogResult.setMessage(contractLogEntity.getMessage());
            changeLogResult.setModifyTime(contractLogEntity.getModifyTime());
            changeLogResults.add(changeLogResult);
        }
        contractInfo.setChangeLogs(JSON.toJSONString(changeLogResults));


        //addition部分
        List<AdditionEntity> additions = AdditionEntity.dao.partitionId(contractId).selectList("id","contract_id","content","signed_role","state");
        List<AdditionResult> additionResults = new ArrayList<>();
        for(AdditionEntity a :additions){
            additionResults.add(new AdditionResult(a.getId(),a.getContractId(),a.getContent(),a.getSignedRole(),a.getState()));
        }

        for(AdditionResult additionResult : additionResults){

            String signedRoleString = additionResult.getSignedRole();
            List<SignedRole> signedRoles = JSON.parseArray(signedRoleString,SignedRole.class);
            for(SignedRole signedRole : signedRoles){
                if(signedRole.getRoleId().equals(operationRoleId)){
                    additionResult.setIsSelf(1);
                    break;
                }
            }
        }

        contractInfo.setAdditions(JSON.toJSONString(additionResults));


        //members部分

        List<Integer> kinds = getKinds(contractId);
        List<ContractMemberResult> contractMemberResults = new ArrayList<>();
        for(int kind : kinds){
            long allianceId = 0;
            List<ContractRoleEntity> contractRoleEntities = ContractRoleEntity.dao.partitionId(contractId).eq("kind",kind).selectList();
            ContractMemberResult contractMemberResult = new ContractMemberResult();
            List<String> userAndRoles = new ArrayList<>();
            for(ContractRoleEntity contractRoleEntity : contractRoleEntities){

                userAndRoles.add(roleService.getNameByRoleId(contractRoleEntity.getRoleId()));
                allianceId = contractRoleEntity.getAllianceId();
            }
            contractMemberResult.setName(JSON.toJSONString(userAndRoles));
            contractMemberResult.setKind(kind);
            if(roleEntity.getAllianceId() == allianceId){
                contractMemberResult.setIsInKind(1);
            }
            contractMemberResults.add(contractMemberResult);
        }
        Collections.sort(contractMemberResults,new SortByMemberKind());
        contractInfo.setMembers(JSON.toJSONString(contractMemberResults));

        return contractInfo;
    }

    @Override
    public String checkHistoryContent(long operationRoleId, long changeLogId, long contractId, long alliance) {
        List<Long> contractIds = getContractList(operationRoleId);
        boolean isFind = false;
        List<ContractEntity> result = new ArrayList<>();
        for(Long contract : contractIds){
            if(contract == contractId) {
                isFind = true;
                break;
            }
        }
        if(!isFind) return null;

        ContractLogEntity contractLogEntity = ContractLogEntity.dao.findById(changeLogId,contractId);

        return contractLogEntity.getDetail();
    }

    @Override
    public AdditionEntity addAddition(long operationRoleId, long contractId, String additionContent) {
        AdditionEntity additionEntity = new AdditionEntity();
        additionEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        additionEntity.setContractId(contractId);
        additionEntity.setContent(additionContent);
        //这里不知道要不要设置
        additionEntity.setSignedRole("[]");
        additionEntity.setState(1);
        additionEntity.save();

        ContractLogEntity contractLogEntity = new ContractLogEntity();
        contractLogEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        contractLogEntity.setContractId(contractId);
        contractLogEntity.setHasDetail(1);
        contractLogEntity.setDetail(additionContent);
        contractLogEntity.setMessage(roleService.getNameByRoleId(operationRoleId) + "发起了补充条款");
        contractLogEntity.save();

        //第二步,把所有role的addition位置0
        List<ContractRoleEntity> roleList = ContractRoleEntity.dao.partitionId(contractId).selectList();
        for(ContractRoleEntity c : roleList){
            c.setAddition(0);
            c.setAdditionTime(null);
            c.update();
        }

        return additionEntity;
    }

    @Override
    public List<KindMember> getMemberByKind(long operationRoleId, long contractId, long allianceId) {
        List<KindMember> kindMembers = new ArrayList<>();
        boolean isFind = false;
        List<ContractRoleEntity> contractRoleEntities = ContractRoleEntity.dao.partitionId(contractId).eq("alliance_id",allianceId).selectList();
        for(ContractRoleEntity contractRoleEntity : contractRoleEntities){
            if(contractRoleEntity.getRoleId() == operationRoleId){
                isFind = true;
            }
            KindMember kindMember = new KindMember();
            kindMember.setName(roleService.getNameByRoleId(operationRoleId));
            kindMember.setRoleId(contractRoleEntity.getRoleId());
            kindMember.setKind(contractRoleEntity.getKind());
            kindMember.setAllianceId(contractRoleEntity.getAllianceId());
            kindMembers.add(kindMember);
        }
        if(!isFind)
            return null;
        return kindMembers;
    }

    @Override
    public AdditionEntity editAddition(long operationRoleId, long additionId, String additionContent, long allianceId, long contractId) {
        RoleEntity ope = RoleEntity.dao.findById(operationRoleId,allianceId);
        AdditionEntity addition = AdditionEntity.dao.findById(additionId,contractId);
        if(addition != null && addition.getState() == 1){
            addition.setContent(additionContent);
            addition.update();

            ContractLogEntity log = new ContractLogEntity();
            log.setContractId(addition.getContractId());
            log.setMessage(ope.getTitle()+"修改了附加条款");
            log.setModifyTime(TimeUtil.getCurrentSqlTime());
            log.setHasDetail(0);
            log.save();

            return addition;
        }

        return null;
    }


    private void recorcSimpleLog(long contractId,String content){
        ContractLogEntity log = new ContractLogEntity();
        log.setContractId(contractId);
        log.setMessage(content);
        log.setModifyTime(TimeUtil.getCurrentSqlTime());
        log.setHasDetail(0);
        log.setDetail("");
        log.save();
    }

    private List<Long> getContractList(long operationRoleId){
        List<ContractRoleEntity> contractList = ContractRoleEntity.dao.eq("role_id",operationRoleId).selectList("contract_id");
        List<Long> contractIds = new ArrayList<>();
        for(ContractRoleEntity c : contractList){
            contractIds.add(c.getContractId());
        }
        return contractIds;
    }

    private List<Long> getAllianceList(long operationRoleId,long contractId){
        List<ContractRoleEntity> contractList = ContractRoleEntity.dao.partitionId(contractId).selectList("alliance_id");
        List<Long> allianceIds = new ArrayList<>();
        for(ContractRoleEntity c : contractList){
            if(!allianceIds.contains(c.getAllianceId())){
                allianceIds.add(c.getAllianceId());
            }
        }
        return allianceIds;

    }

    private List<Integer> getKinds(long contractId){
        List<ContractRoleEntity> roles = ContractRoleEntity.dao.partitionId(contractId).selectList();
        List<Integer> kinds = new ArrayList<>();
        for(ContractRoleEntity c : roles){
            if(!kinds.contains(c.getKind())){
                kinds.add(c.getKind());
            }
        }
        return kinds;
    }


}
