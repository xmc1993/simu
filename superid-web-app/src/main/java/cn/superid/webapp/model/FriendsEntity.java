package cn.superid.webapp.model;

import cn.superid.jpa.annotation.PartitionId;
import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by njuTms on 16/10/28.
 */
@Table(name = "friends")
@Entity
public class FriendsEntity extends ExecutableModel{
    public final static ConditionalDao<FriendsEntity> dao = new ConditionalDao<>(FriendsEntity.class);
    private long id;
    private long fromUserId;
    private long toUserId;
    private long friendApplicationId;
    private int state; //默认为0,表示是好友关系,1表示断绝好友关系
    private Timestamp createTime;
    private Timestamp modifyTime;

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @PartitionId
    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

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

    public long getFriendApplicationId() {
        return friendApplicationId;
    }

    public void setFriendApplicationId(long friendApplicationId) {
        this.friendApplicationId = friendApplicationId;
    }
}
