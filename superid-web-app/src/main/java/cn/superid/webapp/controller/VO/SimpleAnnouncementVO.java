package cn.superid.webapp.controller.VO;


import javax.persistence.Transient;

/**
 * Created by jizhenya on 16/12/5.
 */
public class SimpleAnnouncementVO {

    private String title;
    private long id;
    private long affairId;
    private String thumbContent;
    private String affairName;

    @Transient
    private long creatorId;

    @Transient
    private String roleName;

    @Transient
    private String username;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAffairId() {
        return affairId;
    }

    public void setAffairId(long affairId) {
        this.affairId = affairId;
    }

    public String getThumbContent() {
        return thumbContent;
    }

    public void setThumbContent(String thumbContent) {
        this.thumbContent = thumbContent;
    }

    public String getAffairName() {
        return affairName;
    }

    public void setAffairName(String affairName) {
        this.affairName = affairName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }
}