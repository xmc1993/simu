package cn.superid.webapp.service.vo;

/**
 * Created by jessiechen on 03/01/17.
 */
public class AffairMemberSearchVo {
    private String username;
    private String superid;
    private int gender;
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
}
