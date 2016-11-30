package cn.superid.webapp.model;

import cn.superid.jpa.annotation.PartitionId;
import cn.superid.jpa.orm.ConditionalDao;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by njuTms on 16/11/30.
 * 用来记录盟中的用户,可能会存在将用户拉进盟,但没有赋予角色的情况
 */
@Table(name = "alliance_user")
public class AllianceUserEntity {
    public final static ConditionalDao<AllianceUserEntity> dao = new ConditionalDao<>(AllianceUserEntity.class);
    private long id;
    private long allianceId;
    private long userId;
    private int state;

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @PartitionId
    public long getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(long allianceId) {
        this.allianceId = allianceId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
