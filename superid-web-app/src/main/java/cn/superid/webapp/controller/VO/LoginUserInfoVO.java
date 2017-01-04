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
    private String username = "";
    private int gender;
    private Timestamp birthday ;
    private String avatar;
    private String mobile;
    private String email;
    private int publicType;
    private long homepageAffairId;
    private long personalRoleId;
    private long personalAllianceId;
    private String superid;
    private String address;
    private boolean isAuthenticated;

    private Map<Long,List<Object>> members;
    //private List<UserAllianceRolesVO> roles;
    private List<AllianceRolesVO> roles;


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

    public List<AllianceRolesVO> getRoles() {
        return roles;
    }

    public void setRoles(List<AllianceRolesVO> roles) {
        this.roles = roles;
    }

    public String getSuperid() {
        return superid;
    }

    public void setSuperid(String superid) {
        this.superid = superid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }
}
