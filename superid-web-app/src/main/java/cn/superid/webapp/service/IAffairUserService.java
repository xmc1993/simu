package cn.superid.webapp.service;

import cn.superid.webapp.model.AffairUserEntity;
import cn.superid.webapp.service.vo.AffairUserVO;

import java.util.List;

/**
 * Created by njuTms on 16/12/15.
 */
public interface IAffairUserService {

    /**
     * 检测是否已经是该事务的成员,便于进行add或者update操作
     * @param allianceId
     * @param affairId
     * @param userId
     * @return
     */
    public AffairUserEntity isAffairUser(long allianceId,long affairId,long userId);

    /**
     * 先判断是否是该事务成员,如果是就更新,不是就添加
     * @param allianceId
     * @param affairId
     * @param roleId
     * @return
     */
    public AffairUserEntity addAffairUser(long allianceId,long affairId,long userId,long roleId);



    /**
     * 获取某个事务中的所有成员,返回类型见AffairUserVO
     * @param allianceId
     * @param affairId
     * @return
     */
    public List<AffairUserVO> getAllAffairUsers(long allianceId,long affairId);

}
