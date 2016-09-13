package cn.superid.webapp.model.base;

import cn.superid.jpa.annotation.Cacheable;
import cn.superid.jpa.orm.Dao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jizhenya on 16/9/12.
 */
@Table(name = "role")
@Cacheable
public class RoleBaseInfo  extends ExecutableModel {
    public final static Dao<RoleBaseInfo> dao = new Dao<>(RoleBaseInfo.class);
    private long id;
    private long userId;
    private long allianceId;
    private String title;

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
}
