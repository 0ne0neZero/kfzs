package com.wishare.finance.apps.model.invoice.nuonuo.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description:
 */
@Getter
@Setter
@ApiModel("红字专用发票信息表申请入参")
public class InvoiceRedApplyF {

    @ApiModelProperty(value = "企业税号",required = true)
    @NotBlank(message = "企业税号不能为空")
    private String taxnum;

    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不能为空")
    private String tenantId;

    @ApiModelProperty(value = "24 位申请单号:12 位 金税盘编号+12 位该红字信息表请求时间 (YYMMDDHHMMSS)", required = true)
    @NotBlank(message = "24 位申请单号不能为空")
    private String billNo;

    @ApiModelProperty(value = "购方名称", required = true)
    @NotBlank(message = "购方名称不能为空")
    private String buyerName;

    @ApiModelProperty(value = "购方税号", required = true)
    @NotBlank(message = "购方税号不能为空")
    private String buyerTaxNo;

    @ApiModelProperty("申请说明")
    private String applyRemark;

    @ApiModelProperty(value = "发票种类: s 增值税专用发票，b 增值税电子专用发票", required = true)
    @NotBlank(message = "发票种类不能为空")
    private String invoiceLine;

    @ApiModelProperty(value = "对应蓝票代码，10位或12位，11位请左补0", required = true)
    @NotBlank(message = "对应蓝票代码不能为空")
    private String oriInvoiceCode;

    @ApiModelProperty(value = "对应蓝票号码，不满8位请左补0", required = true)
    @NotBlank(message = "对应蓝票号码不能为空")
    private String oriInvoiceNumber;

    @ApiModelProperty("对应蓝票的开票年月，YYYYMM格式")
    private String blueInvoiceTime;

    @ApiModelProperty(value = "销方名称", required = true)
    @NotBlank(message = "销方名称不能为空")
    private String sellerName;

    @ApiModelProperty(value = "销方税号", required = true)
    @NotBlank(message = "销方税号不能为空")
    private String sellerTaxNo;

    @ApiModelProperty(value = "合计税额，带负号，精确到小数点后面两位", required = true)
    @NotNull(message = "合计税额不能为空")
    private BigDecimal taxAmount;

    @ApiModelProperty(value = "合计不含税金额，带负号，精确到小数点后面两位", required = true)
    @NotNull(message = "合计不含税金额不能为空")
    private BigDecimal taxExcludedAmount;

    @ApiModelProperty(value = "成品油标志:0,非成品油;1,成品油", required = true)
    @NotBlank(message = "成品油标志不能为空")
    private String productOilFlag;

    @Valid
    @ApiModelProperty(value = "红字申请明细", required = true)
    @NotNull(message = "红字申请明细不能为空")
    private List<InvoiceRedApplyDetailF> invoiceRedApplyDetails;
}
