import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.model.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jizhenya on 16/9/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-all-test.xml")
public class JPATest {

    @Test
    public void testJoin(){
        RoleEntity role = RoleEntity.dao.join(UserEntity.dao).on("user_id","id").eq("a.id",5).selectOne("a.id","a.user_id");
        System.out.println(role.getId());
        System.out.println(role.getUserId());
    }

    @Test
    public void testSelectOne(){
        RoleEntity role = RoleEntity.dao.eq("id",5).selectOne("id","user_id");
        System.out.println(role.getId());
        System.out.println(role.getUserId());

    }
}
