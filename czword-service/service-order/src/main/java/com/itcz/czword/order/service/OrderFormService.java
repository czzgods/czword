package com.itcz.czword.order.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itcz.czword.model.common.PageRequest;
import com.itcz.czword.model.dto.order.OrderCreateDto;
import com.itcz.czword.model.entity.order.OrderForm;
import com.itcz.czword.model.vo.order.OrderFormVo;

/**
* @author 李钟意
* @description 针对表【order_form(订单表)】的数据库操作Service
* @createDate 2024-04-05 20:56:28
*/
public interface OrderFormService extends IService<OrderForm> {

    /**
     * 分页查询所有订单
     * @param pageRequest
     * @return
     */
    Page<OrderFormVo> listOrder(PageRequest pageRequest);

    /**
     * 删除订单
     * @param id
     * @return
     */
    Boolean deleteById(Long id);

    /**
     * 创建订单
     * @param orderCreateDto
     * @return
     */
    OrderFormVo createOrder(OrderCreateDto orderCreateDto);
}
