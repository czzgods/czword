package com.itcz.czword.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户删除请求体")
public class UserDeleteDto {
    @Schema(description = "用户id")
    private Long id;
}
