package cn.superid.webapp.service.forms;

/**
 * Created by njuTms on 16/11/16.
 */
//为了使用鹏哥的setByObject方法,必须form字段名和数据表对应,所以前端传来的修改form中的isHomepage必须去除
public class AffairInfoForm {
    private String name;
    private Integer publicType;
    private String description;
    private String shortName;
    private String logoUrls;
    private Integer guestLimit;

    public AffairInfoForm(String name, Integer publicType, String description, String shortName, String logoUrls,Integer guestLimit) {
        this.name = name;
        this.publicType = publicType;
        this.description = description;
        this.shortName = shortName;
        this.logoUrls = logoUrls;
        this.guestLimit = guestLimit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPublicType() {
        return publicType;
    }

    public void setPublicType(Integer publicType) {
        this.publicType = publicType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLogoUrls() {
        return logoUrls;
    }

    public void setLogoUrls(String logoUrls) {
        this.logoUrls = logoUrls;
    }

    public Integer getGuestLimit() {
        return guestLimit;
    }

    public void setGuestLimit(Integer guestLimit) {
        this.guestLimit = guestLimit;
    }
}
