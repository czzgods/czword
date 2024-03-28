package com.itcz.czword.common.service.config;

import com.itcz.czword.common.service.interceptor.LoginAuthInterceptor;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UserWebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    private LoginAuthInterceptor userLoginAuthInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLoginAuthInterceptor)
                .excludePathPatterns("/user/login/**","/email/sendEmail","/user/sendEmail","/user/register") //排除这些请求
                .addPathPatterns("/user/**");
    }
}
