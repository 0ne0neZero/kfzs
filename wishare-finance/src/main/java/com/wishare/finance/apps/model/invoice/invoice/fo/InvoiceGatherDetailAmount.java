package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("收款明细开票金额")
public class InvoiceGatherDetailAmount {

    @ApiModelProperty("收款明细id")
    private Long gatherDetailId;

    @ApiModelProperty("开票金额")
    private Long invoiceAmount;

}
