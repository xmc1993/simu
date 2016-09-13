package cn.superid.webapp.service;

import cn.superid.webapp.service.forms.FileForm;
import cn.superid.webapp.service.forms.FolderForm;

import java.util.List;

/**
 * Created by xiaofengxu on 16/9/2.
 */
public interface IFileService {

    public List<FolderForm> getChildFolder(Long folderId);

    public List<FileForm> getChildFile(Long folderId);

    public boolean addFolder(Long folderId,String name,Long operationRoleId,Long affairId,Long taskId);

    public boolean removeFile(long id);


}
