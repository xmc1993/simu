package cn.superid.webapp.service;

import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AffairMemberApplicationEntity;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.security.IGetPermissions;
import org.elasticsearch.index.engine.Engine;

import java.util.List;

/**
 * Created by zp on 2016/7/26.
 */
public interface IAffairService  {

    public String getPermissions(String permissions,long permissionGroupId,long affairId) throws Exception;


    public AffairEntity createAffair(CreateAffairForm createAffairForm) throws Exception;



    public AffairEntity createRootAffair(long allianceId,String name,long roleId,int type);

    public List<AffairEntity> getAllChildAffair(long allianceId,long affairId) throws Exception;


    /**
     * 失效事务
     * @param affairId
     * @return
     */
    public boolean disableAffair(Long allianceId,Long affairId) throws Exception;
}
