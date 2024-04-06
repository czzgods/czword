package com.itcz.czword.order.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.itcz.czword.common.service.exception.BusinessException;
import com.itcz.czword.common.utils.UserContextUtil;
import com.itcz.czword.model.common.PageRequest;
import com.itcz.czword.model.constant.OrderConstant;
import com.itcz.czword.model.dto.interfaces.InterfaceAssign;
import com.itcz.czword.model.dto.order.OrderCommitDto;
import com.itcz.czword.model.dto.order.OrderCreateDto;
import com.itcz.czword.model.dto.order.OrderDelayDto;
import com.itcz.czword.model.entity.order.OrderForm;
import com.itcz.czword.model.entity.user.User;
import com.itcz.czword.model.enums.ErrorCode;
import com.itcz.czword.model.vo.order.OrderFormVo;
import com.itcz.czword.order.mapper.OrderFormMapper;
import com.itcz.czword.order.service.OrderFormService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
* @author 李钟意
* @description 针对表【order_form(订单表)】的数据库操作Service实现
* @createDate 2024-04-05 20:56:28
*/
@Service
@Slf4j
public class OrderFormServiceImpl extends ServiceImpl<OrderFormMapper, OrderForm>
    implements OrderFormService {
    @Resource
    private OrderFormMapper orderFormMapper;
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public Page<OrderFormVo> listOrder(PageRequest pageRequest) {
        long current = pageRequest.getCurrent();
        long pageSize = pageRequest.getPageSize();
        Page<OrderForm> page = new Page<>(current,pageSize);
        //进行分页查询
        page(page,null);
        //获取分页查询数据
        List<OrderForm> orderFormList = page.getRecords();
        //类型转换
        List<OrderFormVo> orderFormVoList = orderFormList.stream().map(orderForm -> {
            OrderFormVo orderFormVo = new OrderFormVo();
            BeanUtils.copyProperties(orderForm, orderFormVo);
            return orderFormVo;
        }).collect(Collectors.toList());
        Page<OrderFormVo> orderFormVoPage = new Page<>(page.getCurrent(),page.getSize(),page.getTotal());
        orderFormVoPage.setRecords(orderFormVoList);
        return orderFormVoPage;
    }

    @Override
    public Boolean deleteById(Long id) {
        if(id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<OrderForm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderForm::getId,id);
        OrderForm orderForm = orderFormMapper.selectOne(queryWrapper);
        if(Objects.isNull(orderForm)){
            throw new BusinessException(ErrorCode.ORDER_IS_NOT_EXISTS);
        }
        boolean remove = removeById(id);
        return remove;
    }

    @Override
    @Transactional //开启事务
    public OrderFormVo createOrder(OrderCreateDto orderCreateDto) {
        //获取用户id
        Long userId = orderCreateDto.getUserId();
        //生成订单id
        String orderId = OrderNumGenerate(userId);
        //从redis中查询订单
        String orderStr = redisTemplate.opsForValue().get(OrderConstant.ORDER_NUM + userId + ":" + orderId);
        if(!StringUtils.hasText(orderStr)){
            //说明redis中没有订单数据
            OrderForm orderForm = new OrderForm();
            //类型转换
            BeanUtils.copyProperties(orderCreateDto,orderForm);
            //添加orderId
            orderForm.setOrderNum(orderId);
            //订单信息存入redis
            redisTemplate.opsForValue().set(OrderConstant.ORDER_NUM + userId + ":" + orderId, JSON.toJSONString(orderForm));
            //发送消息到rabbitmq延迟队列
            sendMessageToDelay(new OrderDelayDto(userId,orderId));
            //订单存入数据库
            //orderFormMapper.insert(orderForm);
            //返回数据
            OrderFormVo orderFormVo = new OrderFormVo();
            orderFormVo.setUserId(userId);
            orderFormVo.setOrderNum(orderId);
            orderFormVo.setInterfaceId(orderCreateDto.getInterfaceId());
            orderFormVo.setSubtotal(orderCreateDto.getSubtotal());
            orderFormVo.setQuantity(orderCreateDto.getQuantity());
            orderFormVo.setCreateTime(new Date());
            return orderFormVo;
        }else {
            //表示redis中有数据，用户已经提交了订单信息
            throw new BusinessException(ErrorCode.ORDER_IS_EXISTS);
        }
    }

    @Override
    public void commitOrder(OrderCommitDto orderCommitDto) {
        //获取当前登录用户id
        User user = UserContextUtil.getUser();
        Long userId = user.getId();
        //获取订单编号
        String orderNum = orderCommitDto.getOrderNum();
        //从redis中查询订单信息
        String orderStr = redisTemplate.opsForValue().get(OrderConstant.ORDER_NUM + userId + ":" + orderNum);
        if(!StringUtils.hasText(orderStr)){
            throw new BusinessException(ErrorCode.ORDER_IS_NOT_EXISTS);
        }
        //类型转换
        OrderForm orderForm = JSON.parseObject(orderStr, OrderForm.class);
        //存入数据库并删除redis中的数据信息
        orderFormMapper.insert(orderForm);
        redisTemplate.delete(OrderConstant.ORDER_NUM + userId + ":" + orderNum);
        //到这里说明用户已经支付成功了订单，使用消息队列异步实现用户接口次数增加
        sendMessageToUseCountAdd(new InterfaceAssign(userId,orderForm.getInterfaceId(),orderForm.getQuantity().longValue()));
    }


    /**
     * 发送消息到延迟队列
     * @param orderDelayDto
     */
    private void sendMessageToDelay(OrderDelayDto orderDelayDto) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        rabbitTemplate.convertAndSend(
                OrderConstant.DELAY_EXCHANGE, //发送信息到这个交换机
                OrderConstant.ORDER_TIMEOUT_ROUTING_KEY, //信息的路由key
                orderDelayDto, //发现的消息内容
                message -> {
                    //设置消息15秒到达延迟队列,也就是说15秒后监听到队列有消息，然后删除订单，实现订单延时删除
                    log.error("当前时间:"+new Date()+"订单编号:"+orderDelayDto.getOrderNum());
                    message.getMessageProperties().setDelay(60000);
                    message.getMessageProperties().setMessageId(uuid);
                    return message;
                }
        );
    }

    private void sendMessageToUseCountAdd(InterfaceAssign interfaceAssign) {
        //给消息生成唯一id
        String uuid = UUID.randomUUID().toString().replace("-", "");
        rabbitTemplate.convertAndSend(
                OrderConstant.COUNT_ADD_EXCHANGE, // 信息要发送到的交换机
                OrderConstant.COUNT_ADD_KEY, //信息的路由key
                interfaceAssign, //发送的消息实体
                message ->{
                    //设置消息唯一id
                    message.getMessageProperties().setMessageId(uuid);
                    return message;
                });
    }
    /**
     * 订单编号生成
     * 使用synchronized关键字保证了多线程环境下，生成的id不会重复
     * @return
     */

    private synchronized String OrderNumGenerate(Long userId){
        //使用hutool工具包提供的雪花算法生成工具生成订单id
        Snowflake snowflake = IdUtil.getSnowflake(1l, userId);
        String orderNum = snowflake.nextIdStr();
        return orderNum;
    }
}




