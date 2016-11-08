package cn.superid.webapp.model;

import cn.superid.jpa.annotation.PartitionId;
import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by xiaofengxu on 16/9/30.
 */
@Table(name = "alliance_certification")
public class AllianceCertificationEntity extends ExecutableModel<AllianceCertificationEntity> {

    private long id;
    private long allianceId;
    private String companyName;
    private String companyAddress;
    private String licenseImagePath;
    private String corporationName;
    private String companyType;
    private String businessScope;
    private Float registerMoney;
    private Timestamp setUpDate;
    private Integer checkState = 0;
    private String checkReason = "";
    private long roleId;
    private Timestamp createTime;
    private Timestamp modifyTime;

    public static ConditionalDao<AllianceCertificationEntity> dao = new ConditionalDao<>(AllianceCertificationEntity.class);

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @PartitionId
    public long getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(long allianceId) {
        this.allianceId = allianceId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getLicenseImagePath() {
        return licenseImagePath;
    }

    public void setLicenseImagePath(String licenseImagePath) {
        this.licenseImagePath = licenseImagePath;
    }

    public String getCorporationName() {
        return corporationName;
    }

    public void setCorporationName(String corporationName) {
        this.corporationName = corporationName;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public Float getRegisterMoney() {
        return registerMoney;
    }

    public void setRegisterMoney(Float registerMoney) {
        this.registerMoney = registerMoney;
    }

    public Timestamp getSetUpDate() {
        return setUpDate;
    }

    public void setSetUpDate(Timestamp setUpDate) {
        this.setUpDate = setUpDate;
    }

    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    public String getCheckReason() {
        return checkReason;
    }

    public void setCheckReason(String checkReason) {
        this.checkReason = checkReason;
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

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }
}
