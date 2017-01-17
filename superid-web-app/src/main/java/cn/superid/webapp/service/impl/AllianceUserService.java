package cn.superid.webapp.service.impl;

import cn.superid.webapp.controller.forms.AddAllianceUserForm;
import cn.superid.webapp.enums.state.DealState;
import cn.superid.webapp.enums.state.ValidState;
import cn.superid.webapp.enums.type.DefaultRole;
import cn.superid.webapp.enums.type.InvitationType;
import cn.superid.webapp.model.AllianceUserEntity;
import cn.superid.webapp.model.InvitationEntity;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.security.AffairPermissionRoleType;
import cn.superid.webapp.service.*;
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

    @Autowired
    private IAffairUserService affairUserService;

    @Autowired
    private IAffairMemberService affairMemberService;
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
    public boolean inviteToEnterAlliance(List<AddAllianceUserForm> forms, long allianceId,long roleId,long inviteUserId) {
        long beInvitedUserId ;
        long beInvitedRoleId;
        for(AddAllianceUserForm addAllianceUserForm : forms){
            beInvitedRoleId = addAllianceUserForm.getRoleId();
            beInvitedUserId = addAllianceUserForm.getUserId();
            //同一个人被邀请了两次并且角色都是一样的话,暂定直接覆盖
            InvitationEntity existedInvitation = InvitationEntity.dao.partitionId(allianceId).eq("be_invited_role_id",beInvitedRoleId)
                    .eq("be_invited_user_id",beInvitedUserId).selectOne();
            if(existedInvitation != null) {
                existedInvitation.setAffairId(addAllianceUserForm.getMainAffairId());
                existedInvitation.setInviteUserId(inviteUserId);
                existedInvitation.setInviteRoleId(roleId);
                existedInvitation.setInviteReason(addAllianceUserForm.getComment());
                existedInvitation.setBeInvitedUserId(addAllianceUserForm.getUserId());
                existedInvitation.setBeInvitedRoleId(addAllianceUserForm.getRoleId());
                existedInvitation.setBeInvitedRoleTitle(addAllianceUserForm.getRoleTitle());
                existedInvitation.setPermissions(addAllianceUserForm.getPermissions());
                existedInvitation.setModifyTime(TimeUtil.getCurrentSqlTime());
                existedInvitation.update();

                //TODO:暂时开放
                agreeInvitationToAlliance(existedInvitation.getId(),allianceId,"自动同意");
            }
            //生成一份新的邀请
            else {
                InvitationEntity invitationEntity = new InvitationEntity();
                invitationEntity.setAllianceId(allianceId);
                invitationEntity.setAffairId(addAllianceUserForm.getMainAffairId());
                invitationEntity.setInviteUserId(inviteUserId);
                invitationEntity.setInviteRoleId(roleId);
                invitationEntity.setInviteReason(addAllianceUserForm.getComment());
                invitationEntity.setBeInvitedUserId(addAllianceUserForm.getUserId());
                invitationEntity.setBeInvitedRoleId(addAllianceUserForm.getRoleId());
                invitationEntity.setBeInvitedRoleTitle(addAllianceUserForm.getRoleTitle());
                invitationEntity.setInvitationType(InvitationType.Alliance);
                invitationEntity.setState(DealState.ToCheck);
                invitationEntity.setPermissions(addAllianceUserForm.getPermissions());
                invitationEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
                invitationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
                invitationEntity.save();

                //TODO:暂时开放
                agreeInvitationToAlliance(invitationEntity.getId(),allianceId,"自动同意");
            }
            //TODO 给被邀请人发送消息通知
        }


        return true;
    }

    @Override
    public boolean agreeInvitationToAlliance(long invitationId, long allianceId,String dealReason) {
        InvitationEntity invitationEntity = InvitationEntity.dao.findById(invitationId,allianceId);
        long beInvitedUserId = invitationEntity.getBeInvitedUserId();
        //检测是否是本人,或许可以不要?
//        if(userService.currentUserId() != beInvitedUserId){
//            return false;
//        }
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
        //添加allianceUser
        addAllianceUser(allianceId,userService.currentUserId());
        //添加affairUser
        affairUserService.addAffairUser(allianceId,invitationEntity.getAffairId(),invitationEntity.getBeInvitedUserId(),beInvitedRoleId);
        //添加affairMember,暂定为参与人
        affairMemberService.addMember(allianceId,invitationEntity.getAffairId(),beInvitedRoleId, AffairPermissionRoleType.PARTICIPANT);

        invitationEntity.setDealReason(dealReason);
        invitationEntity.setState(DealState.Agree);
        invitationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        invitationEntity.update();
        return true;
    }

    @Override
    public boolean rejectInvitationToAlliance(long invitationId, long allianceId, String dealReason) {
        InvitationEntity invitationEntity = InvitationEntity.dao.findById(invitationId,allianceId);
        long beInvitedUserId = invitationEntity.getBeInvitedUserId();
        //检测是否是本人,或许可以不要?
        if(userService.currentUserId() != beInvitedUserId){
            return false;
        }
        int isUpdate = InvitationEntity.dao.id(invitationId).partitionId(allianceId).set("deal_reason",dealReason,"state",DealState.Reject);
        return isUpdate>0;
    }
}
