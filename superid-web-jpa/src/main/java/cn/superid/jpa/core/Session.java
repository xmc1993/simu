package cn.superid.jpa.core;


import cn.superid.jpa.orm.ModelMeta;
import cn.superid.jpa.util.Pagination;
import cn.superid.jpa.util.ParameterBindings;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by zp on 2016/7/18
 */
public interface Session {

    int getIndexParamBaseOrdinal();


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

    boolean update(Object entity);

    boolean update(Object entity, List<String> columns);

    void startBatch();

    void endBatch();


    int[] executeBatch();

    void updateBatch(List<Object> entities);

    void saveBatch(List<Object> entities);

    void deleteBatch(List<Object> entities);

    void detach(Object entity);

    void refresh(Object entity);

    Object find(Class<?> cls, Object id);

    Object find(Class<?> cls, Object id,Object partitionId);


    void delete(Object entity);

    void flush();

    Object findOneByNativeSql(Class<?> cls, String queryString, Object... params);

    List findListByNativeSql(Class<?> cls, String queryString, Object... params);

    List findListByNativeSql(Class<?> cls, String queryString, ParameterBindings parameterBindings, Pagination pagination);

    Object findOneByNativeSql(Class<?> cls, String queryString, ParameterBindings parameterBindings);

    List findListByNativeSql(Class<?> cls, String queryString, ParameterBindings parameterBindings);

    int execute(String sql);

    int execute(String sql, ParameterBindings parameterBindings);

    int execute(String sql, Object[] params);


    void copyProperties(Object from,Object to,boolean skipNull);

    HashMap<String, Object> generateHashMapFromEntity(Object entity,boolean skipNull);

    HashMap<byte[], byte[]> generateHashByteMap(Object entity);

    Object generateHashMapFromEntity(HashMap<String, Object> hashMap,Object entity);

    Object generateEntityFromHashMap(HashMap<byte[], byte[]> hashMap,Object entity);

    byte[][] generateZipMap(Object entity);



}
