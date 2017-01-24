package cn.superid.webapp.controller.VO;

import java.beans.Transient;
import java.sql.Timestamp;

/**
 * Created by jizhenya on 17/1/23.
 */
public class ModifyAnnouncementResponseVO {
    private long announcementId;
    private String title;
    private int version;
    private Timestamp modifyTime;
    private long modifierId;
    private long modifierUserId;
    private String roleName;
    private String username;
    private String thumbContent;

    public long getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(long announcementId) {
        this.announcementId = announcementId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public long getModifierId() {
        return modifierId;
    }

    public void setModifierId(long modifierId) {
        this.modifierId = modifierId;
    }

    public long getModifierUserId() {
        return modifierUserId;
    }

    public void setModifierUserId(long modifierUserId) {
        this.modifierUserId = modifierUserId;
    }

    public String getThumbContent() {
        return thumbContent;
    }

    public void setThumbContent(String thumbContent) {
        this.thumbContent = thumbContent;
    }

    @Transient
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Transient
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
