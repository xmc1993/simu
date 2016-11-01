package cn.superid.webapp.model;

import cn.superid.jpa.annotation.PartitionId;
import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.jpa.orm.ExecutableModel;
import org.springframework.security.access.method.P;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by njuTms on 16/10/31.
 */
@Entity
@Table(name = "friends_application")
public class FriendsApplicationEntity extends ExecutableModel {
    public final static ConditionalDao<FriendsApplicationEntity> dao = new ConditionalDao<>(FriendsApplicationEntity.class);
    private long id;
    private long fromUserId;
    private long toUserId;
    private int state; //默认0,表示未处理,1表示接受,2表示拒绝
    private String applyReason;
    private String dealReason;
    private Timestamp createTime;
    private Timestamp modifyTime;

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    @PartitionId
    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public String getDealReason() {
        return dealReason;
    }

    public void setDealReason(String dealReason) {
        this.dealReason = dealReason;
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
}
