import cn.superid.jpa.core.impl.JdbcSessionFactory;
import cn.superid.jpa.redis.RawRedis;
import cn.superid.jpa.util.SerializeUtil;
import model.User;
import org.junit.Test;

import java.sql.Time;

/**
 * Created by xiaofengxu on 16/9/21.
 */
public class TestRedis {
    private static  RawRedis redis = new RawRedis("localhost");
    JdbcSessionFactory jdbcSessionFactory = new JdbcSessionFactory(null);


    @Test
    public void testHmset(){
        final User user = new User();
        user.setName("zp");
        user.setAge(19);
        user.setDetails("hasasasasa");
        user.setId(1);

        Timer.compair(new Execution() {
            @Override
            public void execute() {
                user.setId(user.getId()+1);
                redis.hmset(user.generateZipMap());
            }
        }, new Execution() {
            @Override
            public void execute() {
                user.setId(user.getId()+1);
                redis.hmset(user.getKey(),user.generateHashByteMap());
            }
        },1000);
    }
}
