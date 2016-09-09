package cn.superid.webapp.service;

import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.security.IGetPermissions;
import org.elasticsearch.index.engine.Engine;

/**
 * Created by zp on 2016/7/26.
 */
public interface IAffairService  {

    public String getPermissions(long affairId,long roleId);

    public AffairEntity createAffair(CreateAffairForm createAffairForm) throws Exception;

    public String applyForEnterAffair(long affairId,long roleId);

    public AffairEntity createRootAffair(long allianceId,String name,long roleId,int type);
}
