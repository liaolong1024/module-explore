package org.self.learn.datastructure.lru;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU: 最近最少使用算法实现
 *
 * @author ll
 * @since 2024-12-02 22:30
 */
public class LRUCache<K, V> {
    private final LinkedHashMap<K, V> cache;

    public LRUCache() {
        this.cache = new LinkedHashMap<>(10, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                // 从缓存中删除的条件
                return this.size() > 100;
            }
        };
    }

    public V put(K k, V v) {
        return cache.put(k, v);
    }

    public V get(K k) {
        return cache.get(k);
    }
}
