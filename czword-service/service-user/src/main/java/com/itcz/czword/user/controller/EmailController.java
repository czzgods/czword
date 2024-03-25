package com.itcz.czword.user.controller;


import com.itcz.czword.common.service.annotation.SwRateLimiting;
import com.itcz.czword.model.common.BaseResponse;
import com.itcz.czword.model.common.ResultUtils;
import com.itcz.czword.model.dto.email.EmailBindingDto;
import com.itcz.czword.model.enums.ErrorCode;
import com.itcz.czword.user.service.EmailService;
import com.itcz.czword.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@Tag(name = "邮箱接口")
@RestController
@RequestMapping("/email")
public class EmailController {
    //授权码 ：rhmaociqsnjxciji
    @Resource
    private EmailService emailService;
    @Resource
    private UserService userService;
    @SwRateLimiting
    @Operation(summary = "发送邮箱验证码")
    @PostMapping("/sendEmail")
    public BaseResponse sendRegisterEmail(String email){
        Boolean isSend = emailService.sendRegisterEmail(email);
        if(isSend){
            return ResultUtils.success("验证码发送成功");
        }
        return ResultUtils.error(ErrorCode.OPERATION_ERROR,"验证码发送失败");
    }
    @Operation(summary = "邮箱账号绑定")
    @PostMapping("/bindingEmail")
    public BaseResponse bindingEmail(@RequestBody EmailBindingDto emailBindingDto, HttpServletRequest httpServletRequest){
        userService.bindingEmail(emailBindingDto,httpServletRequest);
        return ResultUtils.success("邮箱账号绑定成功");
    }
}
