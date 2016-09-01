package cn.superid.jpa.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by xmc1993 on 16/8/25.
 */
public class RedisUtil {
    public static Jedis jedisClient = new Jedis("localhost");

    private RedisUtil(){
    }

    /**
     * 返回Redis客户端
     * @return
     */
    public static Jedis getJedisClient(){
        return jedisClient;
    }

}
