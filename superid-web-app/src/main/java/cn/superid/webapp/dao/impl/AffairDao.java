package cn.superid.webapp.dao.impl;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.controller.forms.AffairInfo;
import cn.superid.webapp.dao.IAffairDao;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.service.vo.AffairTreeVO;
import cn.superid.webapp.service.vo.GetRoleVO;
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

    @Override
    public List<GetRoleVO> getOfficials(long affairId, long allianceId) {
        StringBuilder sql = new StringBuilder("select a.role_id as roleId , a.permissions as permissions , b.user_id as userId , b.title as title , d.name as affairName , d.id as affairId from " +
                "(select af.role_id , af.permissions from affair_member af where af.state = 1 and af.affair_id = ? and af.alliance_id = ? and af.permission_group_id < 4 ) a " +
                " join (select bf.title , bf.id , bf.belong_affair_id , bf.user_id from role bf where bf.alliance_id = ? ) b " +
                " join (select df.id , df.name from affair df where df.alliance_id = ? ) d " +
                " on a.role_id = b.id and b.belong_affair_id = d.id ");
        ParameterBindings p1 = new ParameterBindings();
        p1.addIndexBinding(affairId);
        p1.addIndexBinding(allianceId);
        p1.addIndexBinding(allianceId);
        p1.addIndexBinding(allianceId);
        return RoleEntity.dao.getSession().findListByNativeSql(GetRoleVO.class, sql.toString(), p1);
    }

    @Override
    public List<AffairTreeVO> getAffairTree(long allianceId, long userId) {
        StringBuilder sb = new StringBuilder("select a.id , a.parent_id , a.name , a.short_name , a.alliance_id , a.superid , a.public_type ,a.owner_role_id, b.is_stuck , a.path , b.role_id as roleId ,a.modify_time from " +
                "(select * from affair where alliance_id = ? and state = 0 ) a " +
                "left join (select role_id,affair_id,is_stuck from affair_user where alliance_id = ? and user_id = ? ) b " +
                "on a.id = b.affair_id ");
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(allianceId);
        p.addIndexBinding(allianceId);
        p.addIndexBinding(userId);
        return AffairEntity.getSession().findListByNativeSql(AffairTreeVO.class, sb.toString(), p);
    }

    @Override
    public List<AffairTreeVO> getAffairTreeByUser(long userId) {
        StringBuilder sb = new StringBuilder("select a.id , a.parent_id , a.name , a.short_name , a.alliance_id , a.superid ,a.owner_role_id, a.public_type , b.is_stuck , a.path , b.role_id as roleId,a.modify_time from " +
                "(select * from affair where alliance_id in (" +
                "select alliance_id from role where user_id = ? ) and state = 0 ) a " +
                "left join (select role_id,affair_id,is_stuck from affair_user where user_id = ? ) b " +
                "on a.id = b.affair_id ");
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(userId);
        p.addIndexBinding(userId);
        return AffairEntity.getSession().findListByNativeSql(AffairTreeVO.class, sb.toString(), p);
    }

    @Override
    public List<AffairInfo> getOutAllianceAffair(long userId) {
        StringBuilder sb = new StringBuilder("select * from " +
                "(select au.affair_id from affair_user au where au.user_id = ? and au.alliance_id not in " +
                "(select alu.alliance_id from alliance_user alu where alu.user_id = ?)) b " +
                "join affair a " +
                "on a.id = b.affair_id ");
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(userId);
        p.addIndexBinding(userId);
        return AffairEntity.getSession().findListByNativeSql(AffairInfo.class, sb.toString(), p);
    }
}
