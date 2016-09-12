package cn.superid.webapp.model;

import cn.superid.jpa.orm.Dao;
import cn.superid.jpa.orm.ExecutableModel;
import cn.superid.webapp.utils.TimeUtil;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by jizhenya on 16/9/9.
 */
@Table(name = "folder")
public class FolderEntity extends ExecutableModel {
    public final static Dao<FolderEntity> dao = new Dao<>(FolderEntity.class);
    private Long id;
    private String name;
    private String path;
    private Long affairId;
    private Long taskId;
    private Timestamp createTime = TimeUtil.getCurrentSqlTime();
    private Long uploader;
    private long parentId;

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getUploader() {
        return uploader;
    }

    public void setUploader(Long uploader) {
        this.uploader = uploader;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public Long getAffairId() {
        return affairId;
    }

    public void setAffairId(Long affairId) {
        this.affairId = affairId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
