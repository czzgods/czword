package com.itcz.common.service.annotation.unify;

import com.itcz.common.service.annotation.EnableUserAspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableUserAspect //允许进行权限校验
public @interface UserServiceConfig {
}
