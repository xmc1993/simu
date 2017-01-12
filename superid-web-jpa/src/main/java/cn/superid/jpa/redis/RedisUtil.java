package cn.superid.jpa.redis;
import cn.superid.jpa.exceptions.JedisRuntimeException;
import cn.superid.jpa.orm.ExecutableModel;
import cn.superid.jpa.orm.FieldAccessor;
import cn.superid.jpa.orm.ModelMeta;
import cn.superid.jpa.util.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Redis 工具类
 * @author zp
 *
 */
public class RedisUtil {

    protected static ReentrantLock lockJedis = new ReentrantLock();
    protected static Logger logger = Logger.getLogger(RedisUtil.class);
    private static JedisPool jedisPool = null;
    private String host ="192.168.1.100";
    private int port = 6378;
    private int timeout =2000;
    private int dbIndex =0;
    private String password =null;
    private static byte[] hmFeature = {'0'};//check is real hashmap not empty or hset one field

    /**
     * 初始化Redis连接池
     */

    public RedisUtil(JedisPoolConfig jedisPoolConfig,String host,int port,int timeout,String password ){
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        if(StringUtil.isEmpty(password)){
            password =null;
        }
        this.password = password;
        jedisPool = new JedisPool(jedisPoolConfig,host,port,timeout,password);
    }

    public RedisUtil(JedisPoolConfig jedisPoolConfig ){
        jedisPool = new JedisPool(jedisPoolConfig,this.host,this.port,this.timeout,this.password);
    }



    public static Jedis getJedis() {
        assert ! lockJedis.isHeldByCurrentThread();
        lockJedis.lock();

        if (jedisPool == null) {
            throw new JedisRuntimeException("You should init the pool");
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (Exception e) {
            throw new JedisRuntimeException("Get jedis error : "+e);
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


    public static Object batchGet(Object[] ids, Class<?> clazz, String... fields){
        Jedis jedis = getJedis();
        if(jedis!=null){
            ModelMeta modelMeta = ModelMeta.getModelMeta(clazz);
            byte[][] byteFields;
            if(fields==null||fields.length==0){
                byteFields = modelMeta.getCachedFields();
                fields = modelMeta.getFieldNames(false);
            }else{
                byteFields = BinaryUtil.toBytesArray(fields);
            }

            List<Object> result = new ArrayList<>(ids.length);
            Pipeline pipeline= jedis.pipelined();
            List<Response<List<byte[]>>> list = new ArrayList<>(ids.length);
            Response<List<byte[]>> response;
            for(int i=0,length=ids.length;i<length;i++){
                byte[] key = generateKey(modelMeta.getKey(),BinaryUtil.getBytes(ids[i]));
                response =pipeline.hmget(key,byteFields);
                list.add(response);
            }
            pipeline.sync();
            int i=0;
            for(Response<List<byte[]>> rsp:list){
               Object o=  generateObjectByBytes(modelMeta,rsp.get(),fields);
               modelMeta.getIdAccessor().setProperty(o,ids[i++]);
               result.add(o);
            }
            return result;

        }
        return null;
    }



    public static Long delete(byte[] key){
        Jedis jedis = getJedis();
        if(jedis!=null){
            Long result = jedis.del(key);
            jedis.close();
            return result;
        }
        return null;
    }

    public static Long hset(byte[] key, byte[] field, byte[] value){
        Jedis jedis = getJedis();
        if(jedis!=null){
            Long index = jedis.hset(key,  field,  value);
            jedis.close();
            return index;
        }
        return null;
    }


    /**
     *
     * @param modelMeta
     * @param list
     * @param fields order map list
     * @return
     */
    public static Object generateObjectByBytes(ModelMeta modelMeta,List<byte[]> list,String... fields){
        try {
            if(!isPOJO(list)){
                return null;
            }
            Object result = modelMeta.getModelCls().newInstance();
            int i=1;
            for(String field:fields){
                FieldAccessor fieldAccessor = FieldAccessor.getFieldAccessor(modelMeta.getModelCls(),field);
                fieldAccessor.setProperty(result,BinaryUtil.getValue(list.get(i++),fieldAccessor.getPropertyType()));
            }
            return result;
        } catch (Exception e) {
            throw new JedisRuntimeException(e);
        }
    }

    public static Object findByKey(Object id,Class<?> clazz){
        try {
            Object result = clazz.newInstance();
            Jedis jedis = getJedis();
            if(jedis!=null){
                ModelMeta modelMeta = ModelMeta.getModelMeta(clazz);
                List<byte[]> list = jedis.hmget(generateKey(modelMeta.getKey(),BinaryUtil.getBytes(id)),modelMeta.getCachedFields());
                jedis.close();
                if(!RedisUtil.isPOJO(list)){
                    return null;
                }
                int index=1;

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

    public static List<byte[]> findByKey(byte[] key,byte[]... fields){
        List<byte[]> list =null;
        try {
            Jedis jedis = getJedis();
            if(jedis!=null){
                list = jedis.hmget(key,fields);
                jedis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }





    public static byte[] generateKey(byte[] key, byte[] idByte){
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

    public static byte[] getHmFeature(){
        return hmFeature;
    }


    public static boolean isPOJO(List<byte[]> result){
        if(result!=null&&result.size()>0){
            byte[] var = result.get(0);
            if(var!=null&&var.length>0){
                return var[0]==hmFeature[0];
            }
        }
        return false;
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

    public int getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(int dbIndex) {
        this.dbIndex = dbIndex;
    }
}
