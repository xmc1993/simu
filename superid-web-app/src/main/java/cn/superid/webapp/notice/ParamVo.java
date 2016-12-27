package cn.superid.webapp.notice;

/**
 * Created by xmc1993 on 16/12/26.
 */
public class ParamVo {
    private Long allianceId;
    private Long affairId;
    private Long groupId;
    private Long roleId;
    private Boolean isGuest;
    private Integer eventType;

    public long getAffairId() {
        return affairId;
    }

    public void setAffairId(long affairId) {
        this.affairId = affairId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public Long getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(Long allianceId) {
        this.allianceId = allianceId;
    }

    public void setAffairId(Long affairId) {
        this.affairId = affairId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Boolean getGuest() {
        return isGuest;
    }

    public void setGuest(Boolean guest) {
        isGuest = guest;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }
}
