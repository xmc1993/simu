package cn.superid.webapp.model;

import cn.superid.jpa.annotation.PartitionId;
import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by jizhenya on 16/9/18.
 */
@Table(name = "contract_role")
public class ContractRoleEntity extends ExecutableModel {
    public final static ConditionalDao dao = new ConditionalDao(ContractRoleEntity.class);

    private long id;
    private long contractId;
    private long roleId;
    private long allianceId;
    private int confirmed;
    private int signature;
    private int terminate;//0表示正在进行，1表示同意，2表示拒绝
    private int addition;
    private int kind;//0表示乙方，1表示甲方，2表示丙方,以此类推
    private Timestamp confirmedTime;
    private Timestamp terminateTime;
    private Timestamp signatureTime;
    private Timestamp additionTime;

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

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(long allianceId) {
        this.allianceId = allianceId;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getSignature() {
        return signature;
    }

    public void setSignature(int signature) {
        this.signature = signature;
    }

    public int getTerminate() {
        return terminate;
    }

    public void setTerminate(int terminate) {
        this.terminate = terminate;
    }

    public int getAddition() {
        return addition;
    }

    public void setAddition(int addition) {
        this.addition = addition;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public Timestamp getConfirmedTime() {
        return confirmedTime;
    }

    public void setConfirmedTime(Timestamp confirmedTime) {
        this.confirmedTime = confirmedTime;
    }

    public Timestamp getTerminateTime() {
        return terminateTime;
    }

    public void setTerminateTime(Timestamp terminateTime) {
        this.terminateTime = terminateTime;
    }

    public Timestamp getSignatureTime() {
        return signatureTime;
    }

    public void setSignatureTime(Timestamp signatureTime) {
        this.signatureTime = signatureTime;
    }

    public Timestamp getAdditionTime() {
        return additionTime;
    }

    public void setAdditionTime(Timestamp additionTime) {
        this.additionTime = additionTime;
    }
}
