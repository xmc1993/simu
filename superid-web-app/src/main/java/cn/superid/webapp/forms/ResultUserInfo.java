package cn.superid.webapp.forms;

import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.jpa.orm.ExecutableModel;
import cn.superid.webapp.controller.forms.UserPrivateInfoForm;
import cn.superid.webapp.model.UserPrivateInfoEntity;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaofengxu on 16/9/6.
 */
@Table(name="user")
public class ResultUserInfo extends ExecutableModel{

    public final static ConditionalDao<ResultUserInfo> dao = new ConditionalDao<>(ResultUserInfo.class);
    private Long id = 0L;
    private String superId = "";
    private boolean isAuthenticated = false;
    private String avatar = "";
    private int state = 0;
    private String videoUrl = "";
    private String videoImg = "";
    private int isVideo = 0;
    private String realname = "";
    private String username = "";
    private Timestamp birthday;
    private int age = 0;
    private String email = "";
    private String mobile = "";
    private Long personalRoleId = 0L;
    private String idCard = "";
    private int marriageStatus = 0;
    private int educationLevel;
    private String school;
    private Timestamp createTime;
    private String address;
    private String detailAddress;
    private String description;//描述
    private double faith;
    private int gender;
    private UserPrivateInfoForm userPrivateInfoForm;
    private List<String> nickNames ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSuperId() {
        return superId;
    }

    public void setSuperId(String superId) {
        this.superId = superId;
    }

    public boolean getIsAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoImg() {
        return videoImg;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }

    public int getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(int isVideo) {
        this.isVideo = isVideo;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public Long getPersonalRoleId() {
        return personalRoleId;
    }

    public void setPersonalRoleId(Long personalRoleId) {
        this.personalRoleId = personalRoleId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public int getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(int marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    public int getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(int educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Transient
    public UserPrivateInfoForm getUserPrivateInfoForm() {
        return userPrivateInfoForm;
    }

    public void setUserPrivateInfoForm(UserPrivateInfoForm userPrivateInfoForm) {
        this.userPrivateInfoForm = userPrivateInfoForm;
    }

    @Transient
    public List<String> getNickNames() {
        return nickNames;
    }

    public void setNickNames(List<String> nickNames) {
        this.nickNames = nickNames;
    }
}
