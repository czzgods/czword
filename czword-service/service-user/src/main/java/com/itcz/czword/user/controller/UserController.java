package com.itcz.czword.user.controller;

import com.itcz.czword.common.service.annotation.AuthCheck;
import com.itcz.czword.common.service.exception.BusinessException;
import com.itcz.czword.model.common.BaseResponse;
import com.itcz.czword.model.common.ResultUtils;
import com.itcz.czword.model.constant.UserConstant;
import com.itcz.czword.model.dto.user.LoginAccountDto;
import com.itcz.czword.model.dto.user.LoginByEmailDto;
import com.itcz.czword.model.dto.user.UserDeleteDto;
import com.itcz.czword.model.dto.user.UserRegister;
import com.itcz.czword.model.enums.ErrorCode;
import com.itcz.czword.model.vo.user.LoginUserVo;
import com.itcz.czword.model.vo.user.LoginVo;
import com.itcz.czword.user.service.EmailService;
import com.itcz.czword.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public BaseResponse userLogout(HttpServletRequest httpServletRequest){
        Boolean logout = userService.userLogout(httpServletRequest);
        if(logout) {
            return ResultUtils.success("用户退出登录成功");
        }else {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
    }
    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/getLoginUser")
    public BaseResponse<LoginUserVo> getLoginUser(HttpServletRequest httpServletRequest){
        LoginUserVo loginUserVo = userService.getLoginUser(httpServletRequest);
        return ResultUtils.success(loginUserVo);
    }
    @Operation(summary = "管理员删除用户")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody UserDeleteDto userDeleteDto){
        if(userDeleteDto == null || userDeleteDto.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean remove  = userService.deleteUser(userDeleteDto);
        if(!remove){
            return ResultUtils.error(ErrorCode.NO_AUTH_ERROR);
        }else {
            return ResultUtils.success(remove);
        }
    }
    @Operation(summary = "用户注销账号")
    @GetMapping("/logOff")
    public BaseResponse<Boolean> userLogOff(HttpServletRequest httpServletRequest){
        Boolean result = userService.userLogOff(httpServletRequest);
        return ResultUtils.success(result);
    }
}
