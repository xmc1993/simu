package cn.superid.webapp.utils.cache.impl;

import cn.superid.webapp.utils.cache.ICache;
import cn.superid.webapp.utils.cache.ICacheManager;
import net.sf.ehcache.CacheManager;

/**
 * Created by zoowii on 15/12/23.
 */
public class EhcacheCacheManager implements ICacheManager {
    private CacheManager cacheManager;

    public EhcacheCacheManager(String configPath) {
        this.cacheManager = CacheManager.newInstance(this.getClass().getClassLoader().getResourceAsStream(configPath));
    }
    @Override
    public <K, V> ICache<K, V> getCache(String name) {
        return new EhcacheCache<>(this.cacheManager.getCache(name));
    }
}
