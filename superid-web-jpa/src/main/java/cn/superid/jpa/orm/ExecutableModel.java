package cn.superid.jpa.orm;


import cn.superid.jpa.core.AbstractSession;
import cn.superid.jpa.core.Session;
import cn.superid.jpa.redis.RedisUtil;
import cn.superid.jpa.util.BinaryUtil;
import cn.superid.jpa.util.ParameterBindings;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class ExecutableModel<T>  implements Serializable,Executable{


    public static Session getSession() {
        Session session =AbstractSession.currentSession();
        return session;
    }


    @Override
    public void save() {
        save(getSession());
    }

    /**
     * if cacheable,cached to redis
     * @param session
     */
    public void save(Session session) {
        session.save(this);
        ModelMeta meta = ModelMetaFactory.getEntityMetaOfClass(this.getClass());
        if(meta.isCacheable()){
            RedisUtil.save(this);
        }

    }


    @Override
    public void update() {
        update(getSession());
    }

    public void update(Session session) {
        ModelMeta meta = ModelMetaFactory.getEntityMetaOfClass(this.getClass());
        if(meta.isCacheable()){
            RedisUtil.save(this);
        }
        session.update(this);
    }

    @Override
    public void delete() {
        ModelMeta meta = ModelMetaFactory.getEntityMetaOfClass(this.getClass());
        if(meta.isCacheable()){
            RedisUtil.delete(this.generateKey());
        }
        delete(getSession());
    }



    public void delete(Session session) {
        session.delete(this);
    }


    public void refresh() {
        refresh(getSession());
    }


    public void refresh(Session session) {
        session.refresh(this);
    }


    public static int execute(String sql,ParameterBindings parameterBindings) {
        return getSession().execute(sql,parameterBindings);
    }

    public static int execute(String sql,Object... params){
        return execute(sql,new ParameterBindings(params));
    }

    public void copyPropertiesTo(Object to){
       getSession().copyProperties(this,to,false);
    }

    public void copyPropertiesFrom(Object from){
       getSession().copyProperties(from,this,false);
    }

    public void copyPropertiesToAndSkipNull(Object to){
        getSession().copyProperties(this,to,true);
    }

    public void copyPropertiesFromAndSkipNull(Object from){
        getSession().copyProperties(from,this,true);
    }



    public HashMap<String,Object> hashMap(){ return getSession().generateHashMapFromEntity(this,false);}

    public HashMap<String,Object> hashMapSkipNull(){ return getSession().generateHashMapFromEntity(this,true);}


    public Map<byte[],byte[]> generateHashByteMap(){ return getSession().generateHashByteMap(this);}


    public byte[][] generateZipMap(){
        return getSession().generateZipMap(this);
    }

    public byte[] generateKey(){
        ModelMeta meta = ModelMetaFactory.getEntityMetaOfClass(this.getClass());
        Object id= BinaryUtil.getBytes(meta.getIdAccessor().getProperty(this));
        return  RedisUtil.generateKey(meta.getKey(),BinaryUtil.getBytes(id));
    }

}
