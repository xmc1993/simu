package cn.superid.webapp.controller.VO;

/**
 * Created by jizhenya on 16/12/2.
 */
public class SimpleAnnouncementIdVO {

    private long announcementId;
    private long modifyTime;

    public SimpleAnnouncementIdVO(long announcementId, long modifyTime) {
        this.announcementId = announcementId;
        this.modifyTime = modifyTime;
    }

    public SimpleAnnouncementIdVO(){

    }

    public long getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(long announcementId) {
        this.announcementId = announcementId;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }
}
