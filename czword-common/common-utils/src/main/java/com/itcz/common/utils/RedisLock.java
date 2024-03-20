package com.itcz.common.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collections;
import java.util.List;

public class RedisLock {

    public static final int DEFAULT_SECOND_LEN = 10; // 10 s

    private RedisTemplate<String, String> redisTemplate;

    private static final String LOCK_LUA = "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then redis.call('expire', KEYS[1], ARGV[2]) return 'true' else return 'false' end";
    private static final String UNLOCK_LUA = "if redis.call('get', KEYS[1]) == ARGV[1] then redis.call('del', KEYS[1]) end return 'true' ";

    private RedisScript lockRedisScript;
    private RedisScript unLockRedisScript;

    private RedisSerializer<String> argsSerializer;
    private RedisSerializer<String> resultSerializer;

    /**
     * 初始化lua 脚本
     */
    public void init(RedisTemplate<String, String> redisTemplate) {

        this.redisTemplate = redisTemplate;

        argsSerializer = new StringRedisSerializer();
        resultSerializer = new StringRedisSerializer();

        lockRedisScript = RedisScript.of(LOCK_LUA, String.class);
        unLockRedisScript = RedisScript.of(UNLOCK_LUA, String.class);
    }


    public boolean lock(String lock, String val) {
        return this.lock(lock, val, DEFAULT_SECOND_LEN);
    }


    public boolean lock(String lock, String val, int second) {
        List<String> keys = Collections.singletonList(lock);
        String flag = redisTemplate.execute(lockRedisScript, argsSerializer, resultSerializer, keys, val, String.valueOf(second));
        return Boolean.valueOf(flag);
    }


    public void unlock(String lock, String val) {
        List<String> keys = Collections.singletonList(lock);
        redisTemplate.execute(unLockRedisScript, argsSerializer, resultSerializer, keys, val);
    }


}