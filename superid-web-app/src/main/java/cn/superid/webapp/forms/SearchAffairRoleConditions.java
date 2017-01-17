package cn.superid.webapp.forms;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by xiaofengxu on 16/12/30.
 */
@ApiModel
public class SearchAffairRoleConditions {
    private String key;
    private String lastTitlePY;
    private int limit;
    @ApiModelProperty(notes = "如果不筛选活跃程度,不需要传")
    private Boolean active;
    @ApiModelProperty(notes = "盟内角色true,否则false,不区分不传")
    private Boolean inAlliance;
    @ApiModelProperty(notes = "事务id,逗号隔开,如果是本事务可以不要传")
    private String affairIds;
    private boolean needCount;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLastTitlePY() {
        return lastTitlePY;
    }

    public void setLastTitlePY(String lastTitlePY) {
        this.lastTitlePY = lastTitlePY;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }


    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getInAlliance() {
        return inAlliance;
    }

    public void setInAlliance(Boolean inAlliance) {
        this.inAlliance = inAlliance;
    }

    public String getAffairIds() {
        return affairIds;
    }

    public void setAffairIds(String affairIds) {
        this.affairIds = affairIds;
    }

    public boolean isNeedCount() {
        return needCount;
    }

    public void setNeedCount(boolean needCount) {
        this.needCount = needCount;
    }
}
