package cn.superid.jpa.core;


import cn.superid.jpa.util.ModelMeta;
import cn.superid.jpa.util.ParameterBindings;

import java.util.List;

/**
 * Created by zp on 2016/7/18
 */
public interface Session {

    int getIndexParamBaseOrdinal();


    ModelMeta getEntityMetaOfClass(Class<?> entityCls);

    Transaction getTransaction();

    void begin();

    void commit();

    boolean isOpen();

    boolean isRunning();

    void rollback();

    boolean isClosed();

    void close();

    void closeFully();

    void shutdown();

    int getTransactionNestedLevel();

    boolean isTransactionActive();

    void save(Object entity);

    void update(Object entity);

    void startBatch();

    void endBatch();


    int[] executeBatch();

    void updateBatch(List<Object> entities);

    void saveBatch(List<Object> entities);

    void deleteBatch(List<Object> entities);

    void detach(Object entity);

    void refresh(Object entity);

    Object find(Class<?> cls, Object id);

    Object findTiny(Class<?> cls, Object id);

    void delete(Object entity);

    void flush();

    Session asThreadLocal();

    Object findOne(Class<?> cls, String queryString, Object... params);

    List findList(Class<?> cls, String queryString, Object... params);

    Object findOne(Class<?> cls, String queryString, ParameterBindings parameterBindings);

    List findList(Class<?> cls, String queryString, ParameterBindings parameterBindings);

    int execute(String sql);

    int execute(String sql, ParameterBindings parameterBindings);


}
