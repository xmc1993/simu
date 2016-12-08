package cn.superid.webapp.controller.VO;

import java.util.List;

/**
 * Created by njuTms on 16/12/7.
 * 一个用户拥有的所有盟的所有角色
 */
public class UserAllianceRolesVO {
    private long allianceId;
    private String allianceName;
    private List<SimpleRoleVO> roles;

    public long getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(long allianceId) {
        this.allianceId = allianceId;
    }

    public String getAllianceName() {
        return allianceName;
    }

    public void setAllianceName(String allianceName) {
        this.allianceName = allianceName;
    }

    public List<SimpleRoleVO> getRoles() {
        return roles;
    }

    public void setRoles(List<SimpleRoleVO> roles) {
        this.roles = roles;
    }
}
