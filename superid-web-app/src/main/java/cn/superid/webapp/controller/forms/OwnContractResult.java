package cn.superid.webapp.controller.forms;

import java.sql.Timestamp;

/**
 * Created by jizhenya on 16/9/20.
 */
public class OwnContractResult {
    private Long contractId;
    private String contractTitle;
    private int state;//0表示失效,1表示正在发起,2表示发起成功未确认，3表示已确认正生效
    private String alliances;
    private Timestamp createTime;
    private Timestamp signatureTime;

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getContractTitle() {
        return contractTitle;
    }

    public void setContractTitle(String contractTitle) {
        this.contractTitle = contractTitle;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAlliances() {
        return alliances;
    }

    public void setAlliances(String alliances) {
        this.alliances = alliances;
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
}
