package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/10/17
 * @Description:
 */
@Getter
@Setter
@ApiModel("账单开票金额")
public class InvoiceBillAmount {

    @ApiModelProperty("账单id")
    private Long billId;

    @ApiModelProperty("开票金额")
    private Long invoiceAmount;
}
