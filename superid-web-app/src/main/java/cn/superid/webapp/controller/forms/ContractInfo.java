package cn.superid.webapp.controller.forms;


import java.sql.Timestamp;

/**
 * Created by njutms on 16/8/11.
 */
public class ContractInfo {
    private long id;
    private String title;
    private String content;
    private String changeLogs;
    private String alliances;
    private String additions;
    private String members;
    private long dgId;
    private int state;
    private Timestamp createTime;
    private Timestamp signatureTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChangeLogs() {
        return changeLogs;
    }

    public void setChangeLogs(String changeLogs) {
        this.changeLogs = changeLogs;
    }

    public String getAlliances() {
        return alliances;
    }

    public void setAlliances(String alliances) {
        this.alliances = alliances;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getAdditions() {
        return additions;
    }

    public void setAdditions(String additions) {
        this.additions = additions;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getSignatureTime() {
        return signatureTime;
    }

    public void setSignatureTime(Timestamp signatureTime) {
        this.signatureTime = signatureTime;
    }

    public long getDgId() {
        return dgId;
    }

    public void setDgId(long dgId) {
        this.dgId = dgId;
    }
}
