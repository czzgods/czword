package com.itcz.czword.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.itcz.czword.model.entity.order.OrderForm;
import com.itcz.czword.order.mapper.OrderFormMapper;
import com.itcz.czword.order.service.OrderFormService;
import org.springframework.stereotype.Service;

/**
* @author 李钟意
* @description 针对表【order_form(订单表)】的数据库操作Service实现
* @createDate 2024-04-05 20:56:28
*/
@Service
public class OrderFormServiceImpl extends ServiceImpl<OrderFormMapper, OrderForm>
    implements OrderFormService {

}




