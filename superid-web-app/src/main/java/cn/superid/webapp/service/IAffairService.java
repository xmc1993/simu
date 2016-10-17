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

    /**
     * 获取所有一级子事务
     * @param allianceId
     * @param affairId
     * @return id,allianceId,name
     * @throws Exception
     */
    public List<AffairEntity> getAllDirectChildAffair(long allianceId,long affairId) throws Exception;


    /**
     * 失效事务
     * @param affairId
     * @return
     */
    public boolean disableAffair(Long allianceId,Long affairId) throws Exception;

    /**
     * 在正式操作一个事务之前,检测是否有特殊情况需要处理
     * @param allianceId
     * @param affairId
     * @return 0表示无特殊情况,1表示有子事务,2表示有交易
     * @throws Exception
     */
    public int canGenerateAffair(long allianceId,long affairId) throws Exception;

    public boolean moveAffair(long allianceId,long affairId,long targetAffairId,long roleId) throws Exception;


    /**
     *
     * @param allianceId
     * @param affairId
     * @param params 希望返回的数据
     * @return
     */
    public List<AffairEntity> getAllChildAffairs(long allianceId,long affairId,String... params);
}
