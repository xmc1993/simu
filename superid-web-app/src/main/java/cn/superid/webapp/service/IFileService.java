package cn.superid.webapp.service;

import cn.superid.webapp.controller.forms.AddFileForm;
import cn.superid.webapp.service.forms.FileForm;
import cn.superid.webapp.service.forms.FolderForm;

import java.util.List;

/**
 * Created by xiaofengxu on 16/9/2.
 */
public interface IFileService {

    public List<FolderForm> getChildFolder(long folderId,long affairId);

    public List<FileForm> getChildFile(long folderId,long affairId);

    public boolean addFolder(long folderId,String name,long operationRoleId,long affairId,long taskId);

    public boolean addFile(AddFileForm addFileForm);

    public boolean removeFile(long id,long folderId);

    public boolean removeFolder(long affairId,long folderId);


}
