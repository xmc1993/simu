package cn.superid.webapp.controller.forms;

import java.sql.Timestamp;

/**
 * Created by njuTms on 16/8/26.
 */
public class ChangeLogResult {
    private long id;
    private long contractId;
    private int hasDetail;
    private String message;
    private Timestamp modifyTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public int getHasDetail() {
        return hasDetail;
    }

    public void setHasDetail(int hasDetail) {
        this.hasDetail = hasDetail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }
}
