package com.itcz.czword.model.vo.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单表
 * @TableName order_form
 */
@Data
public class OrderFormVo implements Serializable {
    /**
     * 订单编号
     */
    private String orderNum;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 接口id
     */
    private Long interfaceId;

    /**
     * 使用次数
     */
    private Integer quantity;

    /**
     * 金额
     */
    private Double subtotal;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}