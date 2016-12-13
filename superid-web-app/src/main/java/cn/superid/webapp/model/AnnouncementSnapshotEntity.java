package cn.superid.webapp.model;

import cn.superid.jpa.annotation.PartitionId;
import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jizhenya on 16/12/12.
 */
@Table(name = "announcement_snapshot")
public class AnnouncementSnapshotEntity extends ExecutableModel {

    public final static ConditionalDao<AnnouncementSnapshotEntity> dao = new ConditionalDao<>(AnnouncementSnapshotEntity.class);

    private long id;
    private int version = 0;
    private String content = "";
    private long modifierId;
    private String title = "";
    private long announcementId;
    private long hsitoryId;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getModifierId() {
        return modifierId;
    }

    public void setModifierId(long modifierId) {
        this.modifierId = modifierId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @PartitionId
    public long getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(long announcementId) {
        this.announcementId = announcementId;
    }

    public long getHsitoryId() {
        return hsitoryId;
    }

    public void setHsitoryId(long hsitoryId) {
        this.hsitoryId = hsitoryId;
    }
}
