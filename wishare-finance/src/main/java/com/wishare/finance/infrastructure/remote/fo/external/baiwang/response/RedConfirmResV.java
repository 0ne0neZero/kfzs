package com.wishare.finance.infrastructure.remote.fo.external.baiwang.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Linitly
 * @date: 2023/6/20 11:43
 * @descrption:
 */
@Data
@ApiModel(value = "红字申请响应model")
public class RedConfirmResV {

    @ApiModelProperty(value = "红字确认单流水号")
    private String redConfirmSerialNo;

    @ApiModelProperty(value = "红字确认单编号")
    private String redConfirmNo;

    @ApiModelProperty(value = "红字确认单UUID")
    private String redConfirmUuid;

    @ApiModelProperty(value = "红字确认单状态")
    private String confirmState;

    @ApiModelProperty(value = "红字发票号码，红字确认单符合直接开票条件时会直接返回开出的红字发票的发票号码")
    private String redInvoiceNo;
}
