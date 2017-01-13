package cn.superid.webapp.model;

import cn.superid.jpa.annotation.PartitionId;
import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by jizhenya on 16/9/26.
 */
@Table(name = "announcement_history")
public class AnnouncementHistoryEntity extends ExecutableModel {

    public final static ConditionalDao dao = new ConditionalDao(AnnouncementHistoryEntity.class);

    private long id;
    private long announcementId;
    private String title;
    private long creatorId;
    private Timestamp createTime ;
    private int version;
    private String increment;
    private String decrement;
    private long modifierId;
    private int state;
    private String thumbContent;
    private Timestamp modifyTime ;
    private long affairId;
    private long allianceId;
    private String entityMap;
    private long creatorUserId;
    private long modifierUserId;
    private int isTop = 0;
    private int publicType = 0;

    public AnnouncementHistoryEntity(){}

    public AnnouncementHistoryEntity(AnnouncementEntity announcementEntity){
        announcementId = announcementEntity.getId();
        title = announcementEntity.getTitle();
        creatorId = announcementEntity.getModifierId();
        createTime = announcementEntity.getModifyTime();
        version = announcementEntity.getVersion();
        decrement = announcementEntity.getDecrement();
        modifierId = announcementEntity.getModifierId();
        state = announcementEntity.getState();
        thumbContent = announcementEntity.getThumbContent();
        modifyTime = announcementEntity.getModifyTime();
        affairId = announcementEntity.getAffairId();
        allianceId = announcementEntity.getAllianceId();
        creatorUserId = announcementEntity.getModifierUserId();
        modifierUserId = announcementEntity.getModifierUserId();
        isTop = announcementEntity.getIsTop();
        publicType = announcementEntity.getPublicType();
    }

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getIncrement() {
        return increment;
    }

    public void setIncrement(String increment) {
        this.increment = increment;
    }

    public String getDecrement() {
        return decrement;
    }

    public void setDecrement(String decrement) {
        this.decrement = decrement;
    }

    public long getModifierId() {
        return modifierId;
    }

    public void setModifierId(long modifierId) {
        this.modifierId = modifierId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getThumbContent() {
        return thumbContent;
    }

    public void setThumbContent(String thumbContent) {
        this.thumbContent = thumbContent;
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

    @PartitionId
    public long getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(long allianceId) {
        this.allianceId = allianceId;
    }

    public String getEntityMap() {
        return entityMap;
    }

    public void setEntityMap(String entityMap) {
        this.entityMap = entityMap;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public long getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(long creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public long getModifierUserId() {
        return modifierUserId;
    }

    public void setModifierUserId(long modifierUserId) {
        this.modifierUserId = modifierUserId;
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
}
