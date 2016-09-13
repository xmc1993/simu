package cn.superid.webapp.forms;


import cn.superid.webapp.enums.IntBoolean;
import cn.superid.webapp.model.UserEntity;
import com.wordnik.swagger.annotations.ApiModel;

@ApiModel
public class AllianceCreateForm {
    private String shortName;
    private String name;
    private String logoUrl;
    private int isPersonal = IntBoolean.FALSE;
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
}
