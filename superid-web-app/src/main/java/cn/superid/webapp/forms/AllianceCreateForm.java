package cn.superid.webapp.forms;


import cn.superid.webapp.enums.IntBoolean;
import cn.superid.webapp.model.UserEntity;
import com.wordnik.swagger.annotations.ApiModel;

@ApiModel
public class AllianceCreateForm {
    private String name;
    private String logoUrl;
    private String code;
    private boolean isPersonal = false;
    private long userId;
    private UserEntity userEntity;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
