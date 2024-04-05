package com.itcz.czword.order;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.itcz.czword.common.service.annotation.unify.OrderServiceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableKnife4j
@OrderServiceConfig
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
        System.out.println("订单服务启动成功!!");
    }
}
