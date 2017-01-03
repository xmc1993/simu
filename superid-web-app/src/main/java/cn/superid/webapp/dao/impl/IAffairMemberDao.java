package cn.superid.webapp.dao.impl;

import cn.superid.webapp.forms.AffairRoleCard;
import cn.superid.webapp.forms.SearchAffairRoleConditions;
import cn.superid.webapp.model.AffairMemberEntity;

import java.util.List;

/**
 * Created by xiaofengxu on 16/12/30.
 */
public interface IAffairMemberDao {
   /**
    * 根据条件查询一个事务内的角色
    *
    * @param allianceId
    * @param affairIds
    * @param conditions
    * @return
    */
   List<AffairRoleCard> searchAffairRoles(long allianceId, long affairId, SearchAffairRoleConditions conditions);

}