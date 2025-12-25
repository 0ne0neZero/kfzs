package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 票据账单信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/21
 */
@Getter
@Setter
@ApiModel("票据账单信息")
public class InvoiceBillInfoF {

    @ApiModelProperty(value = "账单id")
    private Long billId;

    @ApiModelProperty(value = "账单id")
    private Integer billType;

    @ApiModelProperty(value = "账单编码")
    private String billNo;

}
