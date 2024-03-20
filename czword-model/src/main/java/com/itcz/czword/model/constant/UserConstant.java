package com.itcz.czword.model.constant;

/**
 * 用户常量
 *
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    //  region 权限

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    /**
     * 被封号
     */
    String BAN_ROLE = "ban";
    /**
     * 邮箱账号绑定验证码
     */
    String EMAIL_BINDING_CODE = "email:binding:code:";
    /**
     * 邮箱验证登录验证码
     */
    String EMAIL_LOGIN_CODE="email:login:code:";
    /**
     * 邮箱验证码发送
     */
    String EMAIL_SEND_CODE = "email:send:code:";
    /**
     * 邮箱验证码的锁
     */
    String EMAIL_SEND_LOCK = "email:send:lock:";
    // endregion
}
