package cn.superid.webapp.controller.forms;

import java.security.Timestamp;

/**
 * Created by jizhenya on 16/9/13.
 */
public class AddFileForm {

    private String fileId;
    private String fileName;
    private long folderId;
    private long uploader;
    private long size;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFolderId() {
        return folderId;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public long getUploader() {
        return uploader;
    }

    public void setUploader(long uploader) {
        this.uploader = uploader;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
