package cn.superid.webapp.controller.VO;

import com.wordnik.swagger.annotations.ApiModel;

import java.util.List;

/**
 * Created by njuTms on 17/1/3.
 */
@ApiModel
public class AddAffairRoleFormVO {
    List<Long> allianceRoles; //盟内角色
    List<Long> outAllianceRoles; //盟外角色

    public List<Long> getAllianceRoles() {
        return allianceRoles;
    }

    public void setAllianceRoles(List<Long> allianceRoles) {
        this.allianceRoles = allianceRoles;
    }

    public List<Long> getOutAllianceRoles() {
        return outAllianceRoles;
    }

    public void setOutAllianceRoles(List<Long> outAllianceRoles) {
        this.outAllianceRoles = outAllianceRoles;
    }
}
