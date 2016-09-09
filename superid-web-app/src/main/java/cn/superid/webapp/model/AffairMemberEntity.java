package cn.superid.webapp.model;

import cn.superid.jpa.orm.Dao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by njuTms on 16/8/31.
 */
@Table(name = "affair_member")
public class AffairMemberEntity extends ExecutableModel {
    public final static Dao<AffairMemberEntity> dao = new Dao<>(AffairMemberEntity.class);
    private long id;
    private long affairId;
    private long roleId;
    private long userId;
    private int type;
    private int state;
    private String permissions;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private long permissionGroupId;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAffairId() {
        return affairId;
    }

    public void setAffairId(long affairId) {
        this.affairId = affairId;
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

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
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

    public long getPermissionGroupId() {
        return permissionGroupId;
    }

    public void setPermissionGroupId(long permissionGroupId) {
        this.permissionGroupId = permissionGroupId;
    }
}
