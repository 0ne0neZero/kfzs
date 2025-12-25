package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("新增收款单入参（包含明细）")
public class AddGatherBillDetailF {

    @ApiModelProperty("收款类型 0应收，1预收")
    private Integer gatherType;

    @ApiModelProperty("应收单id")
    private Long recBillId;

    @ApiModelProperty("应收单编号")
    private String recBillNo;

    @ApiModelProperty("成本中心id")
    private Long costCenterId;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

    @ApiModelProperty("费项id")
    private Long chargeItemId;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty("收费组织id")
    private String cpOrgId;

    @ApiModelProperty("收费组织名称")
    private String cpOrgName;

    @ApiModelProperty("上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty("上级收费单元名称")
    private String supCpUnitName;

    @ApiModelProperty("收费单元id")
    private String cpUnitId;

    @ApiModelProperty("收费单元名称")
    private String cpUnitName;

    @ApiModelProperty("结算渠道\n" +
            "ALIPAY：支付宝，\n" +
            "WECHATPAY:微信支付，\n" +
            "CASH:现金，\n" +
            "POS: POS机，\n" +
            "UNIONPAY:银联，\n" +
            "SWIPE: 刷卡，\n" +
            "BANK:银行汇款，\n" +
            "CARRYOVER:结转，\n" +
            "CHEQUE: 支票\n" +
            "OTHER: 其他")
    private String payChannel;

    @ApiModelProperty("结算方式(0线上，1线下)")
    private Integer payWay;

    @ApiModelProperty("收款金额（单位：分）")
    private Long payAmount;

    @ApiModelProperty("收费对象类型")
    private Integer payerType;

    @ApiModelProperty("付款人id")
    private String payerId;

    @ApiModelProperty("付款人名称")
    private String payerName;

    @ApiModelProperty("付款方手机号")
    private String payerPhone;

    @ApiModelProperty("收款方id")
    private String payeeId;

    @ApiModelProperty("收款人名称")
    private String payeeName;

    @ApiModelProperty("收款方手机号")
    private String payeePhone;

    @ApiModelProperty("收款时间")
    private LocalDateTime payTime;

    @ApiModelProperty("收费开始时间")
    private LocalDateTime chargeStartTime;

    @ApiModelProperty("收费结束时间")
    private LocalDateTime chargeEndTime;

}
