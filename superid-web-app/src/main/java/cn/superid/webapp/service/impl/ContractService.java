package cn.superid.webapp.service.impl;

import cn.superid.webapp.model.*;
import cn.superid.webapp.service.IContractService;
import cn.superid.webapp.service.IRoleService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.service.forms.SignForm;
import cn.superid.webapp.utils.TimeUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.management.relation.RoleResult;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jizhenya on 16/9/18.
 */
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
    public List<ContractTemplateEntity> listTemplate(long operationRoleId , long allianceId) {

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


}
