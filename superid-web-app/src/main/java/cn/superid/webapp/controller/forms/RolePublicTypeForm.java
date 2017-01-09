package cn.superid.webapp.controller.forms;

import com.wordnik.swagger.annotations.ApiModel;

/**
 * Created by jizhenya on 17/1/9.
 */
@ApiModel
public class RolePublicTypeForm {

    private Long roleId;
    private int publicType;
    private Long allianceId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public int getPublicType() {
        return publicType;
    }

    public void setPublicType(int publicType) {
        this.publicType = publicType;
    }

    public Long getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(Long allianceId) {
        this.allianceId = allianceId;
    }
}
