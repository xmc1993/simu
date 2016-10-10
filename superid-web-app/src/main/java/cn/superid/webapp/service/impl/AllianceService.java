package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.jpa.util.StringUtil;
import cn.superid.webapp.enums.IntBoolean;
import cn.superid.webapp.enums.StateType;
import cn.superid.webapp.forms.AllianceCertificationForm;
import cn.superid.webapp.forms.AllianceCreateForm;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AllianceCertificationEntity;
import cn.superid.webapp.model.AllianceEntity;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.security.AffairPermissionRoleType;
import cn.superid.webapp.service.IAffairService;
import cn.superid.webapp.service.IAllianceService;
import cn.superid.webapp.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
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
    public String getPermissions(long alliance, long roleId) throws Exception{
        RoleEntity roleEntity= RoleEntity.dao.findById(roleId,alliance);
        if((roleEntity == null)||(StringUtil.isEmpty(roleEntity.getPermissions()))){
            throw  new Exception("找不到盟成员");
        }

        return roleEntity.getPermissions();
    }

    @Override
//    @Transactional
    public AllianceEntity createAlliance(AllianceCreateForm allianceCreateForm) {
        AllianceEntity allianceEntity;
        if(allianceCreateForm.getIsPersonal()== IntBoolean.TRUE){
            allianceEntity = new AllianceEntity();
            allianceEntity.setName(allianceCreateForm.getName()+"的盟");
            allianceEntity.setIsPersonal(IntBoolean.TRUE);
            allianceEntity.setShortName(allianceCreateForm.getShortName());
            allianceEntity.setState(StateType.Disabled);//等待验证身份
            allianceEntity.save();

        }else{
            allianceEntity = new AllianceEntity();
            allianceEntity.setName(allianceCreateForm.getName());
            allianceEntity.setShortName(allianceCreateForm.getShortName());
            allianceEntity.setIsPersonal(IntBoolean.FALSE);
            allianceEntity.setState(StateType.Disabled);//等待验证
            allianceEntity.save();//在验证成功之后再创建角色

        }

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setTitle(allianceCreateForm.getName());
        roleEntity.setUserId(allianceCreateForm.getUserId());
        roleEntity.setAllianceId(allianceEntity.getId());
        roleEntity.setBelongAffairId(0);
        roleEntity.setPermissions(AffairPermissionRoleType.OWNER);
        roleEntity.setAllocatePermissions(AffairPermissionRoleType.OWNER);
        roleEntity.setType(allianceCreateForm.getIsPersonal());
        roleEntity.save();

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
//        Integer result = (Integer) RoleEntity.getSession().findOne(Integer.class,
//                "select 1 from role r where r.user_id = ? and exists( select 1 from role l where l.user_id =? and l.alliance_id = r.alliance_id) limit 1",new ParameterBindings(userId1,userId2));
//
        return false;
    }

    @Override
    public boolean validName(String code) {
        return !AllianceEntity.dao.eq("shortName",code).exists();
    }

    @Override
    public AllianceCertificationEntity addAllianceCertification(AllianceCertificationForm allianceCertificationForm, long roleId,long allianceId) {
        AllianceCertificationEntity allianceCertificationEntity = new AllianceCertificationEntity();
        allianceCertificationEntity.setAllianceId(allianceId);
        allianceCertificationEntity.copyPropertiesFrom(allianceCertificationForm);
        allianceCertificationEntity.setRoleId(roleId);
        allianceCertificationEntity.save();
        return allianceCertificationEntity;
    }

    @Override
    public boolean editAllianceCertification(AllianceCertificationForm allianceCertificationForm, long roleId) {
        allianceCertificationForm.setRoleId(roleId);
        return AllianceCertificationEntity.dao.set(allianceCertificationForm)>0;
    }
}
