package cn.superid.admin.model;

import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by njuTms on 16/10/9.
 */
@Table(name = "admin")
public class AdminEntity extends ExecutableModel{
    public static ConditionalDao dao = new ConditionalDao(AdminEntity.class);

    private long id;
    private String name;
    private String password;

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
