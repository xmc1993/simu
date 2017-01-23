
import cn.superid.jpa.cache.RedisTemplate;
import cn.superid.jpa.core.Session;
import cn.superid.jpa.exceptions.JdbcRuntimeException;
import cn.superid.jpa.util.Expr;
import cn.superid.jpa.util.Pagination;
import cn.superid.jpa.util.ParameterBindings;
import junit.framework.TestCase;
import model.BaseUser;
import model.Role;
import model.User;
import org.junit.Assert;
import org.springframework.beans.BeanUtils;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by zp on 2016/7/20.
 */
public class TestExecute extends TestCase {
    static {
        new InitResource();
    }
    public void testFindById() {
//        User user = User.findById()
    }

    public User testSave() {
        User user = new User();
        user.setName("zp");
        user.setAge(18);
        user.save();
        Assert.assertTrue(User.dao.findById(user.getId()) != null);
        return user;
    }


    public void testUpdate() {
        User user = User.dao.findById(1);
        user.setAge(25);
        user.update();
        User _user = User.dao.findById(1);
        Assert.assertTrue(_user.getAge() == 25);
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
        List<User> list = User.dao.findListByNativeSql("select * from user where name=?", "zp");
        Assert.assertTrue(list != null);
    }

    public void testFindOne() {
        testSave();
        User user = User.dao.findOneByNativeSql("select * from user where name=? and age =? limit 1 ", "zp",18);
        Assert.assertTrue(user != null);
    }

    public void testSelectOne() {
        testSave();
        User user = User.dao.eq("name", "xxf").selectOne("id", "name");
        Assert.assertTrue(user != null);
    }


    public void testSelectList() {
        List<User> users = User.dao.eq("name", "zp").asc("age").selectList("id","name");
        Assert.assertTrue(users != null);
        List<Long> ids =(List<Long>) User.dao.eq("name", "zp").asc("age").selectList(Long.class,"id");
        Assert.assertTrue(ids != null);

    }

    public void testOrConditions(){
        List<User> users1 = User.dao.or(Expr.eq("name","zp"),Expr.eq("name","xxf")).selectList();
        String[] names ={"zp","xxf"};
        List<User> users2 = User.dao.in("name",names).selectList();
        Assert.assertTrue(users1.size()==users2.size());

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
        User _user = User.dao.findById(user.getId());
        Assert.assertTrue(((User) _user).getAge() == 38);


        User.dao.eq("name", "xxf").set(" age = age + 1 ", null);
        Object __user = User.dao.findById(user.getId());
        Assert.assertTrue(((User) __user).getAge() == 39);

    }

    public void testExecute() {
        User.execute("update user set name=? where name='zp'", new ParameterBindings("jzy"));
        Assert.assertTrue(User.dao.eq("name", "zp").selectOne() == null);
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
        pagination.setPage(1);
        pagination.setSize(20);
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
        Role _role = Role.dao.findById(role.getId(), user.getId());
        Assert.assertTrue(((Role) _role).getTitle().equals("开发人员"));

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

    @org.junit.Test
    public void testHmget(){
       final User user = new User();
        user.setName("zphahah");
        user.setAge(19);
        user.setDetails("hasasasasa");
        user.save();

        Timer.compair(new Execution() {
            @Override
            public void execute() {
                User.dao.findById(user.getId());
            }
        }, new Execution() {
            @Override
            public void execute() {
                User.dao.id(user.getId()).selectOne();

            }
        },10);

        User user1 = User.dao.findById(user.getId());
        Assert.assertTrue(user1.getName().equals(user.getName()));
    }

    @org.junit.Test
    public void testExecuteBatch(){
        Session session =User.getSession();

        List<Object> users = new ArrayList<>();
        for(int i=0;i<10;i++){
            User user = new User();
            user.setName("zp"+i);
            users.add(user);
        }
        session.saveBatch(users);//批量保存userlist

        session.startBatch();
        for(int i=0;i<10;i++){
            User.execute("update user set name=? where name=?", new ParameterBindings("jzy"+i,"zp"+i));//批量修改
        }
        session.executeBatch();
        session.endBatch();

//        BaseUser.dao.partitionId("a").id("a").selectOne();
    }


    @org.junit.Test
    public void testIsNull(){
//        User user = User.dao.isNotNull("name").selectOne();
//        Assert.assertTrue(user!=null);

//        User user = new User();
//        Timer timer = new Timer();
//        String c ="asasasasasas";
//        int b=0;
//        for(int i=0;i<100000000;i++){
//            a = BinaryUtil.getBytes(c);
//        }
//        System.out.print(a);
//        timer.end();
    }

    @org.junit.Test
    public void testRedisBatch(){
        List<User> users = User.dao.gt("id",0).selectList();

        final Integer[] ids = new Integer[users.size()];
        int i=0;
        for(User user:users){
            BaseUser baseUser = new BaseUser();
            user.copyPropertiesTo(baseUser);
            RedisTemplate.save(baseUser);
            ids[i++] = user.getId();
        }

        Timer.compair(new Execution() {
            @Override
            public void execute() {
                List<BaseUser> result =(List<BaseUser>) RedisTemplate.batchGet(ids, BaseUser.class,"name");
            }
        }, new Execution() {
            @Override
            public void execute() {
                List<BaseUser> result = new ArrayList<BaseUser>();
                for(int i=0;i<ids.length;i++){
                }
            }
        },300);


    }
}
