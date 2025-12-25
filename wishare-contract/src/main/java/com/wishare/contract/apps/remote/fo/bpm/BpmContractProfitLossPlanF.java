package com.wishare.contract.apps.remote.fo.bpm;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 损益计划
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class BpmContractProfitLossPlanF {
    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("合同Id")
    private Long contractId;
    @ApiModelProperty("损益确认时间")
    private String confirmTime;
    @ApiModelProperty("计划损益金额（原币/含税）")
    private BigDecimal amountTaxIncluded;
    @ApiModelProperty("本币金额（含税）")
    private BigDecimal localCurrencyAmount;
    @ApiModelProperty("本币金额（不含税）")
    private BigDecimal taxExcludedAmount;
    @ApiModelProperty("费项名称")
    private String chargeItemName;
    @ApiModelProperty("成本中心名称")
    private String costName;
    @ApiModelProperty("责任部门名称")
    private String orgName;
    @ApiModelProperty("票据类型")
    private String billType;
    @ApiModelProperty("税率")
    private String taxRate;
    @ApiModelProperty("税额")
    private BigDecimal taxAmount;
    @ApiModelProperty("预算科目")
    private String budgetAccount;
    @ApiModelProperty("摘要")
    private String summary;
    @ApiModelProperty("损益核算方式")
    private Integer profitLossAccounting;
    @ApiModelProperty("已收款/付款金额")
    private BigDecimal paymentAmount;

    public BpmContractProfitLossPlanF(String confirmTime, BigDecimal localCurrencyAmount, BigDecimal taxExcludedAmount, String costName, String billType, String taxRate, String budgetAccount) {
        this.confirmTime = confirmTime;
        this.localCurrencyAmount = localCurrencyAmount;
        this.taxExcludedAmount = taxExcludedAmount;
        this.costName = costName;
        this.billType = billType;
        this.taxRate = taxRate;
        this.budgetAccount = budgetAccount;
    }
}
