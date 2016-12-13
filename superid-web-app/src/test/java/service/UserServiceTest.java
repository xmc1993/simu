package service;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.controller.VO.SimpleAllianceVO;
import cn.superid.webapp.controller.VO.SimpleRoleVO;
import cn.superid.webapp.controller.VO.UserAllianceRolesVO;
import cn.superid.webapp.forms.EditUserBaseInfo;
import cn.superid.webapp.forms.EditUserDetailForm;
import cn.superid.webapp.forms.ResultUserInfo;
import cn.superid.webapp.model.AllianceEntity;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.model.cache.UserBaseInfo;
import cn.superid.webapp.security.IAuth;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.service.vo.AllianceRolesVO;
import cn.superid.webapp.utils.PasswordEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import util.JUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

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
        userEntity.setId(1888L);
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

    @Test
    public void testUserAllianceRoles(){
        long userid = 1893L;
        List<UserAllianceRolesVO> roles1 = new ArrayList<>();
        List<UserAllianceRolesVO> roles2 = new ArrayList<>();
        List<AllianceRolesVO> testVos ;
        StringBuilder sb;
        ParameterBindings p;

        //算法1
        long beginTime = System.currentTimeMillis();
        //先取出用户拥有的所有盟
        sb = new StringBuilder("select a.id, a.name from alliance a where a.id in (select alliance_id from role r where r.user_id = ? group by alliance_id)");
        p = new ParameterBindings();
        p.addIndexBinding(userid);
        List<SimpleAllianceVO> allianceVOs = AllianceEntity.getSession().findList(SimpleAllianceVO.class,sb.toString(),p);

        //对于该用户的每个盟
        for(SimpleAllianceVO simpleAllianceVO : allianceVOs){
            UserAllianceRolesVO userAllianceRolesVO = new UserAllianceRolesVO();
            userAllianceRolesVO.setAllianceId(simpleAllianceVO.getId());
            userAllianceRolesVO.setAllianceName(simpleAllianceVO.getName());
            //根据allianceId和userId去role表中取出相应的数据
            sb = new StringBuilder("select r.id, r.title from role r where r.user_id = ? and r.alliance_id = ?");
            p = new ParameterBindings();
            p.addIndexBinding(userid);
            p.addIndexBinding(simpleAllianceVO.getId());
            List<SimpleRoleVO> simpleRoleVOs = RoleEntity.getSession().findList(SimpleRoleVO.class,sb.toString(),p);
            //将list转为字符串放到userAllianceRole中
            userAllianceRolesVO.setRoles(simpleRoleVOs);

            roles1.add(userAllianceRolesVO);
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-beginTime);


        //算法2
        beginTime = System.currentTimeMillis();
        sb = new StringBuilder(
                "select t1.id,t1.name,t2.role_id,t2.title  from  (select a.id,a.name from alliance  a where a.id in (select alliance_id from role where user_id = 1893 group by alliance_id)) t1 " +
                "join" +
                "(select r.id as role_id,r.title,r.alliance_id from role r where r.user_id = 1893) t2 " +
                "on t1.id = t2.alliance_id ");
        testVos = AllianceEntity.getSession().findList(AllianceRolesVO.class,sb.toString());
        List<Long> allianceIds = new ArrayList<>();
        List<SimpleRoleVO> simpleRoleVOs = new ArrayList<>();
        //用于定位,用法在循环里有解释
        int index = -1;
        //下面的这个for循环的前提条件是取出来的数据是group by allianceId的,不然就错了
        //应该是group的,因为t1表是Group盟的,就是说在join的时候一张表是已经group的
        //算法的思路是,先将盟id抽取出来,然后遍历testVos,如果盟id相同就更新结果list中的相应的盟id的记录
        for(AllianceRolesVO allianceRolesVO : testVos){
            if(allianceIds.contains(allianceRolesVO.getAllianceId())){
                SimpleRoleVO simpleRoleVO = new SimpleRoleVO();
                simpleRoleVO.setRoleId(allianceRolesVO.getRoleId());
                simpleRoleVO.setRoleName(allianceRolesVO.getRoleName());
                simpleRoleVOs.add(simpleRoleVO);
                roles2.get(index).setRoles(simpleRoleVOs);
                continue;
            }

            UserAllianceRolesVO userAllianceRolesVO = new UserAllianceRolesVO();
            userAllianceRolesVO.setAllianceId(allianceRolesVO.getAllianceId());
            userAllianceRolesVO.setAllianceName(allianceRolesVO.getAllianceName());
            //碰到不一样的就重置simpleRoles
            simpleRoleVOs = new ArrayList<>();
            SimpleRoleVO simpleRoleVO = new SimpleRoleVO();
            simpleRoleVO.setRoleId(allianceRolesVO.getRoleId());
            simpleRoleVO.setRoleName(allianceRolesVO.getRoleName());
            simpleRoleVOs.add(simpleRoleVO);
            userAllianceRolesVO.setRoles(simpleRoleVOs);
            roles2.add(userAllianceRolesVO);
            //定位现在的userAllianceRolesVO
            index++;

            allianceIds.add(allianceRolesVO.getAllianceId());

        }
        endTime = System.currentTimeMillis();
        System.out.println(endTime-beginTime);

    }


}
