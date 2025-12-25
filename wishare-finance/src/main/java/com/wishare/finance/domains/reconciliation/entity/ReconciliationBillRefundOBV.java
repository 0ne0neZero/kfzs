package com.wishare.finance.domains.reconciliation.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 对账账单退款值对象
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/24
 */
@Getter
@Setter
@ApiModel("对账账单退款值对象")
public class ReconciliationBillRefundOBV {

    @ApiModelProperty(value = "退款单id")
    private Long id;

    @ApiModelProperty(value = "账单id")
    private Long billId;

    @ApiModelProperty(value = "账单类型")
    private Integer billType;

    @ApiModelProperty(value = "退款编号")
    private String refundNo;

    @ApiModelProperty(value = "退款金额")
    private Long refundAmount;

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty(value = "退款方式")
    private Integer refundWay;

    @ApiModelProperty(value = "图款渠道")
    private String refundChannel;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty("退款时间")
    private LocalDateTime refundTime;

}
