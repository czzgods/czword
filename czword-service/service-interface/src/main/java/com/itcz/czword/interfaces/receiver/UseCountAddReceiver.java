package com.itcz.czword.interfaces.receiver;

import com.itcz.czword.interfaces.service.UserInterfaceInfoService;
import com.itcz.czword.model.constant.OrderConstant;
import com.itcz.czword.model.dto.interfaces.InterfaceAssign;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 接收用户提交订单后对用户接口数量增加的方法
 */
@Component
@Slf4j
public class UseCountAddReceiver {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    public static final String MESSAGE_REPEAT="message:repeat:";

    @RabbitListener(queues = OrderConstant.COUNT_ADD_QUEUE)
    public void userCountAdd(InterfaceAssign interfaceAssign,
                             Channel channel,
                             @Header(AmqpHeaders.DELIVERY_TAG)long deliveryTag,
                             Message message){
        //获取消息唯一id
        String messageId = message.getMessageProperties().getMessageId();
        try {
            //判断该消息是否存在于redis中
            //setIfAbsent---如果有，就不存,返回false。没有，就存，返回true
            Boolean isRepeat = redisTemplate.opsForValue().setIfAbsent(MESSAGE_REPEAT + messageId, messageId, 1, TimeUnit.HOURS);
            if(!isRepeat){
                //表示该消息已经存在，表明消息已经被消费过了
                //拒绝消息的操作，表示消息未能成功处理，不会重新放回队列。
                channel.basicNack(deliveryTag,false,false);
                return;
            }
            userInterfaceInfoService.doAssign(interfaceAssign);
            channel.basicAck(deliveryTag,true);
            log.info("确认了消息");
        }catch (Exception e){
            try {
                Boolean isRepeat = redisTemplate.opsForValue().setIfAbsent(MESSAGE_REPEAT + messageId, messageId,1,TimeUnit.DAYS);
                if(isRepeat){
                    //当出现异常后，拒绝处理消息，并将消息继续放到队列里面，然后休眠一段时间后，再让监听器去读取消息
                    channel.basicNack(deliveryTag,false,true);
                    Thread.sleep(500);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
