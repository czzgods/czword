package com.itcz.czword;

import com.itcz.czword.common.service.annotation.EnableInterfaceApiWebConfig;
import com.itcz.czword.common.service.annotation.EnableRedisConfig;
import com.itcz.czword.common.service.aspect.LogAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = LogAspect.class))
@EnableRedisConfig
@EnableInterfaceApiWebConfig
public class SentenceApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SentenceApiApplication.class, args);
        System.out.println("毒鸡汤微服务启动!!!");
    }
}
