package cn.superid.jpa.orm;

import cn.superid.jpa.cache.RedisTemplate;
import cn.superid.jpa.util.BinaryUtil;
import cn.superid.jpa.util.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import java.util.List;

/**
 * Created by xiaofengxu on 16/9/28.
 * used for cacheable
 */

public class CacheableDao extends ConditionalDao {

    /**
     * cached id for getById from redis
     */
    protected static ThreadLocal<Object> key = new ThreadLocal<Object>(){
        @Override
        protected Object initialValue() {
            return null;
        }
    };


    public CacheableDao(Class clazz) {
        super(clazz);
        ModelMeta modelMeta = ModelMeta.getModelMeta(this.clazz);
        if(!modelMeta.isCacheable()){
            throw new RuntimeException("You should add annotation cacheable");
        }

    }

    @Override
    public CacheableDao id(Object value) {
        key.set(value);
        super.id(value);
        return this;
    }

    @Override
    public <T> T findById(Object id) {
        Object cached = RedisTemplate.findByKey(id,clazz);
        if(cached!=null){
            return (T) cached;
        }
        T getFromSQl = super.findById(id);
        if(getFromSQl!=null){
            RedisTemplate.save((ExecutableModel) getFromSQl);
        }
        return getFromSQl;
    }



    public <T> T selectOne(String... params) {
        ModelMeta modelMeta = ModelMeta.getModelMeta(this.clazz);
        Object id = key.get();
        if(id!=null){
            byte[] redisKey = RedisTemplate.generateKey(modelMeta.getKey(), BinaryUtil.getBytes(key.get()));
            List<byte[]> result = RedisTemplate.findByKey(redisKey,BinaryUtil.toBytesArray(params));
            if(RedisTemplate.isPOJO(result)){
                Object cached = null;
                try {
                    cached = this.clazz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if(cached!=null){
                    int i =0;
                    for(String field:params){
                        for(ModelMeta.ModelColumnMeta modelColumnMeta:modelMeta.getColumnMetaSet()){
                            if(modelColumnMeta.fieldName.equals(field)){
                                modelColumnMeta.fieldAccessor.setProperty(cached,BinaryUtil.getValue(result.get(i++),modelColumnMeta.fieldType));
                            }
                        }
                    }
                    return (T)cached;
                }
            }
        }else{
            throw  new RuntimeException("Cacheable model should operation by id");
        }
        return (T)super.selectOne(params);
    }

    public Object findFieldByKey(Object key,String field,Class<?> clazz){
        ModelMeta modelMeta = ModelMeta.getModelMeta(this.clazz);
        List<byte[]> result = RedisTemplate.findByKey(RedisTemplate.generateKey(modelMeta.getKey(),BinaryUtil.getBytes(key)),BinaryUtil.getBytes(field));
        if(result!=null&&result.size()>0){
            byte[] bytes = result.get(0);
            if(bytes!=null){
                return BinaryUtil.getValue(result.get(0),clazz);
            }
        }
        StringBuilder sb = new StringBuilder(" SELECT ");
        sb.append(field);
        sb.append(" FROM ");
        sb.append(modelMeta.getTableName());
        sb.append(" WHERE ");
        sb.append(" id = ? limit 1");
        return getSession().findOneByNativeSql(clazz,sb.toString(),key);
    }

    /**
     * delete disabled object from cache and update state
     */
    public int disable(){
        return this.set("state",1);
    }

    @Override
    public int remove() {
        this.deleteFromCache();
        return super.remove();
    }

    private void deleteFromCache(){
        ModelMeta modelMeta = ModelMeta.getModelMeta(this.clazz);
        Object id = key.get();
        if(id!=null){
            byte[] redisKey = RedisTemplate.generateKey(modelMeta.getKey(), BinaryUtil.getBytes(key.get()));
            RedisTemplate.delete(redisKey);
        }else{
            throw  new RuntimeException("Cacheable model should operation by id");
        }
    }


    @Override
    public int set(Object... params) {
        ModelMeta modelMeta = ModelMeta.getModelMeta(this.clazz);
        Object id = key.get();
        if(id!=null){
            byte[] redisKey = RedisTemplate.generateKey(modelMeta.getKey(), BinaryUtil.getBytes(key.get()));
            int i=0;
            byte[] field =null;
            byte[] value;
            if(params.length==2){
                field = BinaryUtil.getBytes(params[0]);
                value = BinaryUtil.getBytes(params[1]);
                RedisTemplate.hset(redisKey,field,value);
            }else{//设置多个属性
                Jedis jedis = RedisTemplate.getJedis();

                if(jedis!=null){
                    Pipeline pipeline =jedis.pipelined();
                    for(Object o:params){
                        if(i%2==0){
                            field = BinaryUtil.getBytes(o);
                        }else{
                            value = BinaryUtil.getBytes(o);
                            pipeline.hset(redisKey,field,value);
                        }
                        i++;
                    }
                    pipeline.sync();
                    jedis.close();
                }
            }

        }else{
            throw  new RuntimeException("Cacheable model should operation by id");
        }

        return super.set(params);
    }

    @Override
    public int setByObject(Object from) {
        Object id = key.get();
        if(id==null){
            throw  new RuntimeException("Cacheable model should operation by id");
        }
        StringBuilder builder = where.get();
        ModelMeta meta = ModelMeta.getModelMeta(from.getClass());

        byte[] redisKey = RedisTemplate.generateKey(meta.getKey(), BinaryUtil.getBytes(id));
        Jedis jedis = RedisTemplate.getJedis();
        Pipeline pipeline =jedis.pipelined();

        ParameterBindings pb = new ParameterBindings();
        StringBuilder sb = new StringBuilder(" UPDATE ");
        sb.append(ModelMeta.getModelMeta(this.clazz).getTableName());
        sb.append(" SET ");
        boolean init =true;
        for (ModelMeta.ModelColumnMeta modelColumnMeta : meta.getColumnMetaSet()) {
            if(modelColumnMeta.isId){
                continue;
            }
            FieldAccessor fieldAccessor = modelColumnMeta.fieldAccessor;
            Object value = fieldAccessor.getProperty(from);
            if(value==null){
                continue;
            }
            if(init){
                init = false;
            }else {
                sb.append(',');
            }
            sb.append(modelColumnMeta.columnName);
            sb.append("=?");
            pb.addIndexBinding(value);
            pipeline.hset(redisKey,modelColumnMeta.binary,BinaryUtil.getBytes(value));
        }
        sb.append(builder);
        String sql = sb.toString();
        ParameterBindings all = pb.addAll(parameterBindings.get());
        builder.delete(whereLength,builder.length());
        parameterBindings.get().clear();
        pipeline.sync();
        return getSession().execute(sql,all);
    }



    @Override
    public <T> T findById(Object id, Object partitionId) {
        Object cached = RedisTemplate.findByKey(id,clazz);
        if(cached!=null){
            return (T) cached;
        }
        T getFromSQl = super.findById(id, partitionId);
        if(getFromSQl!=null){
            RedisTemplate.save((ExecutableModel) getFromSQl);
        }

        return getFromSQl;
    }







}
