package cn.superid.webapp.dao;

import cn.superid.webapp.model.AffairEntity;

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



}
