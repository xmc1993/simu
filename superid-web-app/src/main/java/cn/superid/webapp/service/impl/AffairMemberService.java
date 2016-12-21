package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.StringUtil;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.enums.state.DealState;
import cn.superid.webapp.enums.state.ValidState;
import cn.superid.webapp.model.*;
import cn.superid.webapp.model.cache.RoleCache;
import cn.superid.webapp.security.AffairPermissionRoleType;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.service.IAffairUserService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiaofengxu on 16/9/2.
 */
@Service
public class AffairMemberService implements IAffairMemberService{
    @Autowired
    private IUserService userService;
    @Autowired
    private IAffairUserService affairUserService;
    @Override
    public AffairMemberEntity addMember(Long allianceId,Long affairId, Long roleId,  String permissions,int permissionLevel) {

        AffairMemberEntity affairMemberEntity = new AffairMemberEntity();
        affairMemberEntity.setAllianceId(allianceId);
        affairMemberEntity.setAffairId(affairId);
        affairMemberEntity.setRoleId(roleId);
        affairMemberEntity.setState(ValidState.Valid);
        affairMemberEntity.setPermissions(permissions);
        affairMemberEntity.setPermissionLevel(permissionLevel);
        affairMemberEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.save();
        return affairMemberEntity;
    }


    @Override
    public AffairMemberEntity addCreator(long allianceId, long affairId, long roleId) {
        return addMember(allianceId,affairId,roleId,AffairPermissionRoleType.OWNER, AffairPermissionRoleType.OWNER_ID);
    }


    @Override
    public boolean modifyAffairMemberPermissions(Long allianceId, Long affairId, Long toRoleId, String permissions) throws Exception {


        if (StringUtil.isEmpty(permissions)) {
            throw new Exception("请选择正确的权限");
        }

        int updateCount = AffairMemberEntity.dao.partitionId(allianceId).eq("affair_id", affairId).eq("role_id", toRoleId).set("permissions",permissions);
        return updateCount>0 ? true : false;
    }

    @Override
    public PermissionGroupEntity addPermissionGroup(Long allianceId,Long affairId, String name, String permissions) throws Exception {
        boolean isExist = AffairEntity.dao.id(affairId).partitionId(allianceId).exists();
        if(!isExist){
            throw new Exception("找不到该事务");
        }
        if (StringUtil.isEmpty(permissions)) {
            throw new Exception("请选择正确的权限");
        }
        PermissionGroupEntity permissionGroupEntity = new PermissionGroupEntity();
        permissionGroupEntity.setName(name);
        permissionGroupEntity.setAffairId(affairId);
        permissionGroupEntity.setPermissions(permissions);
        permissionGroupEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        permissionGroupEntity.save();
        return permissionGroupEntity;
    }

    @Override
    public int canApplyForEnterAffair(Long allianceId, Long affairId, Long roleId) {
        boolean affairIsFind = AffairEntity.dao.id(affairId).partitionId(allianceId).exists();
        if(!affairIsFind){
            return ResponseCode.AffairNotExist;
        }
        boolean isExist = AffairMemberEntity.dao.partitionId(allianceId).eq("affair_id",affairId).eq("role_id",roleId).state(ValidState.Valid).exists();
        if(isExist){
            return ResponseCode.MemberIsExistInAffair;
        }
        boolean isApplied = AffairMemberApplicationEntity.dao.partitionId(affairId).eq("role_id",roleId).state(DealState.ToCheck).exists();
        if(isApplied){
            return ResponseCode.WaitForDeal;
        }
        return ResponseCode.OK;
    }

