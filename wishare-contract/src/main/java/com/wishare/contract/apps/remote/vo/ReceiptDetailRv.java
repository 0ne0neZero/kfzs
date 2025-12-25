package com.wishare.contract.apps.remote.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("收据详情")
public class ReceiptDetailRv {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("收据号")
    private String receiptNo;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("法定单位名称")
    private String statutoryBodyName;

    @ApiModelProperty("缴费方式")
    private String settleWayChannelStr;

    @ApiModelProperty("房号名称")
    private String roomName;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("开具时间")
    private LocalDateTime billingTime;

    @ApiModelProperty("开票人")
    private String clerk;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("票据明细")
    private List<InvoiceReceiptDetailRv> invoiceReceiptDetail;

    @ApiModelProperty("合计金额（单位：分）")
    private Long invoiceAmountTotal;

    @ApiModelProperty("合计金额（单位：分）大写中文")
    private String invoiceAmountTotalUppercase;

    @ApiModelProperty("其他金额（单位：分）")
    private List<OtherAmountRv> otherAmountDto;

    @ApiModelProperty("结算时间（年月日时分秒）")
    private List<LocalDateTime> settleTimeList;
}
