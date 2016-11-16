package cn.superid.webapp.service;

import cn.superid.webapp.controller.forms.AddFileForm;
import cn.superid.webapp.service.forms.FileForm;
import cn.superid.webapp.service.forms.FolderForm;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;

/**
 * Created by xiaofengxu on 16/9/2.
 */
public interface IFileService {

    public List<FolderForm> getChildFolder(long folderId,long affairId , long allianceId);

    public List<FileForm> getChildFile(long folderId ,long affairId , long allianceId);

    public boolean addFolder(long folderId,String name,long operationRoleId,long affairId,long taskId , long allianceId);

    public boolean addFile(AddFileForm addFileForm , long affairId , long allianceId);

    public boolean removeFile(long id , long allianceId);

    public boolean removeFolder(long affairId,long folderId , long allianceId);

    public boolean renameFolder(long folderId,String name , long allianceId);

    public List<FileForm> getHistoryFile(long fileId , long allianceId);

    public long createRootFolderForTask(long allianceId,long affairId,long taskId,long folderId,long role);

    public long createRootFolderForAffair(long allianceId,long affairId,long role);

    public String condense_picture(CommonsMultipartFile picture,String big , String small , long allianceId);


}
