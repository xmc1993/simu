package cn.superid.webapp.controller.VO;

import cn.superid.webapp.service.vo.AllianceRolesVO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by njuTms on 16/11/16.
 */
//登录后返回给前端的信息
public class LoginUserInfoVO {
    private long id;
    private String username;
    private String avatar;
    private long homepageAffairId;
    private long personalRoleId;
    private long personalAllianceId;
    private String superid;
    private boolean isAuthenticated;
    private int gender;
    private Map<Long,List<Object>> members;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getHomepageAffairId() {
        return homepageAffairId;
    }

    public void setHomepageAffairId(long homepageAffairId) {
        this.homepageAffairId = homepageAffairId;
    }

    public long getPersonalRoleId() {
        return personalRoleId;
    }

    public void setPersonalRoleId(long personalRoleId) {
        this.personalRoleId = personalRoleId;
    }

    public long getPersonalAllianceId() {
        return personalAllianceId;
    }

    public void setPersonalAllianceId(long personalAllianceId) {
        this.personalAllianceId = personalAllianceId;
    }

    public Map<Long, List<Object>> getMembers() {
        return members;
    }

    public void setMembers(Map<Long, List<Object>> members) {
        this.members = members;
    }

    public String getSuperid() {
        return superid;
    }

    public void setSuperid(String superid) {
        this.superid = superid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
