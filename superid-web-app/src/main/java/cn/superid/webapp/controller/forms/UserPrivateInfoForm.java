package cn.superid.webapp.controller.forms;

import cn.superid.jpa.orm.ExecutableModel;

/**
 * Created by jizhenya on 17/1/10.
 */
public class UserPrivateInfoForm extends ExecutableModel{

    private int personalTags;
    private int realname;
    private int idCard;
    private int mobile;
    private int email;
    private int birthday;

    public int getPersonalTags() {
        return personalTags;
    }

    public void setPersonalTags(int personalTags) {
        this.personalTags = personalTags;
    }

    public int getRealname() {
        return realname;
    }

    public void setRealname(int realname) {
        this.realname = realname;
    }

    public int getIdCard() {
        return idCard;
    }

    public void setIdCard(int idCard) {
        this.idCard = idCard;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public int getEmail() {
        return email;
    }

    public void setEmail(int email) {
        this.email = email;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }
}
