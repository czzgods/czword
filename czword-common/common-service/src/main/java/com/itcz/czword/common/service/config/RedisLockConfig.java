package com.itcz.czword.common.service.config;


import com.itcz.czword.common.utils.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisLockConfig {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Bean
    public RedisLock redisLock(){
        RedisLock redisLock=new RedisLock();
        redisLock.init(redisTemplate);
        return redisLock;
    }
}
