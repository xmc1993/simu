package cn.superid.webapp.service.vo;

/**
 * Created by jessiechen on 03/01/17.
 */
public class AffairMemberSearchVo {
    private String username;
    private String superid;
    private long userId;
    private int gender;
    private String avatar;
    private String roleTitle;
    //主事务
    private String belongAffair;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSuperid() {
        return superid;
    }

    public void setSuperid(String superid) {
        this.superid = superid;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    public String getBelongAffair() {
        return belongAffair;
    }

    public void setBelongAffair(String belongAffair) {
        this.belongAffair = belongAffair;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
