package cn.superid.webapp.service.impl;

import cn.superid.webapp.controller.forms.AddAllianceUserForm;
import cn.superid.webapp.enums.state.DealState;
import cn.superid.webapp.enums.state.ValidState;
import cn.superid.webapp.enums.type.DefaultRole;
import cn.superid.webapp.enums.type.InvitationType;
import cn.superid.webapp.model.AllianceUserEntity;
import cn.superid.webapp.model.InvitationEntity;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.service.IAllianceUserService;
import cn.superid.webapp.service.IRoleService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public boolean agreeInvitationToAlliance(long invitationId, String dealReason) {
        return false;
    }
}
