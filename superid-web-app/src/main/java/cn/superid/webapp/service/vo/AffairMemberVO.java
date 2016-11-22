package cn.superid.webapp.service.vo;

import cn.superid.jpa.annotation.PartitionId;
import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.webapp.model.AffairMemberEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Created by jizhenya on 16/11/22.
 */
public class AffairMemberVO {

    private long id;
    private long affairId;
    private long allianceId;
    private long roleId;
    private int state;
    private String permissions; //事务内的权限
    private Timestamp createTime;
    private Timestamp modifyTime;
    private int permissionLevel;
    private String title;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
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

    public int getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(int permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
