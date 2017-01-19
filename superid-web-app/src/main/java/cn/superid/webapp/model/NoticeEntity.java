package cn.superid.webapp.model;

import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by jessiechen on 17/01/17.
 */
@Table(name = "notice")
public class NoticeEntity extends ExecutableModel {
    public final static ConditionalDao dao = new ConditionalDao(NoticeEntity.class);
    private long id;
    private short state;  //规则见ValidState类
    private int type; //消息类型
    private Timestamp createTime;
    private long userId;  //接受消息的用户Id
    private String content;   //消息的具体内容，格式是json字符串
    private String urls;

    @Id
    @Column(name = "id")
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }
}

