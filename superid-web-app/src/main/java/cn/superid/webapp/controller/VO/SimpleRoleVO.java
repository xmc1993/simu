package cn.superid.webapp.controller.VO;

/**
 * Created by jizhenya on 16/11/22.
 */
public class SimpleRoleVO {

    private long roleId;
    private String roleName;

    public SimpleRoleVO(long roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public SimpleRoleVO(){}

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
}

