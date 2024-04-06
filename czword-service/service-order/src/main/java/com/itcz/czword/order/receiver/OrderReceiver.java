package com.itcz.czword.order.receiver;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itcz.czword.model.constant.OrderConstant;
import com.itcz.czword.model.dto.order.OrderDelayDto;
import com.itcz.czword.model.entity.order.OrderForm;
import com.itcz.czword.order.mapper.OrderFormMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;

@Component
@Slf4j
public class OrderReceiver {
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Resource
    private OrderFormMapper orderFormMapper;
    /**
     * 延迟队列删除消息
     * @param orderDelayDto 类型为 OrderDelayDto，表示要处理的消息。
     * @param channel 表示 RabbitMQ 的通道。
     * @param deliveryTag 表示消息的交付标签。
     */
    @RabbitListener(queues = OrderConstant.ORDER_TIMEOUT_QUEUE_NAME)
    public void orderTimeoutDelete(OrderDelayDto orderDelayDto, Channel channel,@Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            //订单编号
            String orderNum = orderDelayDto.getOrderNum();
            //用户id
            Long userId = orderDelayDto.getUserId();
            //从redis中查询数据
            String orderStr = redisTemplate.opsForValue().get(OrderConstant.ORDER_NUM + userId+":"+orderNum);
            if(!StringUtils.hasText(orderStr)){
                //redis中没有数据，表示当前用户未创建订单或者订单已经支付成功
                log.info("订单:"+orderNum+"已支付完成");
                return;
            }
            //redis中存在数据则表明在延迟队列所设置的延迟时间内，订单未完成支付
            redisTemplate.delete(OrderConstant.ORDER_NUM + userId+":"+orderNum);
            //删除数据库（逻辑删除）
            LambdaQueryWrapper<OrderForm> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderForm::getUserId,userId);
            queryWrapper.eq(OrderForm::getOrderNum,orderNum);
            orderFormMapper.delete(queryWrapper);
            log.info("订单:"+orderNum+"删除完成");
        }finally {
            try {
                //表示消息已成功处理，可以从队列中删除。
                //deliveryTag 表示消息的交付标签。
                channel.basicAck(deliveryTag,true);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
