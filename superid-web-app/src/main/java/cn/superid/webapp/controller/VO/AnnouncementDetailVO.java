package cn.superid.webapp.controller.VO;

import cn.superid.webapp.controller.forms.AnnouncementForm;
import cn.superid.webapp.controller.forms.EditDistanceForm;

import java.util.List;

/**
 * Created by jizhenya on 17/1/22.
 */
public class AnnouncementDetailVO {

    private AnnouncementForm announcement;
    private List<EditDistanceForm> historys;
    private List<String> entityMaps;

    public AnnouncementDetailVO(AnnouncementForm announcement, List<EditDistanceForm> historys, List<String> entityMaps) {
        this.announcement = announcement;
        this.historys = historys;
        this.entityMaps = entityMaps;
    }

    public AnnouncementDetailVO(){}

    public AnnouncementForm getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(AnnouncementForm announcement) {
        this.announcement = announcement;
    }

    public List<EditDistanceForm> getHistorys() {
        return historys;
    }

    public void setHistorys(List<EditDistanceForm> historys) {
        this.historys = historys;
    }

    public List<String> getEntityMaps() {
        return entityMaps;
    }

    public void setEntityMaps(List<String> entityMaps) {
        this.entityMaps = entityMaps;
    }
}
