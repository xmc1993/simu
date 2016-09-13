package cn.superid.webapp.service.impl;

import cn.superid.webapp.model.FileEntity;
import cn.superid.webapp.model.FolderEntity;
import cn.superid.webapp.service.IFileService;
import cn.superid.webapp.service.IRoleService;
import cn.superid.webapp.service.forms.FileForm;
import cn.superid.webapp.service.forms.FolderForm;
import cn.superid.webapp.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaofengxu on 16/9/2.
 */
@Service
public class FileService implements IFileService{

    @Autowired
    private IRoleService roleService;

    @Override
    public List<FolderForm> getChildFolder(long folderId,long affairId) {

        FolderEntity folder = FolderEntity.dao.findById(folderId,affairId);
        if(folder == null){
            return null;
        }
        List<FolderEntity> folders = null;
        if(folder.getTaskId() == null ){
            folders = FolderEntity.dao.eq("affair_id",folder.getAffairId()).eq("state",1).lk("path",folder.getPath()+"%").selectList();
        }else{
            folders = FolderEntity.dao.eq("task_id",folder.getTaskId()).eq("state",1).lk("path",folder.getPath()+"%").selectList();
        }

        if(folders == null ){
            return null;
        }

        List<FolderForm> result = new ArrayList<>();
        for(FolderEntity f : folders){
            result.add(new FolderForm(f.getName(),f.getId()));
        }

        return result;
    }

    @Override
    public List<FileForm> getChildFile(long folderId,long affairId) {
        FolderEntity folder = FolderEntity.dao.findById(folderId,affairId);

        if(folder == null){
            return null;
        }
        List<FileEntity> files = FileEntity.dao.eq("folder_id",folderId).eq("state",1).selectList();
        if(files == null){
            return null;
        }
        List<FileForm> result = new ArrayList<>();
        for(FileEntity f : files){
            boolean hasHistory = false;
            if(f.getHistoryId()!= ""){
                hasHistory = true;
            }
            result.add(new FileForm(f.getId(),f.getFileId(),f.getName(),roleService.getNameByRoleId(f.getUploader()),f.getUploader(),f.getCreateTime(),f.getSize(),hasHistory));
        }

        return result;
    }

    @Override
    public boolean addFolder(long folderId, String name, long operationRoleId,long affairId,long taskId) {
        FolderEntity folder = new FolderEntity();
        FolderEntity parent = FolderEntity.dao.findById(folderId);
        folder.setName(name);
        int count = 0 ;

        if(taskId == 0){
            count = FolderEntity.dao.eq("affair_id",affairId).eq("parent_id",folderId).count();

        }else{
            count = FolderEntity.dao.eq("task_id",taskId).eq("parent_id",folderId).count();
        }
        folder.setPath(parent.getPath()+"/"+(count+1));
        folder.setAffairId(affairId);
        folder.setTaskId(taskId);
        folder.setCreateTime(TimeUtil.getCurrentSqlTime());
        folder.setUploader(operationRoleId);
        folder.save();


        return true;
    }

    @Override
    public boolean removeFile(long id,long folderId) {
        FileEntity file = FileEntity.dao.findById(id,folderId);
        if(file == null | file.getState() == 0){
            return false;
        }
        file.setState(0);
        file.update();
        return true;
    }
}
