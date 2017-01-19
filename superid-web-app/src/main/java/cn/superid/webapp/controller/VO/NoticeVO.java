package cn.superid.webapp.controller.VO;

import cn.superid.webapp.notice.Link;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

/**
 * Created by jessiechen on 18/01/17.
 */
@ApiModel
public class NoticeVO {
    private long id;
    @ApiModelProperty(notes = "0未读 1已读")
    private short state;  //规则见ValidState类
    @ApiModelProperty(notes = "消息类型")
    private int type; //消息类型
    @ApiModelProperty(notes = "创建时间")
    private Timestamp createTime;
    @ApiModelProperty("接受消息的用户Id")
    private long userId;  //接受消息的用户Id
    @ApiModelProperty(notes = "消息内容")
    private String content;
    @ApiModelProperty(notes = "消息内容中链接描述数组")
    private Link[] urls;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Link[] getUrls() {
        return urls;
    }

    public void setUrls(Link[] urls) {
        this.urls = urls;
    }
}
