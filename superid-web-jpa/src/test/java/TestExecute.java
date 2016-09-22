
import cn.superid.jpa.core.impl.JdbcSessionFactory;
import cn.superid.jpa.exceptions.JdbcRuntimeException;
import cn.superid.jpa.util.Expr;
import cn.superid.jpa.util.Pagination;
import cn.superid.jpa.util.ParameterBindings;
import com.alibaba.druid.pool.DruidDataSource;
import junit.framework.TestCase;
import model.Role;
import model.User;
import org.junit.Assert;
import org.springframework.beans.BeanUtils;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by zp on 2016/7/20.
 */
public class TestExecute extends TestCase {

    public static DruidDataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("superid");
        dataSource.setPassword("superid");
        dataSource.setUrl("jdbc:mysql://rm-bp1943x791y4e3z21.mysql.rds.aliyuncs.com/jpa");
        dataSource.setInitialSize(5);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(1000);
        return dataSource;
    }

    static {
        JdbcSessionFactory jdbcSessionFactory = new JdbcSessionFactory(getDataSource());
    }

    public void testFindById() {
//        User user = User.findById()
    }

    public User testSave() {
        User user = new User();
        user.setName("zp");
        user.setAge(18);
        user.save();
        System.out.println("a");

        Assert.assertTrue(User.dao.findById(user.getId()) != null);
        return user;
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
        testSave();
        User user = User.dao.findOne("select * from user where name=?", "zp");
        Assert.assertTrue(user != null);
    }

    public void testSelectOne() {
        User user = User.dao.eq("name", "xxf").selectOne("id", "name");
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

    public void testHashMapFromEntity() {
        User user = new User();
        user.setName("src/test");
        user.setAge(18);
        user.save();
        HashMap<String,Object> hashMap=User.getSession().generateHashMapFromEntity(user,false);
        Assert.assertTrue(hashMap.get("age").equals(18));

        User user1 = new User();
        User.getSession().generateHashMapFromEntity(hashMap, user1);

        Assert.assertTrue(user1.getAge() == 18);

    }

    public void testHashByteMapFromEntity() {
        User user = new User();
        user.setName("src/test");
        user.setAge(18);
        user.save();
//        Assert.assertTrue(hashMap.get("age").equals(18));
//
//        User user1 = new User();
//        User.getSession().generateHashByteMapFromEntity(hashMap, user1);
//
//        Assert.assertTrue(user1.getAge() == 18);

    }


    public void testSet() {
        User user = new User();
        user.setAge(33);
        user.setName("tms");
        user.save();
        User.dao.eq("name", "tms").set("name", "xxf", "age", 38);//把tms改成xxf，年龄改为38
        Assert.assertTrue(User.dao.findById(user.getId()).getAge() == 38);

        User.dao.eq("name", "xxf").set(new Expr(" age = age + 1 "));
        Assert.assertTrue(User.dao.findById(user.getId()).getAge() == 39);

        User.dao.eq("name", "xxf").set(" age = age + 1 ", null);
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
        User user1 = User.dao.findTinyById(user.getId());
        Assert.assertTrue(user1.getDetails() == null);
    }

    public void testCopy() {
        UserAddForm userAddForm = new UserAddForm();
        userAddForm.setName("zp");
        userAddForm.setAge(10);
        User user = new User();
        user.copyPropertiesFrom(userAddForm);
        Assert.assertTrue(user.getName().equals("zp") && user.getAge() == 10);

        UserAddForm test2 = new UserAddForm();
        user.copyPropertiesTo(test2);
        Assert.assertFalse(test2.getName().equals("zp") && test2.getAge() == null);

    }

    public void testPagination() {
        Pagination pagination = new Pagination();
        List<User> users = User.dao.eq("name", "xxf").selectByPagination(pagination);
        Assert.assertFalse(users.size() > pagination.getTotal());

    }

    public void testUpdateWithPartition(){
        User user=testSave();
        Role role = new Role();
        role.setTitle("开发人员");
        try {
            role.save();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof JdbcRuntimeException);
            e.printStackTrace();
        }

        role.setUserId(user.getId());
        role.save();
        Assert.assertTrue(role.getUserId()==user.getId());
    }

    public void testDeleteWithPartition(){
        User user=testSave();
        Role role = new Role();
        role.setTitle("开发人员");
        role.setUserId(user.getId());
        try {
            role.delete();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof JdbcRuntimeException);
        }

        role.save();

        try {
            Role.dao.findById(role.getId());
        } catch (Exception e) {
            Assert.assertTrue(e instanceof JdbcRuntimeException);
            e.printStackTrace();
        }
        Assert.assertTrue(Role.dao.findById(role.getId(),user.getId()).getTitle().equals("开发人员"));


        role.delete();

    }


    /**
     * using 1324ms, start at Thu Sep 08 16:55:34 CST 2016, end at Thu Sep 08 16:55:35 CST 2016
     using 1115ms, start at Thu Sep 08 16:55:35 CST 2016, end at Thu Sep 08 16:55:36 CST 2016
     using 1156ms, start at Thu Sep 08 16:55:36 CST 2016, end at Thu Sep 08 16:55:37 CST 2016
     */
    public void testPerformance() {
//        User user = new User();
//        user.setAge(33);
//        user.setName("tms");
//        user.save();
//
//        Timer timer = new Timer();
//        for (int i = 0; i < 1000; i++) {
//            User.dao.eq("name", "tms").set("name", "xxf", "age", 38);//把tms改成xxf，年龄改为38
//        }
//        timer.end();
//
//        Timer timer1 = new Timer();
//        for (int i = 0; i < 1000; i++) {
//            User.execute("update user set name = ? ,age=? where name =?", new ParameterBindings("tms", 38, "xxf"));
//        }
//        timer1.end();
//        DataSource dataSource =getDataSource();
//        Connection connection = null;
//        PreparedStatement preparedStatement = null;
//        Timer timer2 = new Timer();
//        for (int i = 0; i < 1000; i++) {
//
//            try {
//                connection = dataSource.getConnection();
//                preparedStatement = connection.prepareStatement("update user set name = ? ,age=? where name =?");
//                preparedStatement.setObject(1, "xxf");
//                preparedStatement.setObject(2, 38);
//                preparedStatement.setObject(3, "tms");
//                preparedStatement.executeUpdate();
//                preparedStatement.close();
//
//            } catch (SQLException e) {
//
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (connection != null)
//                        connection.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//        timer2.end();



    }

    /**
     * using 385ms, start at Sun Sep 18 15:54:39 CST 2016, end at Sun Sep 18 15:54:39 CST 2016
     using 932ms, start at Sun Sep 18 15:54:39 CST 2016, end at Sun Sep 18 15:54:40 CST 2016
     */
    public void  testCopyProperties(){
        UserAddForm userAddForm = new UserAddForm();
        userAddForm.setName("zp");
        userAddForm.setAge(10);

        Timer timer =new Timer();
        for(int i=0;i<1000000;i++){
            User user = new User();
            user.copyPropertiesFrom(userAddForm);
        }
        timer.end();

        Timer timer1=new Timer();
        for(int i=0;i<1000000;i++){
            User user = new User();
            BeanUtils.copyProperties(userAddForm,user);
        }
        timer1.end();

    }




    @Test(threadPoolSize = 10, invocationCount = 100)
    public void testMutilThread(){
        testUpdate();
        testDelete();
        testSelectOne();

    }

}
