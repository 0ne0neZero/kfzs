package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/9/14
 * @Description: 退款明细
 */
@Getter
@Setter
public class BillRefundDto {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("账单id")
    private Long billId;

    @ApiModelProperty("退款编号")
    private String refundNo;

    @ApiModelProperty("结算渠道")
    private String refundChannel;

    /**
     * 退款方式(1汇款，2支票，3其他，4现金，5线下-支付宝，6线下-微信,7-原路退回)
     */
    private String refundMethod;

    @ApiModelProperty("退款方式(0线上，1线下)")
    private Integer refundWay;

    @ApiModelProperty("外部退款编号（支付宝退款单号，银行流水号等）")
    private String outRefundNo;

    @ApiModelProperty("退款金额（单位：分）")
    private Long refundAmount;

    @ApiModelProperty("退款时间")
    private LocalDateTime refundTime;

    @ApiModelProperty("审核记录id")
    private Long billApproveId;

    @ApiModelProperty("申请退款时间")
    private LocalDateTime approveTime;

    @ApiModelProperty("收费对象类型")
    private Integer refunderType;

    @ApiModelProperty("附件")
    private String fileInfo;

    @ApiModelProperty("退款人ID")
    private String refunderId;

    @ApiModelProperty("退款人名称")
    private String refunderName;

    @ApiModelProperty("退款状态：0待退款，1退款中，2已退款，3未生效")
    private Integer state;

    @ApiModelProperty("推凭状态：0未推凭，1已推凭")
    private Integer inferenceState;

    @ApiModelProperty("收费开始时间")
    private LocalDateTime chargeStartTime;

    @ApiModelProperty("收费结束时间")
    private LocalDateTime chargeEndTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("操作人id")
    private String operator;

    @ApiModelProperty("操作人名称")
    private String operatorName;

    @ApiModelProperty("修改时间")
    private LocalDateTime gmtModify;

}
