package cn.superid.webapp.model;

import cn.superid.jpa.annotation.PartitionId;
import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by njuTms on 16/8/31.
 */
@Table(name = "affair")
public class AffairEntity extends ExecutableModel {
    public final static ConditionalDao<AffairEntity> dao = new ConditionalDao<>(AffairEntity.class);
    private long id;
    private long parentId = 0;
    private String superid = "";
    private String name = "";
    private String nameAbbr = "";
    private String logoUrl = "";
    private String description = "";
    private long ownerRoleId = 0;
    private long allianceId = 0;
    private int publicType = 0;
    private int type = 0; //原指个人事务还是盟事务
    private int state = 0;
    private int isFree = 0;
    private String path = ""; //以0-1-2此种形式记录本事务所在位置,也就是创建顺序
    private int level = 0; //以1开始,记录本事务所在层数
    private Timestamp createTime;
    private Timestamp modifyTime;
    private int guestVisible = 0;
    private int guestCreateDg = 0;
    private int pathIndex = 0; //创建顺序
    private long folderId = 0;
    private String shortName = "";
    private String covers = "";
    private int guestLimit = 0;
    private int guestNumber = 0;

    private List<AffairEntity> children;


    @Id
    @Column(name = "id")
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

    public long getOwnerRoleId() {
        return ownerRoleId;
    }

    public void setOwnerRoleId(long ownerRoleId) {
        this.ownerRoleId = ownerRoleId;
    }

    @PartitionId
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

    @Transient
    public List<AffairEntity> getChildren() {
        return children;
    }

    public void setChildren(List<AffairEntity> children) {
        this.children = children;
    }

    public String getSuperid() {
        return superid;
    }

    public void setSuperid(String superid) {
        this.superid = superid;
    }

    public String getCovers() {
        return covers;
    }

    public void setCovers(String covers) {
        this.covers = covers;
    }

    public int getGuestLimit() {
        return guestLimit;
    }

    public void setGuestLimit(int guestLimit) {
        this.guestLimit = guestLimit;
    }

    public int getGuestNumber() {
        return guestNumber;
    }

    public void setGuestNumber(int guestNumber) {
        this.guestNumber = guestNumber;
    }
}
