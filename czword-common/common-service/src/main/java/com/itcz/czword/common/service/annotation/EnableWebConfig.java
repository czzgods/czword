package com.itcz.czword.common.service.annotation;


import com.itcz.czword.common.service.config.UserWebMvcConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(UserWebMvcConfiguration.class)
public @interface EnableWebConfig {
}
