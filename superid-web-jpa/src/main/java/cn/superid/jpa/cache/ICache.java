package cn.superid.jpa.cache;

import cn.superid.jpa.orm.ExecutableModel;

import java.util.List;

/**
 * Created by xiaofengxu on 17/1/12.
 */
public interface ICache<T> {
    boolean save(T entity);

    boolean delete(Object key);

    boolean update(T entity,String... fields);

    T findByKey(Object id,Class<?> clazz,String... fields);

    List<T> batchGet(Object[] ids, Class<?> clazz, String... fields);

    boolean batchSave(List<T> entities);

    boolean batchDelete(Object[] keys);

    boolean batchUpdate(T entity,String... fields);

}
