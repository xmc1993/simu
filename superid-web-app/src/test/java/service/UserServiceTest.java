package service;

import cn.superid.webapp.forms.EditUserBaseInfo;
import cn.superid.webapp.forms.EditUserDetailForm;
import cn.superid.webapp.forms.ResultUserInfo;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.model.cache.UserBaseInfo;
import cn.superid.webapp.security.AffairPermissionRoleType;
import cn.superid.webapp.security.IAuth;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.utils.PasswordEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import util.JUnit4ClassRunner;

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
    private IAffairMemberService affairMemberService;

    private UserEntity addUser(){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("汤茂思");
        userEntity.setPassword(PasswordEncryptor.encode("123456"));
        userEntity.setMobile("15958586666");
        UserEntity result = userService.createUser(userEntity);
        return  result;
    }

    @Test
    public void testFindUser(){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("大哥鹏");
        userEntity.setPassword(PasswordEncryptor.encode("123456"));
        userEntity.setMobile("15951818231");
        userEntity.save();
        UserEntity.dao.findById(userEntity.getId());

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
        JUnit4ClassRunner.setSessionAttr("userId",testUser.getId());
        EditUserBaseInfo editUserBaseInfo = new EditUserBaseInfo();
        editUserBaseInfo.setAvatar("test");
        userService.editBaseInfo(editUserBaseInfo);
        UserBaseInfo userBaseInfo=UserBaseInfo.dao.findById(testUser.getId());
        Assert.assertTrue(userBaseInfo.getAvatar().equals("test"));
        Assert.assertTrue(userBaseInfo.getUsername().equals(testUser.getUsername()));

    }


    @Test
    public void testEditDetailInfo(){
        UserEntity testUser = addUser();
        auth.setSessionAttr("userId",testUser.getId());
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
       auth.setSessionAttr("userId",testUser.getId());
       ResultUserInfo resultUserInfo=userService.getUserInfo(testUser.getId());
       Assert.assertTrue(testUser.getUsername().equals(testUser.getUsername()));
   }

    @Test
    public void testChangePassWord(){
        UserEntity testUser = addUser();

        JUnit4ClassRunner.setSessionAttr("userId",testUser.getId());
        userService.changePwd("123456","111111");

    }



}
