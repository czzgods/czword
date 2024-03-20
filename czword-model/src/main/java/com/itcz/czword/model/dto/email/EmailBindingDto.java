package com.itcz.czword.model.dto.email;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "邮箱账号绑定实体类")
public class EmailBindingDto {
    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "邮箱验证码")
    private String code;
}
