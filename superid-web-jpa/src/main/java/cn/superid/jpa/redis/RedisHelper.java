package cn.superid.jpa.redis;


import redis.clients.jedis.Jedis;

import java.util.HashMap;

/**
 * Created by xmc1993 on 16/9/1.
 */
public final class RedisHelper<T>{

    private RedisHelper(){}

    public static void setEntityCache(HashMap h){
        RedisUtil.getJedisClient().hmset("", h);
    }

    public static Object getEntityCache(Class clzz, int id){
        String key = clzz.getName() + id;

    }
}
