package cn.superid.webapp.model;

import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by njuTms on 16/9/23.
 */
@Entity
@Table(name = "message")
public class MessageEntity extends ExecutableModel {
    private long id;
    private int type;
    private int subType;
    private long toRole;
    private long affairId;
    private String displayName;
    private long toUserId;
    private String content;

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public long getToRole() {
        return toRole;
    }

    public void setToRole(long toRole) {
        this.toRole = toRole;
    }

    public long getAffairId() {
        return affairId;
    }

    public void setAffairId(long affairId) {
        this.affairId = affairId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
