package cn.superid.webapp.model.cache;

import cn.superid.jpa.annotation.Cacheable;
import cn.superid.jpa.orm.CacheableDao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by xiaofengxu on 16/10/8.
 */
@Table(name = "affair_member")
@Cacheable( key = "afm")
public class AffairMemberCache extends ExecutableModel {
    public final static CacheableDao<AffairMemberCache> dao = new CacheableDao<>(AffairMemberCache.class);
    private long id;
    private long affairId;
    private long allianceId;
    private long roleId;
    private String permissions;
    private long permissionGroupId;
    private int state;

    @Id
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

    public long getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(long allianceId) {
        this.allianceId = allianceId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public long getPermissionGroupId() {
        return permissionGroupId;
    }

    public void setPermissionGroupId(long permissionGroupId) {
        this.permissionGroupId = permissionGroupId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
