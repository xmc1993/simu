package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.Expr;
import cn.superid.jpa.util.ParameterBindings;
import cn.superid.jpa.util.StringUtil;
import cn.superid.webapp.controller.VO.SimpleRoleVO;
import cn.superid.webapp.enums.IntBoolean;
import cn.superid.webapp.enums.StateType;
import cn.superid.webapp.enums.state.CertificationState;
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

import java.util.ArrayList;
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
        allianceEntity.setVerified(CertificationState.NotCertificated);//等待验证
        allianceEntity.setCreateTime(TimeUtil.getCurrentSqlTime());

        if(allianceCreateForm.getIsPersonal() == true){
            allianceEntity.setName(allianceCreateForm.getName()+"的事务");
        }
        else{
            allianceEntity.setName(allianceCreateForm.getName());
        }
        allianceEntity.save();

        if(allianceCreateForm.getIsPersonal() == true){
            String code = generateCode(allianceEntity.getId());
            allianceEntity.setCode(code);
            AllianceEntity.dao.id(allianceEntity.getId()).set("code",code);
        }else{
            allianceEntity.setCode(allianceCreateForm.getCode());
            AllianceEntity.dao.id(allianceEntity.getId()).set("code",allianceCreateForm.getCode());
        }


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

        AffairEntity affairEntity = affairService.createRootAffair(allianceEntity.getId(),allianceCreateForm.getName(),roleEntity.getId(), allianceCreateForm.getIsPersonal()?1:0,allianceEntity.getCode());

        RoleEntity.dao.id(roleEntity.getId()).partitionId(allianceEntity.getId()).set("belongAffairId",affairEntity.getId());//更新所属事务

        allianceEntity.setOwnerRoleId(roleEntity.getId());
        allianceEntity.setRootAffairId(affairEntity.getId());
        AllianceEntity.dao.id(allianceEntity.getId()).set("ownerRoleId",roleEntity.getId(),"rootAffairId",affairEntity.getId());//更新拥有者和根事务

        if(allianceCreateForm.getIsPersonal()==true){
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
        return !AllianceEntity.dao.eq("code",code).exists();
    }

    @Override
    public boolean addAllianceCertification(AllianceCertificationForm allianceCertificationForm, long roleId,long allianceId) {
        /*
        //填写验证信息的时候,检测该盟是否是已被验证或者等待验证中
        boolean isFind = AllianceCertificationEntity.dao.partitionId(allianceId).eq("roleId",roleId).or(new Expr("checkState","=",CertificationState.Normal),new Expr("checkState","=",CertificationState.WaitCertificated)).exists();
        if(isFind){
            return false;
        }
        */
        AllianceCertificationEntity allianceCertificationEntity = new AllianceCertificationEntity();
        allianceCertificationEntity.setAllianceId(allianceId);
        allianceCertificationEntity.copyPropertiesFrom(allianceCertificationForm);
        allianceCertificationEntity.setRoleId(roleId);
        allianceCertificationEntity.setCheckState(CertificationState.WaitCertificated);
        allianceCertificationEntity.save();

        AllianceEntity.dao.id(allianceId).set("applyCertificateState",CertificationState.WaitCertificated);
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

    @Override
    public List<SimpleRoleVO> getRoleByAlliance(long allianceId) {
        List<RoleEntity> roleEntityList = RoleEntity.dao.partitionId(allianceId).selectList("id","title");
        List<SimpleRoleVO> result = new ArrayList<>();
        for(RoleEntity r : roleEntityList){
            SimpleRoleVO one =new SimpleRoleVO(r.getId() , r.getTitle());
            result.add(one);
        }

        return result;
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
