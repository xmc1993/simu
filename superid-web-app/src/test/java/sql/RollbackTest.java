package sql;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.model.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by njuTms on 16/12/13.
 * 测试事务回滚
 */

@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/spring-all-test.xml"})
public class RollbackTest {
    @Test
    public void testRollback(){

        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding("tmssss");
        p.addIndexBinding(1899L);
        StringBuilder correctSql = new StringBuilder("update user set username = ? where id= ?");
        //int count = UserEntity.dao.id(1899L).set("username","tms");
        int count = UserEntity.getSession().execute(correctSql.toString(),p);

        UserEntity user = UserEntity.dao.findById(1899L);
        System.out.println(1);
        /*
        try{
            StringBuilder incorrectSql = new StringBuilder("update user set userame = 'tms' where id=1899");
            UserEntity.getSession().execute(incorrectSql.toString());
        }catch (RuntimeException e){
            e.printStackTrace();
        }
        */

    }

}
