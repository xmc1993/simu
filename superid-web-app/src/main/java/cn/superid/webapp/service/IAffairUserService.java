package cn.superid.webapp.service;

import cn.superid.webapp.model.AffairUserEntity;
import cn.superid.webapp.service.vo.AffairUserVO;

import java.util.List;

/**
 * Created by njuTms on 16/12/15.
 */
public interface IAffairUserService {
    /**
     * 在添加affairMember的时候必然会调用,
     * @param allianceId
     * @param affairId
     * @param roleId
     * @return
     */
    public AffairUserEntity addAffairUser(long allianceId,long affairId,long roleId);


    /**
     * 获取某个事务中的所有成员,返回类型见AffairUserVO
     * @param allianceId
     * @param affairId
     * @return
     */
    public List<AffairUserVO> getAllAffairUsers(long allianceId,long affairId);

}
