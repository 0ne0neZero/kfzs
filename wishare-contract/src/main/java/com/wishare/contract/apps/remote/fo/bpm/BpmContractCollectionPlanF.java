package com.wishare.contract.apps.remote.fo.bpm;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 收款计划
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BpmContractCollectionPlanF {
    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("合同Id")
    private Long contractId;
    @ApiModelProperty("计划收款时间")
    private String plannedCollectionTime;
    @ApiModelProperty("款项比例")
    private String paymentProportion;
    @ApiModelProperty("摘要")
    private String summary;
    @ApiModelProperty("费项名称")
    private String chargeItemName;
    @ApiModelProperty("成本中心名称")
    private String costName;
    @ApiModelProperty("责任部门名称")
    private String orgName;
    @ApiModelProperty("票据类型/发票类型(名称)")
    private String billType;
    @ApiModelProperty("税率")
    private String taxRate;
    @ApiModelProperty("本币金额（含税）")
    private BigDecimal localCurrencyAmount;
    @ApiModelProperty("本币金额（不含税）")
    private BigDecimal taxExcludedAmount;
    @ApiModelProperty("税额")
    private BigDecimal taxAmount;
    @ApiModelProperty("计划收款金额（原币/含税）")
    private BigDecimal plannedCollectionAmount;
    @ApiModelProperty("损益核算方式")
    private Integer profitLossAccounting;
    @ApiModelProperty("已收款/付款金额")
    private BigDecimal paymentAmount;
    @ApiModelProperty("已开票/收票金额")
    private BigDecimal invoiceAmount;
    @ApiModelProperty("减免金额")
    private BigDecimal creditAmount;


    public BpmContractCollectionPlanF(String plannedCollectionTime, String billType, String taxRate, BigDecimal localCurrencyAmount, BigDecimal taxExcludedAmount) {
        this.plannedCollectionTime = plannedCollectionTime;
        this.billType = billType;
        this.taxRate = taxRate;
        this.localCurrencyAmount = localCurrencyAmount;
        this.taxExcludedAmount = taxExcludedAmount;
    }
}
