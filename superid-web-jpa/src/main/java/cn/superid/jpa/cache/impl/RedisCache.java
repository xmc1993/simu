package cn.superid.jpa.cache.impl;

import cn.superid.jpa.cache.ICache;
import cn.superid.jpa.cache.RedisTemplate;
import cn.superid.jpa.orm.ExecutableModel;
import cn.superid.jpa.util.BinaryUtil;

import java.util.List;

/**
 * Created by xiaofengxu on 17/1/13.
 */
public class RedisCache implements ICache {
    @Override
    public boolean save(ExecutableModel entity) {
        return RedisTemplate.OK.equals(RedisTemplate.save(entity));
    }

    @Override
    public boolean delete(Object id) {
        return RedisTemplate.delete(BinaryUtil.getBytes(id))>0;
    }

    @Override
    public boolean update(ExecutableModel entity, String... fields) {

        return false;
    }

    @Override
    public <T> T findByKey(Object id, Class<?> clazz, String... fields) {
        return null;
    }

    @Override
    public <T> T batchGet(Object[] ids, Class<?> clazz, String... fields) {
        return null;
    }

    @Override
    public boolean batchSave(List<ExecutableModel> entities) {
        return false;
    }

    @Override
    public boolean batchDelete(Object[] keys) {
        return false;
    }

    @Override
    public boolean batchUpdate(ExecutableModel entity, String... fields) {
        return false;
    }
}
