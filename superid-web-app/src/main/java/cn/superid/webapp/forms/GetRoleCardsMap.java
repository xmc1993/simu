package cn.superid.webapp.forms;

import java.util.List;

/**
 * Created by xiaofengxu on 17/1/17.
 */
public class GetRoleCardsMap {
    private String affairId;
    private List<AffairRoleCard> roles;
    private int masterCount;
    private int guestCount;

    public String getAffairId() {
        return affairId;
    }

    public void setAffairId(String affairId) {
        this.affairId = affairId;
    }

    public List<AffairRoleCard> getRoles() {
        return roles;
    }

    public void setRoles(List<AffairRoleCard> roles) {
        this.roles = roles;
    }

    public int getMasterCount() {
        return masterCount;
    }

    public void setMasterCount(int masterCount) {
        this.masterCount = masterCount;
    }

    public int getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }
}
