package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.Expr;
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
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zp on 2016/8/8.
 */
@Service
public class AllianceService  implements IAllianceService{

    public static int CODE_LENTH = 8;

    @Autowired
    private IUserService userService;

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
        AllianceEntity allianceEntity = new AllianceEntity();

        allianceEntity.setIsPersonal(allianceCreateForm.getIsPersonal());
        allianceEntity.setVerified(StateType.NotCertificated);//等待验证
        allianceEntity.setCreateTime(TimeUtil.getCurrentSqlTime());

        if(allianceCreateForm.getIsPersonal() == IntBoolean.TRUE){
            allianceEntity.setName(allianceCreateForm.getName()+"的事务");
        }
        else{
            allianceEntity.setName(allianceCreateForm.getName());
        }
        allianceEntity.save();

        String shortName = generateCode(allianceEntity.getId());
        allianceEntity.setShortName(shortName);
        //这样应该更新不了吧= =
        //AllianceEntity.dao.findById(allianceEntity.getId()).setShortName(shortName);
        AllianceEntity.dao.id(allianceEntity.getId()).set("shortName",shortName);

        RoleEntity roleEntity = new RoleEntity();
        //需求说是直接叫盟主
        roleEntity.setTitle("盟主");
        roleEntity.setUserId(allianceCreateForm.getUserId());
        roleEntity.setAllianceId(allianceEntity.getId());
        roleEntity.setBelongAffairId(0);
        roleEntity.setPermissions(AffairPermissionRoleType.OWNER);
        roleEntity.setAllocatePermissions(AffairPermissionRoleType.OWNER);
        //创建盟的人在这个盟里的默认角色
        roleEntity.setType(1);
        roleEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
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
    public boolean addAllianceCertification(AllianceCertificationForm allianceCertificationForm, long roleId,long allianceId) {
        boolean isFind = AllianceCertificationEntity.dao.partitionId(allianceId).eq("roleId",roleId).or(new Expr("checkState","=",0),new Expr("checkState","=",1)).exists();
        if(isFind){
            return false;
        }
        AllianceCertificationEntity allianceCertificationEntity = new AllianceCertificationEntity();
        allianceCertificationEntity.setAllianceId(allianceId);
        allianceCertificationEntity.copyPropertiesFrom(allianceCertificationForm);
        allianceCertificationEntity.setRoleId(roleId);
        allianceCertificationEntity.save();

        AllianceEntity.dao.id(allianceId).set("applyCertificateState",2);
        return true;
    }

    @Override
    public boolean editAllianceCertification(AllianceCertificationForm allianceCertificationForm, long roleId) {
        allianceCertificationForm.setRoleId(roleId);
        return AllianceCertificationEntity.dao.set(allianceCertificationForm)>0;
    }

    @Override
    public long getDefaultRoleIdFromAlliance(long allianceId) {
        long roleId = RoleEntity.dao.partitionId(allianceId).eq("user_id",userService.currentUserId()).eq("type",1).selectOne("id").getId();
        return roleId;
    }

    @Override
    public List<AllianceEntity> getAllianceList() {
        StringBuilder sb = new StringBuilder("select * from alliance where id in (" +
                "select alliance_id from role where user_id = ? )");
        ParameterBindings p =new ParameterBindings();
        p.addIndexBinding(userService.currentUserId());
        return AllianceEntity.dao.findList(sb.toString(),p);
    }

    private String generateCode(long allianceId){
        String result = allianceId+"";
        int vacancy = CODE_LENTH - result.length();
        if(vacancy > 0){
            for(int i = 0 ; i < vacancy ; i++){
                result = "0"+result;
            }
        }
        return result;
    }

}
