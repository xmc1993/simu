package cn.superid.webapp.dao.impl;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.dao.IAllianceDao;
import cn.superid.webapp.model.AffairEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jizhenya on 17/2/3.
 */
@Component
public class AllianceDao implements IAllianceDao{
    @Override
    public List<Long> getAllianceIdOfUser(long userId) {
        StringBuilder sql = new StringBuilder("select distinct alliance_id from role where user_id = ? ");
        ParameterBindings pb = new ParameterBindings();
        pb.addIndexBinding(userId);
        return AffairEntity.getSession().findListByNativeSql(Long.class, sql.toString(), pb);
    }
}
