import cn.superid.webapp.controller.UserController;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.utils.AliSmsDao;
import cn.superid.webapp.utils.PasswordEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.validation.constraints.AssertTrue;
import java.util.Enumeration;

/**
 * Created by zp on 2016/8/9.
 */
@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/spring-all.xml"})
public class UserServiceTest{
    @Autowired
    private IUserService userService;

    @Test
    public void testCreateUser(){
        for(int i=0;i<10;i++){
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername("大哥鹏");
            userEntity.setPassword(PasswordEncryptor.encode("123456"));
            userEntity.setMobile("15951818230"+i);
            UserEntity result = userService.createUser(userEntity);
            Assert.assertFalse(result==null);


        }

    }




    @Test
    public void getVerifyCode(){
        UserEntity.execute("SET @uids := '';" +
                "UPDATE user u" +
                "   SET u.mobile = '1234'" +
                " WHERE u.username ='大哥鹏'" +
                "   AND ( SELECT @uids := CONCAT_WS(',',u.id, @uids) );" +
                "SELECT @uids;");
    }


}
