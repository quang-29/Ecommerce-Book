package org.example.bookstore.service;

import org.example.bookstore.service.Interface.BaseRedisService;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class BaseRedisServiceImpl<K,F,V> implements BaseRedisService<K,F,V> {

    private final RedisTemplate<K, V> redisTemplate;
    private final HashOperations<K, F, V> hashOperations;

    public BaseRedisServiceImpl(RedisTemplate<K, V> redisTemplate, HashOperations<K, F, V> hashOperations) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = hashOperations;
    }

    @Override
    public void set(K key, V value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setTimeToLive(K key, long timeoutInDays) {
        redisTemplate.expire(key, timeoutInDays, TimeUnit.DAYS);
    }

    @Override
    public void hashSet(K key, F field, V value) {
        hashOperations.put(key, field, value);
    }

    @Override
    public boolean hashExists(K key, F field) {
        return hashOperations.hasKey(key, field);
    }

    @Override
    public Object get(K key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Map<String, Object> getField(K key) {
        return (Map<String, Object>) hashOperations.entries(key);
    }

    @Override
    public Object hashGet(K key, F field) {
        return hashOperations.get(key, field);
    }

    @Override
    public List<Object> hashGetByFieldPrefix(K key, String filedPrefix) {
        List<Object> objects = new ArrayList<>();

        Map<String, Object> hashEntries = (Map<String, Object>) hashOperations.entries(key);

        for (Map.Entry<String, Object> entry : hashEntries.entrySet()) {
            if (entry.getKey().startsWith(filedPrefix)) {
                objects.add(entry.getValue());
            }
        }
        return objects;
    }

    @Override
    public Set<String> getFieldPrefixes(K key) {
        return (Set<String>) hashOperations.entries(key).keySet();
    }

    @Override
    public void delete(K key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delete(K key, F field) {
        hashOperations.delete(key, field);
    }


    @Override
    public void delete(K key, List<F> fields) {
        for (F field : fields) {
            hashOperations.delete(key, field);
        }
    }
}
