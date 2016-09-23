package cn.superid.jpa.orm;


import cn.superid.jpa.core.AbstractSession;
import cn.superid.jpa.core.Session;
import cn.superid.jpa.redis.RedisUtil;
import cn.superid.jpa.util.BinaryUtil;
import cn.superid.jpa.util.ParameterBindings;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Transient;
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

    public void save(Session session) {
        ModelMeta meta = getSession().getEntityMetaOfClass(this.getClass());
        if(meta.isCacheable()){
            RedisUtil.save(this);
        }
        session.save(this);
    }


    @Override
    public void update() {
        update(getSession());
    }

    public void update(Session session) {
        session.update(this);
    }

    @Override
    public void delete() {
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
        ModelMeta meta = getSession().getEntityMetaOfClass(this.getClass());
        Object id= BinaryUtil.getBytes(meta.getIdAccessor().getProperty(this));
        byte[] idByte = BinaryUtil.getBytes(id);
        byte[] key = meta.getKey();
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

}
