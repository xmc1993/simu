import cn.superid.webapp.controller.UserController;
import cn.superid.webapp.forms.EditUserBaseInfo;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.model.base.UserBaseInfo;
import cn.superid.webapp.security.IAuth;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.utils.AliSmsDao;
import cn.superid.webapp.utils.PasswordEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.validation.constraints.AssertTrue;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zp on 2016/8/9.
 */
@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/spring-all-test.xml"})
public class UserServiceTest{
    @Autowired
    private IUserService userService;
    @Autowired
    private IAuth auth;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private UserEntity addUser(){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("大哥鹏");
        userEntity.setPassword(PasswordEncryptor.encode("123456"));
        userEntity.setMobile("15951818231");
        UserEntity result = userService.createUser(userEntity);
        return  result;
    }

    @Test
    public void testCreateUser(){
        UserEntity result = addUser();
        Assert.assertFalse(result==null);

    }

    @Test
     public void testEditInfo(){
        UserEntity testUser = addUser();
        auth.authUser(testUser.getId());
        EditUserBaseInfo editUserBaseInfo = new EditUserBaseInfo();
        editUserBaseInfo.setAvatar("test");
        userService.editBaseInfo(editUserBaseInfo);
        UserBaseInfo userBaseInfo=UserBaseInfo.dao.findById(testUser.getId());
        Assert.assertTrue(userBaseInfo.getAvatar().equals("test"));

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

    @Test
    public void testRedis(){
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps("test");
        Map<String, String> data = new HashMap();
        data.put("test", "test");

        ops.putAll(data);
    }

}
