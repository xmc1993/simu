package cn.superid.webapp.dao;
import cn.superid.webapp.controller.VO.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by jizhenya on 17/1/4.
 */
public interface IAnnouncementDao {

    public List<SimpleAnnouncementIdVO> getIdByAffair(long affairId, long allianceId , boolean isContainChild);

    public List<SimpleAnnouncementVO> getOverview(String ids, long allianceId);

    public List<AnnouncementVersionVO> getAllVersion(long allianceId , long announcementId);

    public List<SimpleDraftIdVO> getDraftByAffair(long affairId, long allianceId, long roleId);

    public List<SimpleAnnouncementIdVO> searchAnnouncement(String content, Long affairId, Long allianceId, boolean containChild);

    public List<SimpleAnnouncementHistoryVO> getAnnouncementHistoryList(long affairId, long allianceId, int count, Timestamp time);
}
