package com.itcz.czword.user;


import com.itcz.common.service.annotation.unify.UserServiceConfig;
import com.itcz.czword.user.properties.EmailProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = EmailProperties.class)
@UserServiceConfig
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
        System.out.println("用户微服务启动！！！");
    }
}
