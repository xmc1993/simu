package util;

import cn.superid.jpa.orm.FieldAccessor;
import cn.superid.jpa.util.StringUtil;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.utils.Timer;
import cn.superid.webapp.utils.CheckFrequencyUtil;
import cn.superid.webapp.utils.Timer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

/**
 * Created by zp on 2016/8/4.
 */
@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-all-test.xml")
public class UtilTest {
    @Test
    public void testCheckFrequency(){
        Assert.assertFalse(CheckFrequencyUtil.isFrequent("test"));
        Assert.assertFalse(CheckFrequencyUtil.isFrequent("test"));
        Assert.assertFalse(CheckFrequencyUtil.isFrequent("test"));
        Assert.assertFalse(CheckFrequencyUtil.isFrequent("test"));
        Assert.assertTrue(CheckFrequencyUtil.isFrequent("test"));
    }

    @Test
    public void testCheck(){
        System.out.println(StringUtil.longToString(1481522657490L));
        System.out.println(StringUtil.generateId(1481522657490L,8));
        System.out.println(StringUtil.generateId(148152L,8));
        System.out.println(StringUtil.generateId(148151L,8));



    }

    @Test
    public void testReflect(){
        UserEntity userEntity=new UserEntity();
        userEntity.setAge(20);
        FieldAccessor fieldAccessor = FieldAccessor.getFieldAccessor(userEntity.getClass(), "age");

        UserEntity userEntity1 =new UserEntity();

        int a;

        Timer timer =new Timer();
        for(int i=0;i<10000000;i++){
            a = userEntity.getAge();
        }
        timer.end();

        Timer timer1 = new Timer();
        for(int i=0;i<10000000;i++){
            a = (int)fieldAccessor.getProperty(userEntity);;
        }
        timer1.end();

        Timer timer2=new Timer();
        for(int i=0;i<10000000;i++){
            userEntity.copyPropertiesTo(userEntity1);
        }
        timer2.end();
    }







}
