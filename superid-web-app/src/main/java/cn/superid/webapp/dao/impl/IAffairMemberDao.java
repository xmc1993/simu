package cn.superid.webapp.dao.impl;

import cn.superid.webapp.forms.AffairRoleCard;
import cn.superid.webapp.forms.SearchAffairMemberConditions;
import cn.superid.webapp.forms.SearchAffairRoleConditions;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.service.vo.AffairMemberSearchVo;

import java.util.List;

/**
 * Created by xiaofengxu on 16/12/30.
 */
public interface IAffairMemberDao {
    /**
     * 根据条件查询一个事务内的角色
     *
     * @param allianceId
     * @param affairId
     * @param conditions
     * @return
     */
    List<AffairRoleCard> searchAffairRoles(long allianceId, long affairId, SearchAffairRoleConditions conditions);


    /**
     * 搜索事务的盟内成员列表，并提供分页和多条件排序
     * @param allianceId
     * @param affairId
     * @param conditions
     * @return
     */
    List<AffairMemberSearchVo> searchAffairMembers(long allianceId, long affairId, SearchAffairMemberConditions conditions);

}