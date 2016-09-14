package cn.superid.webapp.service.impl;

import cn.superid.webapp.controller.forms.AddFileForm;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.FileEntity;
import cn.superid.webapp.model.FolderEntity;
import cn.superid.webapp.service.IFileService;
import cn.superid.webapp.service.IRoleService;
import cn.superid.webapp.service.forms.FileForm;
import cn.superid.webapp.service.forms.FolderForm;
import cn.superid.webapp.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            folders = FolderEntity.dao.partitionId(affairId).state(1).eq("parent_id",folder.getId()).selectList();
        }else{
            folders = FolderEntity.dao.eq("task_id",folder.getTaskId()).state(1).eq("parent_id",folder.getId()).partitionId(affairId).selectList();
        }

        if(folders == null ){
            return null;
        }

        List<FolderForm> result = new ArrayList<>();
        for(FolderEntity f : folders){
            //计算该目录下所有文件数量
            int count = FileEntity.dao.partitionId(affairId).lk("path",f.getPath()+"/%").count();

            result.add(new FolderForm(f.getName(),f.getId(),count));
        }

        return result;
    }

    @Override
    public List<FileForm> getChildFile(long folderId,long affairId) {
        FolderEntity folder = FolderEntity.dao.findById(folderId,affairId);

        if(folder == null){
            return null;
        }
        List<FileEntity> files = FileEntity.dao.partitionId(affairId).eq("folder_id",folderId).state(1).selectList();
        if(files == null){
            return null;
        }
        List<FileForm> result = new ArrayList<>();
        for(FileEntity f : files){
            boolean hasHistory = false;
            if(!f.getHistoryId().equals("")){
                hasHistory = true;
            }
            result.add(new FileForm(f.getId(),f.getFileId(),f.getName(),roleService.getNameByRoleId(f.getUploader()),f.getUploader(),f.getCreateTime(),f.getSize(),hasHistory));
        }

        return result;
    }

    @Override
    public boolean addFolder(long folderId, String name, long operationRoleId,long affairId,long taskId) {
        FolderEntity folder = new FolderEntity();
        FolderEntity parent = FolderEntity.dao.findById(folderId,affairId);
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
        folder.setState(1);
        folder.setParentId(folderId);
        folder.save();


        return true;
    }

    @Override
    @Transactional
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
        file.setAffairId(form.getAffairId());

        //计算path

        int count = FileEntity.dao.partitionId(form.getFolderId()).count();
        file.setPath(folder.getPath()+"/"+(count+1));

        //第三步,计算history_id
        FileEntity latest = FileEntity.dao.partitionId(form.getAffairId()).eq("folder_id",form.getFolderId()).state(1).eq("name",form.getFileName()).selectOne();
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
    public boolean removeFile(long id,long affairId) {
        FileEntity file = FileEntity.dao.findById(id,affairId);
        if(file == null | file.getState() == 0){
            return false;
        }
        file.setState(0);
        file.update();
        return true;
    }

    @Override
    @Transactional
    public boolean removeFolder(long affairId, long folderId) {
        //第一步,本文件夹状态为置为失效
        FolderEntity folder = FolderEntity.dao.findById(folderId,affairId);
        if(folder == null | folder.getState() == 0){
            return false;
        }
        folder.setState(0);
        folder.update();


        //第二步,子文件夹状态为置为失效
        List<FolderEntity> folders = null;
        if(folder.getTaskId() == 0L ){
            folders = FolderEntity.dao.partitionId(affairId).state(1).lk("path",folder.getPath()+"/%").selectList();
        }else{
            folders = FolderEntity.dao.eq("task_id",folder.getTaskId()).state(1).lk("path",folder.getPath()+"/%").partitionId(affairId).selectList();
        }

        if(folders != null & folders.size() > 0){
            for(FolderEntity f: folders){
                f.setState(0);
                f.update();
            }
        }


        //第三步,文件状态为置为失效
        List<FileEntity> files = FileEntity.dao.partitionId(affairId).lk("path",folder.getPath()+"/%").selectList();
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

    @Override
    public boolean renameFolder(long affairId, long folderId, String name) {
        FolderEntity folder = FolderEntity.dao.findById(folderId,affairId);
        if(folder == null){
            return false;
        }
        folder.setName(name);
        folder.update();

        return true;
    }

    @Override
    public List<FileForm> getHistoryFile(long fileId, long affairId) {
        FileEntity file = FileEntity.dao.findById(fileId,affairId);
        if(file == null){
            return null;
        }
        String[] ids = file.getHistoryId().split(",");
        List<FileForm> result = new ArrayList<>();
        for(int i = 0 ; i < ids.length ; i++){
            FileEntity f = FileEntity.dao.findById(Integer.parseInt(ids[i]),affairId);
            if(f != null){
                result.add(new FileForm(f.getId(),f.getFileId(),f.getName(),roleService.getNameByRoleId(f.getUploader()),f.getUploader(),f.getCreateTime(),f.getSize(),false));
            }
        }
        return result;
    }

    @Override
    public long createRootFolder(long allianceId,long affairId, long taskId, long folderId,long role) {
        AffairEntity affair = AffairEntity.dao.findById(affairId,allianceId);
        long id = 0;
        if(affair == null){
            return 0;
        }
        if(taskId == 0){
            //表示创建事务的根文件夹
            FolderEntity folder = new FolderEntity();
            folder.setName(affair.getName()+"的根文件夹");
            folder.setPath("1");
            folder.setAffairId(affairId);
            folder.setTaskId(0);
            folder.setCreateTime(TimeUtil.getCurrentSqlTime());
            folder.setUploader(role);
            folder.setParentId(0);
            folder.setState(1);
            folder.save();
            id = folder.getId();
        }else{
            FolderEntity parent = FolderEntity.dao.findById(folderId,affairId);
            if(parent == null){
                return 0;
            }
            FolderEntity folder = new FolderEntity();
            folder.setName(affair.getName()+"的根文件夹");
            int count = FolderEntity.dao.eq("task_id",taskId).eq("parent_id",folderId).partitionId(affairId).count();
            folder.setPath(parent.getPath()+"/"+(count+1));
            folder.setAffairId(affairId);
            folder.setTaskId(taskId);
            folder.setCreateTime(TimeUtil.getCurrentSqlTime());
            folder.setUploader(role);
            folder.setParentId(folderId);
            folder.setState(1);
            folder.save();
            id = folder.getId();
        }

        return id;
    }
}
