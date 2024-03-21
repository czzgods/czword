package com.itcz.czword.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itcz.czword.model.dto.email.EmailBindingDto;
import com.itcz.czword.model.dto.user.LoginAccountDto;
import com.itcz.czword.model.dto.user.LoginByEmailDto;
import com.itcz.czword.model.entity.user.User;
import com.itcz.czword.model.vo.user.LoginUserVo;
import com.itcz.czword.model.vo.user.LoginVo;

public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @param userName
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String userName);

    /**
     * 账户邮箱绑定
     * @param emailBindingDto
     */
    void bindingEmail(EmailBindingDto emailBindingDto);

    /**
     * 使用账号密码登录
     * @param loginAccountDto
     * @return
     */
    LoginVo login(LoginAccountDto loginAccountDto);

    /**
     * 使用邮箱登录
     * @param loginByEmailDto
     * @return
     */
    LoginVo loginByEmail(LoginByEmailDto loginByEmailDto);

    /**
     * 用户退出登录
     */
    void userLogout();

    /**
     * 获取当前登录用户
     * @return
     */
    LoginUserVo getLoginUser();

}
