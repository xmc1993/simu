package cn.superid.webapp.forms;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

/**
 * Created by xiaofengxu on 16/10/8.
 */
@ApiModel
public class AllianceCertificationForm {
    private String companyName;
    private String companyAddress;
    private String licenseImagePath;
    private String corporationName;
    private String companyType;
    private String businessScope;
    private Float registerMoney;
    private Timestamp setUpDate;
    @ApiModelProperty(notes = "不需要传")
    private long roleId;


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


    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }
}
