package cn.superid.webapp.service.forms;

/**
 * Created by njuTms on 16/11/16.
 * 前端传来的修改事务信息字段,包含是否是首页
 */
public class ModifyAffairInfoForm {
    private String name;
    private Integer publicType;
    private String description;
    private String shortName;
    private String logoUrls;
    private Integer isHomepage;
    private int guestLimit;
    private Integer isStuck;

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

    public Integer getIsHomepage() {
        return isHomepage;
    }

    public void setIsHomepage(Integer isHomepage) {
        this.isHomepage = isHomepage;
    }

    public int getGuestLimit() {
        return guestLimit;
    }

    public void setGuestLimit(int guestLimit) {
        this.guestLimit = guestLimit;
    }

    public Integer getIsStuck() {
        return isStuck;
    }

    public void setIsStuck(Integer isStuck) {
        this.isStuck = isStuck;
    }
}
