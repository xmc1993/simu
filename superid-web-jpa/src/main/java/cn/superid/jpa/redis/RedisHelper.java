package cn.superid.jpa.redis;


import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xmc1993 on 16/9/1.
 */
public final class RedisHelper<T>{

    private RedisHelper(){}

    /**
     * 将对象的HashMap映射载入到REDIS中
     * @param clazz
     * @param id
     * @param h
     */
    public static void setEntityCache(Class clazz, long id,HashMap h) {
        HashMap<String, Object> hashMap = new HashMap<>();
        String key = clazz.getName() + id;
    }

    public static Object getEntityCache(Class clzz, int id){
        String key = clzz.getName() + id;
        return null;

    }


    /**
     * 从缓存中获取
     * @param clazz
     * @param id
     * @return
     */
    public static Map getEntityCache(Class clazz, long id){
        String key = clazz.getName() + id;
        return RedisUtil.getJedisClient().hgetAll(key);
    }
}
