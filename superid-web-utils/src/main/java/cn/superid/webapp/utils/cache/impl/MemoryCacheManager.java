package cn.superid.webapp.utils.cache.impl;

import cn.superid.webapp.utils.cache.ICache;
import cn.superid.webapp.utils.cache.ICacheManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zoowii on 14/10/20.
 */
public class MemoryCacheManager implements ICacheManager {
    private final Map<String, ICache> caches = new ConcurrentHashMap<>();

    @Override
    public <K, V> ICache<K, V> getCache(String name) {
        synchronized (caches) {
            if (caches.containsKey(name) && caches.get(name) != null) {
                return caches.get(name);
            }
            ICache<K, V> cache = new MemoryCache<>();
            caches.put(name, cache);
            return cache;
        }
    }
}
