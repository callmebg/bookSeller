package com.example.demo.Util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@SuppressWarnings(value = {"unchecked","rewtypes"})
@Component
public class RedisCache {

    @Autowired
    public RedisTemplate redisTemplate;

    public <T> ValueOperations<String,T>setCacheObject(String key,T value)
    {
        ValueOperations<String,T> operations=redisTemplate.opsForValue();
        operations.set(key,value);
        return operations;
    }

    public <T> T getCacheObject(String key)
    {
        ValueOperations<String,T> operations=redisTemplate.opsForValue();
        return  operations.get(key);
    }

    public void deleteCacheObject(String key)
    {
        redisTemplate.delete(key);
    }
}
