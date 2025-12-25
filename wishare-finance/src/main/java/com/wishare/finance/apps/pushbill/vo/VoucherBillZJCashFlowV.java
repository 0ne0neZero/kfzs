package com.wishare.finance.apps.pushbill.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@ApiModel("资金收款单下的现金流量")
public class VoucherBillZJCashFlowV {
    private String billNo;


    private String cashFlowName;

    private String cashFlowNameExtId;

    @ApiModelProperty("币种(货币代码)（CNY:人民币）")
    private String currency = "人民币";


    @ApiModelProperty("金额")
    private BigDecimal amount;
}
