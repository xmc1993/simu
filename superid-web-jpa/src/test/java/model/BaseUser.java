package model;

import cn.superid.jpa.annotation.Cacheable;
import cn.superid.jpa.orm.CacheableDao;
import cn.superid.jpa.orm.ExecutableModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by xiaofengxu on 16/9/28.
 */
@Entity
@Table(name = "user")
@Cacheable(key = "us")
public class BaseUser  extends ExecutableModel {
    public static CacheableDao dao = new CacheableDao(BaseUser.class);
    private int id;
    private String name;
    private int age;

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
