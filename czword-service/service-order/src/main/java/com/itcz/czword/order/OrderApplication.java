package com.itcz.czword.order;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.itcz.czword.common.service.annotation.unify.OrderServiceConfig;
import com.itcz.czword.common.service.aspect.LogAspect;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = "com.itcz.czword",excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = LogAspect.class))
@OrderServiceConfig
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
        System.out.println("订单服务启动成功!!");
    }
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
