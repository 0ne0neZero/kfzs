package com.wishare.finance.apps.pushbill.vo.dx;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author Wyt
 */
@Data
@ApiModel(value = "对下结算单-成本明细-前端展示数据")
public class DxInvoiceDetails {

    @ApiModelProperty(value = "项目ID")
    private String communityId;

    @ApiModelProperty(value = "发票类型")
    private String invoiceType;

    @ApiModelProperty(value = "发票号码")
    private String invoiceNum;

    @ApiModelProperty("发票代码")
    private String invoiceCode;

    @ApiModelProperty("校验码")
    private String verificationCode;

    @ApiModelProperty("开票日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate invoiceDate;

    @ApiModelProperty("本位币币种编码")
    private String originCurrencyCode = "156";

    @ApiModelProperty("币种]")
    private String originCurrencyType = "CNY-人民币";

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate = BigDecimal.valueOf(1.000000);

    @ApiModelProperty(value = "不含税金额")
    private BigDecimal taxExcludedAmount;

    @ApiModelProperty(value = "税额")
    private BigDecimal taxAmount;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "价税合计")
    private BigDecimal invoiceTaxAmount;

    @ApiModelProperty(value = "是否出口退税：0-否，1-是")
    private String isExportDrawback = "0";

    @ApiModelProperty(value = "验证状态：0,验证通过;1,验证未通过;")
    private Integer verifyStatus ;

    @ApiModelProperty(value = "可抵扣税额-金额-原币")
    private BigDecimal deductibleTaxAmount;

    @ApiModelProperty(value = "可抵扣金额-金额-原币")
    private BigDecimal deductibleAmount;

    @ApiModelProperty(value = "税码")
    private String taxCode;


    /**
     * 辅助计算的
     **/
    private BigDecimal deductionAmount;
    @ApiModelProperty("购方名称")
    private String inname;
    @ApiModelProperty("购方识别号")
    private String gfsbh;
    @ApiModelProperty("销方名称")
    private String outname;
    @ApiModelProperty("销方识别号")
    private String xfsbh;


}
