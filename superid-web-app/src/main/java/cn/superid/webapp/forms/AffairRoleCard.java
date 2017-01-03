package cn.superid.webapp.forms;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by xiaofengxu on 16/12/30.
 */
@ApiModel
public class AffairRoleCard {//盟角色卡片
    private long roleId;
    private String title;
    private String name;
    private String superId;
    private int gender;
    private long affairMemberId;
    private String avatar;
    @ApiModelProperty("主事务id")
    private long belongAffairId;
    private String belongAffairName;
    private String permissions;
    private String titleAbbr;
    private String nameAbbr;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getAffairMemberId() {
        return affairMemberId;
    }

    public void setAffairMemberId(long affairMemberId) {
        this.affairMemberId = affairMemberId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getBelongAffairId() {
        return belongAffairId;
    }

    public void setBelongAffairId(long belongAffairId) {
        this.belongAffairId = belongAffairId;
    }

    public String getBelongAffairName() {
        return belongAffairName;
    }

    public void setBelongAffairName(String belongAffairName) {
        this.belongAffairName = belongAffairName;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuperId() {
        return superId;
    }

    public void setSuperId(String superId) {
        this.superId = superId;
    }

    public String getTitleAbbr() {
        return titleAbbr;
    }

    public void setTitleAbbr(String titleAbbr) {
        this.titleAbbr = titleAbbr;
    }

    public String getNameAbbr() {
        return nameAbbr;
    }

    public void setNameAbbr(String nameAbbr) {
        this.nameAbbr = nameAbbr;
    }
}
