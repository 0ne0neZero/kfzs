package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 对账退款信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/24
 */
@Getter
@Setter
@ApiModel("对账退款信息")
public class ReconciliationRefundDto {

    @ApiModelProperty(value = "退款单id")
    private Long id;

    @ApiModelProperty(value = "账单id")
    private String billId;

    @ApiModelProperty(value = "账单类型")
    private Integer billType;

    @ApiModelProperty(value = "退款编号")
    private String refundNo;

    @ApiModelProperty(value = "退款金额")
    private Long refundAmount;

    @ApiModelProperty(value = "退款方式")
    private Integer refundWay;

    @ApiModelProperty(value = "图款渠道")
    private String refundChannel;

    @ApiModelProperty("退款时间")
    private LocalDateTime refundTime;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称中文")
    private String statutoryBodyName;


}
