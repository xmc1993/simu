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

    public final static ConditionalDao<AnnouncementHistoryEntity> dao = new ConditionalDao<>(AnnouncementHistoryEntity.class);

    private long id;
    private long announcementId;
    private String title;
    private long roleId;
    private String content;
    private Timestamp createTime ;
    private int version;
    private String increment;
    private String decrement;
    private long modifierId;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @PartitionId
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

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
