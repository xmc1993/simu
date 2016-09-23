import cn.superid.jpa.core.impl.JdbcSessionFactory;
import cn.superid.jpa.redis.RawRedis;
import cn.superid.jpa.redis.RedisUtil;
import cn.superid.jpa.util.SerializeUtil;
import model.User;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

import java.sql.Time;
import java.util.List;

/**
 * Created by xiaofengxu on 16/9/21.
 */
public class TestRedis {

    private static  RawRedis redis = new RawRedis("localhost");
    JdbcSessionFactory jdbcSessionFactory = new JdbcSessionFactory(null);
    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMaxTotal(300);
        jedisPoolConfig.setTestOnBorrow(true);
        new RedisUtil(jedisPoolConfig);
    }
    @Test
    public void testHmset(){
        final User user = new User();
        user.setName("zp");
        user.setAge(19);
        user.setDetails("hasasasasa");
        user.setId(1);

        String result= RedisUtil.save(user);
        System.out.println(result);

//        Timer.compair(new Execution() {
//            @Override
//            public void execute() {
//                user.setId(user.getId()+1);
//                redis.hmset(user.generateKey(),user.generateHashByteMap());
//
//            }
//        }, new Execution() {
//            @Override
//            public void execute() {
//                user.setId(user.getId()+1);
//                Jedis jedis=RedisUtil.getJedis();
//                jedis.hmset(user.generateKey(),user.generateHashByteMap());
//                jedis.close();
//
//            }
//        },1000);
    }

    @Test
    public void testHmget(){
        User user = new User();
        user.setId(4);

        List<byte[]> result = redis.hmget(user.generateKey(),"name".getBytes());
        int a= 0;
    }
}
