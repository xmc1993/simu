package cn.superid.webapp.dao.impl;

import cn.superid.webapp.dao.IAffairDao;
import cn.superid.webapp.model.AffairEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xiaofengxu on 17/1/17.
 */
@Component
public class AffairDao implements IAffairDao {
    @Override
    public List<AffairEntity> getChildAffairs(long allianceId, long affairId, String... columns) {
        AffairEntity currentAffair = AffairEntity.dao.partitionId(allianceId).id(affairId).selectOne("path");
        if(currentAffair==null){
            return null;
        }
        return AffairEntity.dao.partitionId(allianceId).lk("path",currentAffair.getPath()+"%").selectList(columns);
    }
}
