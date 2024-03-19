package com.itcz.czword.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "用户注册请求体")
public class UserRegister {
    @Schema(description = "账号")
    @NotBlank(message = "账号不能为空")
    @Size(min = 6,max = 32,message = "账号长度最短不低于6位，最长不超过32位")
    private String userAccount;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6,max = 32,message = "账号长度最短不低于6位，最长不超过32位")
    private String userPassword;

    @Schema(description = "再次输入密码")
    @NotBlank(message = "密码不能为空")
    private String checkPassword;
    @Schema(description = "用户昵称")
    @NotBlank(message = "用户昵称不能为空")
    @Size(min = 3,max = 12,message = "账号长度最短不低于3位，最长不超过12位")
    private String userName;
}