    @Override
    public int applyForEnterAffair(Long allianceId,Long affairId, Long roleId,String applyReason){
        int code = canApplyForEnterAffair(allianceId,affairId,roleId);
        if(code!=0){
            return code;
        }
        /*
        AffairMemberEntity affairMemberEntity = new AffairMemberEntity();
        affairMemberEntity.setAllianceId(allianceId);
        affairMemberEntity.setAffairId(affairId);
        affairMemberEntity.setPermissions("");
        affairMemberEntity.setRoleId(roleId);
        affairMemberEntity.setUserId(userService.currentUserId());
        affairMemberEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.setState(2);
        affairMemberEntity.setPermissionGroupId(AffairPermissionRoleType.VISITOR_ID);
        affairMemberEntity.save();
        */
        AffairMemberApplicationEntity affairMemberApplicationEntity = new AffairMemberApplicationEntity();
        affairMemberApplicationEntity.setRoleId(roleId);
        affairMemberApplicationEntity.setUserId(userService.currentUserId());
        affairMemberApplicationEntity.setAffairId(affairId);
        affairMemberApplicationEntity.setAllianceId(allianceId);
        affairMemberApplicationEntity.setState(DealState.ToCheck);
        affairMemberApplicationEntity.setApplyReason(applyReason);
        affairMemberApplicationEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        affairMemberApplicationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberApplicationEntity.setDealReason("");
        affairMemberApplicationEntity.save();
        return ResponseCode.OK;
    }




    @Override
    public int agreeAffairMemberApplication(Long allianceId,Long affairId,Long applicationId, Long dealRoleId,String dealReason){

        AffairMemberApplicationEntity affairMemberApplicationEntity = AffairMemberApplicationEntity.dao.findById(applicationId,affairId);
        if ((affairMemberApplicationEntity == null)||(affairMemberApplicationEntity.getState() != DealState.ToCheck)) {
            return ResponseCode.ApplicationNotExist;
        }
        boolean isExist  = AffairEntity.dao.id(affairMemberApplicationEntity.getAffairId()).partitionId(allianceId).exists();
        if (!isExist) {
            return ResponseCode.AffairNotExist;
        }

        addMember(allianceId,affairId,affairMemberApplicationEntity.getRoleId(),"",AffairPermissionRoleType.GUEST_ID);
        //添加事务成员
        //TODO 要不要检测affairUser是否存在
        affairUserService.addAffairUser(allianceId,affairId,affairMemberApplicationEntity.getRoleId(),affairMemberApplicationEntity.getUserId());
        /*
        AffairMemberEntity affairMemberEntity = new AffairMemberEntity();
        affairMemberEntity.setAllianceId(allianceId);
        affairMemberEntity.setAffairId(affairId);
        affairMemberEntity.setPermissions("");
        affairMemberEntity.setRoleId(affairMemberApplicationEntity.getRoleId());
        affairMemberEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.setState(0);
        affairMemberEntity.setPermissionGroupId(AffairPermissionRoleType.GUEST_ID);
        affairMemberEntity.save();

        AffairMemberEntity affairMemberEntity = AffairMemberEntity.dao.partitionId(allianceId)
                .eq("role_id",affairMemberApplicationEntity.getRoleId()).eq("affair_id",affairId).selectOne();
        affairMemberEntity.setState(0);
        affairMemberEntity.setPermissionGroupId(AffairPermissionRoleType.GUEST_ID);
        affairMemberEntity.update();

        AffairMemberEntity affairMemberEntity = new AffairMemberEntity();
        affairMemberEntity.setRoleId(affairMemberApplicationEntity.getRoleId());
        affairMemberEntity.setAffairId(affairEntity.getRoleId());
        affairMemberEntity.setUserId(affairMemberApplicationEntity.getUserId());
        affairMemberEntity.setState(0);
        affairMemberEntity.setPermissions("");
        affairMemberEntity.setPermissionGroupId(5L);
        affairMemberEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.save();
         */


        //更新申请信息
        affairMemberApplicationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberApplicationEntity.setState(DealState.Agree);
        affairMemberApplicationEntity.setDealRoleId(dealRoleId);
        affairMemberApplicationEntity.setDealUserId(userService.currentUserId());
        affairMemberApplicationEntity.setDealReason(dealReason);
        affairMemberApplicationEntity.update();

        return ResponseCode.OK;
    }

