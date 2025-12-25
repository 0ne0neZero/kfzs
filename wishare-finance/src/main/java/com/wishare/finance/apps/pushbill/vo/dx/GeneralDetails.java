package com.wishare.finance.apps.pushbill.vo.dx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author longhuadmin
 */
@Data
@ApiModel(value = "通用确认收入确认明细")
public class GeneralDetails {


    @ApiModelProperty(value = "预算科目ID")
    private String YSKMID;


    @ApiModelProperty(value = "项目ID")
    private String communityId;

    @ApiModelProperty(value = "项目名称")
    private String communityName;

    @ApiModelProperty("中交合同编码-CT码")
    private String conMainCode;


    @ApiModelProperty(value = "合同id")
    private String contractId;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "往来单位-名称")
    private String exchangeUnitName;

    @ApiModelProperty(value = "往来单位-主数据编码")
    private String exchangeUnitMainCode;

    @ApiModelProperty(value = "原币币种")
    private String originCurrencyType = "CNY-人民币";

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate = BigDecimal.valueOf(1.000000);

    @ApiModelProperty("税率")
    private BigDecimal taxRate;

    @ApiModelProperty("税额（单位：分）")
    private BigDecimal taxAmount;

    @ApiModelProperty("不含税金额")
    private BigDecimal taxExcludedAmount;

    @ApiModelProperty(value = "含税金额",required = true)
    private BigDecimal taxIncludedAmount;

    @ApiModelProperty(value = "税率id")
    private String taxRateId;

    @ApiModelProperty(value = "税率对应计税方式编码")
    private String taxType;

    @ApiModelProperty(value = "税率对应计税方式名称")
    private String taxTypeName;

    @ApiModelProperty(value = "科目名称")
    private String subjectName;



}
