package com.itcz.czword.model.entity.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单表
 * @TableName order_form
 */
@TableName(value ="order_form")
@Data
public class OrderForm implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}