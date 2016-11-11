package cn.superid.webapp.forms;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by xiaofengxu on 16/8/31.
 */
@ApiModel
public class CreateAffairForm {
    private String name;
    private long affairId;
    private Integer number;
    private long operationRoleId;
    private int publicType;
    private long allianceId;
    private String logo;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAffairId() {
        return affairId;
    }

    public void setAffairId(long affairId) {
        this.affairId = affairId;
    }

    public long getOperationRoleId() {
        return operationRoleId;
    }

    public void setOperationRoleId(long operationRoleId) {
        this.operationRoleId = operationRoleId;
    }

    public int getPublicType() {
        return publicType;
    }

    public void setPublicType(int publicType) {
        this.publicType = publicType;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public long getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(long allianceId) {
        this.allianceId = allianceId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