    @Override
    public int rejectAffairMemberApplication(Long allianceId,Long affairId,Long applicationId, Long dealRoleId,String dealReason){
        AffairMemberApplicationEntity affairMemberApplicationEntity = AffairMemberApplicationEntity.dao.findById(applicationId,affairId);
        if (affairMemberApplicationEntity == null) {
            return ResponseCode.ApplicationNotExist;
        }
        //更新申请信息
        affairMemberApplicationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberApplicationEntity.setState(DealState.Reject);
        affairMemberApplicationEntity.setDealRoleId(dealRoleId);
        affairMemberApplicationEntity.setDealUserId(userService.currentUserId());
        affairMemberApplicationEntity.setDealReason(dealReason);
        affairMemberApplicationEntity.update();
        return ResponseCode.OK;
    }

    @Override
    public int canInviteToEnterAffair(Long allianceId, Long affairId, Long beInvitedRoleId) {
        //异常流程
        boolean affairIsFind = AffairEntity.dao.id(affairId).partitionId(allianceId).exists();
        if(!affairIsFind){
            return ResponseCode.AffairNotExist;
        }
        boolean isExist = AffairMemberEntity.dao.partitionId(allianceId).eq("affairId",affairId).eq("roleId",beInvitedRoleId).state(ValidState.Valid).exists();
        if(isExist){
            return ResponseCode.MemberIsExistInAffair;
        }
        boolean isInvited = AffairMemberInvitationEntity.dao.partitionId(affairId).eq("beInvitedRoleId",beInvitedRoleId).state(DealState.ToCheck).exists();
        if(isInvited){
            return ResponseCode.WaitForDeal;
        }
        return ResponseCode.OK;
    }

    @Override
    public int inviteToEnterAffair(long allianceId, long affairId, long inviteRoleId,long inviteUserId,long beInvitedRoleId,int memberType,String inviteReason) {
        int code = canInviteToEnterAffair(allianceId,affairId,beInvitedRoleId);
        if(code!=0){
            return code;
        }
        //生成邀请记录
        AffairMemberInvitationEntity affairMemberInvitationEntity = new AffairMemberInvitationEntity();
        affairMemberInvitationEntity.setState(DealState.ToCheck);
        affairMemberInvitationEntity.setAffairId(affairId);
        affairMemberInvitationEntity.setBeInvitedRoleId(beInvitedRoleId);
        affairMemberInvitationEntity.setBeInvitedUserId(RoleCache.dao.findById(beInvitedRoleId).getUserId());
        affairMemberInvitationEntity.setInviteRoleId(inviteRoleId);
        affairMemberInvitationEntity.setInviteUserId(inviteUserId);
        affairMemberInvitationEntity.setInviteReason(inviteReason);
        affairMemberInvitationEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        if(memberType==0){
            affairMemberInvitationEntity.setPermissionLevel(AffairPermissionRoleType.OFFICIAL_ID);
        }else {
            affairMemberInvitationEntity.setPermissionLevel(AffairPermissionRoleType.GUEST_ID);
        }
        affairMemberInvitationEntity.save();

        //判断被邀请的是否是本盟成员,如果是则无需同意,直接拉入事务
        boolean isInSameAlliance = RoleEntity.dao.id(beInvitedRoleId).partitionId(allianceId).exists();
        if(isInSameAlliance){
            AffairMemberInvitationEntity.dao.id(affairMemberInvitationEntity.getId()).partitionId(affairId)
                    .set("state",DealState.Agree,"dealReason","本盟人员");
            AffairMemberEntity affairMemberEntity = new AffairMemberEntity();
            affairMemberEntity.setAffairId(affairId);
            affairMemberEntity.setAllianceId(allianceId);
            affairMemberEntity.setRoleId(beInvitedRoleId);
            affairMemberEntity.setState(ValidState.Valid);
            //判断被邀请的角色是不是自己的某个父事务的负责人
            AffairEntity currentAffair = AffairEntity.dao.id(affairId).partitionId(allianceId).selectOne("id");
            if(isOwnerOfParentAffair(beInvitedRoleId,currentAffair.getId(),allianceId)){
                //如果是,将权限设置为owner
                affairMemberEntity.setPermissionLevel(AffairPermissionRoleType.OWNER_ID);
                AffairMemberInvitationEntity.dao.id(affairMemberInvitationEntity.getId()).partitionId(affairId)
                        .set("permissionLevel",AffairPermissionRoleType.OWNER_ID);
            }
            else {
                //如果不是,根据前端选择的权限类型分配给其官方还是客方
                if(memberType==0){
                    affairMemberEntity.setPermissionLevel(AffairPermissionRoleType.OFFICIAL_ID);
                }else {
                    affairMemberEntity.setPermissionLevel(AffairPermissionRoleType.GUEST_ID);
                }
            }
            affairMemberEntity.save();
            //TODO 发送消息通知
            //affairMemberEntity.setPermissions(AffairPermissionRoleType.);
        }
        //不是本盟成员
        else {
            //TODO 发送消息通知
        }
        return ResponseCode.OK;
    }

