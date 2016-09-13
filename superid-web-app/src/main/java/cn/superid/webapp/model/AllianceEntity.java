package cn.superid.webapp.model;

import cn.superid.jpa.annotation.PartitionId;
import cn.superid.jpa.orm.Dao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by zp on 2016/8/9.
 */
@Table(name = "alliance")
public class AllianceEntity extends ExecutableModel {
    public final static Dao<AllianceEntity> dao = new Dao<>(AllianceEntity.class);
    private long id;
    private String name;
    private long ownerRoleId;
    private int state;
    private int applyCertificateState;
    private long verifyAffairId;
    private long rootAffairId;
    private String logoUrl;
    private int isPersonal;
    private String shortName;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private String description;
    private double faith;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOwnerRoleId() {
        return ownerRoleId;
    }

    public void setOwnerRoleId(long ownerRoleId) {
        this.ownerRoleId = ownerRoleId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getApplyCertificateState() {
        return applyCertificateState;
    }

    public void setApplyCertificateState(int applyCertificateState) {
        this.applyCertificateState = applyCertificateState;
    }

    public long getVerifyAffairId() {
        return verifyAffairId;
    }

    public void setVerifyAffairId(long verifyAffairId) {
        this.verifyAffairId = verifyAffairId;
    }

    public long getRootAffairId() {
        return rootAffairId;
    }

    public void setRootAffairId(long rootAffairId) {
        this.rootAffairId = rootAffairId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getIsPersonal() {
        return isPersonal;
    }

    public void setIsPersonal(int isPersonal) {
        this.isPersonal = isPersonal;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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
}
