package cn.superid.webapp.service;

import cn.superid.webapp.controller.VO.AffairOverviewVO;
import cn.superid.webapp.controller.forms.AffairInfo;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.service.forms.ModifyAffairInfoForm;
import cn.superid.webapp.service.forms.SimpleRoleForm;
import cn.superid.webapp.service.vo.AffairTreeVO;

import java.util.List;

/**
 * Created by zp on 2016/7/26.
 */
public interface IAffairService {

    String getPermissions(String permissions, int permissionLevel, long affairId) throws Exception;


    AffairInfo createAffair(CreateAffairForm createAffairForm) throws Exception;


    AffairEntity createRootAffair(long allianceId, long userId,String name, long roleId, int type, String logo);

    /**
     * 根据前端传来的状态寻找对应的盟内的事务
     *
     * @param allianceId
     * @param state
     * @return 返回值根据前端需要进行返回, 现在未定义 TODO 返回值
     */
    List<AffairEntity> getAffairByState(long allianceId, int state);

    /**
     * 获取所有一级子事务
     *
     * @param allianceId
     * @param affairId
     * @return id, allianceId, name
     * @throws Exception
     */
    List<AffairEntity> getAllDirectChildAffair(long allianceId, long affairId) throws Exception;


    /**
     * 失效事务
     *
     * @param affairId
     * @return
     */
    boolean disableAffair(Long allianceId, Long affairId);

    /**
     * 恢复失效事务
     *
     * @param allianceId
     * @param affairId
     * @return
     * @throws Exception
     */
    boolean enableAffair(long allianceId, long affairId) throws Exception;

    /**
     * 在正式操作一个事务之前,检测是否有特殊情况需要处理
     * 比如点击失效,然后调用这个接口,确认按钮再调用disable方法
     *
     * @param allianceId
     * @param affairId
     * @return 见responseCode
     */
    int canGenerateAffair(long allianceId, long affairId);

    int moveAffair(long allianceId, long affairId, long targetAffairId, long roleId) throws Exception;

    boolean handleMoveAffair(long allianceId, long affairId, long targetAffairId, long roleId, boolean isAgree);

    /**
     * 参数为空则没有修改
     *
     * @param allianceId
     * @param affairId
     * @param modifyAffairInfoForm
     * @return
     */
    boolean modifyAffairInfo(long allianceId, long affairId, ModifyAffairInfoForm modifyAffairInfoForm);

    /**
     * @param allianceId
     * @param affairId
     * @param params     希望返回的数据
     * @return
     */
    List<AffairEntity> getAllChildAffairs(long allianceId, long affairId, String... params);

    /**
     * @param allianceId
     * @param affairId
     * @param urls       用逗号隔开的urls
     * @return
     */
    boolean updateCovers(long allianceId, long affairId, String urls);

    boolean updateTags(long allianceId, long affairId, String tags);

    /**
     * 获取
     *
     * @param allianceId
     * @param affairId
     * @return
     */
    List<SimpleRoleForm> getAllRoles(long allianceId, long affairId);


    /**
     * @param allianceId
     * @param affairId
     * @return
     */
    AffairOverviewVO affairOverview(long allianceId, long affairId);

    /**
     * 判断一个事务是否是另一个的子事务
     *
     * @param childAffairId
     * @param parentAffairId
     * @return
     */
    boolean isChildAffair(long allianceId, long childAffairId, long parentAffairId);

    /**
     * 不用传参,参数为userId,从currentUser中拿
     *
     * @return
     */
    List<AffairTreeVO> getAffairTreeByUser();

    AffairTreeVO getAffairTree(long allianceId);

    /**
     * 获取事务信息
     *
     * @param allianceId
     * @param affairId
     * @return
     */
    AffairInfo getAffairInfo(long allianceId, long affairId, long roleId);

    /**
     * 切换角色
     *
     * @param affairId
     * @param allianceId
     * @param newRoleId
     * @return
     */
    boolean switchRole(long affairId, long allianceId, long newRoleId);

    /**
     * 获取一个用户参与的盟外事务
     * 比如以A盟的角色去参加B盟的事务,但是该用户不是B盟的人
     *
     * @return
     */
    List<AffairInfo> getOutAllianceAffair();

    /**
     * 好多地方用到,先抽出来
     * @param allianceId
     * @param affairId
     * @return
     */
    boolean isValidAffair(long allianceId, long affairId);

    /**
     *
     * @param affairId
     * @return
     */
    boolean stickAffair(long allianceId,long affairId,boolean isStuck);

    /**
     *
     * @param affairId
     * @return
     */
    boolean setHomepage(long affairId);


}
