package com.wishare.finance.apps.model.invoice.invoice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 补录发票认领分页
 */
@Getter
@Setter
@ApiModel("补录发票认领分页信息")
public class InvoiceClaimPageDto {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty(value = "交易时间")
    private LocalDateTime payTime;

    @ApiModelProperty("单据编号")
    private String billNo;

    @ApiModelProperty("单据ID")
    private String billId;

    @ApiModelProperty("单据来源")
    private Integer sysSource;

    @ApiModelProperty("单据类型")
    private String billType;

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty("成本中心id")
    private Long costCenterId;

    @ApiModelProperty("成中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "房号")
    private String roomName;

    @ApiModelProperty(value = "房号Id")
    private String roomId;

    @ApiModelProperty(value = "收费对象ID")
    private String payerId;

    @ApiModelProperty(value = "收费对象名称")
    private String payerName;

    @ApiModelProperty("结算方式")
    private String payChannel;

    @ApiModelProperty("结算方式名称")
    private String payChannelName;

    @ApiModelProperty("费项")
    private String chargeItemName;

    @ApiModelProperty(value = "结算金额")
    private Long settleAmount;

    @ApiModelProperty("账单开始时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime endTime;
}
