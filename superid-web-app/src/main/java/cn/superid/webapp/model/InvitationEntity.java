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
@Table(name = "invitation")
public class InvitationEntity extends ExecutableModel {
    public final static ConditionalDao dao = new ConditionalDao(InvitationEntity.class);
    private long id;
    private long allianceId;
    private long affairId;
    private long inviteRoleId;
    private long inviteUserId;
    private String inviteReason;
    private long beInvitedRoleId;
    private long beInvitedUserId;
    private String beInvitedRoleTitle;
    private String dealReason = "";
    private String permissions;
    private int state = 0; //0等待处理,1同意,2拒绝
    private int invitationType;
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
    public long getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(long allianceId) {
        this.allianceId = allianceId;
    }

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

    public long getInviteUserId() {
        return inviteUserId;
    }

    public void setInviteUserId(long inviteUserId) {
        this.inviteUserId = inviteUserId;
    }

    public String getInviteReason() {
        return inviteReason;
    }

    public void setInviteReason(String inviteReason) {
        this.inviteReason = inviteReason;
    }

    public long getBeInvitedRoleId() {
        return beInvitedRoleId;
    }

    public void setBeInvitedRoleId(long beInvitedRoleId) {
        this.beInvitedRoleId = beInvitedRoleId;
    }

    public long getBeInvitedUserId() {
        return beInvitedUserId;
    }

    public void setBeInvitedUserId(long beInvitedUserId) {
        this.beInvitedUserId = beInvitedUserId;
    }

    public String getBeInvitedRoleTitle() {
        return beInvitedRoleTitle;
    }

    public void setBeInvitedRoleTitle(String beInvitedRoleTitle) {
        this.beInvitedRoleTitle = beInvitedRoleTitle;
    }

    public String getDealReason() {
        return dealReason;
    }

    public void setDealReason(String dealReason) {
        this.dealReason = dealReason;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public int getInvitationType() {
        return invitationType;
    }

    public void setInvitationType(int invitationType) {
        this.invitationType = invitationType;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
