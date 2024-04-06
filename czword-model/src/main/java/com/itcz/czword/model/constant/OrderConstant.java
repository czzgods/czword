package com.itcz.czword.model.constant;

public interface OrderConstant {
    //订单存入redis中的key
    String ORDER_NUM="order:num:";
    //延迟交换机名称
    String DELAY_EXCHANGE="delay_exchange";
    //延迟交换机类型
    String DELAY_EXCHANGE_TYPE="x-delayed-message";
    //延迟队列的名称
    String ORDER_TIMEOUT_QUEUE_NAME="order_delay_queue";
    //延迟队列路由key
    String ORDER_TIMEOUT_ROUTING_KEY="order_timeout_deletion";

    //订单提交后通知增加次数的交换机
    String COUNT_ADD_EXCHANGE="count_add_exchange";
    //订单提交后通知增加次数的队列
    String COUNT_ADD_QUEUE="count_add_queue";
    //订单确定队列路由key
    String COUNT_ADD_KEY="count_add";

}
