package cn.superid.webapp.controller.VO;

import java.sql.Timestamp;

/**
 * Created by jizhenya on 16/12/2.
 */
public class SimpleAnnouncementIdVO {

    private long announcementId;
    private Timestamp modifyTime;
    private long affairId;

    public SimpleAnnouncementIdVO(long announcementId, Timestamp modifyTime, long affairId) {
        this.announcementId = announcementId;
        this.modifyTime = modifyTime;
        this.affairId = affairId;
    }

    public SimpleAnnouncementIdVO(){

    }

    public long getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(long announcementId) {
        this.announcementId = announcementId;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public long getAffairId() {
        return affairId;
    }

    public void setAffairId(long affairId) {
        this.affairId = affairId;
    }
}
