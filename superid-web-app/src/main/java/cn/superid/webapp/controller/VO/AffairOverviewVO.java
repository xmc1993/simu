package cn.superid.webapp.controller.VO;

import com.wordnik.swagger.annotations.ApiModel;

/**
 * Created by jessiechen on 17/01/17.
 */
@ApiModel
public class AffairOverviewVO {
    private int members;
    private int files;
    private int announcements;
    private int tasks;

    public int getTasks() {
        return tasks;
    }

    public void setTasks(int tasks) {
        this.tasks = tasks;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public int getFiles() {
        return files;
    }

    public void setFiles(int files) {
        this.files = files;
    }

    public int getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(int announcements) {
        this.announcements = announcements;
    }
}