    @Override
    public int agreeInvitation(long allianceId,long affairId,long invitationId,String dealReason) {
        AffairMemberInvitationEntity affairMemberInvitationEntity = AffairMemberInvitationEntity.dao.findById(invitationId,affairId);
        if ((affairMemberInvitationEntity == null)||(affairMemberInvitationEntity.getState() != DealState.ToCheck)) {
            return ResponseCode.InvitationNotExist;
        }
        boolean isExist  = AffairEntity.dao.id(affairId).partitionId(allianceId).exists();
        if (!isExist) {
            return ResponseCode.AffairNotExist;
        }

        //加入事务
        addMember(allianceId,affairId,affairMemberInvitationEntity.getBeInvitedRoleId(),"",affairMemberInvitationEntity.getPermissionLevel());

        //更新邀请信息
        affairMemberInvitationEntity.setDealReason(dealReason);
        affairMemberInvitationEntity.setState(DealState.Agree);
        affairMemberInvitationEntity.update();
        return ResponseCode.OK;
    }

    @Override
    public int rejectInvitation(long allianceId, long affairId, long invitationId, String dealReason) {
        AffairMemberInvitationEntity affairMemberInvitationEntity = AffairMemberInvitationEntity.dao.findById(invitationId,affairId);
        if ((affairMemberInvitationEntity == null)||(affairMemberInvitationEntity.getState() != DealState.ToCheck)) {
            return ResponseCode.InvitationNotExist;
        }


        //更新邀请信息
        affairMemberInvitationEntity.setDealReason(dealReason);
        affairMemberInvitationEntity.setState(DealState.Reject);
        affairMemberInvitationEntity.update();
        return ResponseCode.OK;

    }


    public boolean isOwnerOfParentAffair(long roleId, long affairId,long allianceId){
        AffairEntity affairEntity = AffairEntity.dao.id(affairId).partitionId(allianceId).selectOne("level","path");
        int level = affairEntity.getLevel();
        String path = affairEntity.getPath();

        StringBuilder sb = new StringBuilder("");
        Object[] paths = new Object[level];
        String[] indexs = path.split("-");
        paths[0] = indexs[0];
        sb.append(indexs[0]);
        if(indexs.length>1){
            for(int i=1;i<level;i++){
                sb.append("-"+indexs[i]);
                paths[i] = sb.toString();
            }
        }

        //获取该事务的所有父事务owner
        List<AffairEntity> parentAffairOwners = AffairEntity.dao.partitionId(allianceId).in("path",paths).selectList("owner_role_id");
        //用不了contain。。。
        for(AffairEntity affair : parentAffairOwners){
            if (roleId == affair.getOwnerRoleId()){
                return true;
            }
        }
        return false;
        /*
        //获取该事务的所有父事务id
        List<AffairEntity> affairEntities = AffairEntity.dao.partitionId(allianceId).in("path",paths).selectList("id");
        Object[] parentAffairIds = new Object[affairEntities.size()];

        for(int i=0;i<affairEntities.size();i++){
            parentAffairIds[i] = affairEntities.get(i).getRoleId();
        }

        //根据父事务id和被邀请人的id以及权限级别去寻找
        boolean isOwner = AffairMemberEntity.dao.partitionId(allianceId).in("affairId",parentAffairIds)
                .eq("roleId",roleId).eq("permissionLevel",AffairPermissionRoleType.OWNER_ID).exists();
        */
    }

}
