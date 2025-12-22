package com.irtrains.train_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisLock {

    private final StringRedisTemplate redisTemplate;

    /**
     * Tries to acquire a lock on a specific seat.
     * Returns TRUE if lock was acquired successfully.
     * Returns FALSE if seat is already locked by someone else.
     */
    public boolean acquireLock(String key, String value, long timeoutSeconds) {

        // 1. Generate a unique key for the specific seat on that specific date
        // 2. ATOMIC OPERATION: Set key only if it does not exist
        // setIfAbsent is the Java equivalent of Redis `SETNX`
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, value, timeoutSeconds, TimeUnit.SECONDS);


        return Boolean.TRUE.equals(success);
    }

    public void releaseLock(String key) {

        redisTemplate.delete(key);
    }

    public boolean isLocked(String key) {
        // .hasKey() checks existence without modifying anything
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public String getLockOwner(String key) {

        return redisTemplate.opsForValue().get(key);
    }
}