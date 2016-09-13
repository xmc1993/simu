package cn.superid.webapp.service.forms;

import java.sql.Timestamp;

/**
 * Created by jizhenya on 16/9/12.
 */
public class FileForm {

    private Long id;
    private String fileId;
    private String name;
    private String uploaderName;
    private Long uploaderId;
    private Timestamp createTime;
    private Long size;

    public FileForm(Long id,String fileId, String name, String uploaderName, Long uploaderId, Timestamp createTime,Long size) {
        this.id = id;
        this.fileId = fileId;
        this.name = name;
        this.uploaderName = uploaderName;
        this.uploaderId = uploaderId;
        this.createTime = createTime;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public Long getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(Long uploaderId) {
        this.uploaderId = uploaderId;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
