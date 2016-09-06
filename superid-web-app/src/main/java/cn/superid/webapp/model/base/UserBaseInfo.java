package cn.superid.webapp.model.base;

import cn.superid.jpa.annotation.Cacheable;
import cn.superid.jpa.orm.Dao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by xiaofengxu on 16/9/5.
 */
@Table(name = "user")
@Cacheable
public class UserBaseInfo extends ExecutableModel{
    public final static Dao<UserBaseInfo> dao = new Dao<>(UserBaseInfo.class);
    private long id;
    private String avatar;
    private int gender;
    private Timestamp birthday;
    private String username;

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
