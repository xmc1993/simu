package cn.superid.webapp.controller.forms;

/**
 * Created by njuTms on 17/1/13.
 * 在事务成员详细信息中的小卡片展示,暂时只有两个字段
 */
public class SimpleRoleCard {
    private long roleId;
    private String roleTitle;
    private long belongAffairId;
    private String belongAffairName;

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
}
