package util;

import cn.superid.jpa.orm.FieldAccessor;
import cn.superid.jpa.util.StringUtil;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.utils.Timer;
import cn.superid.webapp.utils.CheckFrequencyUtil;
import cn.superid.webapp.utils.Timer;
import com.sun.istack.internal.NotNull;
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

    private void test(@NotNull String a){
        System.out.println(a);
    }

    @Test
    public void testCheck(){

        test(null);
        util.Timer.compair(100000, new Execution() {
                    @Override
                    public void execute() {
                        String p = "testaaaaaaaaaaa";
                        String a = p + "/";
                    }
                }, new Execution() {
                    @Override
                    public void execute() {
                        StringBuilder stringBuilder = new StringBuilder("testaaaaaaaaaaa");
                        stringBuilder.append("/");
                        stringBuilder.toString();
                    }
                }
        );

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
