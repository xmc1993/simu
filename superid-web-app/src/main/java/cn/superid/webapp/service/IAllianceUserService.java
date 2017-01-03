package cn.superid.webapp.service;

import cn.superid.webapp.controller.forms.AddAllianceUserForm;
import cn.superid.webapp.model.AllianceUserEntity;

import java.util.List;

/**
 * Created by njuTms on 16/12/15.
 */
public interface IAllianceUserService{
    public AllianceUserEntity addAllianceUser(long allianceId,long userId);


    /**
     * 邀请盟成员
     * @param forms 包含userId,主事务id,角色id,如果是添加角色就是0,以及高级权限
     * @param allianceId
     * @return
     */
    public boolean inviteToEnterAlliance(List<AddAllianceUserForm> forms, long allianceId,long roleId);

    /**
     *
     * @param invitationId
     * @param dealReason
     * @return
     */
    public boolean agreeInvitationToAlliance(long invitationId,long allianceId,long beInvitedUserId,String dealReason);

    public boolean rejectInvitationToAlliance(long invitationId,long allianceId,long beInvitedUserId,String dealReason);
}
