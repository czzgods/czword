package com.itcz.czword.model.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema(description = "订单提交请求体")
public class OrderCommitDto {

    //订单编号
    @NotBlank(message = "订单编号不能为空")
    @Schema(description = "订单编号")
    private String orderNum;

}
