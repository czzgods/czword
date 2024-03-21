package com.itcz.czword.user.controller;

import com.itcz.common.service.exception.BusinessException;
import com.itcz.czword.model.common.BaseResponse;
import com.itcz.czword.model.common.ResultUtils;
import com.itcz.czword.model.dto.user.LoginAccountDto;
import com.itcz.czword.model.dto.user.LoginByEmailDto;
import com.itcz.czword.model.dto.user.UserRegister;
import com.itcz.czword.model.enums.ErrorCode;
import com.itcz.czword.model.vo.user.LoginVo;
import com.itcz.czword.user.service.EmailService;
import com.itcz.czword.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private EmailService emailService;
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegister userRegister) {
        if(userRegister == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegister.getUserAccount();
        String userPassword = userRegister.getUserPassword();
        String checkPassword = userRegister.getCheckPassword();
        String userName = userRegister.getUserName();
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,userName)){
            return null;
        }
        long userId = userService.userRegister(userAccount,userPassword,checkPassword,userName);
        return ResultUtils.success(userId);
    }
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public BaseResponse<LoginVo> login(@RequestBody LoginAccountDto loginAccountDto){
        LoginVo login = userService.login(loginAccountDto);
        return ResultUtils.success(login);
    }
    @Operation(summary = "邮箱登录")
    @PostMapping("/login/email")
    public BaseResponse<LoginVo> loginByEmail(@RequestBody LoginByEmailDto loginByEmailDto){
        LoginVo loginVo = userService.loginByEmail(loginByEmailDto);
        return ResultUtils.success(loginVo);
    }
    @Operation(summary = "发送邮箱验证码")
    @PostMapping("/sendEmail")
    public BaseResponse sendEmail(String email){
        boolean isSend = emailService.sendEmail(email);
        if(isSend){
            return ResultUtils.success("验证码发送成功");
        }else {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
    }
}
