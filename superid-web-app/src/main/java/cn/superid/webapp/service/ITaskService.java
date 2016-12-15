package cn.superid.webapp.service;

import cn.superid.webapp.controller.forms.AddTaskForm;
import cn.superid.webapp.model.TaskEntity;
import cn.superid.webapp.service.forms.ContentState;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by jizhenya on 16/10/11.
 */
public interface ITaskService {

    public TaskEntity addTask(long creator , long allianceId , long affairId , AddTaskForm taskForm);

    public boolean addRole(long role , long invitees , long taskId );

    public boolean removeRole(long role , long loser , long taskId);

    public boolean addAnnouncement(String title , long affairId , long allianceId, long taskId , long roleId , int isTop , int publicType , ContentState content);

    public List<TaskEntity> getAllValidTask(long allianceId, long affairId, String... params);

}
