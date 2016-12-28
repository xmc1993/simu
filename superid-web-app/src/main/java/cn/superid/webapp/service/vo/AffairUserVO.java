package cn.superid.webapp.service.vo;

/**
 * Created by njuTms on 16/12/28.
 */
public class AffairUserVO {
    private long userId;
    private String userName;
    private String superid;
    private int gender;
    private long roleId;
    private String title;
    private long belongAffairId;
    private String belongAffairName;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getBelongAffairId() {
        return belongAffairId;
    }

    public void setBelongAffairId(long belongAffairId) {
        this.belongAffairId = belongAffairId;
    }

    public String getBelongAffairName() {
        return belongAffairName;
    }

    public void setBelongAffairName(String belongAffairName) {
        this.belongAffairName = belongAffairName;
    }
}
