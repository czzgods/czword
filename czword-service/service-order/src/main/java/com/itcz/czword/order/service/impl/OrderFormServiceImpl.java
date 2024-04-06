package com.itcz.czword.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.itcz.czword.common.service.exception.BusinessException;
import com.itcz.czword.model.common.PageRequest;
import com.itcz.czword.model.entity.order.OrderForm;
import com.itcz.czword.model.enums.ErrorCode;
import com.itcz.czword.model.vo.order.OrderFormVo;
import com.itcz.czword.order.mapper.OrderFormMapper;
import com.itcz.czword.order.service.OrderFormService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author 李钟意
* @description 针对表【order_form(订单表)】的数据库操作Service实现
* @createDate 2024-04-05 20:56:28
*/
@Service
public class OrderFormServiceImpl extends ServiceImpl<OrderFormMapper, OrderForm>
    implements OrderFormService {
    @Resource
    private OrderFormMapper orderFormMapper;

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
}




