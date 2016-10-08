package cn.superid.webapp.service;

import cn.superid.webapp.model.AffairMemberApplicationEntity;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.model.PermissionGroupEntity;

/**
 * Created by xiaofengxu on 16/9/2.
 */
public interface IAffairMemberService {
    AffairMemberEntity addMember(Long allianceId,Long affairId,Long roleId,String permissions,long permissionGroupId) throws Exception;//type为

    /**
     * 给某个角色设置权限组
     * @param affairId
     * @param allianceId
     * @param toRoleId 被操作的角色id
     * @param permissionGroupId
     * @return
     * @throws Exception
     */
    public boolean allocateAffairMemberPermissionGroup(Long allianceId,Long affairId,Long toRoleId, Long permissionGroupId) throws Exception;


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
     * 通过申请的id找到事务的成员
     * @param applicationId
     * @return
     */
    public AffairMemberApplicationEntity findAffairMemberApplicationById(Long affairId, Long applicationId);

    /**
     * 同意加入事务的申请
     * @param applicationId
     * @param dealRoleId
     * @param dealReason
     * @return
     * @throws Exception
     */
    public AffairMemberEntity agreeAffairMemberApplication(Long allianceId,Long affairId,Long applicationId, Long dealRoleId,String dealReason) throws Exception;

    /**
     * 拒绝加入事务的申请
     * @param applicationId
     * @param dealRoleId
     * @param dealReason
     * @return
     * @throws Exception
     */
    public AffairMemberApplicationEntity rejectAffairMemberApplication(Long allianceId,Long affairId,Long applicationId, Long dealRoleId,String dealReason) throws Exception;

    public String applyForEnterAffair(Long allianceId,Long affairId,Long roleId) throws Exception;
}
