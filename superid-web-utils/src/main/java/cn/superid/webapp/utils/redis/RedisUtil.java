package cn.superid.webapp.utils.redis;

import redis.clients.jedis.Jedis;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xmc1993 on 16/8/25.
 */
public class RedisUtil {
    public static Jedis jedisClient = new Jedis("192.168.1.100:6378");

    private RedisUtil(){
    }

    /**
     * 返回Redis客户端
     * @return
     */
    public static Jedis getJedisClient(){
        return jedisClient;
    }
//
//    public static String hmset(String key, Map<String, byte[]> map) throws UnsupportedEncodingException {
//        Map<byte[], byte[]> res = map2map(map);
////        return jedisClient.hmset(ByteUtil.stringToBytes(key), res);
//    }

    public static Map<byte[], byte[]> map2map(Map<String, byte[]> source) throws UnsupportedEncodingException {
        Map<byte[], byte[]> map = new HashMap<>();
        for(Map.Entry<String, byte[]> entry : source.entrySet()){
//            map.put(ByteUtil.stringToBytes(entry.getKey()), entry.getValue());
        }

        return map;
    }

}
