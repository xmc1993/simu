package cn.superid.webapp.service.impl;

import cn.superid.webapp.controller.forms.AddAllianceUserForm;
import cn.superid.webapp.enums.state.DealState;
import cn.superid.webapp.enums.state.ValidState;
import cn.superid.webapp.enums.type.DefaultRole;
import cn.superid.webapp.enums.type.InvitationType;
import cn.superid.webapp.model.AllianceUserEntity;
import cn.superid.webapp.model.InvitationEntity;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.service.IAffairUserService;
import cn.superid.webapp.service.IAllianceUserService;
import cn.superid.webapp.service.IRoleService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.List;

/**
 * Created by njuTms on 16/12/21.
 */
@Service
public class AllianceUserService implements IAllianceUserService {
    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IAffairUserService affairUserService;
    @Override
    public AllianceUserEntity addAllianceUser(long allianceId, long userId) {
        AllianceUserEntity allianceUserEntity = new AllianceUserEntity();
        allianceUserEntity.setAllianceId(allianceId);
        allianceUserEntity.setUserId(userId);
        allianceUserEntity.setState(ValidState.Valid);
        allianceUserEntity.save();
        return allianceUserEntity;
    }

    @Override
    public boolean inviteToEnterAlliance(List<AddAllianceUserForm> forms, long allianceId,long roleId) {
        for(AddAllianceUserForm addAllianceUserForm : forms){
            InvitationEntity invitationEntity = new InvitationEntity();
            invitationEntity.setAllianceId(allianceId);
            invitationEntity.setAffairId(addAllianceUserForm.getMainAffairId());
            invitationEntity.setInviteUserId(userService.currentUserId());
            invitationEntity.setInviteRoleId(roleId);
            invitationEntity.setInviteReason(addAllianceUserForm.getComment());
            invitationEntity.setBeInvitedUserId(addAllianceUserForm.getUserId());
            invitationEntity.setBeInvitedRoleId(addAllianceUserForm.getRoleId());
            invitationEntity.setBeInvitedRoleTitle(addAllianceUserForm.getRoleName());
            invitationEntity.setInvitationType(InvitationType.Alliance);
            invitationEntity.setState(DealState.ToCheck);
            invitationEntity.setPermissions(addAllianceUserForm.getPermissions());
            invitationEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
            invitationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
            invitationEntity.save();

            //TODO 给被邀请人发送消息通知
        }
        return true;
    }

    @Override
    public boolean agreeInvitationToAlliance(long invitationId, long allianceId,long beInvitedUserId,String dealReason) {
        //检测是否是本人,或许可以不要?
        if(userService.currentUserId() != beInvitedUserId){
            return false;
        }
        InvitationEntity invitationEntity = InvitationEntity.dao.findById(invitationId,allianceId);
        long beInvitedRoleId = invitationEntity.getBeInvitedRoleId();
        //如果roleId是0,说明是新增的角色
        if(0 == beInvitedRoleId) {
            //添加角色
            RoleEntity roleEntity = roleService.createRole(invitationEntity.getBeInvitedRoleTitle(),allianceId,userService.currentUserId(),invitationEntity.getAffairId(),invitationEntity.getPermissions(),DefaultRole.IsDefault);
            beInvitedRoleId = roleEntity.getId();
        }
        else {
            //找到停用的那个角色,恢复状态位、所属user和默认角色
            int isUpdate = RoleEntity.dao.id(beInvitedRoleId).partitionId(allianceId).set("state",ValidState.Valid,"user_id",userService.currentUserId(),"type",DefaultRole.IsDefault);
            if(isUpdate<=0) {
                return false;
            }
        }
        //添加affairUser
        affairUserService.addAffairUser(allianceId,invitationEntity.getAffairId(),beInvitedRoleId);
        //添加allianceUser
        addAllianceUser(allianceId,userService.currentUserId());

        return true;
    }
}
