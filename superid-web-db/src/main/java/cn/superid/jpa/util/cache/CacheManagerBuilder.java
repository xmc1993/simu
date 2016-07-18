package cn.superid.jpa.util.cache;


/**
 *  on 16/2/20.
 */
public class CacheManagerBuilder {
    public static cn.superid.jpa.util.cache.ICacheManager createCacheManagerBuilder(String clsFullName) throws Exception {
        Class<?> cls = Class.forName(clsFullName);
        if(cls == null || !cn.superid.jpa.util.cache.ICacheManager.class.isAssignableFrom(cls)) {
            return null;
        }
        return (cn.superid.jpa.util.cache.ICacheManager) cls.newInstance();
    }
}
