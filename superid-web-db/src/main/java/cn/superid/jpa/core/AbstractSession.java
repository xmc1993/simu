package cn.superid.jpa.core;

import cn.superid.jpa.util.ModelMeta;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zp on 2016/7/18
 */
public abstract class AbstractSession implements Session {

    /**
     * to solve problem of nested transaction
     */
    protected final Queue<Object> txStack = new ConcurrentLinkedQueue<Object>();

    @Override
    public int getIndexParamBaseOrdinal() {
        return 1;
    }

    @Override
    public void begin() {
        txStack.add(1);
        if (getTransactionNestedLevel() > 1) {
            return;
        }
        getTransaction().begin();
    }

    @Override
    public boolean isRunning() {
        return txStack.size() > 0;
    }

    @Override
    public void commit() {
        if (!isOpen()) {
            begin();
        }
        txStack.poll();
        if (getTransactionNestedLevel() > 0) {
            return;
        }
        getTransaction().commit();
    }

    @Override
    public void startBatch() {

    }

    @Override
    public void endBatch() {

    }

    @Override
    public int[] executeBatch() {
        return new int[0];
    }

    @Override
    public void updateBatch(List<Object> entities) {
        for (Object entity : entities) {
            update(entity);
        }
    }

    @Override
    public void saveBatch(List<Object> entities) {
        for (Object entity : entities) {
            save(entity);
        }
    }

    @Override
    public void deleteBatch(List<Object> entities) {
        for (Object entity : entities) {
            delete(entity);
        }
    }


    @Override
    public void rollback() {
        if (!isTransactionActive()) {
            return;
        }
        txStack.poll();
        if (getTransactionNestedLevel() > 0) {
            return;
        }
        getTransaction().rollback();
    }

    @Override
    public boolean isClosed() {
        return !isOpen();
    }

    @Override
    public void closeFully() {
        txStack.clear();
        close();
    }

    @Override
    public int getTransactionNestedLevel() {
        return txStack.size();
    }

    @Override
    public boolean isTransactionActive() {
        return getTransaction().isActive();
    }

    private static final Map<Class<?>, ModelMeta> ENTITY_META_CACHE = new HashMap<Class<?>, ModelMeta>();

    @Override
    public synchronized ModelMeta getEntityMetaOfClass(Class<?> entityCls) {
        if(ENTITY_META_CACHE.containsKey(entityCls)) {
            return ENTITY_META_CACHE.get(entityCls);
        }
        ModelMeta modelMeta = ModelMeta.getModelMeta(entityCls);
        ENTITY_META_CACHE.put(entityCls, modelMeta);
        return modelMeta;
    }

    /**
     * manually bind session to current thread
     */
    private static final ThreadLocal<WeakReference<Session>> defaultThreadLocalSessions = new ThreadLocal<WeakReference<Session>>();

    private static transient SessionFactory defaultSessionFactory = null;

    public static void setDefaultSessionFactory(SessionFactory sessionFactory) {
        defaultSessionFactory = sessionFactory;
    }

    public static void setDefaultSessionFactoryIfEmpty(SessionFactory sessionFactory) {
        if (sessionFactory == null) {
            return;
        }
        if (defaultSessionFactory == null) {
            synchronized (AbstractSession.class) {
                if (defaultSessionFactory == null) {
                    defaultSessionFactory = sessionFactory;
                }
            }
        }
    }

    /**
     * check whether session binded to current thread first
     * and set result to binded session of current thread
     *
     * @return current session of current thread
     */
    public static Session currentSession() {
        WeakReference<Session> defaultSession = defaultThreadLocalSessions.get();
        if (defaultSession != null && defaultSession.get() != null) {
            return defaultSession.get();
        }
        SessionFactory sessionFactory = (SessionFactory) defaultSessionFactory;
        if(sessionFactory==null) {
            return null;
        }
        Session session = sessionFactory.currentSession();
        defaultThreadLocalSessions.set(new WeakReference<Session>(session));
        return session;
    }

    public static void removeBindingCurrentSession() {
        defaultThreadLocalSessions.remove();
    }

    public static Session bindCurrentSession(Session session) {
        synchronized (defaultThreadLocalSessions) {
            if(session == null) {
                defaultThreadLocalSessions.remove();
            } else {
                defaultThreadLocalSessions.set(new WeakReference<Session>(session));
            }
            return session;
        }
    }

    @Override
    public Session asThreadLocal() {
        return bindCurrentSession(this);
    }




}
