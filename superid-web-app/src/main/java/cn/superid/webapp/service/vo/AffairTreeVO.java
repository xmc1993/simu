package cn.superid.webapp.service.vo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jizhenya on 16/11/14.
 */
public class AffairTreeVO implements Comparable<AffairTreeVO> {
    private long id;
    private long parentId;
    private String superid;
    private String name ="";
    private String logoUrl="";
    private String description="";
    private long createRoleId;
    private long allianceId;
    private int publicType;
    private int type; //原指个人事务还是盟事务
    private int state;
    private int isFree;
    private String path =""; //以0-1-2此种形式记录本事务所在位置,也就是创建顺序
    private int level; //以1开始,记录本事务所在层数
    private Timestamp createTime ;
    private Timestamp modifyTime ;
    private String videoUrl ="";
    private String videoImg ="";
    private int guestVisible;
    private int guestCreateDg;
    private int isVideo;
    private int pathIndex; //创建顺序
    private long folderId;
    private String shortName;
    private long roleId;
    private int isStuck;//是否置顶

    private List<AffairTreeVO> children = new ArrayList<>();



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getSuperid() {
        return superid;
    }

    public void setSuperid(String superid) {
        this.superid = superid;
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

    public long getCreateRoleId() {
        return createRoleId;
    }

    public void setCreateRoleId(long createRoleId) {
        this.createRoleId = createRoleId;
    }

    public long getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(long allianceId) {
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

    public int getPathIndex() {
        return pathIndex;
    }

    public void setPathIndex(int pathIndex) {
        this.pathIndex = pathIndex;
    }

    public long getFolderId() {
        return folderId;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getIsStuck() {
        return isStuck;
    }

    public void setIsStuck(int isStuck) {
        this.isStuck = isStuck;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public List<AffairTreeVO> getChildren() {
        return children;
    }

    public void setChildren(List<AffairTreeVO> children) {
        this.children = children;
    }

    @Override
    public int compareTo(AffairTreeVO a) {
        if(this.getPath().length()>a.getPath().length()){
            return 1;
        }else if(this.getPath().length()<a.getPath().length()){
            return -1;
        }
        return 0;
    }
}
