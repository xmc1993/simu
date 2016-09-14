package cn.superid.webapp.service;

import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.model.PermissionGroupEntity;

/**
 * Created by xiaofengxu on 16/9/2.
 */
public interface IAffairMemberService {
    AffairMemberEntity addMember(Long allianceId,Long affairId,Long roleId,String permissions,long permissionGroupId);//type为

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
}
