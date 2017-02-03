package cn.superid.webapp.dao;

import cn.superid.webapp.controller.forms.AffairInfo;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.service.vo.AffairTreeVO;
import cn.superid.webapp.service.vo.GetRoleVO;

import java.util.List;

/**
 * Created by xiaofengxu on 17/1/17.
 */
public interface IAffairDao {
    /**
     * @param allianceId
     * @param affairId
     * @param columns 需要查的列
     * @return
     */
    List<AffairEntity> getChildAffairs(long allianceId, long affairId, String... columns);

    List<GetRoleVO> getOfficials(long affairId, long allianceId);

    List<AffairTreeVO> getAffairTree(long allianceId, long userId);

    List<AffairTreeVO> getAffairTreeByUser(long userId);

    List<AffairInfo> getOutAllianceAffair(long userId);

}
