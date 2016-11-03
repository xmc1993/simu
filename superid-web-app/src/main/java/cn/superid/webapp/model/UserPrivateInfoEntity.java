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
    private boolean actualName;
    private boolean identityCard;
    private boolean phoneNumber;
    private boolean mail;
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

    public boolean isPersonalTags() {
        return personalTags;
    }

    public void setPersonalTags(boolean personalTags) {
        this.personalTags = personalTags;
    }

    public boolean isActualName() {
        return actualName;
    }

    public void setActualName(boolean actualName) {
        this.actualName = actualName;
    }

    public boolean isIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(boolean identityCard) {
        this.identityCard = identityCard;
    }

    public boolean isPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(boolean phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isMail() {
        return mail;
    }

    public void setMail(boolean mail) {
        this.mail = mail;
    }

    public boolean isBirthday() {
        return birthday;
    }

    public void setBirthday(boolean birthday) {
        this.birthday = birthday;
    }
}
