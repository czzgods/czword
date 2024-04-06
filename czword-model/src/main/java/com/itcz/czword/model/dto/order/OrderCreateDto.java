package com.itcz.czword.model.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "订单创建请求体")
public class OrderCreateDto {
    //订单用户id
    @NotBlank(message = "用户ID不能为空")
    @Schema(description = "用户id")
    private Long userId;
    //订单接口id
    @NotBlank(message = "接口id不能为空")
    @Schema(description = "接口id")
    private Long interfaceId;
    //购买次数
    @NotBlank(message = "使用次数不能为空")
    @Schema(description = "使用次数")
    @Digits(integer =5 ,fraction = 0,message = "必须为整数")
    @Min(value = 1,message = "最少为1")
    @Max(value = 9999,message = "最多为9999")
    private Integer quantity;
    //金额
    @NotBlank(message = "金额不能为空")
    @Schema(description = "总金额")
    @Digits(integer =5 ,fraction = 0,message = "必须为整数")
    @Min(value = 1,message = "最少为1")
    @Max(value = 99999,message = "最多为99999")
    private Double subtotal;

}
