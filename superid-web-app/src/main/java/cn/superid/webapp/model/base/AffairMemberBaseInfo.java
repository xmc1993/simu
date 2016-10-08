package cn.superid.webapp.model.base;

import cn.superid.jpa.annotation.Cacheable;
import cn.superid.jpa.orm.CacheableDao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by njuTms on 16/10/8.
 */
@Table(name = "affair_member")
@Cacheable(key = "am")
public class AffairMemberBaseInfo extends ExecutableModel {
    public final static CacheableDao<AffairMemberBaseInfo> dao = new CacheableDao<>(AffairMemberBaseInfo.class);

    private long id;
    private String permissions;
    private long permissionGroupId;

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
