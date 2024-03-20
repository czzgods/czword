package com.itcz.czword.user.service;

public interface EmailService {
    /**
     * 发送邮箱验证码
     * @param email
     * @return
     */
    Boolean sendRegisterEmail(String email);
}
