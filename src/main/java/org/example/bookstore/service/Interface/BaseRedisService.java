package org.example.bookstore.service.Interface;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BaseRedisService <K,F,V>{

    void set(K key, V value);

    void setTimeToLive(K key, long timeoutInDays);

    void hashSet(K key, F field, V value);

    boolean hashExists(K key, F field);

    Object get(K key);

    public Map<String, Object> getField(K key);

    Object hashGet(K key, F field);

    List<Object> hashGetByFieldPrefix(K key, String filedPrefix);

    Set<String> getFieldPrefixes(K key);

    void delete(K key);

    void delete(K key, F field);

    void delete(K key, List<F> fields);

}
