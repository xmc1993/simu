package cn.superid.webapp.model;

import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by jizhenya on 16/9/18.
 */
@Table(name = "contract")
public class ContractEntity extends ExecutableModel {

    public final static ConditionalDao dao = new ConditionalDao(ContractEntity.class);

    private long id;
    private String title;
    private String content;
    private int state; //0表示失效,1表示正在发起,2表示发起成功未确认，3表示已确认正生效,4表示正在终止
    private long discussGroupId;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private Timestamp signatureTime;
    private Timestamp terminateTime;
    private int isBlock;

    @Id
    @Column(name = "id")
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getDiscussGroupId() {
        return discussGroupId;
    }

    public void setDiscussGroupId(long discussGroupId) {
        this.discussGroupId = discussGroupId;
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

    public Timestamp getSignatureTime() {
        return signatureTime;
    }

    public void setSignatureTime(Timestamp signatureTime) {
        this.signatureTime = signatureTime;
    }

    public Timestamp getTerminateTime() {
        return terminateTime;
    }

    public void setTerminateTime(Timestamp terminateTime) {
        this.terminateTime = terminateTime;
    }

    public int getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(int isBlock) {
        this.isBlock = isBlock;
    }
}
