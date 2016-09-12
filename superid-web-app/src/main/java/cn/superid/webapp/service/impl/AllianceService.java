package cn.superid.webapp.service.impl;

import cn.superid.jpa.exceptions.JdbcRuntimeException;
import cn.superid.webapp.enums.AllianceType;
import cn.superid.webapp.enums.IntBoolean;
import cn.superid.webapp.enums.RoleType;
import cn.superid.webapp.forms.AllianceCreateForm;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AllianceEntity;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.security.PermissionRoleType;
import cn.superid.webapp.service.IAffairService;
import cn.superid.webapp.service.IAllianceService;
import cn.superid.webapp.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zp on 2016/8/8.
 */
@Service
public class AllianceService  implements IAllianceService{
    @Autowired
    private IRoleService roleService;

    @Autowired
    private IAffairService affairService;

    @Override
    public String getPermissions(long alliance, long roleId) {
        return null;
    }

    @Override
    @Transactional
    public AllianceEntity createAlliance(AllianceCreateForm allianceCreateForm) {
        if(allianceCreateForm.getIsPersonal()== IntBoolean.TRUE){
            AllianceEntity allianceEntity = new AllianceEntity();
            allianceEntity.setName(allianceCreateForm.getName()+"的盟");
            allianceEntity.setShortName(allianceCreateForm.getShortName());
            allianceEntity.save();

            RoleEntity roleEntity = roleService.createRole(allianceCreateForm.getName(),allianceEntity.getId(),0, "*", AllianceType.Personal);//创建一个盟主角色

            AffairEntity affairEntity = affairService.createRootAffair(allianceEntity.getId(),allianceCreateForm.getName(),roleEntity.getId(), AllianceType.Personal);

            roleEntity.setBelongAffairId(affairEntity.getId());
            roleEntity.update();
        }


        return new AllianceEntity();
    }

    @Override
    public boolean inSameAlliance(long userId1, long userId2) {
        return false;
    }

    @Override
    public boolean validName(String name) {
        return AllianceEntity.dao.eq("name",name).exists();
    }
}
