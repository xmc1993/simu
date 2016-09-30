package service;

import cn.superid.jpa.redis.RedisUtil;
import cn.superid.webapp.model.base.UserBaseInfo;

import cn.superid.webapp.utils.TimeUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import util.Execution;
import util.Timer;

/**
 * Created by jizhenya on 16/8/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-all-test.xml")
public class RedisCacheTest {

    @Test
    public void test(){

        UserBaseInfo.dao.findById(104);

        UserBaseInfo.dao.id(104).set("username","xxf");
        UserBaseInfo var = UserBaseInfo.dao.findById(104);
        Assert.assertTrue(UserBaseInfo.dao.findById(104).getUsername().equals("xxf"));

        final UserBaseInfo userBaseInfo = UserBaseInfo.dao.findById(104L);
        Assert.assertTrue(userBaseInfo.getUsername().equals("xxf"));

        UserBaseInfo getFromRedis =(UserBaseInfo) RedisUtil.findByKey(104L,UserBaseInfo.class);
        Assert.assertTrue(getFromRedis.getUsername().equals("xxf"));

        String username =(String) UserBaseInfo.dao.findFieldByKey(104,"username",String.class);
        Assert.assertTrue(username.equals("xxf"));

        UserBaseInfo.dao.id(104).set("username","aaa");
        Assert.assertTrue(UserBaseInfo.dao.findById(104).getUsername().equals("aaa"));

        UserBaseInfo.dao.id(104).set("username","jzy","avatar","test");
        Assert.assertTrue(UserBaseInfo.dao.findById(104).getAvatar().equals("test" ));

        UserBaseInfo userBaseInfo1 = new UserBaseInfo();
        userBaseInfo1.setBirthday(TimeUtil.getCurrentSqlTime());
        userBaseInfo1.setUsername("setByObject");
        UserBaseInfo.dao.id(104).setByObject(userBaseInfo1);
        UserBaseInfo userBaseInfo2 = UserBaseInfo.dao.findById(104);
        Assert.assertTrue(userBaseInfo2.getUsername().equals("setByObject" )&&userBaseInfo2.getAvatar().equals("test"));



//        Timer.compair(100, new Execution() {
//            @Override
//            public void execute() {
//                UserBaseInfo.dao.findById(104L);
//            }
//        }, new Execution() {
//            @Override
//            public void execute() {
//                RedisUtil.findByKey(104L, UserBaseInfo.class);
//            }
//        }, new Execution() {
//            @Override
//            public void execute() {
//                UserBaseInfo.dao.findFieldByKey(104, "username", String.class);
//            }
//        }, new Execution() {
//            @Override
//            public void execute() {
//                UserBaseInfo.dao.id(104).set("username", "jzy", "avatar", "test");
//            }
//        }, new Execution() {
//            @Override
//            public void execute() {
//                UserBaseInfo userBaseInfo1=UserBaseInfo.dao.findById(104L);
//                userBaseInfo1.setUsername("xxf");
//                userBaseInfo1.setAvatar("test");
//                userBaseInfo1.setBirthday(TimeUtil.getCurrentSqlTime());
//                userBaseInfo1.update();
//            }
//        });

    }


}
