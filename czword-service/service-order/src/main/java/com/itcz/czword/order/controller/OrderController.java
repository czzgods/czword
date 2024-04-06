package com.itcz.czword.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itcz.czword.common.service.annotation.AuthCheck;
import com.itcz.czword.model.common.BaseResponse;
import com.itcz.czword.model.common.PageRequest;
import com.itcz.czword.model.common.ResultUtils;
import com.itcz.czword.model.constant.UserConstant;
import com.itcz.czword.model.enums.ErrorCode;
import com.itcz.czword.model.vo.order.OrderFormVo;
import com.itcz.czword.order.service.OrderFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Tag(name = "订单接口")
public class OrderController {
    @Resource
    private OrderFormService orderFormService;
    @Operation(summary = "分页查询订单")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/list/order")
    public BaseResponse<Page<OrderFormVo>>listOrder(PageRequest pageRequest){
        Page<OrderFormVo> orderFormVoPage = orderFormService.listOrder(pageRequest);
        return ResultUtils.success(orderFormVoPage);
    }

    @Operation(summary = "删除订单")
    @GetMapping("/deleteById/{id}")
    public BaseResponse deleteById(@PathVariable("id") Long id){
        Boolean success = orderFormService.deleteById(id);
        if(success) {
            return ResultUtils.success("订单删除成功");
        }else {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
    }
}
