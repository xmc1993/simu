package cn.superid.webapp.controller.VO;

import cn.superid.webapp.controller.forms.AddAffairRoleForm;

import java.util.List;

/**
 * Created by njuTms on 17/1/3.
 */
public class AddAffairRoleFormVO {
    List<AddAffairRoleForm> allianceRoles; //盟内角色
    List<AddAffairRoleForm> outAllianceRoles; //盟外角色

    public List<AddAffairRoleForm> getAllianceRoles() {
        return allianceRoles;
    }

    public void setAllianceRoles(List<AddAffairRoleForm> allianceRoles) {
        this.allianceRoles = allianceRoles;
    }

    public List<AddAffairRoleForm> getOutAllianceRoles() {
        return outAllianceRoles;
    }

    public void setOutAllianceRoles(List<AddAffairRoleForm> outAllianceRoles) {
        this.outAllianceRoles = outAllianceRoles;
    }
}
