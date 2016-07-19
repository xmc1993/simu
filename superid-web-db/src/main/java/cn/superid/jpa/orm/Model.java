package cn.superid.jpa.orm;


import cn.superid.jpa.core.AbstractSession;
import cn.superid.jpa.core.Session;
import cn.superid.jpa.util.ExcuteConditions;
import cn.superid.jpa.util.ParameterBindings;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.List;

@MappedSuperclass
public abstract class Model extends ExcuteConditions implements Serializable{


    public static Session getSession() {
        return AbstractSession.currentSession();
    }

    public abstract void beforeSave(Session session);

    public  abstract void afterSave(Session session);

    public void save() {
        save(getSession());
    }

    public void save(Session session) {
        beforeSave(session);
        session.save(this);
        afterSave(session);
    }

    public abstract void beforeUpdate(Session session);

    public abstract void afterUpdate(Session session);

    public void update() {
        update(getSession());
    }

    public void update(Session session) {
        beforeUpdate(session);
        session.update(this);
        afterUpdate(session);
    }

    public abstract void beforeDelete(Session session);

    public abstract void afterDelete(Session session);

    public void delete() {
        delete(getSession());
    }

    public void delete(Session session) {
        beforeDelete(session);
        session.delete(this);
        afterDelete(session);
    }


    public void refresh() {
        refresh(getSession());
    }

    public void refresh(Session session) {
        session.refresh(this);
    }

    public Object findOne(String sql,Object... params){
        return getSession().findOne(this.getClass(),sql,params);

    }

    public List findList(String sql,Object... params){
        return getSession().findList(this.getClass(), sql, params);
    }

    public Object findOne(String sql,ParameterBindings parameterBindings){
        return getSession().findOne(this.getClass(),sql,parameterBindings);
    }

    public List findList(String sql,ParameterBindings parameterBindings){
        return getSession().findList(this.getClass(), sql, parameterBindings);
    }


   public Object findById(Object id){
       this.eq("a","a");
       return getSession().find(this.getClass(),id);
   }

   public Object findTinyById(Object id){
       return getSession().findTiny(this.getClass(),id);
   }








}
