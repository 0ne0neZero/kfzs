package com.wishare.finance.apps.model.reconciliation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 流水领用记录发票和收据详情
 *
 * @author yancao
 */
@Setter
@Getter
@ApiModel("流水领用记录发票和收据详情")
public class FlowInvoiceDetailV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("票据编号")
    private String invoiceReceiptNo;

    @ApiModelProperty("开票金额")
    private Long priceTaxAmount;

    @ApiModelProperty("开票时间")
    private LocalDateTime billingTime;

    @ApiModelProperty("实收金额")
    private Long settleAmount;

    @ApiModelProperty("票据类型： 1: 增值税普通发票 2: 增值税专用发票 3: 增值税电子发票 4: 增值税电子专票")
    private Integer type;

    @ApiModelProperty("票据状态：1 开票中 2 开票成功 3 开票失败 4 红冲中 5 已红冲 6 已作废 7 开票成功，签章失败")
    private Integer state;

    @ApiModelProperty("开票人")
    private String clerk;

}
