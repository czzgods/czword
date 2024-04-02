package com.itcz.czword.model.dto.interfaces;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Schema(description = "用户分配接口实体类")
public class InterfaceAssign implements Serializable {


    @NotBlank(message="用户Id不能为空")
    @Schema(description = "用户id")
    private Long userId;
    @NotBlank(message="接口Id不能为空")
    @Schema(description = "接口id")
    private Long interfaceId;

    //使用次数
    @NotBlank(message = "调用次数不能为空")
    @Min(value = 1,message = "最少不能少于1")
    @Max(value = 9999,message = "最多不能超过9999")
    @Schema(description = "使用次数")
    private Long useCount;
}
