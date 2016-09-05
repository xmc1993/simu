package cn.superid.jpa.orm;


import cn.superid.jpa.core.AbstractSession;
import cn.superid.jpa.core.Session;
import cn.superid.jpa.util.ParameterBindings;

import java.io.Serializable;
import java.util.HashMap;

public abstract class ExecutableModel<T>  implements Serializable,Executable{



    public static Session getSession() {
        return AbstractSession.currentSession();
    }


    @Override
    public void save() {
        save(getSession());
    }

    public void save(Session session) {
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
       getSession().copyProperties(this,to);
    }

    public void copyPropertiesFrom(Object from){
       getSession().copyProperties(from,this);
    }

    public HashMap<String,Object> getHashMap(){ return getSession().getHashMapFromEntity(this);}

    public HashMap<String,byte[]> getHashByteMap(){ return getSession().getHashByteMapFromEntity(this);}


}
