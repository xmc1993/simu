package cn.superid.webapp.service.impl;

import cn.superid.jpa.orm.SQLDao;
import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AffairUserEntity;
import cn.superid.webapp.model.cache.RoleCache;
import cn.superid.webapp.service.IAffairUserService;
import cn.superid.webapp.service.vo.AffairUserVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by njuTms on 16/12/15.
 */
@Service
public class AffairUserService implements IAffairUserService{
    @Override
    public AffairUserEntity addAffairUser(long allianceId, long affairId, long roleId) {

        RoleCache roleCache = RoleCache.dao.findById(roleId);
        AffairUserEntity affairUserEntity = AffairUserEntity.dao.partitionId(allianceId).eq("affairId",affairId).eq("userId",roleCache.getUserId()).selectOne();

        if(affairUserEntity==null){
            affairUserEntity =new AffairUserEntity();
            affairUserEntity.setAffairId(affairId);
            affairUserEntity.setAllianceId(allianceId);
            affairUserEntity.setRoleId(roleId);
            affairUserEntity.setUserId(roleCache.getUserId());
            affairUserEntity.save();
        }else {
            affairUserEntity.setRoleId(roleId);//新角色作为当前角色
            affairUserEntity.update();
        }

        return affairUserEntity;
    }

    @Override
    public List<AffairUserVO> getAllAffairUsers(long allianceId, long affairId) {
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(affairId);
        p.addIndexBinding(allianceId);
        List<AffairUserVO> results = AffairUserEntity.getSession().findList(AffairUserVO.class, SQLDao.GET_AFFAIR_USERS.toString(),p);
        return results;
    }
}
