package cn.superid.webapp.controller.forms;

/**
 * Created by njuTms on 17/1/3.
 * 邀请事务角色
 */
public class AddAffairRoleForm {
    private long userId;
    private long roleId;
    private String roleTitle;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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
}
