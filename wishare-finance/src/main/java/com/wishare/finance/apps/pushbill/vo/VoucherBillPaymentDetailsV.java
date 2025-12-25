package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("款项明细")
public class VoucherBillPaymentDetailsV {

    @ApiModelProperty("变动")
    private String changeType;

    @ApiModelProperty("款项名称")
    private String paymentName;

    @ApiModelProperty("应收应付编号")
    private String billNo;

    @ApiModelProperty("未核销金额")
    private Long writeOffInfoAmount;

    @ApiModelProperty("原币金额")
    private Long amount;

}
