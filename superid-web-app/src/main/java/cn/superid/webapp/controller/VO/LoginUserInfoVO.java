package cn.superid.webapp.controller.VO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by njuTms on 16/11/16.
 */
//登录后返回给前端的信息
public class LoginUserInfoVO {
    private long id;
    private String username = "";
    private int gender;
    private Timestamp birthday ;
    private String avatar;
    private String mobile;
    private String email;
    private int publicType;
    private Map<Long,List<Long>> members;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPublicType() {
        return publicType;
    }

    public void setPublicType(int publicType) {
        this.publicType = publicType;
    }

    public Map<Long, List<Long>> getMembers() {
        return members;
    }

    public void setMembers(Map<Long, List<Long>> members) {
        this.members = members;
    }
}
