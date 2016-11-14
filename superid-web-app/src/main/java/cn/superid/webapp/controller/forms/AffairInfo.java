package cn.superid.webapp.controller.forms;

/**
 * Created by njuTms on 16/11/14.
 */
public class AffairInfo {
    private long id;
    private String name;
    private String description;
    private int publicType;
    private String logoUrl;
    private boolean isHomepage;
    private int isPersonal;
    private String shortName;
    private int isSticked;
    private String permissions;

    private String covers;
    private String overView;

    private String tags;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsPersonal() {
        return isPersonal;
    }

    public void setIsPersonal(int isPersonal) {
        this.isPersonal = isPersonal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPublicType() {
        return publicType;
    }

    public void setPublicType(int publicType) {
        this.publicType = publicType;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public boolean isHomepage() {
        return isHomepage;
    }

    public void setHomepage(boolean homepage) {
        isHomepage = homepage;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getCovers() {
        return covers;
    }

    public void setCovers(String covers) {
        this.covers = covers;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }


    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getIsSticked() {
        return isSticked;
    }

    public void setIsSticked(int isSticked) {
        this.isSticked = isSticked;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
