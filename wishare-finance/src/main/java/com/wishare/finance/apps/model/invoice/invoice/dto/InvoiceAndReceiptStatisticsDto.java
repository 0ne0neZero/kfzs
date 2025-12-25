package com.wishare.finance.apps.model.invoice.invoice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 发票领用金额
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("发票合计金额和领用金额")
public class InvoiceAndReceiptStatisticsDto {

    @ApiModelProperty("合计金额")
    private Long totalAmount;

    @ApiModelProperty("已领用金额")
    private Long claimAmount;

    @ApiModelProperty("未领用金额")
    private Long unclaimedAmount;

    @ApiModelProperty("房号id")
    private String roomId;

    @ApiModelProperty("实收金额")
    private Long settleAmount;

    public InvoiceAndReceiptStatisticsDto() {
        totalAmount = 0L;
        claimAmount = 0L;
        unclaimedAmount = 0L;
        settleAmount = 0L;
    }
}
