package cn.superid.webapp.controller.forms;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by njuTms on 16/11/14.
 */
@ApiModel
public class AffairInfo {
    private long id;
    private String superid = "";
    private String name = "";
    private String shortName;
    private String description = "";
    private int publicType;
    private String logoUrl = "";
    private int guestLimit;
    private Timestamp modifyTime;
    private String covers = "";
    private Object overView;
    private String tags = "";

    private long roleId;
    @ApiModelProperty(notes = "用于右上角显示的角色")
    private String roleTitle;
    private boolean isStuck;

    //以下字段已跟cto确认返回
    private long roleAllianceId;
    private long affairMemberId;
    private String permissions;

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

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
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

    public Object getOverView() {
        return overView;
    }

    public void setOverView(Object overView) {
        this.overView = overView;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public boolean getIsStuck() {
        return isStuck;
    }

    public void setIsStuck(boolean isStuck) {
        this.isStuck = isStuck;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getGuestLimit() {
        return guestLimit;
    }

    public void setGuestLimit(int guestLimit) {
        this.guestLimit = guestLimit;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getSuperid() {
        return superid;
    }

    public void setSuperid(String superid) {
        this.superid = superid;
    }

    public long getAffairMemberId() {
        return affairMemberId;
    }

    public void setAffairMemberId(long affairMemberId) {
        this.affairMemberId = affairMemberId;
    }

    public long getRoleAllianceId() {
        return roleAllianceId;
    }

    public void setRoleAllianceId(long roleAllianceId) {
        this.roleAllianceId = roleAllianceId;
    }
}
