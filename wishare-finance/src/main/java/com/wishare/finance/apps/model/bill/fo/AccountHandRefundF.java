package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/14 9:48
 * @version: 1.0.0
 */
@Setter
@Getter
@ApiModel("差额退款")
public class AccountHandRefundF {

    @ApiModelProperty("账单id")
    private Long id;

    @ApiModelProperty("账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单")
    private Integer billType;

    @ApiModelProperty("退款金额")
    private Long refundAmount;

    @ApiModelProperty("退款原因")
    private String refundReason;
}
