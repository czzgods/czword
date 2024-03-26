package com.itcz.czword.model.entity.log;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 操作日志表
 * @TableName log
 */
@TableName(value ="log")
@Data
public class Log implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 请求URL
     */
    private String URL;

    /**
     * 描述信息
     */
    private String businessName;

    /**
     * 请求响应格式
     */
    private String httpMethod;

    /**
     * 调用方法的全路径名
     */
    private String typeName;

    /**
     * 调用的方法名
     */
    private String methodName;

    /**
     * 请求的IP
     */
    private String remoteHost;

    /**
     * 传入的参数
     */
    private String args;

    /**
     * 返回值
     */
    private String ret;

    /**
     * 方法执行耗时, 单位:ms
     */
    private Long cost_time;
    /**
     * 异常
     */
    private String exception;
    /**
     * 操作时间
     */
    private LocalDateTime creat_time;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}