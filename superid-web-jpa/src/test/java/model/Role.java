package model;

import cn.superid.jpa.annotation.PartitionId;
import cn.superid.jpa.orm.Dao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Id;

/**
 * Created by xiaofengxu on 16/9/12.
 */
public class Role extends ExecutableModel{
    public static Dao<Role> dao = new Dao(Role.class);

    private int id;
    private int userId;
    private String title;
    private String details;


    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @PartitionId
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
