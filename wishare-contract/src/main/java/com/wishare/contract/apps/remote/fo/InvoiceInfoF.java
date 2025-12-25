package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class InvoiceInfoF {

    @ApiModelProperty("账单ids")
    private List<Long> billIds;

    @ApiModelProperty("发票收据主表ids")
    private List<Long> invoiceReceiptIds;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

    public InvoiceInfoF(List<Long> billIds, List<Long> invoiceReceiptIds) {
        this.billIds = billIds;
        this.invoiceReceiptIds = invoiceReceiptIds;
    }
}
