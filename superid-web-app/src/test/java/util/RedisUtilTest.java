package util;

import cn.superid.webapp.utils.redis.RedisUtil;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Created by xmc1993 on 16/8/25.
 */
public class RedisUtilTest {
    @Test
    public void testSetKey(){
        Jedis jedisClient = RedisUtil.getJedisClient();
//        jedisClient.set("test", "test");
        System.out.println(jedisClient.del("test"));
        System.out.println("key test 的值为: " + jedisClient.get("test"));
    }
}
