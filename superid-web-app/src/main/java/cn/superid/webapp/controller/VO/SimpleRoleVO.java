package cn.superid.webapp.controller.VO;

import cn.superid.jpa.orm.ConditionalDao;

/**
 * Created by jizhenya on 16/11/22.
 */
public class SimpleRoleVO {

    public final static ConditionalDao dao = new ConditionalDao(SimpleRoleVO.class);

    private long roleId;
    private String roleName;
    private String allianceName;
    private String allianceId;

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

    public String getAllianceName() {
        return allianceName;
    }

    public void setAllianceName(String allianceName) {
        this.allianceName = allianceName;
    }

    public String getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(String allianceId) {
        this.allianceId = allianceId;
    }
}

