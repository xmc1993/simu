package cn.superid.webapp.forms;

import com.wordnik.swagger.annotations.ApiModel;

/**
 * Created by jizhenya on 17/2/9.
 */
@ApiModel
public class OtherRoleCard {

    private long roleId;
    private String roleTitle;
    private String username;
    private long userId;
    private String avatar;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
