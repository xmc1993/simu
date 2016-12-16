package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.Expr;
import cn.superid.webapp.controller.forms.AddTaskForm;
import cn.superid.webapp.enums.state.TaskState;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.TaskEntity;
import cn.superid.webapp.model.TaskLogEntity;
import cn.superid.webapp.model.TaskRoleEntity;
import cn.superid.webapp.service.IAnnouncementService;
import cn.superid.webapp.service.IFileService;
import cn.superid.webapp.service.IRoleService;
import cn.superid.webapp.service.ITaskService;
import cn.superid.webapp.service.forms.ContentState;
import cn.superid.webapp.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jizhenya on 16/10/12.
 */
@Service
public class TaskService  implements ITaskService{

    @Autowired
    private IFileService fileService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IAnnouncementService announcementService;

    @Override
    public TaskEntity addTask(long creator, long allianceId, long affairId, AddTaskForm taskForm) {
        //第一步:存task
        TaskEntity task = new TaskEntity();
        task.setAllianceId(allianceId);
        task.setCreator(creator);
        task.setAffairId(affairId);
        task.setState(1);
        task.setDescription(taskForm.getDescription());
        task.setCircleDate(taskForm.getCircleDate());
        task.setIsCircle(taskForm.getIsCircle());
        task.setIsRemind(taskForm.getIsRemind());
        task.setCreateTime(TimeUtil.getCurrentSqlTime());
        task.setIsRemind(taskForm.getIsRemind());
        task.setRemindTime(taskForm.getRemindTime());
        task.setTitle(taskForm.getTitle());
        task.save();

        //第二步,存role
        List<String> strs = Arrays.asList(taskForm.getRoles().split(","));
        for(String s : strs){
            TaskRoleEntity r = new TaskRoleEntity();
            r.setRoleId(Long.parseLong(s));
            r.setTaskId(task.getId());
            r.save();
        }

        //TODO:生成讨论组并关联,并把所有人加入讨论组

        //生成根文件夹
        AffairEntity affair = AffairEntity.dao.findById(affairId,allianceId);
        long folderId = fileService.createRootFolderForTask(allianceId,affairId,task.getId(),affair.getFolderId(),creator);
        task.setFolderId(folderId);
        TaskEntity.dao.partitionId(allianceId).id(task.getId()).set("folderId",folderId);

        //生成log
        StringBuilder message = new StringBuilder();
        message.append(roleService.getNameByRoleId(creator));
        message.append("于");
        message.append(TimeUtil.getCurrentSqlTime());
        message.append("创建任务");
        createLog(task.getId(),message.toString());

        return task;
    }

    @Override
    public boolean addRole(long role, long invitees, long taskId) {
        TaskRoleEntity newRole = new TaskRoleEntity();
        newRole.setTaskId(taskId);
        newRole.setRoleId(invitees);
        newRole.save();

        //生成log
        StringBuilder message = new StringBuilder();
        message.append(roleService.getNameByRoleId(role));
        message.append("邀请");
        message.append(roleService.getNameByRoleId(invitees));
        message.append("加入任务");
        createLog(taskId,message.toString());
        return true;
    }

    @Override
    public boolean removeRole(long role, long loser, long taskId) {
        TaskRoleEntity taskRole = TaskRoleEntity.dao.partitionId(taskId).eq("roleId",loser).selectOne();
        taskRole.delete();

        //生成log
        StringBuilder message = new StringBuilder();
        message.append(roleService.getNameByRoleId(role));
        message.append("将");
        message.append(roleService.getNameByRoleId(loser));
        message.append("移出任务");
        createLog(taskId,message.toString());
        return true;
    }

    @Override
    public boolean addAnnouncement(String title, long affairId, long allianceId, long taskId, long roleId, int isTop, int publicType, ContentState content) {
        boolean result = announcementService.createAnnouncement(title,affairId,allianceId,taskId,roleId,isTop,publicType,content);
        if(result){
            //生成log
            StringBuilder message = new StringBuilder();
            message.append(roleService.getNameByRoleId(roleId));
            message.append("创建了公告");
            createLog(taskId,message.toString());
            return true;
        }


        return false;
    }

    @Override
    public List<TaskEntity> getAllValidTask(long allianceId, long affairId, String... params) {
        List<TaskEntity> result = TaskEntity.dao.partitionId(allianceId).eq("affair_id",affairId).state(TaskState.OnGoing).selectList(params);
        return result;
    }

    private void createLog(long taskId , String message ){
        TaskLogEntity log = new TaskLogEntity();
        log.setTaskId(taskId);
        log.setMessage(message);
        log.setCreateTime(TimeUtil.getCurrentSqlTime());
        log.save();
    }
}
