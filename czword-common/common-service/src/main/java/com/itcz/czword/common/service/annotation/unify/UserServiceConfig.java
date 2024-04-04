package com.itcz.czword.common.service.annotation.unify;

import com.itcz.czword.common.service.annotation.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableUserAspect //允许进行权限校验
@EnableWebConfig //登录拦截器
@EnableRedisConfig //redis配置
@EnableSwRateLimiting //开启滑动限流
@EnableLog //开启日志
@EnablePage //开启分页
@EnableRedisLock //开启分布式
@EnableFeignInterceptor //开启Feign拦截器
@EnableAysnThreadPool //开启异步线程池
public @interface UserServiceConfig {
}
