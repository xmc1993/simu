package cn.superid.webapp.controller.forms;

import com.wordnik.swagger.annotations.ApiModel;

/**
 * Created by jizhenya on 17/1/6.
 */
@ApiModel
public class ChangePublicTypeForm {

    private Integer personalTags;
    private Integer realname;
    private Integer idCard;
    private Integer mobile;
    private Integer email;
    private Integer birthday;

    public Integer getPersonalTags() {
        return personalTags;
    }

    public void setPersonalTags(Integer personalTags) {
        this.personalTags = personalTags;
    }

    public Integer getRealname() {
        return realname;
    }

    public void setRealname(Integer realname) {
        this.realname = realname;
    }

    public Integer getIdCard() {
        return idCard;
    }

    public void setIdCard(Integer idCard) {
        this.idCard = idCard;
    }

    public Integer getMobile() {
        return mobile;
    }

    public void setMobile(Integer mobile) {
        this.mobile = mobile;
    }

    public Integer getEmail() {
        return email;
    }

    public void setEmail(Integer email) {
        this.email = email;
    }

    public Integer getBirthday() {
        return birthday;
    }

    public void setBirthday(Integer birthday) {
        this.birthday = birthday;
    }
}
