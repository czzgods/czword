package com.itcz.czword.common.service.config;

import com.itcz.czword.model.constant.OrderConstant;
import org.springframework.amqp.core.*;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OrderRabbitMqConfig {
    /**
     * 定义一个延迟交换机
     */
    @Bean
    public CustomExchange delayExchange(){
        Map<String,Object> args = new HashMap<>();
        //args 是一个参数映射，用于设置交换机的属性。在这里，我们设置了 "x-delayed-type" 参数为 "direct"，
        // 以便延迟交换机按照 direct 类型的规则进行消息路由。
        args.put("x-delayed-type","direct");
        /*DELAY_EXCHANGE_NAME：这是交换机的名称，您可以自定义它。在这里，它被设置为一个常量，用于标识延迟交换机。
        "x-delayed-message"：这是交换机的类型。在这里，我们创建了一个特殊类型的交换机，用于处理延迟队列中的消息。这个类型的交换机需要安装 RabbitMQ 插件 rabbitmq_delayed_message_exchange。
        true：这表示交换机是持久化的。持久化的交换机会在 RabbitMQ 服务器重启后继续存在。
        false：这表示交换机不会自动删除。如果设置为 true，则交换机会在没有绑定的队列时自动删除。
        args：这是一个参数映射，用于设置交换机的其他属性。在这里，我们设置了 "x-delayed-type" 参数为 "direct"，以便延迟交换机按照 direct 类型的规则进行消息路由。*/
        return new CustomExchange(OrderConstant.DELAY_EXCHANGE, OrderConstant.DELAY_EXCHANGE_TYPE,true,false,args);
    }

    /**
     * 定义一个订单完成提交后通知添加接口使用次数的交换机
     */
    @Bean
    public Exchange addCountExchange(){
        //                             交换机名称                            是否持久化     是否自动删除
        return new TopicExchange(OrderConstant.COUNT_ADD_EXCHANGE,true,false,null);
    }

    /**
     * 定义一个延迟队列
     */
    @Bean
    public Queue delayQueue(){
        //                                      延迟队列的名称               是否持久化
        return new Queue(OrderConstant.ORDER_TIMEOUT_QUEUE_NAME,true);
    }

    /**
     * 定义一个订单确认后增加接口使用次数的消息队列
     */
    @Bean
    public Queue addCountQueue(){
        return new Queue(OrderConstant.COUNT_ADD_QUEUE,true);
    }

    /**
     * 绑定延迟交换机与延迟队列
     */
    @Bean
    public Binding delayBinding(){
        /*OrderConstant.ORDER_TIMEOUT_ROUTING_KEY：这是路由键，它决定了消息从交换机流向队列的路径。在这里，我们使用了一个常量作为路由键。
          .noargs()：这表示不使用任何额外的参数。在这个绑定中，我们不需要传递其他参数。*/
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(OrderConstant.ORDER_TIMEOUT_ROUTING_KEY).noargs();
    }

    /**
     * 绑定订单确认交换机和队列
     */
    @Bean
    public Binding binding(){
        return BindingBuilder.bind(addCountQueue()).to(addCountExchange()).with(OrderConstant.COUNT_ADD_KEY).noargs();
    }
}
