package cn.superid.webapp.service.impl;

import cn.superid.webapp.dao.SQLDao;
import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.model.AffairUserEntity;
import cn.superid.webapp.service.IAffairUserService;
import cn.superid.webapp.service.vo.AffairUserVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by njuTms on 16/12/15.
 */
@Service
public class AffairUserService implements IAffairUserService {
    @Override
    public AffairUserEntity isAffairUser(long allianceId, long affairId, long userId) {
        return AffairUserEntity.dao.partitionId(allianceId).eq("affairId", affairId).eq("userId", userId).selectOne();
    }

    @Override
    public AffairUserEntity addAffairUser(long allianceId, long affairId, long userId, long roleId) {
        AffairUserEntity affairUserEntity = isAffairUser(allianceId,affairId,userId);
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


    @Override
    public List<AffairUserVO> getAllAffairUsers(long allianceId, long affairId) {
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(affairId);
        p.addIndexBinding(allianceId);
        List<AffairUserVO> results = AffairUserEntity.getSession().findListByNativeSql(AffairUserVO.class, SQLDao.GET_AFFAIR_USERS.toString(), p);
        return results;
    }
}
