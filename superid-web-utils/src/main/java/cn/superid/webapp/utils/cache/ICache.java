package cn.superid.webapp.utils.cache;

import java.util.Collection;
import java.util.Set;

/**
 * Created by zoowii on 14/10/20.
 */
public interface ICache<K, V> {
    void put(K key, V value);

    void put(K key, V value, int timeoutSeconds);

    V get(K key);

    void remove(K key);

    void clean();

    int size();

    Set<K> keys();

    Collection<V> values();
}
