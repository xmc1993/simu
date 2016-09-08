package cn.superid.webapp.model;

import cn.superid.jpa.orm.Dao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by jizhenya on 16/9/7.
 */
@Table(name = "task")
public class TaskEntity extends ExecutableModel{

    public final static Dao<TaskEntity> dao = new Dao<>(TaskEntity.class);
    private Long id;
    private Long affairId;
    private String title;
    private int state;
    private String description;
    private int isRemind;
    private Timestamp remindTime;
    private int isCircle;
    private String circleDate;
    private Timestamp createTime;
    private Timestamp endTime;
    private Long discussGroupId;

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAffairId() {
        return affairId;
    }

    public void setAffairId(Long affairId) {
        this.affairId = affairId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Long getDiscussGroupId() {
        return discussGroupId;
    }

    public void setDiscussGroupId(Long discussGroupId) {
        this.discussGroupId = discussGroupId;
    }
}
