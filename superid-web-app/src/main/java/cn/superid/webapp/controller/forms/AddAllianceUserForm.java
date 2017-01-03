package cn.superid.webapp.controller.forms;

/**
 * Created by jizhenya on 16/11/24.
 */
public class AddAllianceUserForm {

    private long userId;
    private long mainAffairId;
    private long roleId;
    private String roleName;
    private String permissions;
    private String comment;


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getMainAffairId() {
        return mainAffairId;
    }

    public void setMainAffairId(long mainAffairId) {
        this.mainAffairId = mainAffairId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
