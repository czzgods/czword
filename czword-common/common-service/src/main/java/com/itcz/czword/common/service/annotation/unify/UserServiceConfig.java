package com.itcz.czword.common.service.annotation.unify;

import com.itcz.czword.common.service.annotation.EnableRateLimiting;
import com.itcz.czword.common.service.annotation.EnableRedisConfig;
import com.itcz.czword.common.service.annotation.EnableUserAspect;
import com.itcz.czword.common.service.annotation.EnableWebConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableUserAspect //允许进行权限校验
@EnableWebConfig //登录拦截器
@EnableRedisConfig //redis配置
@EnableRateLimiting //开启限流
public @interface UserServiceConfig {
}
