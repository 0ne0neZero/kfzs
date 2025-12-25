package com.wishare.finance.apps.pushbill.vo.dx.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Wyt
 */
@Data
@ApiModel(value = "对下结算单-成本明细-前端展示数据")
public class DxCostDetailsV {

    private String billId;

    @ApiModelProperty(value = "项目ID")
    private String communityId;

    @ApiModelProperty(value = "项目")
    private String communityName;

    @ApiModelProperty(value = "业务科目Id")
    private String paymentId;

    @ApiModelProperty(value = "业务科目编码")
    private String paymentCode;

    @ApiModelProperty(value = "业务科目名称")
    private String paymentName;

    @ApiModelProperty(value = "价税合计-金额-原币")
    private BigDecimal taxInclusiveTotalAmount;

    @ApiModelProperty(value = "可抵扣税额-金额-原币")
    private BigDecimal deductibleTaxAmount;

    @ApiModelProperty(value = "不含税-金额-原币")
    private BigDecimal taxExcludedAmount;

    @ApiModelProperty(value = "预算科目")
    private String subjectLevelLastId;

    @ApiModelProperty(value = "预算科目")
    private String subjectLevelLastName;



}
