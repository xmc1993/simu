package cn.superid.webapp.model.cache;

import cn.superid.jpa.annotation.Cacheable;
import cn.superid.jpa.orm.CacheableDao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jizhenya on 16/9/12.
 */
@Table(name = "role")
@Cacheable( key = "rl")
public class RoleCache extends ExecutableModel {
    public final static CacheableDao<RoleCache> dao = new CacheableDao<>(RoleCache.class);
    private long id;
    private long userId;
    private long allianceId;
    private String title;
    private String permissions;
    private int state;

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(long allianceId) {
        this.allianceId = allianceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
