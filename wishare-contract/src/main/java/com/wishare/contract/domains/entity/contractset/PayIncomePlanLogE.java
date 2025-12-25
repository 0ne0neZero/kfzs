package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收入计划(PayIncomePlan)实体类
 *
 * @author makejava
 * @since 2024-11-29 15:55:10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//@TableName("pay_income_plan_log")
public class PayIncomePlanLogE implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private String id;

    /**
     * 收入/成本计划id
     */
    private String planId;

    /**
     * 收款/结算计划id
     */
    private String incomePayPlanId;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 暂估确收金额-旧
     */
    private BigDecimal oldConfirmedAmountReceivedTemp;

    /**
     * 暂估确收金额-新
     */
    private BigDecimal newConfirmedAmountReceivedTemp;

    /**
     * 税额-旧
     */
    private BigDecimal oldTaxAmount;

    /**
     * 税额-新
     */
    private BigDecimal newTaxAmount;

    /**
     * 调整原因-旧
     */
    private String oldAdjustment;

    /**
     * 调整原因-新
     */
    private String newAdjustment;

    /**
     * 计划类型,0:收入计划 1:成本计划
     */
    private Integer planType;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人名称
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 是否删除  0 正常 1 删除
     */
    @TableLogic
    private Integer deleted;

}

