package cn.superid.webapp.service;

import cn.superid.webapp.model.RoleEntity;

/**
 * Created by zp on 2016/7/26.
 */
public interface IRoleService {

    RoleEntity createRole(String title,long allianceId,long belongAffairId,String permissions,int type);

    public String getNameByRoleId(Long roleId);

}
