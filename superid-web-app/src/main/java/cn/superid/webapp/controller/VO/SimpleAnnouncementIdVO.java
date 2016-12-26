package cn.superid.webapp.controller.VO;

import cn.superid.jpa.orm.ConditionalDao;

import java.sql.Timestamp;

/**
 * Created by jizhenya on 16/12/2.
 */
public class SimpleAnnouncementIdVO {

    public final static ConditionalDao<SimpleAnnouncementIdVO> dao = new ConditionalDao<>(SimpleAnnouncementIdVO.class);

    private long announcementId;
    private Timestamp modifyTime;
    private long affairId;
    private int isTop;

    public SimpleAnnouncementIdVO(long announcementId, Timestamp modifyTime, long affairId, int isTop) {
        this.announcementId = announcementId;
        this.modifyTime = modifyTime;
        this.affairId = affairId;
        this.isTop = isTop;
    }

    public SimpleAnnouncementIdVO(){}

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

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }
}
