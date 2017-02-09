package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.jpa.util.StringUtil;
import cn.superid.webapp.controller.VO.SimpleRoleVO;
import cn.superid.webapp.enums.SuperIdNumber;
import cn.superid.webapp.enums.state.CertificationState;
import cn.superid.webapp.enums.state.DealState;
import cn.superid.webapp.forms.AllianceCertificationForm;
import cn.superid.webapp.forms.AllianceCreateForm;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AllianceCertificationEntity;
import cn.superid.webapp.model.AllianceEntity;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.security.AffairPermissionRoleType;
import cn.superid.webapp.service.*;
import cn.superid.webapp.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zp on 2016/8/8.
 */
@Service
public class AllianceService  implements IAllianceService {

    public static int CODE_LENTH = 8;

    @Autowired
    private IUserService userService;

    @Autowired
    private IAffairService affairService;
    @Autowired
    private IRoleService roleService;

    @Autowired
    private IAllianceUserService allianceUserService;

    @Override
    public String getPermissions(long alliance, long roleId) throws Exception {
        RoleEntity roleEntity = RoleEntity.dao.findById(roleId, alliance);
        if ((roleEntity == null) || (StringUtil.isEmpty(roleEntity.getPermissions()))) {
            throw new Exception("找不到盟成员");
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
        allianceEntity.setCode(allianceCreateForm.getCode());
        allianceEntity.setName(allianceCreateForm.getName());
        allianceEntity.save();

        allianceUserService.addAllianceUser(allianceEntity.getId(), allianceCreateForm.getUserId());

        RoleEntity roleEntity = roleService.createRole(allianceCreateForm.getRoleTitle(), allianceEntity.getId(),
                allianceCreateForm.getUserId(), 0L, AffairPermissionRoleType.OWNER, 1);

        AffairEntity affairEntity = affairService.createRootAffair(allianceEntity.getId(), allianceCreateForm.getUserId(), allianceCreateForm.getName()
                , roleEntity.getId(), allianceCreateForm.getIsPersonal() ? 1 : 0, allianceEntity.getCode());

        RoleEntity.dao.id(roleEntity.getId()).partitionId(allianceEntity.getId()).set("belongAffairId", affairEntity.getId());//更新所属事务

        String superid = StringUtil.generateId(allianceEntity.getId(), SuperIdNumber.COMMON_CODE_LENGTH);
        allianceEntity.setOwnerRoleId(roleEntity.getId());
        allianceEntity.setRootAffairId(affairEntity.getId());
        allianceEntity.setCode(superid);
        allianceEntity.update();

        return allianceEntity;

    }

    @Override
    public boolean inSameAlliance(long userId1, long userId2) {
//        Integer result = (Integer) RoleEntity.getSession().findOneByNativeSql(Integer.class,
//                "select 1 from role r where r.user_id = ? and exists( select 1 from role l where l.user_id =? and l.alliance_id = r.alliance_id) limit 1",new ParameterBindings(userId1,userId2));
//
        return false;
    }

    @Override
    public boolean validName(String code) {
        return !AllianceEntity.dao.eq("code", code).exists();
    }

    @Override
    public boolean addAllianceCertification(AllianceCertificationForm allianceCertificationForm, long roleId, long allianceId) {
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
        allianceCertificationEntity.setCheckState(DealState.ToCheck);
        allianceCertificationEntity.save();

        AllianceEntity.dao.id(allianceId).set("verified", CertificationState.WaitCertificated);
        return true;
    }

    @Override
    public boolean editAllianceCertification(AllianceCertificationForm allianceCertificationForm, long roleId) {
        allianceCertificationForm.setRoleId(roleId);
        return AllianceCertificationEntity.dao.setByObject(allianceCertificationForm) > 0;
    }

    @Override
    public long getDefaultRoleIdFromAlliance(long allianceId) {
        RoleEntity _role = RoleEntity.dao.partitionId(allianceId).eq("user_id", userService.currentUserId()).eq("type", 1).selectOne("id");
        long roleId = _role.getId();
        return roleId;
    }

    @Override
    public List<AllianceEntity> getAllianceList() {
        StringBuilder sb = new StringBuilder("select * from alliance where id in (" +
                "select alliance_id from role where user_id = ? )");
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(userService.currentUserId());
        return AllianceEntity.dao.findListByNativeSql(sb.toString(), p);
    }

    @Override
    public List<SimpleRoleVO> getRoleByAlliance(long allianceId) {

        List<RoleEntity> roleEntityList = RoleEntity.dao.partitionId(allianceId).selectList("id", "title");
        List<SimpleRoleVO> result = new ArrayList<>();
        for (RoleEntity r : roleEntityList) {
            SimpleRoleVO one = new SimpleRoleVO(r.getId(), r.getTitle());
            result.add(one);
        }

        return result;
    }

    @Override
    public boolean verifyAllianceName(String name) {
        return AllianceEntity.dao.eq("name",name).exists();
    }

    @Override
    public boolean isCertificated(long allianceId) {
        return AllianceEntity.dao.id(allianceId).state(CertificationState.Normal).exists();
    }
}

