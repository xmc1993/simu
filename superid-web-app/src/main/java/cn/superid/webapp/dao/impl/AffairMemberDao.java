package cn.superid.webapp.dao.impl;

import cn.superid.jpa.util.Pagination;
import cn.superid.jpa.util.ParameterBindings;
import cn.superid.utils.ArrayUtil;
import cn.superid.utils.StringUtil;
import cn.superid.webapp.dao.IAffairMemberDao;
import cn.superid.webapp.forms.AffairRoleCard;
import cn.superid.webapp.forms.SearchAffairMemberConditions;
import cn.superid.webapp.forms.SearchAffairRoleConditions;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.service.vo.AffairMemberSearchVo;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by xiaofengxu on 16/12/30.
 */
@Component
public class AffairMemberDao implements IAffairMemberDao {

    @Override
    public List<AffairRoleCard> searchAffairRoles(long allianceId, long affairId, SearchAffairRoleConditions conditions) {//TODO  先简单点来,等我redis再改善点,取出所有roleId,然后在内存里面查。。。。


        if (conditions.getLimit() < 10 || conditions.getLimit() > 100) conditions.setLimit(20);


        boolean hasKey = StringUtil.notEmpty(conditions.getKey());
        String key = "%" + conditions.getKey() + "%";


        StringBuilder sql = new StringBuilder("select r.id as role_id,r.belong_affair_id,r.title as role_title ,r.title_abbr,u.username,u.id as user_id ,u.name_abbr,u.avatar, u.gender,ta.id as affairMemberId" +
                ",ta.permissions,a.name as belongAffairName , alliance.id as allianceId,alliance.name as allianceName from ");
        ParameterBindings parameterBindings = new ParameterBindings();
//        sql.append("(select *  from affair_member am where am.alliance_id = ? and affair_id in (0 ");//查出满足所有要求的affairmember
//        parameterBindings.addIndexBinding(allianceId);

//
//        for(int i=0;i<affairIds.length;i++){
//            sql.append(",?");
//            parameterBindings.addIndexBinding(affairIds[i]);
//        }
//        sql.append(") ");

        sql.append("(select *  from affair_member am where am.alliance_id = ? and affair_id =? ");//查出满足所有要求的affairmember

        parameterBindings.addIndexBinding(allianceId);
        parameterBindings.addIndexBinding(affairId);

        if (conditions.getActive() != null) {
            if (conditions.getActive()) {
                sql.append(" and am.state=0 ");
            } else {
                sql.append(" and am.state>0 ");
            }
        }
        sql.append(" )  ta  join role r  join affair a join user u join alliance on a.id = r.belong_affair_id and ta.role_id = r.id and r.user_id = u.id and alliance.id = r.alliance_id where 1=1 ");

        if (hasKey) {
            sql.append(" and ( r.title like ? or r.title_abbr like ? or u.username like ? or u.name_abbr like ? ) ");
            parameterBindings.addIndexBinding(key, 4);
        }
        if (conditions.getInAlliance() != null) {//判断是不是同一个盟
            if (conditions.getInAlliance()) {
                sql.append(" and r.alliance_id = ?");
            } else {
                sql.append(" and r.alliance_id <> ?");
            }
            parameterBindings.addIndexBinding(allianceId);
        }

        if (StringUtil.notEmpty(conditions.getLastTitlePY())) {//以最后一个拼音往后取
            sql.append(" and r.title_abbr > ?");
            parameterBindings.addIndexBinding(conditions.getLastTitlePY());
        }

        sql.append(" order by am.modify_time asc limit ? ");
        parameterBindings.addIndexBinding(conditions.getLimit());

        return (List<AffairRoleCard>) AffairMemberEntity.getSession().findListByNativeSql(AffairRoleCard.class, sql.toString(), parameterBindings.getIndexParametersArray());
    }

    @Override
    public List<AffairMemberSearchVo>  searchAffairMembers(long allianceId, long affairId, SearchAffairMemberConditions conditions,Pagination pagination) {
        StringBuilder sb = new StringBuilder("select distinct u.id,u.username as username , u.superid as superid ,u.gender as gender,r.title as roleTitle,a.name as belongAffair" +
                " from (select affair_id,role_id from affair_member where alliance_id= ? and affair_id ");
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(allianceId);
        if (conditions.isIncludeSubAffair()) {
            AffairEntity affairEntity = AffairEntity.dao.findById(affairId, allianceId);
            if (affairEntity == null) return Collections.emptyList();
            else {
                List<Long> idList = AffairEntity.getSession().findListByNativeSql(Long.class, "select id from affair where alliance_id=? and path like ?", allianceId, affairEntity.getPath() + "%");
                if (idList == null)
                    idList = Arrays.asList(affairId);
                sb.append(" in (").append(ArrayUtil.join(idList.toArray(), ",")).append(")) am ");
            }

        } else {
            sb.append("= ? ) am ");
            p.addIndexBinding(affairId);
        }
        sb.append("join (select id,user_id,belong_affair_id,title from role where alliance_id= ?) r on am.role_id=r.id ");
        p.addIndexBinding(allianceId);
        sb.append("join (select id, level from affair where alliance_id= ? )a2 on am.affair_id=a2.id ");
        p.addIndexBinding(allianceId);
        sb.append("join (select id,name from affair where alliance_id= ?) a on r.belong_affair_id=a.id ");
        p.addIndexBinding(allianceId);
        sb.append("join (select user_id from alliance_user where state= ? and alliance_id= ? ) au on au.user_id=r.user_id ");
        if(conditions.isAllianceUser())p.addIndexBinding(0);
        else p.addIndexBinding(1);
        p.addIndexBinding(allianceId);
        sb.append("join (select id,username,superid,gender from user ");
        if (cn.superid.jpa.util.StringUtil.notEmpty(conditions.getKey())) {
            sb.append("where username like ? or name_abbr like ? ");
            p.addIndexBinding("%" + conditions.getKey() + "%");
            p.addIndexBinding("%" + conditions.getKey() + "%");
        }
        sb.append(") u on r.user_id=u.id ");
        sb.append(" order by ");
        switch (conditions.getSortColumn()) {
            case "name":
                sb.append("u.username");
                break;
            case "gender":
                sb.append("u.gender");
                break;
            case "role":
                sb.append("r.title");
                break;
            case "affair":
                sb.append("a2.level");
                break;
            default:
                sb.append("u.username");
                break;
        }
        if (conditions.isReverseSort()) sb.append(" desc ");
        else sb.append(" asc ");
        return AffairMemberEntity.getSession().findListByNativeSql(AffairMemberSearchVo.class, sb.toString(), p, pagination);
    }

    /*
    @Override
    public List<AffairRoleCard> getAllAffairRoles(long allianceId, long affairId) {
        StringBuilder sb = new StringBuilder("select am.role_id, r.title as roleTitle,r.belong_affair_id as homepageAffairId, b.homepageAffairName, r.user_id, u.username,u.gender,u.avatar from affair_member am " +
                "join role r on r.id = am.role_id " +
                "join user u on u.id = r.user_id " +
                "join (select a.id as affairId ,a.name as homepageAffairName , alliance.name as allianceName from affair a \n" +
                "      join alliance on alliance.id = a.alliance_id) b on b.affairId = r.belong_affair_id \n" +
                "where am.alliance_id = ? and am.affair_id = ?");
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(allianceId);
        p.addIndexBinding(affairId);
        List<AffairRoleCard> cards = AffairMemberEntity.getSession().findListByNativeSql(AffairRoleCard.class,sb.toString(),p);
        return cards;
    }
    */

}
