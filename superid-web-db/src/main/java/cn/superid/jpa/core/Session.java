package cn.superid.jpa.core;


import cn.superid.jpa.jdbcorm.ModelMeta;
import cn.superid.jpa.jdbcorm.sqlmapper.SqlMapper;
import cn.superid.jpa.query.ParameterBindings;

import java.util.List;


public interface Session {

    int getIndexParamBaseOrdinal();

    SqlMapper getSqlMapper();

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

}
