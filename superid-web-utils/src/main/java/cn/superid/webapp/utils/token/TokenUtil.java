package cn.superid.webapp.utils.token;


import cn.superid.jpa.cache.impl.RedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.Set;
import java.util.UUID;

/**
 * Created by xmc1993 on 16/8/25.
 */
public final class TokenUtil {

    private TokenUtil() {
    }

    private static final String PREFIX = "chatToken";

    /**
     * 用户登录时生成TOKEN
     *
     * @param uid
     */
    public static String setLoginToken(Long uid) {
        String token = UUID.randomUUID().toString();
        Jedis jedis = RedisTemplate.getJedis();

        if (jedis != null) {
            jedis.sadd(getKey(uid), token);
            jedis.close();
            return token;
        }

        return null;
    }

    /**
     * 用户登出时注销TOKEN
     *
     * @param uid 用户ID
     * @return
     */
    public static boolean invaildLoginToken(Long uid, String token) {
        Jedis jedis = RedisTemplate.getJedis();
        if (jedis != null) {
            boolean res = jedis.srem(getKey(uid), token) != 0;
            jedis.close();
            return res;
        }
        return false;
    }

    /**
     * 获得一个用户的所有TOKEN (可多处登录)
     *
     * @param uid
     * @return
     */
    public static Set<String> getLoginToken(Long uid) {
        Jedis jedis = RedisTemplate.getJedis();
        if (jedis != null) {
            Set<String> res = jedis.smembers(getKey(uid));
            jedis.close();
            return res;
        }
        return null;
    }

    private static String getKey(Long uid) {
        return PREFIX + String.valueOf(uid);
    }

}
