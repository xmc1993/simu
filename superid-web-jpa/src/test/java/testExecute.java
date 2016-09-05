

import cn.superid.jpa.core.impl.JdbcSessionFactory;
import cn.superid.jpa.util.Expr;
import cn.superid.jpa.util.Pagination;
import cn.superid.jpa.util.ParameterBindings;
import com.alibaba.druid.pool.DruidDataSource;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;


/**
 * Created by zp on 2016/7/20.
 */
public class testExecute extends TestCase {

    public static DruidDataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("superid");
        dataSource.setPassword("superid");
        dataSource.setUrl("jdbc:mysql://192.168.1.100/test");
        dataSource.setInitialSize(5);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(10);
        return dataSource;
    }

    static {
        JdbcSessionFactory jdbcSessionFactory = new JdbcSessionFactory(getDataSource());
    }

    public void testFindById() {
//        User user = User.findById()
    }

    public void testSave() {
        User user = new User();
        user.setName("zp");
        user.setAge(18);
        user.save();
        Assert.assertTrue(User.dao.findById(user.getId()) != null);
    }


    public void testUpdate() {
        User user = User.dao.findById(1);
        user.setAge(25);
        user.update();
        Assert.assertTrue(User.dao.findById(1).getAge() == 25);
    }


    public void testDelete() {
        User user = new User();
        user.setName("src/test");
        user.setAge(18);
        user.save();
        user.delete();
        Assert.assertTrue(User.dao.findById(user.getId()) == null);
    }


    public void testFindList() {
        List<User> list = User.dao.findList("select * from user where name=?", "zp");
        Assert.assertTrue(list != null);
    }

    public void testFindOne() {
        User user = User.dao.findOne("select * from user where name=?", "zp");
        Assert.assertTrue(user != null);
    }

    public void testSelectOne() {
        User user = User.dao.eq("name", "xxf").selectOne("id","name");
        Assert.assertTrue(user != null);
    }


    public void testSelectList() {
        List<User> users = User.dao.eq("name", "zp").asc("age").selectList();
        Assert.assertTrue(users != null);
    }


    public void testRemove() {
        User user = new User();
        user.setAge(33);
        user.setName("tms");
        user.save();
        User.dao.eq("name", "tms").remove();
        Assert.assertTrue(User.dao.findById(user.getId()) == null);
    }

    public void testHashMapFromEntity(){
        User user = new User();
        user.setName("src/test");
        user.setAge(18);
        user.save();
        HashMap<String,Object> hashMap=User.getSession().getHashMapFromEntity(user);
        Assert.assertTrue(hashMap.get("age").equals(18));

        User user1=new User();
        User.getSession().generateHashMapFromEntity(hashMap,user1);

        Assert.assertTrue(user1.getAge()==18);

    }


    public void testSet() {
        User user = new User();
        user.setAge(33);
        user.setName("tms");
        user.save();
        User.dao.eq("name", "tms").set("name", "xxf", "age", 38);//把tms改成xxf，年龄改为38
        Assert.assertTrue(User.dao.findById(user.getId()).getAge() == 38);

        User.dao.eq("name","xxf").set(new Expr(" age = age + 1 "));
        Assert.assertTrue(User.dao.findById(user.getId()).getAge() == 39);

        User.dao.eq("name","xxf").set(" age = age + 1 ",null);
        Assert.assertTrue(User.dao.findById(user.getId()).getAge() == 40);

    }

    public void testExecute() {
        User.execute("update user set name=? where name='zp'", new ParameterBindings("jzy"));
        Assert.assertTrue(User.dao.eq("name", "zp").selectOne() == null);
    }


    public void testTiny() {
        User user = new User();
        user.setName("jzy");
        user.setDetails("The MyBatis SQL mapper framework makes it easier to use a relational database with object-oriented applications. MyBatis couples objects with stored procedures or SQL statements" +
                " using a XML descriptor or annotations." +
                " Simplicity is the biggest advantage of the MyBatis" +
                " data mapper over object relational mapping tools.");
        user.save();
        User user1 =User.dao.findTinyById(user.getId());
        Assert.assertTrue(user1.getDetails()==null);
    }

    public void testCopy(){
        UserAddForm userAddForm = new UserAddForm();
        userAddForm.setName("zp");
        userAddForm.setAge(10L);//类型不同不会copy
        User user=new User();
        user.copyPropertiesFrom(userAddForm);
        Assert.assertTrue(user.getName().equals("zp")&&user.getAge()==0);

        UserAddForm test2 = new UserAddForm();
        user.copyPropertiesTo(test2);
        Assert.assertTrue(test2.getName().equals("zp")&&test2.getAge()==null);

    }

    public void testPagination(){
        Pagination pagination=new Pagination();
        List<User> users=User.dao.eq("name","xxf").selectByPagination(pagination);
        Assert.assertFalse(users.size()>pagination.getTotal());

    }



}
