package cn.superid.webapp.controller.forms;

/**
 * Created by njutms on 16/8/17.
 */
public class AdditionResult {
    private long id;
    private long contractId;
    private String content;
    private String signedRole;
    private int isSelf = 0;
    private int state;

    public AdditionResult(long id, long contractId, String content, String signedRole, int state) {
        this.id = id;
        this.contractId = contractId;
        this.content = content;
        this.signedRole = signedRole;
        this.state = state;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSignedRole() {
        return signedRole;
    }

    public void setSignedRole(String signedRole) {
        this.signedRole = signedRole;
    }

    public int getIsSelf() {
        return isSelf;
    }

    public void setIsSelf(int isSelf) {
        this.isSelf = isSelf;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
