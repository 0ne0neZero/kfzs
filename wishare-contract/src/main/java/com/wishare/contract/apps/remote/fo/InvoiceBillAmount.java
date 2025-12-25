package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("账单开票金额")
public class InvoiceBillAmount {

    @ApiModelProperty("账单id")
    private Long billId;

    @ApiModelProperty("开票金额")
    private Long invoiceAmount;
}
