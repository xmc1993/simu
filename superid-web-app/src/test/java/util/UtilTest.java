package util;

import cn.superid.jpa.orm.FieldAccessor;
import cn.superid.utils.StringUtil;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.utils.CheckFrequencyUtil;
import cn.superid.webapp.utils.Timer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

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
        Assert.assertFalse(StringUtil.isEmail("15951818230163.com"));
        Assert.assertTrue(StringUtil.isEmail("15951818239@163.com"));
        Assert.assertFalse(StringUtil.isMobile("159518182309"));
        Assert.assertTrue(StringUtil.isMobile("15951818230"));
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
