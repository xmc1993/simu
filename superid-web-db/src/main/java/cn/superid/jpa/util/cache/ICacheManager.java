package cn.superid.jpa.util.cache;


/**
 *  on 16/2/20.
 */
public interface ICacheManager {
    <K, V> cn.superid.jpa.util.cache.ICache<K, V> getCache(String name);
}
