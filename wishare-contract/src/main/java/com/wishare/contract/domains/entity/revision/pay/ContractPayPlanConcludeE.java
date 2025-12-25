package com.wishare.contract.domains.entity.revision.pay;

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
@TableName("contract_pay_conclude_plan")
public class ContractPayPlanConcludeE extends ContractPlanConcludeE {
    @TableId(value = ID)
    private String id;

    /**
     * 费项成本预估计划分组编码(旧：付款计划编号)
     * 收付款计划编号
     */
    private String payNotecode;

    /**
     * 供应商id
     */
    private String merchant;

    /**
     * 供应商名称
     */
    private String merchantName;

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
     * 预估结算金额(旧:计划收付款金额)
     */
    private BigDecimal plannedCollectionAmount;

    /**
     * 应结算日期(旧：计划收付款时间)
     */
    private LocalDate plannedCollectionTime;

    /**
     * 结算金额
     */
    private BigDecimal settlementAmount;

    /**
     * 扣款金额
     */
    private BigDecimal deductionAmount;

    /**
     * 结算状态 0未结算:1 未完成:2 已完成:3
     */
    private Integer paymentStatus;

    /**
     * 开票/收票金额
     */
    private BigDecimal invoiceApplyAmount;

    /**
     * 开票/收票状态 未完成:1 已完成:2
     */
    private Integer invoiceStatus;

    /**
     * 收付款金额
     */
    private BigDecimal paymentAmount;

    /**
     * 未付金额
     */
    private BigDecimal noPayAmount;

    /**
     * 计划状态 待提交:1 已完成:2 未完成:3
     */
    private Integer planStatus;

    /**
     * 第几笔
     */
    private Integer howOrder;

    /**
     * 结算汇总金额
     */
    private BigDecimal settleSumAmount;

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
     * 是否完成成本计划的核销
     */
    private Integer payCostFinished;

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
    /**
     * 原合同计划ID，NK使用
     */
    private String mainId;
}
