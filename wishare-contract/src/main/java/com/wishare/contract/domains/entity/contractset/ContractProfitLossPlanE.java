package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author wangrui
 * @since 2022-09-13
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_profit_loss_plan")
public class ContractProfitLossPlanE {

    /**
     * 主键
     */
    private Long id;

    /**
     * 费项Id
     */
    private Long chargeItemId;

    /**
     * 成本中心Id
     */
    private Long costId;

    /**
     * 收款计划Id
     */
    private Long collectionPlanId;

    /**
     * 账单id
     */
    private Long billId;

    /**
     * 合同Id
     */
    private Long contractId;

    /**
     * 工程计提Id
     */
    private String engineeringCode;

    /**
     * 责任部门（组织id）
     */
    private Long orgId;

    /**
     * 预算科目
     */
    private String budgetAccount;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 票据类型
     */
    private String billType;

    /**
     * 税率id
     */
    private Long taxRateId;

    /**
     * 税率
     */
    private String taxRate;

    /**
     * 损益确认时间
     */
    private LocalDate confirmTime;

    /**
     * 计划损益金额（原币/含税）
     */
    private BigDecimal amountTaxIncluded;

    /**
     * 本币金额（含税）
     */
    private BigDecimal localCurrencyAmount;

    /**
     * 本币金额（不含税）
     */
    private BigDecimal taxExcludedAmount;

    /**
     * 服务开始日期
     */
    private LocalDate serviceStartDate;

    /**
     * 服务结束日期
     */
    private LocalDate serviceEndDate;

    /**
     * 损益核算方式
     */
    private Integer profitLossAccounting;

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
    private Integer deleted;

    /**
     * 已收款/付款金额
     */
    private BigDecimal paymentAmount;

    /**
     * 已开票/收票金额
     */
    private BigDecimal invoiceAmount;

    /**
     * 收款/付款状态 0未收/付  1部分收/付  2已收/付
     */
    private Integer paymentStatus;

    /**
     * 开票/收票状态 0未开/收  1部分开/收  2已开/收
     */
    private Integer invoiceStatus;

    /**
     * 税额
     */
    private BigDecimal taxAmount;
}
