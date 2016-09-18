package cn.superid.webapp.model;

import cn.superid.jpa.annotation.PartitionId;
import cn.superid.jpa.orm.Dao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by jizhenya on 16/9/18.
 */
@Table(name = "addition")
public class AdditionEntity extends ExecutableModel {

    public final static Dao<AdditionEntity> dao = new Dao<>(AdditionEntity.class);
    private long id;
    private long contractId;
    private String content;
    private int state;//0表示无效，1表示发起等待确认，2表示生效
    private Timestamp createTime;
    private Timestamp modifyTime;
    private Timestamp confirmedTime;
    private String signedRole;
    private int isBlock;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @PartitionId
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

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Timestamp getConfirmedTime() {
        return confirmedTime;
    }

    public void setConfirmedTime(Timestamp confirmedTime) {
        this.confirmedTime = confirmedTime;
    }

    public String getSignedRole() {
        return signedRole;
    }

    public void setSignedRole(String signedRole) {
        this.signedRole = signedRole;
    }

    public int getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(int isBlock) {
        this.isBlock = isBlock;
    }
}
