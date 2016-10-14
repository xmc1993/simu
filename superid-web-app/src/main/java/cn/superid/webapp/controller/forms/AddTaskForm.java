package cn.superid.webapp.controller.forms;

import java.sql.Timestamp;

/**
 * Created by jizhenya on 16/10/12.
 */
public class AddTaskForm {

    private String title;
    private String roles;
    private Timestamp endTime;
    private int priority;
    private int isRemind;
    private Timestamp remindTime;
    private int isCircle;
    private String circleDate;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getIsRemind() {
        return isRemind;
    }

    public void setIsRemind(int isRemind) {
        this.isRemind = isRemind;
    }

    public Timestamp getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Timestamp remindTime) {
        this.remindTime = remindTime;
    }

    public int getIsCircle() {
        return isCircle;
    }

    public void setIsCircle(int isCircle) {
        this.isCircle = isCircle;
    }

    public String getCircleDate() {
        return circleDate;
    }

    public void setCircleDate(String circleDate) {
        this.circleDate = circleDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
