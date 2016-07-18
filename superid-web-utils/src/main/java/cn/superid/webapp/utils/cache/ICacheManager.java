package cn.superid.webapp.utils.cache;

/**
 * Created by zoowii on 14/10/20.
 */
public interface ICacheManager {
    <K, V> ICache<K, V> getCache(String name);
}
