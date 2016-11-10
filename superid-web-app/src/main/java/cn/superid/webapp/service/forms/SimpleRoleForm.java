package cn.superid.webapp.service.forms;

/**
 * Created by jizhenya on 16/11/7.
 */
public class SimpleRoleForm {

    private long roleId;
    private String userName;
    private String roleName;
    private String permission;
    private long mainAffairId;
    private String mainAffairName;

    public SimpleRoleForm(long roleId, String userName, String roleName, String permission, long mainAffairId, String mainAffairName) {
        this.roleId = roleId;
        this.userName = userName;
        this.roleName = roleName;
        this.permission = permission;
        this.mainAffairId = mainAffairId;
        this.mainAffairName = mainAffairName;
    }

    public long getId() {
        return roleId;
    }

    public void setId(long roleId) {
        this.roleId = roleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public long getMainAffairId() {
        return mainAffairId;
    }

    public void setMainAffairId(long mainAffairId) {
        this.mainAffairId = mainAffairId;
    }

    public String getMainAffairName() {
        return mainAffairName;
    }

    public void setMainAffairName(String mainAffairName) {
        this.mainAffairName = mainAffairName;
    }
}
