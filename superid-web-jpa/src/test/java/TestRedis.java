import cn.superid.jpa.core.impl.JdbcSessionFactory;
import cn.superid.jpa.redis.RedisUtil;
import model.User;
import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by xiaofengxu on 16/9/21.
 */

public class TestRedis {
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
        user.setName("zphahah");
        user.setAge(19);
        user.setDetails("hasasasasa");
        user.setId(1);
        String result= RedisUtil.save(user);
        System.out.println(result);

    }

    @Test//as result,the spend time almost equal;but the memory,save as hashmap,the use-memory-human is 988kb to 2.31M,and save as serialize,988kb to 3.37M
    public void testRedisPoJO(){
        final User user = new User();
        user.setName("zphahah");
        user.setAge(19);
        user.setDetails("hasasasasa");
        user.setId(1);
        Timer.compair(new Execution() {
            @Override
            public void execute() {
                user.setId(user.getId()+1);
                RedisUtil.save(user);
                User user1=(User) RedisUtil.findByKey(user.getId(),user.getClass());
                Assert.assertTrue(user1.getName().equals(user.getName()));

            }
        }, new Execution() {
            @Override
            public void execute() {
                user.setId(user.getId()+1);
                Jedis jedis=RedisUtil.getJedis();
                jedis.set(("user"+user.getId()).getBytes(),SerializeUtil.serialize(user));
                jedis.close();
                jedis = RedisUtil.getJedis();
                User user1 =(User)SerializeUtil.unserialize(jedis.get(("user"+user.getId()).getBytes()));
                jedis.close();
                Assert.assertTrue(user1.getName().equals(user.getName()));
            }
        },10000);

    }







}
