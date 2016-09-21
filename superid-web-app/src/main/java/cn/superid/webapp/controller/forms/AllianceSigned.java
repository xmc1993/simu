package cn.superid.webapp.controller.forms;

import java.sql.Timestamp;

/**
 * Created by njutms on 16/8/18.
 */
public  class AllianceSigned {
    private String allianceName;
    private long roleId;
    private String roleName;
    private int allianceKind;
    private int confirmed;
    private int signature;
    private int terminate;
    private int isSelf = 0;
    private int isInKind = 0;
    private Timestamp confirmedTime;
    private Timestamp signatureTime;
    private Timestamp terminateTime;


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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getIsSelf() {
        return isSelf;
    }

    public void setIsSelf(int isSelf) {
        this.isSelf = isSelf;
    }

    public int getIsInKind() {
        return isInKind;
    }

    public void setIsInKind(int isInKind) {
        this.isInKind = isInKind;
    }

    public int getAllianceKind() {
        return allianceKind;
    }

    public void setAllianceKind(int allianceKind) {
        this.allianceKind = allianceKind;
    }

    public Timestamp getConfirmedTime() {
        return confirmedTime;
    }

    public void setConfirmedTime(Timestamp confirmedTime) {
        this.confirmedTime = confirmedTime;
    }

    public int getSignature() {
        return signature;
    }

    public void setSignature(int signature) {
        this.signature = signature;
    }

    public Timestamp getSignatureTime() {
        return signatureTime;
    }

    public void setSignatureTime(Timestamp signatureTime) {
        this.signatureTime = signatureTime;
    }

    public int getTerminate() {
        return terminate;
    }

    public void setTerminate(int terminate) {
        this.terminate = terminate;
    }

    public Timestamp getTerminateTime() {
        return terminateTime;
    }

    public void setTerminateTime(Timestamp terminateTime) {
        this.terminateTime = terminateTime;
    }

}
