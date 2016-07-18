package cn.superid.jpa.util.cache;



import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  on 16/2/20.
 */
public class MemoryCacheManager implements cn.superid.jpa.util.cache.ICacheManager {
    private final Map<String, cn.superid.jpa.util.cache.ICache> caches = new ConcurrentHashMap<String, cn.superid.jpa.util.cache.ICache>();

    @Override
    public <K, V> cn.superid.jpa.util.cache.ICache<K, V> getCache(String name) {
        synchronized (caches) {
            if (caches.containsKey(name) && caches.get(name) != null) {
                return caches.get(name);
            }
            ICache<K, V> cache = new MemoryCache<K, V>();
            caches.put(name, cache);
            return cache;
        }
    }
}
