package service;

import cn.superid.webapp.forms.EditUserBaseInfo;
import cn.superid.webapp.forms.EditUserDetailForm;
import cn.superid.webapp.forms.ResultUserInfo;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.model.base.UserBaseInfo;
import cn.superid.webapp.security.IAuth;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.tasks.RunningTests;
import cn.superid.webapp.utils.PasswordEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;

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
        Assert.assertTrue(result.getPersonalRoleId()!=0);
    }

    @Test
     public void testEditInfo(){
        UserEntity testUser = addUser();
        RunningTests.userId =testUser.getId();
        EditUserBaseInfo editUserBaseInfo = new EditUserBaseInfo();
        editUserBaseInfo.setAvatar("test");
        userService.editBaseInfo(editUserBaseInfo);
        UserBaseInfo userBaseInfo=UserBaseInfo.dao.findById(testUser.getId());
        Assert.assertTrue(userBaseInfo.getAvatar().equals("test"));
        Assert.assertTrue(userBaseInfo.getUsername().equals("大哥鹏"));

    }


    @Test
    public void testEditDetailInfo(){
        UserEntity testUser = addUser();
        RunningTests.userId =testUser.getId();
        EditUserDetailForm editUserDetailForm= new EditUserDetailForm();
        editUserDetailForm.setAddress("南京");
        userService.editDetailInfo(editUserDetailForm);
        UserEntity userEntity= UserEntity.dao.findById(testUser.getId());
        Assert.assertTrue(userEntity.getEducationLevel()==0);
        Assert.assertTrue(userEntity.getAddress().equals("南京"));

    }

   @Test
   public void testGetUserInfo(){
       UserEntity testUser = addUser();
       RunningTests.userId =testUser.getId();
       ResultUserInfo resultUserInfo=userService.getUserInfo(testUser.getId());
       Assert.assertTrue(testUser.getUsername().equals("大哥鹏"));
   }

    @Test
    public void testChangePassWord(){
        UserEntity testUser = addUser();
        RunningTests.userId =testUser.getId();
        userService.changePwd("123456","111111");


        Assert.assertTrue(testUser.getUsername().equals("大哥鹏"));
    }


    @Test
    public void testRedis(){

        for(int i=0;i<1000;i++){
            RoleEntity.dao.getDRDSAutoId();
        }
        System.out.println(RoleEntity.dao.getDRDSAutoId());
//        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps("test");
//        Map<String, String> data = new HashMap();
//        data.put("test", "test");
//
//        ops.putAll(data);
    }

}
