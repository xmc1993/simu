package cn.superid.webapp.model;

import cn.superid.jpa.orm.ConditionalDao;
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
    public final static ConditionalDao<AllianceEntity> dao = new ConditionalDao<>(AllianceEntity.class);
    private long id;
    private String name;
    private long ownerRoleId;
    private int state;
    private int verified; //0表示正常,1表示没有认证,2表示认证正在等待审核
    private long verifyAffairId;
    private long rootAffairId;
    private String logoUrl;
    private boolean isPersonal;
    private String superId;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private String description;
    private double faith;
    private String code;

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

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
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

    public boolean getIsPersonal() {
        return isPersonal;
    }

    public void setIsPersonal(boolean isPersonal) {
        this.isPersonal = isPersonal;
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

    public String getSuperId() {
        return superId;
    }

    public void setSuperId(String superId) {
        this.superId = superId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
