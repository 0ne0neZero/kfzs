package com.wishare.finance.apps.model.invoice.invoice.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 发票合计金额和领用金额返回信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("发票合计金额和领用金额返回信息")
public class InvoiceAndReceiptStatisticsV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("合计金额")
    private Long totalAmount;

    @ApiModelProperty("已领用金额")
    private Long claimAmount;

    @ApiModelProperty("未领用金额")
    private Long unclaimedAmount;
}
