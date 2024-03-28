package com.itcz.czword.interfaces;


import com.itcz.czword.common.service.annotation.unify.InterfaceServiceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.itcz.czword")
@InterfaceServiceConfig
public class InterfacesApplication {
    public static void main(String[] args) {
        SpringApplication.run(InterfacesApplication.class,args);
        System.out.println("接口微服务启动！！！");
    }
}
