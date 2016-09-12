package cn.superid.webapp.model;

import cn.superid.jpa.orm.Dao;
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
@Table(name = "affair_member_application")
public class AffairMemberApplicationEntity extends ExecutableModel {
    public final static Dao<AffairMemberApplicationEntity> dao = new Dao<>(AffairMemberApplicationEntity.class);
    private long id;
    private long roleId;
    private long userId;
    private long affairId;
    private long allianceId;
    private long dealRoleId;
    private long dealUserId;
    private String dealReason;
    private int state = 0;//0表示未处理,1表示接受,2表示拒绝
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

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAffairId() {
        return affairId;
    }

    public void setAffairId(long affairId) {
        this.affairId = affairId;
    }

    public long getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(long allianceId) {
        this.allianceId = allianceId;
    }

    public long getDealRoleId() {
        return dealRoleId;
    }

    public void setDealRoleId(long dealRoleId) {
        this.dealRoleId = dealRoleId;
    }

    public long getDealUserId() {
        return dealUserId;
    }

    public void setDealUserId(long dealUserId) {
        this.dealUserId = dealUserId;
    }

    public String getDealReason() {
        return dealReason;
    }

    public void setDealReason(String dealReason) {
        this.dealReason = dealReason;
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
