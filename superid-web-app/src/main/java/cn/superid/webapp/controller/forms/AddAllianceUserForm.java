package cn.superid.webapp.controller.forms;

/**
 * Created by jizhenya on 16/11/24.
 */
public class AddAllianceUserForm {

    private long userId;
    private long mainAffairId;
    private String roleName;
    private String permissions;

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
}
