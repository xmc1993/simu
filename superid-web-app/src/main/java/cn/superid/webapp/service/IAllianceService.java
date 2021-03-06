package cn.superid.webapp.service;

import cn.superid.webapp.controller.VO.SimpleRoleVO;
import cn.superid.webapp.forms.AllianceCertificationForm;
import cn.superid.webapp.forms.AllianceCreateForm;
import cn.superid.webapp.model.AllianceCertificationEntity;
import cn.superid.webapp.model.AllianceEntity;
import cn.superid.webapp.model.RoleEntity;

import java.util.List;

/**
 * Created by zp on 2016/7/26.
 */
public interface IAllianceService {

    public String getPermissions(long alliance,long roleId) throws Exception;

    AllianceEntity createAlliance(AllianceCreateForm allianceCreateForm);

    boolean inSameAlliance(long userId1,long userId2);

    boolean validName(String name);

    boolean addAllianceCertification(AllianceCertificationForm allianceCertificationForm,long roleId,long allianceId);

    boolean editAllianceCertification(AllianceCertificationForm allianceCertificationForm,long roleId);

    public long getDefaultRoleIdFromAlliance(long allianceId);

    public List<AllianceEntity> getAllianceList();

    public List<SimpleRoleVO> getRoleByAlliance(long allianceId);

    public boolean verifyAllianceName(String name);

    /**
     * 该盟是否已验证
     * @param allianceId
     * @return
     */
    boolean isCertificated(long allianceId);


}
