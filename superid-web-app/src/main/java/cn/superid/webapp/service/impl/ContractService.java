package cn.superid.webapp.service.impl;

import cn.superid.webapp.model.ContractEntity;
import cn.superid.webapp.model.ContractLogEntity;
import cn.superid.webapp.model.ContractRoleEntity;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.service.IContractService;
import cn.superid.webapp.service.IRoleService;
import cn.superid.webapp.service.forms.SignForm;
import cn.superid.webapp.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.management.relation.RoleResult;
import java.util.List;

/**
 * Created by jizhenya on 16/9/18.
 */
public class ContractService implements IContractService {

    @Autowired
    private IRoleService roleService;

    @Override
    public int confirm(long allianceId,long operatorId, long contractId) {
        //第一步,检查
        ContractEntity contract = ContractEntity.dao.findById(contractId);
        RoleEntity role = RoleEntity.dao.findById(operatorId,allianceId);
        ContractRoleEntity confirmer = ContractRoleEntity.dao.partitionId(contractId).eq("role_id",operatorId).selectOne();
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
            if(c.getRoleId() == operatorId){
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
    public boolean refuseConfirm(long operatorId, long contractId) {
        ContractEntity contract = ContractEntity.dao.findById(contractId);
        ContractRoleEntity refuser = ContractRoleEntity.dao.partitionId(contractId).eq("role_id",operatorId).selectOne();
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
    public SignForm sign(long allianceId, long operatorId, long contractId) {
        ContractEntity contract = ContractEntity.dao.findById(contractId);
        ContractRoleEntity signer = ContractRoleEntity.dao.partitionId(contractId).eq("role_id",operatorId).selectOne();
        RoleEntity role = RoleEntity.dao.findById(operatorId,allianceId);
        if(contract == null | contract.getState() != 2 | signer == null | role ==null){
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
        ContractLogEntity log = new ContractLogEntity();
        log.setContractId(contractId);
        log.setMessage(roleService.getNameByRoleId(operatorId)+ "已确认合同并签字");
        log.setModifyTime(TimeUtil.getCurrentSqlTime());
        log.setHasDetail(0);
        log.setDetail("");
        log.save();

        if(isAllSign){
            //如果所有人已确认
            contract.setState(3);
            contract.setSignatureTime(TimeUtil.getCurrentSqlTime());
            contract.update();
            return new SignForm(2,roleService.getNameByRoleId(operatorId),signer.getSignatureTime().getTime());
        }

        return new SignForm(1,roleService.getNameByRoleId(operatorId),signer.getSignatureTime().getTime());
    }


}
