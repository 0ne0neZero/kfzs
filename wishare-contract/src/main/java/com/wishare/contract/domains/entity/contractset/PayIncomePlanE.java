package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
@TableName("pay_income_plan")
public class PayIncomePlanE implements Serializable {
    private static final long serialVersionUID = 927961094623314529L;


    @TableId(value = "id")
    private String id;
    /**
     * 关联合同ID
     */
    private String contractId;
    /**
     * 账单编号
     */
    private String billCode;
    /**
     * 付费类型ID
     */
    private String payTypeId;
    /**
     * 付费类型
     */
    private String payType;
    /**
     * 数量
     */
    private BigDecimal amountNum;
    /**
     * 账单金额
     */
    private BigDecimal plannedCollectionAmount;
    /**
     * 税率
     */
    private String taxRate;
    /**
     * 税率ID
     */
    private String taxRateId;
    /**
     * 不含税金额
     */
    private BigDecimal noTaxAmount;
    /**
     * 税额
     */
    private BigDecimal taxAmount;
    /**
     * 应收日期
     */
    private LocalDate plannedCollectionTime;
    /**
     * 归属月
     */
    private String belongingMonth;
    /**
     * 账单创建日期
     */
    private LocalDateTime billCreationTime;
    /**
     * 费用开始日期
     */
    private LocalDate costStartTime;
    /**
     * 费用结束日期
     */
    private LocalDate costEndTime;
    /**
     * 法定单位-ID
     */
    private String ourPartyId;
    /**
     * 法定单位-名称
     */
    private String ourParty;
    /**
     * 收费对象-ID
     */
    private String draweeId;
    /**
     * 收费对象-ID
     */
    private String draweeName;
    /**
     * 收费对象类型-名称
     */
    private String drawee;
    /**
     * 业务类型
     */
    private String serviceType;

    /**
     * 审核状态0待提交1审批中2已通过3已拒绝
     */
    private Integer reviewStatus;
    /**
     * 结算状态
     */
    private Integer paymentStatus;
    /**
     * 账单来源
     */
    private String billSource;
    /**
     * 备注
     */
    private String remark;
    /**
     * 合同编号
     */
    private String contractNo;
    /**
     * 收款计划编码
     */
    private String costEstimationCode;
    /**
     * 租户id
     */
    private String tenantId;
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
    private String operator;
    /**
     * 操作人名称
     */
    private String operatorName;
    /**
     * 操作时间
     */
    private LocalDateTime gmtModify;
    /**
     * 是否删除  0 正常 1 删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 推送状态
     */
    private Integer pushStatus;

    /**
     * 收款计划id
     */
    private String planId;
    /**
     * 入账状态
     */
    private Integer iriStatus;


    /**
     * 调整原因
     */
    private String adjustment;

    /**
     * 暂估确收金额
     */
    private BigDecimal confirmedAmountReceivedTemp;

    /**
     * 调整金额
     */
    private BigDecimal adjustmentAmount;
    /**
     * 付款金额
     */
    private BigDecimal paymentAmount;
    /**
     * 核销状态
     */
    private Integer settlementStatus;
    /**
     * 入账时间
     */
    private LocalDateTime iriTime;
    /**
     * 收入计划编码
     */
    private String incomePlanCode;
    /**
     * 账单id
     */
    private Long billId;
    /**
     * 收款计划期数
     */
    private Integer termDate;

    /**
     * 费项
     */
    private String chargeItem;
    /**
     * 操作账单类型(1:计提账单 2:更新实签应收金额)
     */
    private Integer billType;

    /**
     * 原不含税金额
     **/
    private BigDecimal originNoTaxAmount;

    /**
     * 原税额
     **/
    private BigDecimal originTaxAmount;
    /**
     * 减免金额
     **/
    private BigDecimal reductionAmount;
    /**
     * 合同清单id
     */
    private String payFundId;
}

