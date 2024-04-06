package com.itcz.czword.model.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户消息延迟队列的消息发送实体
 */
@Data
@AllArgsConstructor
@Schema(description = "订单延迟消息队列实体")
public class OrderDelayDto implements Serializable {

    //订单用户id
    @NotBlank(message = "用户ID不能为空")
    @Schema(description = "用户id")
    private Long userId;

    //订单编号
    @NotBlank(message = "订单编号不能为空")
    @Schema(description = "订单编号")
    private String orderNum;
}
