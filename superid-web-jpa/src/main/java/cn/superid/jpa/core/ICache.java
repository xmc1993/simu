package cn.superid.jpa.core;

import java.util.Collection;
import java.util.Set;

/**
 *  on 16/2/20.
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
