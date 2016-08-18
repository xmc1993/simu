package cn.superid.webapp.forms;


import cn.superid.webapp.enums.IntBoolean;
import cn.superid.webapp.model.UserEntity;

public class AllianceCreateForm {
    private String allianceId;
    private String SerialNum;
    private String name;
    private String ownerUserId;
    private String logoUrl;
    private int isPersonal = IntBoolean.FALSE;
    private String operationRoleId;
    private UserEntity userEntity;

    public String getSerialNum() {
        return SerialNum;
    }

    public void setSerialNum(String serialNum) {
        SerialNum = serialNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(String allianceId) {
        this.allianceId = allianceId;
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

    public String getOperationRoleId() {
        return operationRoleId;
    }

    public void setOperationRoleId(String operationRoleId) {
        this.operationRoleId = operationRoleId;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
