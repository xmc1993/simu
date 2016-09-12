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
    private long id;
    private String name;
    private String path;
    private long affair_id;
    private long task_id;
    private Timestamp createTime = TimeUtil.getCurrentSqlTime();
    private long uploader;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public long getAffair_id() {
        return affair_id;
    }

    public void setAffair_id(long affair_id) {
        this.affair_id = affair_id;
    }

    public long getTask_id() {
        return task_id;
    }

    public void setTask_id(long task_id) {
        this.task_id = task_id;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public long getUploader() {
        return uploader;
    }

    public void setUploader(long uploader) {
        this.uploader = uploader;
    }
}
