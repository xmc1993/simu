package cn.superid.webapp.model;

import cn.superid.jpa.annotation.PartitionId;
import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by njuTms on 16/9/9.
 */
@Entity
@Table(name = "affair_member_invitation")
public class AffairMemberInvitationEntity extends ExecutableModel {
    public final static ConditionalDao<AffairMemberInvitationEntity> dao = new ConditionalDao<>(AffairMemberInvitationEntity.class);
    private long id;
    private long affairId;
    private long inviteRoleId;
    private long beInvitedRoleId;
    private long permissionGroup;
    private int state = 0;
    private String reason = "";
    private Timestamp createTime;
    private Timestamp modifyTime;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @PartitionId
    public long getAffairId() {
        return affairId;
    }

    public void setAffairId(long affairId) {
        this.affairId = affairId;
    }

    public long getInviteRoleId() {
        return inviteRoleId;
    }

    public void setInviteRoleId(long inviteRoleId) {
        this.inviteRoleId = inviteRoleId;
    }

    public long getBeInvitedRoleId() {
        return beInvitedRoleId;
    }

    public void setBeInvitedRoleId(long beInvitedRoleId) {
        this.beInvitedRoleId = beInvitedRoleId;
    }

    public long getPermissionGroup() {
        return permissionGroup;
    }

    public void setPermissionGroup(long permissionGroup) {
        this.permissionGroup = permissionGroup;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }
}
