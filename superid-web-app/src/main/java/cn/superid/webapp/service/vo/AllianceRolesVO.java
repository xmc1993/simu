package cn.superid.webapp.service.vo;

/**
 * Created by njuTms on 16/12/8.
 * 用于存储从数据库中取出的某个用户拥有的盟的所有角色,需要对应
 * 数据库的字段,然后处理后存为一个双层list返回给前端
 */
public class AllianceRolesVO {
    private long allianceId;
    private String allianceName;
    private long roleId;
    private String roleTitle;

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
