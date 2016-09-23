package cn.superid.jpa.redis;


import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

import cn.superid.jpa.orm.ExecutableModel;
import cn.superid.jpa.orm.ModelMeta;
import org.apache.log4j.Logger;
import redis.clients.jedis.*;
import redis.clients.util.RedisOutputStream;

/**
 * Redis 工具类
 * @author caspar
 *
 */
public class RedisUtil {

    protected static ReentrantLock lockJedis = new ReentrantLock();

    protected static Logger logger = Logger.getLogger(RedisUtil.class);
    private static JedisPool jedisPool = null;
    private String host ="localhost";
    private int port = 6379;
    private int timeout =2000;
    private String password=null;
    private int dbIndex =0;



    /**
     * 初始化Redis连接池
     */

    public RedisUtil(JedisPoolConfig jedisPoolConfig ){
        jedisPool = new JedisPool(jedisPoolConfig,host,port,timeout,password);

    }


    public static Jedis getJedis() {
        //断言 ，当前锁是否已经锁住，如果锁住了，就啥也不干，没锁的话就执行下面步骤
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();

        if (jedisPool == null) {
            throw new RuntimeException("You should init the pool");
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (Exception e) {
            logger.error("Get jedis error : "+e);
        }finally{
            lockJedis.unlock();
        }
        return jedis;
    }



    public static String save(ExecutableModel entity){
        Jedis jedis = getJedis();
        if(jedis!=null){
            String result = jedis.hmset(entity.generateKey(),entity.generateHashByteMap());
            jedis.close();
            return result;
        }
        return null;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public int getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(int dbIndex) {
        this.dbIndex = dbIndex;
    }
}
