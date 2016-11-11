package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.StringUtil;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.model.*;
import cn.superid.webapp.model.cache.RoleCache;
import cn.superid.webapp.security.AffairPermissionRoleType;
import cn.superid.webapp.security.PermissionType;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.Iterator;

/**
 * Created by xiaofengxu on 16/9/2.
 */
@Service
public class AffairMemberService implements IAffairMemberService{
    @Autowired
    private IUserService userService;
    @Override
    public AffairMemberEntity addMember(Long allianceId,Long affairId, Long roleId,  String permissions,long permissionGroupId) {
//        boolean isExist = AffairMemberEntity.dao.partitionId(allianceId).eq("affair_id",affairId).eq("role_id",roleId).exists();不需要检查,这个需要在申请时否掉
//        if(isExist){
//            throw new Exception("该成员已在事务中");
//        }
        AffairMemberEntity affairMemberEntity = new AffairMemberEntity();
        affairMemberEntity.setAllianceId(allianceId);
        affairMemberEntity.setAffairId(affairId);
        affairMemberEntity.setRoleId(roleId);
        affairMemberEntity.setState(0);
        affairMemberEntity.setPermissions(permissions);
        affairMemberEntity.setPermissionGroupId(permissionGroupId);
        affairMemberEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.save();
        return affairMemberEntity;
    }


    @Override
    public AffairMemberEntity addCreator(long allianceId, long affairId, long roleId) {
        return addMember(allianceId,affairId,roleId,AffairPermissionRoleType.OWNER, AffairPermissionRoleType.OWNER_ID);
    }

    public boolean allocateAffairMemberPermissionGroup(Long affairId, Long allianceId, Long toRoleId, Long permissionGroupId) throws Exception {
        if (permissionGroupId == null) {
            throw new Exception("请选择权限组");
        }
        /*
        if ((permissionGroupId.longValue() > 0) || (permissionGroupId.longValue() < 6)) {
            Iterator it = AffairPermissionRoleType.roles.keySet().iterator();
            while (it.hasNext()) {
                Long key = (Long) it.next();
                if (key.longValue() == permissionGroupId) {
                    affairMemberEntity.setPermissionGroupId(permissionGroupId);
                    affairMemberEntity.setPermissions("");
                    affairMemberEntity.update();
                    return true;
                }
            }
        }
         */

        int updateCount = AffairMemberEntity.dao.partitionId(allianceId).eq("affair_id", affairId).eq("role_id", toRoleId).set("permission_group_id",permissionGroupId,"permissions","");
        return updateCount>0 ? true : false;
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
    public int applyForEnterAffair(Long allianceId,Long affairId, Long roleId,String applyReason){
        boolean affairIsFind = AffairEntity.dao.id(affairId).partitionId(allianceId).exists();
        if(!affairIsFind){
            return ResponseCode.AffairNotExist;
        }
        boolean isExist = AffairMemberEntity.dao.partitionId(allianceId).eq("affair_id",affairId).eq("role_id",roleId).state(0).exists();
        if(isExist){
            return ResponseCode.MemberIsExistInAffair;
        }
        boolean isApplied = AffairMemberApplicationEntity.dao.partitionId(affairId).eq("role_id",roleId).eq("affair_id",affairId).state(0).exists();
        if(isApplied){
            return ResponseCode.WaitForDeal;
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
        //TODO 此处测试时没有currentUserId,运行报错
        affairMemberApplicationEntity.setUserId(userService.currentUserId());
        affairMemberApplicationEntity.setAffairId(affairId);
        affairMemberApplicationEntity.setAllianceId(allianceId);
        affairMemberApplicationEntity.setState(0);
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
        if ((affairMemberApplicationEntity == null)||(affairMemberApplicationEntity.getState() != 0)) {
            return ResponseCode.ApplicationNotExist;
        }
        boolean isExist  = AffairEntity.dao.id(affairMemberApplicationEntity.getAffairId()).partitionId(allianceId).exists();
        if (!isExist) {
            return ResponseCode.AffairNotExist;
        }

        addMember(allianceId,affairId,affairMemberApplicationEntity.getRoleId(),"",AffairPermissionRoleType.GUEST_ID);
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

        // TODO: 设置权限,暂时设置为客方
        AffairMemberEntity affairMemberEntity = AffairMemberEntity.dao.partitionId(allianceId)
                .eq("role_id",affairMemberApplicationEntity.getRoleId()).eq("affair_id",affairId).selectOne();
        affairMemberEntity.setState(0);
        affairMemberEntity.setPermissionGroupId(AffairPermissionRoleType.GUEST_ID);
        affairMemberEntity.update();

        AffairMemberEntity affairMemberEntity = new AffairMemberEntity();
        affairMemberEntity.setRoleId(affairMemberApplicationEntity.getRoleId());
        affairMemberEntity.setAffairId(affairEntity.getId());
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
        affairMemberApplicationEntity.setState(1);
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
        affairMemberApplicationEntity.setState(2);
        affairMemberApplicationEntity.setDealRoleId(dealRoleId);
        affairMemberApplicationEntity.setDealUserId(userService.currentUserId());
        affairMemberApplicationEntity.setDealReason(dealReason);
        affairMemberApplicationEntity.update();
        return ResponseCode.OK;
    }

    @Override
    public int inviteToEnterAffair(long allianceId, long affairId, long inviteRoleId,long inviteUserId,long beInvitedRoleId,String inviteReason) {
        //异常流程
        boolean affairIsFind = AffairEntity.dao.id(affairId).partitionId(allianceId).exists();
        if(!affairIsFind){
            return ResponseCode.AffairNotExist;
        }
        boolean isExist = AffairMemberEntity.dao.partitionId(allianceId).eq("affairId",affairId).eq("beInvitedRoleId",beInvitedRoleId).state(0).exists();
        if(isExist){
            return ResponseCode.MemberIsExistInAffair;
        }
        boolean isInvited = AffairMemberInvitationEntity.dao.partitionId(affairId).eq("beInvitedRoleId",beInvitedRoleId)
                .eq("affairId",affairId).state(0).exists();
        if(isInvited){
            return ResponseCode.WaitForDeal;
        }

        //生成邀请记录
        AffairMemberInvitationEntity affairMemberInvitationEntity = new AffairMemberInvitationEntity();
        affairMemberInvitationEntity.setState(0);
        affairMemberInvitationEntity.setAffairId(affairId);
        affairMemberInvitationEntity.setBeInvitedRoleId(beInvitedRoleId);
        affairMemberInvitationEntity.setBeInvitedUserId(RoleCache.dao.findById(beInvitedRoleId).getUserId());
        //TODO 不知道邀请是role还是user,如果是role,userid怎么获取
        affairMemberInvitationEntity.setInviteRoleId(inviteRoleId);
        affairMemberInvitationEntity.setInviteUserId(RoleCache.dao.id(inviteRoleId).selectOne().getUserId());
        affairMemberInvitationEntity.setInviteReason(inviteReason);

        //判断被邀请的是否是本盟成员,如果是则无需同意,直接拉入事务
        boolean isInSameAlliance = RoleEntity.dao.id(beInvitedRoleId).partitionId(allianceId).exists();
        if(isInSameAlliance){

        }
        return ResponseCode.OK;
    }

}
