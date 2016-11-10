import cn.superid.webapp.enums.IntBoolean;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.security.AffairPermissionRoleType;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.service.IRoleService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.utils.PasswordEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by xiaofengxu on 16/10/26.
 */
@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/spring-all-test.xml"})
public class GenerateTestData {
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IAffairMemberService affairMemberService;

    @Test
    public  void generateMembers(){
//        UserEntity test = userService.createUser()
        long l = 15951819999L;
        for(int i=0;i<20;i++){
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername("大哥 "+i+" 代目");
            userEntity.setPassword(PasswordEncryptor.encode("123456"));
            userEntity.setMobile(String.valueOf(l+i));
            UserEntity result = userService.createUser(userEntity);
            affairMemberService.addMember(32L,36L,result.getPersonalRoleId(),null, AffairPermissionRoleType.GUEST_ID);
        }
    }

    @Test
    public void generateOfficialMembers(){
        for(long i=1150;i<1159;i++){
            UserEntity userEntity =UserEntity.dao.findById(i);
            RoleEntity roleEntity =roleService.createRole("小弟"+i,32L,userEntity.getId(),36L,null, IntBoolean.FALSE);
            affairMemberService.addMember(32L,36L,roleEntity.getId(),null, AffairPermissionRoleType.OFFICIAL_ID);
        }
    }
}