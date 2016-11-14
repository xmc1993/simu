package cn.superid.webapp.service;

import cn.superid.webapp.model.AffairMemberApplicationEntity;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.model.PermissionGroupEntity;
import cn.superid.webapp.model.cache.AffairMemberCache;

/**
 * Created by xiaofengxu on 16/9/2.
 */
public interface IAffairMemberService {
    /**
     * 在被邀请人接受邀请或者被申请方接受申请的时候调用
     * @param allianceId
     * @param affairId
     * @param roleId
     * @param permissions
     * @param permissionLevel
     * @return
     */
    AffairMemberEntity addMember(Long allianceId,Long affairId,Long roleId,String permissions,int permissionLevel);//type为

    AffairMemberEntity addCreator(long allianceId,long affairId,long roleId);

    /**
     * 给某个角色设置权限组
     * @param affairId
     * @param allianceId
     * @param toRoleId 被操作的角色id
     * @param permissionGroupId
     * @return
     * @throws Exception
     */
    //public boolean allocateAffairMemberPermissionGroup(Long allianceId,Long affairId,Long toRoleId, Long permissionGroupId) throws Exception;


    /**
     * 修改某个角色的权限
     * @param affairId
     * @param allianceId
     * @param toRoleId
     * @param permissions
     * @return
     * @throws Exception
     */

    public boolean modifyAffairMemberPermissions(Long allianceId,Long affairId,Long toRoleId, String permissions) throws Exception;

    /**
     * 新增事务权限组
     * @param affairId
     * @param name
     * @return
     * @throws Exception
     */
    public PermissionGroupEntity addPermissionGroup(Long allianceId,Long affairId,String name,String permissions) throws Exception;


    /**
     * 申请加入一个事务
     * @param allianceId
     * @param affairId
     * @param roleId
     * @param applyReason
     * @return
     */
    public int applyForEnterAffair(Long allianceId,Long affairId,Long roleId,String applyReason) ;


    /**
     * 同意加入事务的申请
     * @param applicationId
     * @param dealRoleId
     * @param dealReason
     * @return
     */
    public int agreeAffairMemberApplication(Long allianceId,Long affairId,Long applicationId, Long dealRoleId,String dealReason);

    /**
     * 拒绝加入事务的申请
     * @param applicationId
     * @param dealRoleId
     * @param dealReason
     * @return
     */
    public int rejectAffairMemberApplication(Long allianceId,Long affairId,Long applicationId, Long dealRoleId,String dealReason) ;


    /**
     * 邀请别人加入事务,需要判断是盟内还是盟外
     * @param allianceId
     * @param affairId
     * @param inviteRoleId
     * @param beInvitedRoleId
     * @param memberType 盟内默认官方,盟外默认客方
     * @return
     */
    public int inviteToEnterAffair(long allianceId,long affairId,long inviteRoleId, long inviteUserId,long beInvitedRoleId,int memberType,String inviteReason);


}
