package com.itcz.czword.user.controller;

import com.itcz.common.utils.UserContextUtil;
import com.itcz.czword.model.common.BaseResponse;
import com.itcz.czword.model.common.ResultUtils;
import com.itcz.czword.model.entity.user.User;
import com.itcz.czword.model.enums.ErrorCode;
import com.itcz.czword.user.service.EmailService;
import com.itcz.czword.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "邮箱接口")
@RestController
@RequestMapping("/email")
public class EmailController {
    //授权码 ：rhmaociqsnjxciji
    @Resource
    private EmailService emailService;
    @Resource
    private UserService userService;
    @Operation(summary = "发送邮箱验证码")
    @PostMapping("/sendEmail")
    public BaseResponse sendRegisterEmail(String email){
        Boolean isSend = emailService.sendRegisterEmail(email);
        if(isSend){
            return ResultUtils.success("验证码发送成功");
        }
        return ResultUtils.error(ErrorCode.OPERATION_ERROR,"验证码发送失败");
    }
}
