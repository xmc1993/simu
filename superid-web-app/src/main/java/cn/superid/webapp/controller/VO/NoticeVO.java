package cn.superid.webapp.controller.VO;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

/**
 * Created by jessiechen on 18/01/17.
 */
@ApiModel
public class NoticeVO {
    private int id;
    @ApiModelProperty(notes = "")
    private short state;  //规则见ValidState类
    private int type; //消息类型
    private Timestamp createTime;
    private long userId;  //接受消息的用户Id

    private long linkId;     //链接Id，有可能是邀请的Id，也有可能是任务的Id，根据type来具体解释
    private long fromUserId;  //消息发起者的userId
    private String fromUserName;  //消息发起者姓名
    private String fromRoleTitle;  //消息发起者的职务
    private String allianceName;  //盟名称
    private String taskName;       //任务名称
    private String affairName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getLinkId() {
        return linkId;
    }

    public void setLinkId(long linkId) {
        this.linkId = linkId;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getFromRoleTitle() {
        return fromRoleTitle;
    }

    public void setFromRoleTitle(String fromRoleTitle) {
        this.fromRoleTitle = fromRoleTitle;
    }

    public String getAllianceName() {
        return allianceName;
    }

    public void setAllianceName(String allianceName) {
        this.allianceName = allianceName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAffairName() {
        return affairName;
    }

    public void setAffairName(String affairName) {
        this.affairName = affairName;
    }
}
