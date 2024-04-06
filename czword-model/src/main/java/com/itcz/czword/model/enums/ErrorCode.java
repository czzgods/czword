package com.itcz.czword.model.enums;

/**
 * 自定义错误码
 *
 */
public enum ErrorCode {

    SUCCESS(0, "ok"),
    FILE_UPLOAD_ERROR(700,"文件上传失败"),
    REQUEST_METHOD_ERROR(800,"请求方式非法"),
    INTERFACE_EXIST(10000,"接口信息已存在"),
    INTERFACE_NOT_EXIST(10020,"接口不存在"),
    INTERFACE_COUNT_ERROR(10030,"接口使用次数不足"),
    INTERFACE_USER_ERROR(10040,"该用户尚未开通该接口业务"),
    SK_ERROR(20000,"秘钥错误"),
    AK_ERROR(20010,"accessKey不合法"),
    EMAIL_CODE_NOT_EXIT(30000,"邮箱验证码已过期"),
    EMAIL_CODE_ERROR(30001,"邮箱验证码输入错误"),
    RATE_LIMITING_ERROR(30002,"请求频繁，请稍后尝试"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    ACCOUNT_EXIST(40200,"账户已存在"),
    ACCOUNT_NOT_EXIST(40300,"账户不存在"),
    PASSWORD_ERROR(40400,"密码错误"),
    OPERATION_ERROR(50001, "操作失败"),
    ORDER_IS_NOT_EXISTS(60000,"订单信息不存在");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
