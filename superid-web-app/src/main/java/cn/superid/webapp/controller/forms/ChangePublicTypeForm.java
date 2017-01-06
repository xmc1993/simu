package cn.superid.webapp.controller.forms;

import com.wordnik.swagger.annotations.ApiModel;

/**
 * Created by jizhenya on 17/1/6.
 */
@ApiModel
public class ChangePublicTypeForm {

    private Boolean personalTags;
    private Boolean realname;
    private Boolean idCard;
    private Boolean mobile;
    private Boolean email;
    private Boolean birthday;

    public Boolean getPersonalTags() {
        return personalTags;
    }

    public void setPersonalTags(Boolean personalTags) {
        this.personalTags = personalTags;
    }

    public Boolean getRealname() {
        return realname;
    }

    public void setRealname(Boolean realname) {
        this.realname = realname;
    }

    public Boolean getIdCard() {
        return idCard;
    }

    public void setIdCard(Boolean idCard) {
        this.idCard = idCard;
    }

    public Boolean getMobile() {
        return mobile;
    }

    public void setMobile(Boolean mobile) {
        this.mobile = mobile;
    }

    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public Boolean getBirthday() {
        return birthday;
    }

    public void setBirthday(Boolean birthday) {
        this.birthday = birthday;
    }
}
