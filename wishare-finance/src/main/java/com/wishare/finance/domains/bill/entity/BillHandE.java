package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @description: 交账记录
 * @author: pgq
 * @since: 2022/10/9 14:10
 * @version: 1.0.0
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(TableNames.BILL_HAND)
public class BillHandE {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 账单id
     */
    private Long billId;

    /**
     * 账单类型（1:应收账单，2:预收账单，3:临时收费账单）
     */
    private Integer billType;

    /**
     * 交账状态 1-部分交账 2-已交账
     */
    private Integer accountHand;

    /**
     * 交账金额（单位：分）
     */
    private Long handAmount;

    /**
     * 账单金额（单位：分）
     */
    private Long totalAmount;

    /**
     * 交账票据列表
     * [{
     * "invoiceReceiptId": "票据id",
     * "amount": 100, //交账金额（分）
     * "discription": "交账说明"
     * }]
     */
    private String invoiceReceipts;

    /**
     * 备注
     */
    private String remark;

    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 修改人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;


}
