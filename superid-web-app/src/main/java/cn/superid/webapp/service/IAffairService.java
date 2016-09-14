package cn.superid.webapp.service;

import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AffairMemberApplicationEntity;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.security.IGetPermissions;
import org.elasticsearch.index.engine.Engine;

/**
 * Created by zp on 2016/7/26.
 */
public interface IAffairService  {

    public String getPermissions(Long allianceId,Long affairId,Long roleId) throws Exception;

    public AffairEntity createAffair(CreateAffairForm createAffairForm) throws Exception;

    public String applyForEnterAffair(Long allianceId,Long affairId,Long roleId);

    public AffairEntity createRootAffair(long allianceId,String name,long roleId,int type);


    /**
     * 通过申请的id找到事务的成员
     * @param applicationId
     * @return
     */
    public AffairMemberApplicationEntity findAffairMemberApplicationById(Long affairId,Long applicationId);

    /**
     * 同意加入事务的申请
     * @param applicationId
     * @param dealRoleId
     * @param dealReason
     * @return
     * @throws Exception
     */
    public AffairMemberEntity agreeAffairMemberApplication(Long allianceId,Long affairId,Long applicationId, Long dealRoleId,String dealReason) throws Exception;

    /**
     * 拒绝加入事务的申请
     * @param applicationId
     * @param dealRoleId
     * @param dealReason
     * @return
     * @throws Exception
     */
    public AffairMemberApplicationEntity rejectAffairMemberApplication(Long affairId,Long applicationId, Long dealRoleId,String dealReason) throws Exception;

    /**
     * 失效事务
     * @param affairId
     * @return
     */
    public boolean disableAffair(Long allianceId,Long affairId) throws Exception;
}
