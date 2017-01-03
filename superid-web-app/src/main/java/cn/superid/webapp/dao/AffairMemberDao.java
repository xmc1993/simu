package cn.superid.webapp.dao;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.utils.StringUtil;
import cn.superid.webapp.dao.impl.IAffairMemberDao;
import cn.superid.webapp.forms.AffairRoleCard;
import cn.superid.webapp.forms.SearchAffairRoleConditions;
import cn.superid.webapp.model.AffairMemberEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xiaofengxu on 16/12/30.
 */
@Component
public class AffairMemberDao implements IAffairMemberDao{

    @Override
    public List<AffairRoleCard> searchAffairRoles(long allianceId, long affairId, SearchAffairRoleConditions conditions) {//TODO  先简单点来,等我redis再改善点,取出所有roleId,然后在内存里面查。。。。


        if(conditions.getLimit()<10||conditions.getLimit()>100) conditions.setLimit(20);


        boolean hasKey = StringUtil.notEmpty(conditions.getKey());
        String key = "%"+conditions.getKey()+"%";


        StringBuilder sql = new StringBuilder("select r.id as role_id,r.belong_affair_id,r.title,r.title_abbr,u.username,u.superid,u.name_abbr,u.avatar,ta.id as affairMemberId" +
                ",ta.permissions,a.name as belongAffairName from ");
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

        if(conditions.getActive()!=null){
            if(conditions.getActive()){
                sql.append(" and am.state=0 ");
            }else{
                sql.append(" and am.state>0 ");
            }
        }
        sql.append(" )  ta  join role r  join affair a join user u on a.id = r.belong_affair_id and ta.role_id = r.id and r.user_id = u.id where 1=1 ");

        if(hasKey){
            sql.append(" and ( r.title like ? or r.title_abbr like ? or u.superid like ? or u.username like ? or u.name_abbr like ? ) ");
            parameterBindings.addIndexBinding(key,5);
        }
        if(conditions.getInAlliance()!=null){//判断是不是同一个盟
            if(conditions.getInAlliance()){
                sql.append(" and r.alliance_id = ?");
                parameterBindings.addIndexBinding(allianceId);
            }else{
                sql.append(" and r.alliance_id <> ?");
                parameterBindings.addIndexBinding(allianceId);
            }
        }

        if(StringUtil.notEmpty(conditions.getLastTitlePY())){//以最后一个拼音往后取
            sql.append(" and r.title_abbr > ?");
            parameterBindings.addIndexBinding(conditions.getLastTitlePY());
        }

        sql.append(" order by r.title_abbr asc limit ? ");
        parameterBindings.addIndexBinding(conditions.getLimit());

        return (List<AffairRoleCard>) AffairMemberEntity.getSession().findListByNativeSql(AffairRoleCard.class,sql.toString(),parameterBindings.getIndexParametersArray());
    }

}
