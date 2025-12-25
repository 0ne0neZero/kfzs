package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@ApiModel("对下结算单下的发票明细")
public class VoucherBillInvoiceV {

    @ApiModelProperty("发票号码")
    private String invoiceNo;

    @ApiModelProperty("发票代码")
    private String invoiceCode;

    @ApiModelProperty("发票类型")
    private String invoiceType;

    @ApiModelProperty("发票日期")
    private LocalDate invoiceDate;

    @ApiModelProperty("价税合计")
    private Double priceTaxAmount;

    @ApiModelProperty("税额")
    private Double taxAmount;

    @ApiModelProperty("是否分包 0 否 1 是 默认 否")
    private Integer subpackage = 0;

    @ApiModelProperty("是否出口退税 0 否 1 是  默认否")
    private Integer exportTaxRefund = 0;

    @ApiModelProperty("查验状态 0 失败  1 成功  默认成功")
    private Integer verificationStatus = 1;
}
