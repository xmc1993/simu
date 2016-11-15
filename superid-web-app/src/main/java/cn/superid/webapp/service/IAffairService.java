package cn.superid.webapp.service;

import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.controller.forms.AffairInfo;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AffairMemberApplicationEntity;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.model.CoverEntity;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.security.IGetPermissions;
import cn.superid.webapp.service.forms.SimpleRoleForm;
import cn.superid.webapp.service.vo.AffairTreeVO;
import org.elasticsearch.index.engine.Engine;

import java.util.List;
import java.util.Map;

/**
 * Created by zp on 2016/7/26.
 */
public interface IAffairService  {

    public String getPermissions(String permissions,int permissionLevel,long affairId) throws Exception;


    public AffairEntity createAffair(CreateAffairForm createAffairForm) throws Exception;



    public AffairEntity createRootAffair(long allianceId,String name,long roleId,int type);

    /**
     * 根据前端传来的状态寻找对应的盟内的事务
     * @param allianceId
     * @param state
     * @return 返回值根据前端需要进行返回,现在未定义 TODO 返回值
     * @throws Exception
     */
    public List<AffairEntity> getAffairByState(long allianceId,int state) throws Exception;

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
     * 恢复失效事务
     * @param allianceId
     * @param affairId
     * @return
     * @throws Exception
     */
    public boolean validAffair(long allianceId,long affairId) throws Exception;

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
     * @param attribute 需要修改的属性,0暂时保留,暂时有1公开性,2事务名称,3事务描述
     * @param value 对应的值
     * @return
     * @throws Exception
     */
    public boolean modifyAffairInfo(long allianceId,long affairId,int attribute,Object value) throws Exception;

    public boolean modifyAffairInfo(long allianceId,long affairId,Integer publicType,String affairName,String description) throws Exception;

    /**
     *
     * @param allianceId
     * @param affairId
     * @param params 希望返回的数据
     * @return
     */
    public List<AffairEntity> getAllChildAffairs(long allianceId,long affairId,String... params);

    /**
     *
     * @param allianceId
     * @param affairId
     * @param urls 用逗号隔开的urls
     * @return
     */
    public boolean addCovers(long allianceId, long affairId, String urls);

    /**
     *
     * @param allianceId
     * @param affairId
     * @param coverId
     * @return
     */
    public boolean setDefaultCover(long allianceId, long affairId, long coverId);

    /**
     *
     * @param allianceId
     * @param affairId
     * @return
     */
    public List<SimpleRoleForm> getAllRoles(long allianceId, long affairId);

    /**
     *
     * @param allianceId
     * @param affairId
     * @return
     */
    public List<CoverEntity> getCovers(long allianceId, long affairId);

    /**
     *
     * @param allianceId
     * @param affairId
     * @return 0位是member数,1是文件数,2是公告数,3是任务数
     */
    public Map<String, Object> affairOverview(long allianceId, long affairId);

    /**
     * 判断一个事务是否是另一个的子事务
     * @param childAffairId
     * @param parentAffairId
     * @return
     */
    public boolean isChildAffair(long allianceId,long childAffairId, long parentAffairId);

    /**
     * 不用传参,参数为userId,从currentUser中拿
     * @return
     */
    public AffairTreeVO getAffairTree(long allianceId);


    public AffairInfo getAffairInfo(long allianceId,long affairId);

}
