package model;

import cn.superid.jpa.annotation.CacheField;
import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zp on 2016/7/20.
 */
@Entity
@Table(name = "user")
public class User  extends ExecutableModel{
    public static ConditionalDao dao = new ConditionalDao(User.class);
    private int id;
    private String name;
    private int age;
    private String details;

    @Id
    @CacheField(order = 1)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @CacheField(order = 2)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @CacheField(order = 3)
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
