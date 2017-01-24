package cn.superid.jpa.orm;


import cn.superid.jpa.core.AbstractSession;
import cn.superid.jpa.core.Session;
import cn.superid.jpa.cache.RedisTemplate;
import cn.superid.jpa.util.BinaryUtil;
import cn.superid.jpa.util.ParameterBindings;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class ExecutableModel implements Serializable{


    public static Session getSession() {
        Session session =AbstractSession.currentSession();
        return session;
    }


    public void save() {
        save(getSession());
    }

    /**
     * if cacheable,cached to redis
     * @param session
     */
    public void save(Session session) {
        session.save(this);
        ModelMeta meta = ModelMeta.getModelMeta(this.getClass());
        if(meta.isCacheable()){
            RedisTemplate.save(this);
        }
    }


    public void update() {
        update(getSession());
    }

    public void update(Session session) {
        ModelMeta meta = ModelMeta.getModelMeta(this.getClass());
        if(meta.isCacheable()){
            RedisTemplate.save(this);
        }
        session.update(this);
    }

    public void delete() {
        ModelMeta meta = ModelMeta.getModelMeta(this.getClass());
        if(meta.isCacheable()){
            RedisTemplate.delete(this.generateKey());
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
        ModelMeta meta = ModelMeta.getModelMeta(this.getClass());
        Object id= BinaryUtil.getBytes(meta.getIdAccessor().getProperty(this));
        return  RedisTemplate.generateKey(meta.getKey(),BinaryUtil.getBytes(id));
    }

}
