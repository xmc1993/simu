package cn.superid.webapp.model;

import cn.superid.jpa.annotation.NotTooSimple;
import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by zp on 2016/7/25.
 */
@Table(name = "user")
public class UserEntity extends ExecutableModel {
    public final static ConditionalDao<UserEntity> dao = new ConditionalDao<>(UserEntity.class);

    private long id;
    private String superid ="";
    private String password ="";
    private String familyName ="";
    private String givenName="";
    private int state;
    private String videoUrl ="";
    private String videoImg ="";
    private int isVideo;
    private String nicknames = "";
    private boolean isAuthenticated;
    private String username ="";
    private Timestamp birthday;
    private int age;
    private String email ="";
    private String mobile ="";
    private long personalRoleId;
    private String idCard ="";
    private int marriageStatus ;
    private int educationLevel;
    private String school ="";
    private Timestamp createTime;
    private Timestamp modifyTime;
    private String address ="";
    private String detailAddress ="";
    private String description ="";//描述
    private double faith;
    private int gender;
    private int publicType;
    private String avatar;
    private String chatToken;

    private String countryCode; //区号
    private long homepageAffairId;

    private Map<Long,Long> members;


    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSuperid() {
        return superid;
    }

    public void setSuperid(String superid) {
        this.superid = superid;
    }


    @NotTooSimple
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getVideoImg() {
        return videoImg;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNicknames() {
        return nicknames;
    }

    public void setNicknames(String nickNames) {
        this.nicknames = nicknames;
    }

    public boolean getIsAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public int getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(int isVideo) {
        this.isVideo = isVideo;
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

    public long getPersonalRoleId() {
        return personalRoleId;
    }

    public void setPersonalRoleId(long personalRoleId) {
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

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
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

    public int getPublicType() {
        return publicType;
    }

    public void setPublicType(int publicType) {
        this.publicType = publicType;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public long getHomepageAffairId() {
        return homepageAffairId;
    }

    public void setHomepageAffairId(long homepageAffairId) {
        this.homepageAffairId = homepageAffairId;
    }

    @Transient
    public String getChatToken() {
        return chatToken;
    }

    public void setChatToken(String chatToken) {
        this.chatToken = chatToken;
    }

    @Transient
    public Map<Long, Long> getMembers() {
        return members;
    }

    public void setMembers(Map<Long, Long> members) {
        this.members = members;
    }
}
