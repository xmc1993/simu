package cn.superid.jpa.cache;

import cn.superid.jpa.orm.ExecutableModel;

import java.util.List;

/**
 * Created by xiaofengxu on 17/1/12.
 */
public interface ICache {
    boolean save(ExecutableModel entity);

    boolean delete(Object id);

    boolean update(ExecutableModel entity,String... fields);

    <T> T findByKey(Object id,Class<?> clazz,String... fields);

    <T> T batchGet(Object[] ids, Class<?> clazz, String... fields);

    boolean batchSave(List<ExecutableModel> entities);

    boolean batchDelete(Object[] keys);

    boolean batchUpdate(ExecutableModel entity,String... fields);

}
