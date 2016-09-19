package cn.superid.webapp.model;

import cn.superid.jpa.annotation.Cacheable;
import cn.superid.jpa.annotation.PartitionId;
import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by njuTms on 16/8/31.
 */
@Table(name = "role")
@Cacheable
public class RoleEntity extends ExecutableModel {
    public final static ConditionalDao<RoleEntity> dao = new ConditionalDao<>(RoleEntity.class);
    private long id;
    private long userId;
    private long allianceId;
    private long belongAffairId;
    private String title;
    private String permissions;
    private String allocatePermissions;
    private int type;
    private int visible;
    private int state;
    private Timestamp createTime;
    private Timestamp modifyTime;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @PartitionId
    public long getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(long allianceId) {
        this.allianceId = allianceId;
    }

    public long getBelongAffairId() {
        return belongAffairId;
    }

    public void setBelongAffairId(long belongAffairId) {
        this.belongAffairId = belongAffairId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getAllocatePermissions() {

        return allocatePermissions;
    }

    public void setAllocatePermissions(String allocatePermissions) {
        this.allocatePermissions = allocatePermissions;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
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
}
