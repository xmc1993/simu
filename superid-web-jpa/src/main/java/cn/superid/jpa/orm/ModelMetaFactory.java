package cn.superid.jpa.orm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaofengxu on 16/9/23.
 */
public class ModelMetaFactory {
    private static final Map<Class<?>, ModelMeta> ENTITY_META_CACHE = new HashMap<Class<?>, ModelMeta>();

    public synchronized static ModelMeta getEntityMetaOfClass(Class<?> entityCls) {
        if (ENTITY_META_CACHE.containsKey(entityCls)) {
            return ENTITY_META_CACHE.get(entityCls);
        }
        ModelMeta modelMeta = ModelMeta.getModelMeta(entityCls);
        ENTITY_META_CACHE.put(entityCls, modelMeta);
        return modelMeta;
    }

}
