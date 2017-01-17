package cn.superid.webapp.forms;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by xiaofengxu on 16/8/31.
 */
@ApiModel
public class CreateAffairForm extends Form{
    private String name;
    private Long parentAffairId;
    private Integer number;
    private long operationRoleId;
    private Integer publicType;
    private long allianceId;
    private String logo;
    private String description;

    @Override
    public void validate() throws IllegalArgumentException {
        notEmpty("name",name);
        notNull("publicType",publicType);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getParentAffairId() {
        return parentAffairId;
    }

    public void setParentAffairId(long parentAffairId) {
        this.parentAffairId = parentAffairId;
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
