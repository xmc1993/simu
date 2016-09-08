package cn.superid.webapp.model;

import cn.superid.jpa.orm.Dao;
import cn.superid.jpa.orm.ExecutableModel;
import cn.superid.webapp.utils.TimeUtil;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by njuTms on 16/8/31.
 */
@Table(name = "affair")
public class AffairEntity extends ExecutableModel {
    public final static Dao<AffairEntity> dao = new Dao<>(AffairEntity.class);
    private Long id;
    private Long parentId;
    private String name ="";
    private String logoUrl="";
    private String description="";
    private Long createRoleId;
    private Long allianceId;
    private int publicType;
    private int type;
    private int state;
    private int isFree;
    private String path ="";
    private int level;
    private Timestamp createTime = TimeUtil.getCurrentSqlTime();
    private Timestamp modifyTime = TimeUtil.getCurrentSqlTime() ;
    private String videoUrl ="";
    private String videoImg ="";
    private int guestVisible;
    private int guestCreateDg;
    private int isVideo;
    private int index;
    private int pathIndex;


    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreateRoleId() {
        return createRoleId;
    }

    public void setCreateRoleId(Long createRoleId) {
        this.createRoleId = createRoleId;
    }

    public Long getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(Long allianceId) {
        this.allianceId = allianceId;
    }

    public int getPublicType() {
        return publicType;
    }

    public void setPublicType(int publicType) {
        this.publicType = publicType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoImg() {
        return videoImg;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }

    public int getGuestVisible() {
        return guestVisible;
    }

    public void setGuestVisible(int guestVisible) {
        this.guestVisible = guestVisible;
    }

    public int getGuestCreateDg() {
        return guestCreateDg;
    }

    public void setGuestCreateDg(int guestCreateDg) {
        this.guestCreateDg = guestCreateDg;
    }

    public int getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(int isVideo) {
        this.isVideo = isVideo;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPathIndex() {
        return pathIndex;
    }

    public void setPathIndex(int pathIndex) {
        this.pathIndex = pathIndex;
    }
}
