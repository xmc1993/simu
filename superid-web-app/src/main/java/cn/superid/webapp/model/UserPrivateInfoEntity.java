package cn.superid.webapp.model;

import cn.superid.jpa.annotation.PartitionId;
import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by njuTms on 16/11/2.
 */
@Entity
@Table(name = "user_private_info")
public class UserPrivateInfoEntity extends ExecutableModel {
    public final static ConditionalDao<UserPrivateInfoEntity> dao = new ConditionalDao<>(UserPrivateInfoEntity.class);
    private long id;
    private long userId;
    private boolean personalTags;
    private boolean realname;
    private boolean idCard;
    private boolean mobile;
    private boolean email;
    private boolean birthday;

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @PartitionId
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isRealname() {
        return realname;
    }

    public void setRealname(boolean realname) {
        this.realname = realname;
    }

    public boolean isIdCard() {
        return idCard;
    }

    public void setIdCard(boolean idCard) {
        this.idCard = idCard;
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isBirthday() {
        return birthday;
    }

    public void setBirthday(boolean birthday) {
        this.birthday = birthday;
    }

    public boolean isPersonalTags() {
        return personalTags;
    }

    public void setPersonalTags(boolean personalTags) {
        this.personalTags = personalTags;
    }
}
