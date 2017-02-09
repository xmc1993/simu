package cn.superid.webapp.forms;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Created by jizhenya on 17/2/9.
 */
public class SearchRoleConditions {
    @ApiModelProperty(notes = "成员用户名关键字、角色title关键字，接受缩写")
    private String key;

    @ApiModelProperty(notes = "列表显示页码，从1开始")
    private int page=1;

    @ApiModelProperty(notes = "每页显示数量，10-100之间")
    private int count=20;

    @ApiModelProperty(notes = "盟内成员或者盟外成员,不填默认为盟内成员")
    private boolean isAllianceUser=true;

    @ApiModelProperty(notes = "是否需要返回列表总数（分页使用），默认为TRUE，建议在前端已知总数时填FALSE减轻服务器重复计算压力")
    private boolean needTotal=true;

    @ApiModelProperty(notes = "需要一次性取多少页")
    private int section = 1;



    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isAllianceUser() {
        return isAllianceUser;
    }

    public void setIsAllianceUser(boolean isAllianceUser) {
        this.isAllianceUser = isAllianceUser;
    }

    public boolean isNeedTotal() {
        return needTotal;
    }

    public void setNeedTotal(boolean needTotal) {
        this.needTotal = needTotal;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }
}
