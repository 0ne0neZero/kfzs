package com.wishare.contract.apps.fo.revision.pay.bill;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContractSettlementInvoiceDetailF {

    @ApiModelProperty("票据类型")
    private String invoiceType;

    @ApiModelProperty("发票号码")
    private String invoiceNum;

    @ApiModelProperty("发票代码")
    private String invoiceCode;

    @ApiModelProperty("收票含税金额")
    private BigDecimal invoiceTaxAmount;

    @ApiModelProperty("税额")
    private BigDecimal taxAmount;

    @ApiModelProperty("税率")
    private BigDecimal taxRate;

    @ApiModelProperty("可抵扣金额")
    private BigDecimal deductionAmount;

    @ApiModelProperty("开票时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate invoiceDate;

    @ApiModelProperty("验证状态： 0通过；1不通过")
    private Integer verifyStatus;

    @ApiModelProperty("购方名称")
    private String inname;
    @ApiModelProperty("购方识别号")
    private String gfsbh;
    @ApiModelProperty("销方名称")
    private String outname;
    @ApiModelProperty("销方识别号")
    private String xfsbh;

}
