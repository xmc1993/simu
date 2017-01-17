package cn.superid.webapp.forms;

import java.util.List;

/**
 * Created by xiaofengxu on 17/1/17.
 */
public class GetRoleCardsMap {
    private long affairId;
    private List<AffairRoleCard> roles;
    private int officialCount;
    private int guestCount;

    public long getAffairId() {
        return affairId;
    }

    public void setAffairId(long affairId) {
        this.affairId = affairId;
    }

    public List<AffairRoleCard> getRoles() {
        return roles;
    }

    public void setRoles(List<AffairRoleCard> roles) {
        this.roles = roles;
    }

    public int getOfficialCount() {
        return officialCount;
    }

    public void setOfficialCount(int officialCount) {
        this.officialCount = officialCount;
    }

    public int getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }
}
