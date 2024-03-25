package com.itcz.czword.common.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 滑动窗口限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SwRateLimiting {
    //窗口宽度 默认为一分钟
    long time() default 60;
    //允许请求的次数 默认1次
    long count() default 1;
}
