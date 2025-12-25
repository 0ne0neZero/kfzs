package com.wishare.finance.domains.fangyuan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.finance.domains.bill.entity.PayInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ParkRenewInfoPageDto {
    @ApiModelProperty("车场返回编码")
    private String resultCode;

    @ApiModelProperty("车场返回信息")
    private String resultMsg;

    @ApiModelProperty("账单id")
    private String id;

    @ApiModelProperty("房间id")
    private Long roomId;

    @ApiModelProperty("房号名称")
    private String roomName;

    @ApiModelProperty("账单编号")
    private String billNo;

    @ApiModelProperty("项目ID")
    private String communityId;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("费项id")
    private Long chargeItemId;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty("收费对象")
    private String customerName;

    @ApiModelProperty("账单类型")
    private Integer billType;

    @ApiModelProperty("账单金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("应收金额")
    private BigDecimal receivableAmount;

    @ApiModelProperty("结算金额")
    private BigDecimal settleAmount;

    @ApiModelProperty("实收金额")
    private BigDecimal actualPayAmount;

    @ApiModelProperty("账单开始时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private LocalDateTime endTime;

    @ApiModelProperty("应收日")
    private String receivableDate;

    @ApiModelProperty("结算状态（0未结算，1部分结算，2已结算）")
    private Integer settleState;

    @ApiModelProperty("结算时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private LocalDateTime payTime;

    @ApiModelProperty("结算方式字符串")
    private String PayInfosString;

    @ApiModelProperty("结算方式json")
    private List<PayInfo> payInfos;

    @ApiModelProperty("付款人姓名")
    private String payerName;

    @ApiModelProperty("付款人电话")
    private String payerPhone;

    @ApiModelProperty("单价")
    private BigDecimal unitPrice;
}

