package cn.superid.webapp.service.impl;

import cn.superid.jpa.exceptions.JdbcRuntimeException;
import cn.superid.webapp.enums.AllianceType;
import cn.superid.webapp.enums.IntBoolean;
import cn.superid.webapp.enums.RoleType;
import cn.superid.webapp.enums.StateType;
import cn.superid.webapp.forms.AllianceCreateForm;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AllianceEntity;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.security.PermissionRoleType;
import cn.superid.webapp.service.IAffairService;
import cn.superid.webapp.service.IAllianceService;
import cn.superid.webapp.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
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
        AllianceEntity allianceEntity;
        if(allianceCreateForm.getIsPersonal()== IntBoolean.TRUE){
            allianceEntity = new AllianceEntity();
            allianceEntity.setName(allianceCreateForm.getName()+"的盟");
            allianceEntity.setIsPersonal(IntBoolean.TRUE);
            allianceEntity.setShortName(allianceCreateForm.getShortName());
            allianceEntity.setState(StateType.Normal);//个人盟不需要验证
            allianceEntity.save();

        }else{
            allianceEntity = new AllianceEntity();
            allianceEntity.setName(allianceCreateForm.getName());
            allianceEntity.setShortName(allianceCreateForm.getShortName());
            allianceEntity.setIsPersonal(IntBoolean.FALSE);
            allianceEntity.setState(StateType.Disabled);//等待验证
            allianceEntity.save();//在验证成功之后再创建角色

        }

        RoleEntity roleEntity = roleService.createRole(allianceCreateForm.getName(),allianceEntity.getId(),0, "*", allianceCreateForm.getIsPersonal());//创建一个盟主角色
        AffairEntity affairEntity = affairService.createRootAffair(allianceEntity.getId(),allianceCreateForm.getName(),roleEntity.getId(), allianceCreateForm.getIsPersonal());

        RoleEntity.dao.id(roleEntity.getId()).partitionId(allianceEntity.getId()).set("belongAffairId",affairEntity.getId());//更新所属事务

        allianceEntity.setOwnerRoleId(roleEntity.getId());
        allianceEntity.setRootAffairId(affairEntity.getId());
        AllianceEntity.dao.id(allianceEntity.getId()).set("ownerRoleId",roleEntity.getId(),"rootAffairId",affairEntity.getId());//更新拥有者和根事务

        if(allianceCreateForm.getIsPersonal()==IntBoolean.TRUE){
            allianceCreateForm.getUserEntity().setPersonalRoleId(roleEntity.getId());
        }
        return allianceEntity;

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
