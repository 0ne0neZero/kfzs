package com.wishare.finance.infrastructure.remote.fo.external.nuonuo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OutInvoicePrintRequestsF {
    @ApiModelProperty(value = "企业税号")
    private String taxNum;

    @ApiModelProperty(value = "发票代码")
    private String invoiceCode;

    @ApiModelProperty(value = "发票号码")
    private String invoiceNum;

    @ApiModelProperty(value = "价税合计")
    private String money;
}
