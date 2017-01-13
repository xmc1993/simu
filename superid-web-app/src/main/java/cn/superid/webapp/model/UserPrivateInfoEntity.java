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
    public final static ConditionalDao dao = new ConditionalDao(UserPrivateInfoEntity.class);
    private long id;
    private long userId;
    private int personalTags;
    private int realname;
    private int idCard;
    private int mobile;
    private int email;
    private int birthday;

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
