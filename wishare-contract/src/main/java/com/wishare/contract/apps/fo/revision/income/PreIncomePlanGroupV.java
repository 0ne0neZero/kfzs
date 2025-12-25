package com.wishare.contract.apps.fo.revision.income;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @Author dengjie03
 * @Description
 * @Date 2024-11-13
 */
@Data
public class PreIncomePlanGroupV {


    @ApiModelProperty(value = "计划id")
    private String id;

    @ApiModelProperty(value = "费用开始日期")
    private LocalDate costStartTime;

    @ApiModelProperty(value = "费用结束日期")
    private LocalDate costEndTime;

    @ApiModelProperty(value = "结算计划分组(编码)")
    private String settlePlanGroup;

    @ApiModelProperty("应收日期")
    private LocalDate plannedCollectionTime;

    @ApiModelProperty("预估结算金额")
    private BigDecimal plannedCollectionAmount;

    @ApiModelProperty("金额比例")
    private BigDecimal ratioAmount;

    @ApiModelProperty("期数")
    private Integer termDate;

    @ApiModelProperty("合同清单")
    private String serviceType;

    @ApiModelProperty("合同清单")
    private String serviceTypeId;

    @ApiModelProperty("费项")
    private String chargeItem;

    @ApiModelProperty("费项ID")
    private String chargeItemId;

    @ApiModelProperty("税率")
    private String taxRate;

    @ApiModelProperty("税率")
    private String taxRateId;

    @ApiModelProperty("税额")
    private BigDecimal taxAmount;

    @ApiModelProperty("不含税金额")
    private BigDecimal noTaxAmount;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("结算状态 未结算:1 未完成:2 已完成:3")
    private Integer paymentStatus;

    @ApiModelProperty("开票/收票金额")
    @Digits(integer = 15, fraction = 6, message = "计划总金额不正确")
    private BigDecimal invoiceApplyAmount;

    @ApiModelProperty("开票/收票状态 未完成:1 已完成:2")
    private Integer invoiceStatus;

    @ApiModelProperty("暂估确收金额")
    private BigDecimal confirmedAmountReceivedTemp;

}
