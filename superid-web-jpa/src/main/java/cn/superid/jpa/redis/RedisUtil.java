package cn.superid.jpa.redis;
;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import cn.superid.jpa.core.AbstractSession;
import cn.superid.jpa.orm.ExecutableModel;
import cn.superid.jpa.orm.FieldAccessor;
import cn.superid.jpa.orm.ModelMeta;
import cn.superid.jpa.orm.ModelMetaFactory;
import cn.superid.jpa.util.BinaryUtil;
import org.apache.log4j.Logger;
import redis.clients.jedis.*;

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
//
//    public static int update(ExecutableModel entity){
//        Jedis jedis = getJedis();
//        if(jedis!=null){
//            jedis.hset
//
//        }
//        return 0;
//    }


    public static long delete(byte[] key){
        Jedis jedis = getJedis();
        if(jedis!=null){
            long result = jedis.del(key);
            jedis.close();
            return result;
        }
        return 0L;
    }



    public static Object findByKey(byte[] key,Class<?> clazz,byte[][] fields){
        try {
            Object result = clazz.newInstance();
            Jedis jedis = getJedis();
            if(jedis!=null){
                ModelMeta modelMeta = ModelMetaFactory.getEntityMetaOfClass(clazz);
                List<byte[]> list = jedis.hmget(key,fields);
                jedis.close();
                if (list==null||list.size()==0){
                    return null;
                }
                int index=0;
                for(byte[] value:list){
                    byte[] field = fields[index++];
                    for(ModelMeta.ModelColumnMeta modelColumnMeta:modelMeta.getColumnMetaSet()){
                        if(field.length==modelColumnMeta.binary.length){
                            int i;
                            for( i=0;i<field.length;i++){
                                if(field[i]!=modelColumnMeta.binary[i]){
                                    break;
                                }
                            }
                            if(i==field.length){//属于同一个熟悉
                                FieldAccessor fieldAccessor=modelColumnMeta.fieldAccessor;
                                fieldAccessor.setProperty(result, BinaryUtil.getValue(value,modelColumnMeta.fieldType));
                            }
                        }
                    }
                }
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public static Object findByKey(Object id,Class<?> clazz){
        try {
            Object result = clazz.newInstance();
            Jedis jedis = getJedis();
            if(jedis!=null){
                ModelMeta modelMeta = ModelMetaFactory.getEntityMetaOfClass(clazz);
                List<byte[]> list = jedis.hmget(generateKey(modelMeta.getKey(),BinaryUtil.getBytes(id)),modelMeta.getCachedFields());
                jedis.close();
                if (list==null||list.size()==0){
                    return null;
                }
                int index=0;
                for(ModelMeta.ModelColumnMeta modelColumnMeta:modelMeta.getColumnMetaSet()){
                    if(modelColumnMeta.isId){
                        continue;
                    }
                    modelColumnMeta.fieldAccessor.setProperty(result, BinaryUtil.getValue(list.get(index++),modelColumnMeta.fieldType));
                }
                modelMeta.getIdAccessor().setProperty(result,id);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    public static byte[] generateKey(byte[] key,byte[] idByte){
        byte[] result= new byte[idByte.length+key.length];
        for(int j=0;j<result.length;j++){
            if(j<key.length){
                result[j]=key[j];
            }else {
                result[j] = idByte[j-key.length];
            }
        }
        return result;
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
