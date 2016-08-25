package cn.superid.webapp.utils.token;


import cn.superid.webapp.utils.redis.RedisUtil;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by xmc1993 on 16/8/25.
 */
public class TokenUtil {

    private TokenUtil(){}

    /**
     * 用户登录时生成TOKEN
     * @param uid
     */
    public static void setLoginToken(BigInteger uid){
        String token = UUID.randomUUID().toString();
        RedisUtil.getJedisClient().sadd(String.valueOf(uid), token);
    }

    /**
     * 用户登出时注销TOKEN
     * @param uid 用户ID
     * @return
     */
    public static boolean invaildLoginToken(BigInteger uid, String token){
        return RedisUtil.getJedisClient().srem(String.valueOf(uid), token) != 0;
    }

    /**
     * 获得一个用户的所有TOKEN (可多处登录)
     * @param uid
     * @return
     */
    public static Set<String> getLoginToken(BigInteger uid){
        return RedisUtil.getJedisClient().smembers(String.valueOf(uid));

    }

}
