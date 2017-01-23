package cn.superid.webapp.controller.forms;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by jizhenya on 16/10/8.
 */
public class AnnouncementForm {

    private long id;
    private String title;
    private long creatorId;
    private String content;
    private int isTop ;
    private int publicType ;
    private int state;
    private Timestamp createTime ;
    private Timestamp modifyTime ;
    private long modifierId;
    private String roleName;
    private String username;
    private String avatar;
    private int version;

    private List<EditDistanceForm> historys;
    private List<String> entityMaps;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public int getPublicType() {
        return publicType;
    }

    public void setPublicType(int publicType) {
        this.publicType = publicType;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
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

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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
