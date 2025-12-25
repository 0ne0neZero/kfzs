package com.wishare.contract.domains.entity.revision.income;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.contract.domains.entity.revision.ContractPlanConcludeE;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author zhangfuyu
 * @mender 龙江锋
 * @Date 2023/7/6/11:55
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_income_conclude_plan")
public class ContractIncomePlanConcludeE extends ContractPlanConcludeE {
    @TableId(value = ID)
    private String id;

    /**
     * 收付款计划编号
     */
    private String payNotecode;

    /**
     * 客户，合同中的对方单位
     */
    private String customer;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * pid
     */
    private String pid;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 期数
     */
    private Integer termDate;

    /**
     * 计划收付款金额
     */
    private BigDecimal plannedCollectionAmount;

    /**
     * 计划收付款日期
     */
    private LocalDate plannedCollectionTime;

    /**
     * 结算金额
     */
    private BigDecimal settlementAmount;

    /**
     * 未计划金额
     */
    private BigDecimal noPlanAmount;

    /**
     * 结算状态 未结算:1 未完成:2 已完成:3
     */
    private Integer paymentStatus;

    /**
     * 开票金额
     */
    private BigDecimal invoiceApplyAmount;

    /**
     * 减免金额
     */
    private BigDecimal deductionAmount;

    /**
     * 开票 未完成:1 已完成:2
     */
    private Integer invoiceStatus;

    /**
     * 收款金额
     */
    private BigDecimal receiptAmount;

    /**
     * 未收款金额
     */
    private BigDecimal noReceiptAmount;

    /**
     * 计划状态 待提交:1 已完成:2 未完成:3
     */
    private Integer planStatus;

    /**
     * 所属部门
     */
    private String departName;

    /**
     * 第几批
     */
    private Integer howorder;

    /**
     * 结算开始时间
     */
    private LocalDate startTime;

    /**
     * 结算结束时间
     */
    private LocalDate endTime;

    /**
     * 结算周期
     */
    private String timeRanges;


    /**
     *费用开始日期
     */
    private LocalDate costStartTime;
    /**
     * 费用结束日期
     */
    private LocalDate costEndTime;
    /**
     * 成本预估编码
     */
    private String costEstimationCode;

    /**
     * 结算计划分组
     */
    private String settlePlanGroup;

    /**
     * 合同清单id
     */
    private String contractPayFundId;

    /**
     * 调整金额
     */
    private BigDecimal adjustmentAmount;

    /**
     * 调整后应收金额
     */
    private BigDecimal adjustedReceivableAmount;

    /**
     * 暂估确收金额
     */
    private BigDecimal confirmedAmountReceivedTemp;
    /**
     * 审核状态
     */
    private Integer approveState;
    /**
     *最大应收日期
     */
    private LocalDate ptMax;
    /**
     * 最小应收日期
     */
    private LocalDate ptMin;

    /**
     * 是否完成收入计划的核销
     */
    private Integer payIncomeFinished;

    /**
     * 核销税额
     **/
    private BigDecimal settlementTaxAmount;

    /**
     * 核销不含税金额
     **/
    private BigDecimal settlementNoTaxAmount;

    /**
     * 减免金额
     **/
    private BigDecimal reductionAmount;

}
