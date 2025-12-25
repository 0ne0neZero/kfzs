package com.wishare.finance.apps.pushbill.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel(value="支付申请单创建时所需信息")
public class PaymentApplicationFormDetailV {


    @ApiModelProperty("款项明细")
    private List<PaymentApplicationKXDetailV> paymentApplicationKXDetailVS;

    @ApiModelProperty("支付明细")
    private List<PaymentApplicationZFDetailV> paymentApplicationZFDetailVS;




}
