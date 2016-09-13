package cn.superid.webapp.service.impl;

import cn.superid.webapp.controller.forms.AddFileForm;
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
        if(folder.getTaskId() == 0L ){
            folders = FolderEntity.dao.partitionId(affairId).state(1).lk("path",folder.getPath()+"%").selectList();
        }else{
            folders = FolderEntity.dao.eq("task_id",folder.getTaskId()).state(1).lk("path",folder.getPath()+"%").partitionId(affairId).selectList();
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
        List<FileEntity> files = FileEntity.dao.partitionId(folderId).state(1).selectList();
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
            count = FolderEntity.dao.partitionId(affairId).eq("parent_id",folderId).count();

        }else{
            //如果task不为空,表示是在任务中查看文件,则要过滤affair中其他任务文件
            count = FolderEntity.dao.eq("task_id",taskId).eq("parent_id",folderId).partitionId(affairId).count();
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
    public boolean addFile(AddFileForm form) {
        FolderEntity folder = FolderEntity.dao.findById(form.getFolderId(),form.getAffairId());
        if(folder == null){
            return false;
        }

        //第一步,初始化file
        FileEntity file = new FileEntity();
        file.setState(1);
        file.setCreateTime(TimeUtil.getCurrentSqlTime());
        file.setFolderId(form.getFolderId());
        file.setFileId(form.getFileId());
        file.setUploader(form.getUploader());
        file.setSize(form.getSize());
        file.setName(form.getFileName());

        //计算path

        int count = FileEntity.dao.partitionId(form.getFolderId()).count();
        file.setPath(folder.getPath()+"/"+(count+1));

        //第三步,计算history_id
        FileEntity latest = FileEntity.dao.partitionId(form.getFolderId()).state(1).eq("name",form.getFileName()).selectOne();
        if(latest != null){
            if(latest.getHistoryId().equals("")){
                file.setHistoryId(latest.getId()+"");
            }else{
                file.setHistoryId(latest.getHistoryId()+","+latest.getId());
            }

            latest.setState(2);
            latest.update();
        }else{
            file.setHistoryId("");
        }
        file.save();

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

    @Override
    public boolean removeFolder(long affairId, long folderId) {
        //第一步,本文件夹状态为置为失效
        FolderEntity folder = FolderEntity.dao.findById(folderId,affairId);
        if(folder == null){
            return false;
        }
        folder.setState(0);
        folder.update();


        //第二步,子文件夹状态为置为失效
        List<FolderEntity> folders = null;
        if(folder.getTaskId() == 0L ){
            folders = FolderEntity.dao.partitionId(affairId).state(1).lk("path",folder.getPath()+"%").selectList();
        }else{
            folders = FolderEntity.dao.eq("task_id",folder.getTaskId()).state(1).lk("path",folder.getPath()+"%").partitionId(affairId).selectList();
        }

        if(folders != null & folders.size() > 0){
            return false;
        }
        for(FolderEntity f: folders){
            f.setState(0);
            f.update();
        }

        //第三步,文件状态为置为失效
        List<FileEntity> files = FileEntity.dao.partitionId(folderId).state(1).selectList();
        if(files == null){
            return false;
        }
        List<FileForm> result = new ArrayList<>();
        for(FileEntity f : files){
            f.setState(0);
            f.update();
        }


        return true;
    }
}
