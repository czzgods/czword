package com.itcz.czword.common.service.config;

import com.itcz.czword.common.service.interceptor.InterfaceApiInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterfaceApiWebConfig implements WebMvcConfigurer {

    @Bean
    public InterfaceApiInterceptor interfaceApiInterceptor(){
        return new InterfaceApiInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(interfaceApiInterceptor())
                .addPathPatterns("/api/**");
    }
}
