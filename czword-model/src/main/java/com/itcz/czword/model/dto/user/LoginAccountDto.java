package com.itcz.czword.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "用户登录请求体")
public class LoginAccountDto {
    @Schema(description = "用户名")
    @NotBlank(message = "账号不能为空")
    @Size(min = 6,max = 64,message = "账号最短不少于6，最长不大于64")
    private String userAccount;

    @Schema(description = "密码/邮箱验证码")
    @Size(min = 4,max = 32,message = "密码/验证码不少于4，不大于32")
    private String userPassword;
}
