package sql;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.service.IAffairService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * Created by jizhenya on 16/11/25.
 */
@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/spring-all-test.xml"})
public class SQLResolve {

    @Test
    public void fixUserSuperid(){
        StringBuilder sb = new StringBuilder("select * from user");
        ParameterBindings p = new ParameterBindings();
        List<UserEntity> userList = UserEntity.dao.findList(sb.toString(),p);
        for(UserEntity u : userList){
            long id = u.getId();
            String superid = id+"";
            int length = 8 - superid.length();
            for(int i = 0 ; i<length ; i++){
                superid = "0"+superid;
            }
            u.setSuperid(superid);
            u.update();
        }
    }
}
