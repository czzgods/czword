package com.itcz.czword.user;


import com.itcz.czword.common.service.annotation.unify.UserServiceConfig;
import com.itcz.czword.user.properties.EmailProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties(value = EmailProperties.class)
@UserServiceConfig
@ComponentScan(basePackages = "com.itcz.czword")
@EnableFeignClients(basePackages = "com.itcz.czword.feign")
//@MapperScan(basePackages = {"com.itcz.czword.user.mapper","com.itcz.czword.service.mapper"})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
        System.out.println("用户微服务启动！！！");
    }
}
