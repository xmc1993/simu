package cn.superid.webapp.service.impl;

import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.service.IRoleService;
import org.springframework.stereotype.Service;

/**
 * Created by xiaofengxu on 16/9/9.
 */
@Service
public class RoleService implements IRoleService {

    @Override
    public RoleEntity createRole(String title, long allianceId, long belongAffairId, String permissions, int type) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setTitle(title);
        roleEntity.setAllianceId(allianceId);
        roleEntity.setBelongAffairId(belongAffairId);
        roleEntity.setPermissions(permissions);
        roleEntity.setType(type);
        roleEntity.save();
        return roleEntity;
    }
}
