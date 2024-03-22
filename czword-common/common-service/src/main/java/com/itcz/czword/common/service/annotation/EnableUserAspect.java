package com.itcz.czword.common.service.annotation;

import com.itcz.czword.common.service.aspect.AuthAspect;
import com.itcz.czword.common.service.config.UserWebMvcConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(value = AuthAspect.class)// 通过Import注解导入权限校验切面类到Spring容器中
public @interface EnableUserAspect {
}
