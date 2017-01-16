package cn.superid.webapp.controller.VO;

import cn.superid.webapp.controller.forms.SimpleRoleCard;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by njuTms on 17/1/13.
 * 获取事务成员列表之后点击某一个成员需要显示的信息
 */
public class AffairUserInfoVO {
    private Long id ;
    private String superid = "";
    private boolean isAuthenticated = false;
    private String avatar = "";
    private int state = 0;
    private String realname = "";
    private String username = "";
    private Timestamp birthday;
    private String email = "";
    private String mobile = "";
    private String idCard = "";
    private String address;
    private double faith;
    private int gender;
    private String tags;
    @ApiModelProperty(notes = "该用户在该盟中的所有角色")
    private List<SimpleRoleCard> simpleRoleCards;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSuperid() {
        return superid;
    }

    public void setSuperid(String superid) {
        this.superid = superid;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getFaith() {
        return faith;
    }

    public void setFaith(double faith) {
        this.faith = faith;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<SimpleRoleCard> getSimpleRoleCards() {
        return simpleRoleCards;
    }

    public void setSimpleRoleCards(List<SimpleRoleCard> simpleRoleCards) {
        this.simpleRoleCards = simpleRoleCards;
    }
}
