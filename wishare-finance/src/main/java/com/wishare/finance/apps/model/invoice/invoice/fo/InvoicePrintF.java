package com.wishare.finance.apps.model.invoice.invoice.fo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ApiModel(value = "发票打印入参")
public class InvoicePrintF {

    @ApiModelProperty(value = "销方税号")
    @NotNull(message = "销方税号不能为空")
    private String salerTaxNum;

    @ApiModelProperty(value = "发票代码")
    @NotNull(message = "发票代码不能为空")
    private String invoiceCode;

    @ApiModelProperty(value = "发票号码")
    @NotNull(message = "发票号码不能为空")
    private String invoiceNo;

    @ApiModelProperty(value = "价税合计")
    @NotNull(message = "价税合计不能为空")
    private Long priceTaxAmount;

}
