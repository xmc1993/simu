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
}
