package model;

import cn.superid.jpa.annotation.Cacheable;
import cn.superid.jpa.orm.CacheableDao;
import cn.superid.jpa.orm.ExecutableModel;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by xiaofengxu on 16/9/28.
 */
@Entity
@Table(name = "user")
@Cacheable(key = "testus")
public class BaseUser  extends ExecutableModel {
    public static CacheableDao dao = new CacheableDao(BaseUser.class);
    @Protobuf(fieldType = FieldType.UINT32, order=1, required = true)
    private int id;
    @Protobuf(fieldType = FieldType.STRING, order=2, required = true)
    private String name;
    @Protobuf(fieldType = FieldType.UINT32, order=3, required = true)
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
