package cn.superid.webapp.model;

import cn.superid.jpa.orm.Dao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by njuTms on 16/8/31.
 */
@Table(name = "role")
public class RoleEntity extends ExecutableModel {
    public final static Dao<RoleEntity> dao = new Dao<>(RoleEntity.class);
    private Long id;
    private Long userId;
    private Long allianceId;
    private Long belongAffairId;
    private String title;
    private String permissions;
    private int type;
    private int visible;
    private int state;
    private Timestamp createTime;
    private Timestamp modifyTime;

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(Long allianceId) {
        this.allianceId = allianceId;
    }

    public Long getBelongAffairId() {
        return belongAffairId;
    }

    public void setBelongAffairId(Long belongAffairId) {
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
