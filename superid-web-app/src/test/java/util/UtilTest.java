package util;

import cn.superid.jpa.orm.FieldAccessor;
import cn.superid.jpa.util.StringUtil;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.notice.chat.Constant.C2CType;
import cn.superid.webapp.notice.chat.proto.C2C;
import cn.superid.webapp.utils.Timer;
import cn.superid.webapp.utils.CheckFrequencyUtil;
import cn.superid.webapp.utils.Timer;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.sun.istack.internal.NotNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zp on 2016/8/4.
 */
@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-all-test.xml")
public class UtilTest {
    private static Codec<C2C>  codec = ProtobufProxy.create(C2C.class);

    @Test
    public void testCheckFrequency(){
        Assert.assertFalse(CheckFrequencyUtil.isFrequent("test"));
        Assert.assertFalse(CheckFrequencyUtil.isFrequent("test"));
        Assert.assertFalse(CheckFrequencyUtil.isFrequent("test"));
        Assert.assertFalse(CheckFrequencyUtil.isFrequent("test"));
        Assert.assertTrue(CheckFrequencyUtil.isFrequent("test"));
    }

    @Test
    public void test (){
        C2C ping = new C2C(C2CType.PING, null);
        try {

            System.out.println(codec.encode(ping));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheck(){
        final List<UserEntity> test = new ArrayList(100000);
        for(int i=0;i<100000;i++){
            UserEntity userEntity = new UserEntity();
            test.add(userEntity);
        }
        util.Timer.compair(1000, new Execution() {
                    @Override
                    public void execute() {
                        for(UserEntity i:test){
                            i.setId(0);
                        }
                    }
                }, new Execution() {
                    @Override
                    public void execute() {

                        for(int i=0,l=test.size();i<l;i++){
                            UserEntity userEntity = test.get(i);
                            userEntity.setId(0);
                        }
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
