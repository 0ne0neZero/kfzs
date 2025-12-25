package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReceiptDetailRf {

    @ApiModelProperty("收据id")
    private Long invoiceReceiptId;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

    @ApiModelProperty("收据编号")
    private String invoiceReceiptNo;
}
