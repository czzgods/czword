package com.itcz.czword.user.service;

public interface EmailService {
    /**
     * 发送邮箱绑定账号验证码
     * @param email
     * @return
     */
    Boolean sendRegisterEmail(String email);

    /**
     * 发送邮箱登录验证码
     * @param email
     * @return
     */
    boolean sendEmail(String email);
}
