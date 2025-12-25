package com.wishare.finance.apps.model.reconciliation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ApiModel("分页获取支付流水认领记录返回数据")
public class ChannelFlowClaimRecordPageV {

    @ApiModelProperty(value = "交易流水号")
    private String tradeNo;


    /**
     * reconciliationDetailId
     */
    @ApiModelProperty(value = "reconciliationDetailId")
    private String reconciliationDetailId;

    @ApiModelProperty(value = "收款单号")
    private String billNo;

    @ApiModelProperty(value = "收款单号数组")
    private List<String> billNoList;

    @ApiModelProperty(value = "对账时间")
    private LocalDateTime reconcileTime;

    @ApiModelProperty(value = "交易时间")
    private String payTime;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty(value = "项目ID")
    private String communityId;

    @ApiModelProperty(value = "项目名称")
    private String communityName;

    @ApiModelProperty(value = "交易金额")
    private BigDecimal channelTradeAmount;

    @ApiModelProperty(value = "手续费")
    private BigDecimal commission;

    @ApiModelProperty(value = "对账状态 2 已对账")
    private Integer mcReconciliationState;

    @ApiModelProperty(value = "认领状态  2 已认领  其他未认领")
    private Integer flowState;

    @ApiModelProperty(value = "支付渠道")
    private String payWay;

    @ApiModelProperty(value = "交易方式")
    private String payChannel;

    @ApiModelProperty(value = "账单来源")
    private Integer sysSource;

    @ApiModelProperty(value = "商户号")
    private String mchNo;
}
