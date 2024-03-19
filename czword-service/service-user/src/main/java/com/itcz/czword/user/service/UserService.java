package com.itcz.czword.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itcz.czword.model.entity.user.User;

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
}
