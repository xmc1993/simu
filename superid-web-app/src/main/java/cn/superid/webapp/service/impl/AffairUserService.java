package cn.superid.webapp.service.impl;

import cn.superid.webapp.model.AffairUserEntity;
import cn.superid.webapp.service.IAffairUserService;
import org.springframework.stereotype.Service;

/**
 * Created by njuTms on 16/12/15.
 */
@Service
public class AffairUserService implements IAffairUserService {
    @Override
    public AffairUserEntity getAffairUser(long allianceId, long affairId, long userId) {
        return AffairUserEntity.dao.partitionId(allianceId).eq("affairId", affairId).eq("userId", userId).selectOne();
    }

    @Override
    public AffairUserEntity addAffairUser(long allianceId, long affairId, long userId, long roleId) {
        AffairUserEntity affairUserEntity = getAffairUser(allianceId,affairId,userId);
        if(affairUserEntity != null){
            affairUserEntity.setRoleId(roleId);
            affairUserEntity.update();
        }
        else {
            affairUserEntity = new AffairUserEntity();
            affairUserEntity.setAffairId(affairId);
            affairUserEntity.setAllianceId(allianceId);
            affairUserEntity.setRoleId(roleId);
            affairUserEntity.setUserId(userId);
            affairUserEntity.save();
        }
        return affairUserEntity;
    }

}
