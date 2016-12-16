package cn.superid.webapp.service;

import cn.superid.webapp.model.AffairUserEntity;

/**
 * Created by njuTms on 16/12/15.
 */
public interface IAffairUserService {
    /**
     * 在添加affairMember的时候必然会调用,
     * @param allianceId
     * @param affairId
     * @param roleId
     * @param userId
     * @return
     */
    public AffairUserEntity addAffairUser(long allianceId,long affairId,long roleId,long userId);


}
