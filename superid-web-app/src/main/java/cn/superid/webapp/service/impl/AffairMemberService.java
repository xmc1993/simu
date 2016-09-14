package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.StringUtil;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.model.PermissionGroupEntity;
import cn.superid.webapp.security.AffairPermissionRoleType;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.utils.TimeUtil;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.Iterator;

/**
 * Created by xiaofengxu on 16/9/2.
 */
@Service
public class AffairMemberService implements IAffairMemberService {
    @Override
    public AffairMemberEntity addMember(Long allianceId,Long affairId, Long roleId,  String permissions,long permissionGroupId) {
        AffairMemberEntity affairMemberEntity = new AffairMemberEntity();
        affairMemberEntity.setAllianceId(allianceId);
        affairMemberEntity.setAffairId(affairId);
        affairMemberEntity.setRoleId(roleId);
        affairMemberEntity.setPermissions(permissions);
        affairMemberEntity.setPermissionGroupId(permissionGroupId);
        affairMemberEntity.setAllianceId(allianceId);
        affairMemberEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.save();
        return affairMemberEntity;
    }

    public boolean allocateAffairMemberPermissionGroup(Long affairId,Long allianceId,Long toRoleId, Long permissionGroupId) throws Exception {
        AffairMemberEntity affairMemberEntity = AffairMemberEntity.dao.partitionId(allianceId).eq("affairId", affairId).eq("roleId", toRoleId).selectOne();
        if (affairMemberEntity == null) {
            throw new Exception("找不到该事务成员");
        }

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
        affairMemberEntity.setPermissionGroupId(permissionGroupId);
        affairMemberEntity.setPermissions("");
        affairMemberEntity.update();
        return true;
    }

    @Override
    public boolean modifyAffairMemberPermissions(Long allianceId, Long affairId, Long toRoleId, String permissions) throws Exception {
        AffairMemberEntity affairMemberEntity = AffairMemberEntity.dao.partitionId(allianceId).eq("affairId", affairId).eq("roleId", toRoleId).selectOne();
        if (affairMemberEntity == null) {
            throw new Exception("找不到该事务成员");
        }

        if (StringUtil.isEmpty(permissions)) {
            throw new Exception("请选择正确的权限");
        }
        affairMemberEntity.setPermissions(permissions);
        affairMemberEntity.update();
        return true;
    }

    @Override
    public PermissionGroupEntity addPermissionGroup(Long allianceId,Long affairId, String name, String permissions) throws Exception {
        AffairEntity affairEntity = AffairEntity.dao.partitionId(allianceId).findById(affairId);
        if(affairEntity == null){
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
        return null;
    }
}
