package cn.superid.webapp.forms;

import java.sql.Timestamp;

/**
 * Created by xiaofengxu on 16/9/6.
 */
public class EditUserBaseInfo {
    private String avatar;
    private int gender;
    private Timestamp birthday;
    private String username;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
