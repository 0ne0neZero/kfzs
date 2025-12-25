package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author yyx
 * @project wishare-finance
 * @title BillCarryoverDetail
 * @date 2023.09.21  10:16
 * @description 账单结转详情信息
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = TableNames.BILL_CARRYOVER_DETAIL,autoResultMap = true)
public class BillCarryoverDetailE {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 结转明细ID
     */
    private Long billCarryoverId;

    /**
     *  结转账单类型 1-应收账单， 2-预收账单， 3-临时收费账单
     */
    private Integer billType;

    /**
     * 结转账单id
     */
    private Long carriedBillId;

    /**
     * 结转账单编号
     */
    private String carriedBillNo;

    /**
     * 目标账单id
     */
    private Long targetBillId;

    /**
     * 目标账单编号
     */
    private String targetBillNo;

    /**
     * 结转金额（单位：分）
     */
    private Long carryoverAmount;

    /**
     * 结转方式：1抵扣，2结转预收
     */
    private Integer carryoverType;

    /**
     * 结转时间
     */
    private LocalDateTime carryoverTime;

    /**
     * 是否回退
     */
    private Integer back;

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
     * 是否冲销：0未冲销，1已冲销
     */
    private Integer reversed;

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
