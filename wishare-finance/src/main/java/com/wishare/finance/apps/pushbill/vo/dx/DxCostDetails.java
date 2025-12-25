package com.wishare.finance.apps.pushbill.vo.dx;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Wyt
 */
@Data
@ApiModel(value = "对下结算单-成本明细-前端展示数据")
public class DxCostDetails {

    @ApiModelProperty(value = "项目ID")
    private String communityId;

    @ApiModelProperty(value = "业务科目Id")
    private String paymentId;

    @ApiModelProperty(value = "业务科目编码")
    private String paymentCode;

    @ApiModelProperty(value = "业务科目名称")
    private String paymentName;

    @ApiModelProperty(value = "预算科目")
    private String budgetItemId;

    @ApiModelProperty(value = "预算科目")
    private String budgetItemName;

    @ApiModelProperty("【部门编码】")
    private String departmentCode;

    @ApiModelProperty("【部门名称】")
    private String departmentName;

    @ApiModelProperty("本位币币种编码")
    private String originCurrencyCode = "156";

    @ApiModelProperty("币种]")
    private String originCurrencyType = "CNY-人民币";

    @ApiModelProperty(value = "汇率")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal exchangeRate = BigDecimal.ONE.setScale(6);

    @ApiModelProperty(value = "价税合计-金额-原币")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal taxInclusiveTotalAmount;

    @ApiModelProperty(value = "价税合计-金额-本位币")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal taxInclusiveTotalStandardAmount;

    @ApiModelProperty(value = "可抵扣税额-金额-原币")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal deductibleTaxAmount;

    @ApiModelProperty(value = "可抵扣税额-金额-本位币")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal deductibleTaxStandardAmount;

    @ApiModelProperty(value = "不含税-金额-原币")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal taxExcludedAmount;

    @ApiModelProperty(value = "不含税-金额-本位币")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal taxExcludedStandardAmount;





}
