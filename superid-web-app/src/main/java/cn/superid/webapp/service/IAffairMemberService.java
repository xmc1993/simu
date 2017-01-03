package cn.superid.webapp.service;

import cn.superid.webapp.forms.AffairRoleCard;
import cn.superid.webapp.forms.SearchAffairRoleConditions;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.model.PermissionGroupEntity;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaofengxu on 16/9/2.
 */
public interface IAffairMemberService {
    /**
     * 在被邀请人接受邀请或者被申请方接受申请的时候调用
     *
     * @param allianceId
     * @param affairId
     * @param roleId
     * @param permissions
     * @param permissionLevel
     * @return
     */
    AffairMemberEntity addMember(Long allianceId, Long affairId, Long roleId, String permissions, int permissionLevel);//type为

    AffairMemberEntity addCreator(long allianceId, long affairId, long roleId);

    /**
     *
     * @param allianceId
     * @param affairId
     * @param roleId
     * @return
     */
    AffairMemberEntity getAffairMemberInfo(long allianceId, long affairId, long roleId);

    /**
     * 给某个角色设置权限组
     * @param affairId
     * @param allianceId
     * @param toRoleId 被操作的角色id
     * @param permissionGroupId
     * @return
     * @throws Exception
     */
    // boolean allocateAffairMemberPermissionGroup(Long allianceId,Long affairId,Long toRoleId, Long permissionGroupId) throws Exception;


    /**
     * 修改某个角色的权限
     *
     * @param affairId
     * @param allianceId
     * @param toRoleId
     * @param permissions
     * @return
     * @throws Exception
     */

    boolean modifyAffairMemberPermissions(Long allianceId, Long affairId, Long toRoleId, String permissions) throws Exception;

    /**
     * 新增事务权限组
     *
     * @param affairId
     * @param name
     * @return
     * @throws Exception
     */
    PermissionGroupEntity addPermissionGroup(Long allianceId, Long affairId, String name, String permissions) throws Exception;

    /**
     * 在申请加入事务之前检测能否加入该事务,返回异常情况code
     *
     * @param allianceId
     * @param affairId
     * @param roleId
     * @return
     */
    int canApplyForEnterAffair(Long allianceId, Long affairId, Long roleId);

    /**
     * 申请加入一个事务
     *
     * @param allianceId
     * @param affairId
     * @param roleId
     * @param applyReason
     * @return
     */
    int applyForEnterAffair(Long allianceId, Long affairId, Long roleId, String applyReason);


    /**
     * 同意加入事务的申请
     *
     * @param applicationId
     * @param dealRoleId
     * @param dealReason
     * @return
     */
    int agreeAffairMemberApplication(Long allianceId, Long affairId, Long applicationId, Long dealRoleId, String dealReason);

    /**
     * 拒绝加入事务的申请
     *
     * @param applicationId
     * @param dealRoleId
     * @param dealReason
     * @return
     */
    int rejectAffairMemberApplication(Long allianceId, Long affairId, Long applicationId, Long dealRoleId, String dealReason);

    /**
     * 在邀请加入事务之前检测异常情况,返回异常情况code
     *
     * @param allianceId
     * @param affairId
     * @param beInvitedRoleId
     * @return
     */
    int canInviteToEnterAffair(Long allianceId, Long affairId, Long beInvitedRoleId);

    /**
     * 邀请别人加入事务,需要判断是盟内还是盟外
     *
     * @param allianceId
     * @param affairId
     * @param inviteRoleId
     * @param beInvitedRoleId
     * @param memberType      盟内默认官方,盟外默认客方,0表示官方,1表示客方
     * @return
     */
    int inviteToEnterAffair(long allianceId, long affairId, long inviteRoleId, long inviteUserId, long beInvitedRoleId, int memberType, String inviteReason);

    /**
     * 同意别人邀请自己进入事务的请求
     *
     * @param allianceId   当前盟
     * @param affairId     当前事务
     * @param invitationId
     * @param dealReason
     * @return
     */
    int agreeInvitation(long allianceId, long affairId, long invitationId, String dealReason);

    /**
     * 拒绝别人的邀请
     *
     * @param allianceId
     * @param affairId
     * @param invitationId
     * @param dealReason
     * @return
     */
    int rejectInvitation(long allianceId, long affairId, long invitationId, String dealReason);

    /**
     * 判断一个角色是不是一个事务的某个父事务的负责人
     *
     * @param roleId     需要判断的角色
     * @param affairId   需要判断的事务
     * @param allianceId
     * @return
     */
    public boolean isOwnerOfParentAffair(long roleId,long affairId,long allianceId);



    public List<Long> getDirectorIds(long affairId,long allianceId);

    /**
     * 获取事务中联盟外的成员
     *
     * @param affairId
     * @param allianceId
     * @return
     */
    List<AffairMemberEntity> getAffairGuestMembers(long allianceId, long affairId);

    /**
     * 获取affair中的成员人数
     *
     * @param affairId
     * @return
     */
    int countAffairMember(long allianceId, long affairId);



    public Map<Long,List<Object>> getAffairMember();

    public Map<Long,List<Object>> getAffairMemberByAllianceId(long allianceId);


    List<AffairRoleCard> searchAffairRoleCards(long allianceId, long affairId, SearchAffairRoleConditions conditions);
}
