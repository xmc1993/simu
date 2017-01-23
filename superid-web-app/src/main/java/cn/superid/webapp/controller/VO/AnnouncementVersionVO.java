package cn.superid.webapp.controller.VO;

import java.sql.Timestamp;

/**
 * Created by jizhenya on 17/1/23.
 */
public class AnnouncementVersionVO {

    private int version;
    private Timestamp createTime;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
