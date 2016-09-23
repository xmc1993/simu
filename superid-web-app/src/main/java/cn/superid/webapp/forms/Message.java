package cn.superid.webapp.forms;

import java.io.Serializable;

/**
 * Created by xmc1993 on 16/9/12.
 */
public class Message implements Serializable{
    private int type;
    private int subType;
    private long toRoleId;
    private long fromUserId;
    private long relatedId;
    private Long affairId;
    private String displayName;
    private Long toUserId;
    private int state;//0未读,1已读
    private String content;


    public int getType() {
        return type;
    }


    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public long getToRoleId() {
        return toRoleId;
    }

    public void setToRoleId(long toRoleId) {
        this.toRoleId = toRoleId;
    }

    public long getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(long relatedId) {
        this.relatedId = relatedId;
    }

    public Long getAffairId() {
        return affairId;
    }

    public void setAffairId(Long affairId) {
        this.affairId = affairId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
