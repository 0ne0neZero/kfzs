package com.wishare.finance.apps.model.bill.vo;

import com.wishare.finance.domains.bill.consts.enums.PaySourceEnum;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author xujian
 * @date 2022/10/10
 * @Description: 结算信息
 */
@Getter
@Setter
public class BillSettleV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("账单id")
    private Long billId;

    @ApiModelProperty("结算编号")
    private String settleNo;

    @ApiModelProperty("结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）")
    private String settleChannel;

    @ApiModelProperty("结算方式(0线上，1线下)")
    private Integer settleWay;

    @ApiModelProperty("结算方式名称")
    private String settleWayString;

    @ApiModelProperty("外部结算编号（支付宝单号，银行流水号等）")
    private String outSettleNo;

    @ApiModelProperty("结转账单id")
    private Long carriedBillId;

    @ApiModelProperty("结算金额（单位：分）")
    private Long settleAmount;

    @ApiModelProperty("收款金额（单位：分）(合单支付时，收款金额 > 结算金额)")
    private Long payAmount;

    @ApiModelProperty("减免金额（单位：分）")
    private Long discountAmount;

    @ApiModelProperty("票据id")
    private Long invoiceReceiptId;

    @ApiModelProperty("减免说明列表")
    private String discounts;

    @ApiModelProperty("结算时间")
    private LocalDateTime settleTime;

    @ApiModelProperty("收费开始时间")
    private LocalDateTime chargeStartTime;

    @ApiModelProperty("收费结束时间")
    private LocalDateTime chargeEndTime;

    @ApiModelProperty("收费对象类型")
    private Integer payerType;

    @ApiModelProperty("付款方id")
    private String payerId;

    @ApiModelProperty("付款方名称")
    private String payerName;

    @ApiModelProperty("收款方ID")
    private String payeeId;

    @ApiModelProperty("收款方名称")
    private String payeeName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("交易流水号")
    private String tradeNo;

    @ApiModelProperty("支付来源:0-PC管理后台 1-业主端app 2-业主端小程序 3-物管端app 4-物管端小程序 " +
            "10-亿家生活app，11-亿家生活公众号，12-亿家生活小程序，13-亿管家APP，14-亿管家智能POS机，15-亿家生活公众号物管端")
    private Integer paySource;

    @ApiModelProperty("是否有效 0有效 1失效")
    private Integer available;

    public String getSettleChannel() {
        if (TenantUtil.bf64() && PaySourceEnum.业主端app.getCode().equals(paySource)){
            return PaySourceEnum.业主端app.getValue();
        }
        return settleChannel;
    }
}
