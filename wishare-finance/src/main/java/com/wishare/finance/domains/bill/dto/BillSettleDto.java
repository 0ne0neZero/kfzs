package com.wishare.finance.domains.bill.dto;

import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.starter.Global;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/9/6
 * @Description: 结算明细
 */
@Getter
@Setter
public class BillSettleDto {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    /**
     * 主键id
     */
    private Long id;
    /**
     * 收款单id
     */
    private Long gatherBillId;
    /**
     * 账单id
     */
    private Long billId;
    /**
     * 结算编号
     */
    private String settleNo;
    /**
     * 结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）
     */
    private String settleChannel;
    /**
     * 结算方式(0线上，1线下)
     */
    private Integer settleWay;
    /**
     * 结算方式名称
     */
    private String settleWayString;
    /**
     * 外部结算编号（支付宝单号，银行流水号等）
     */
    private String outSettleNo;
    /**
     * 结转账单id
     */
    private Long carriedBillId;
    /**
     * 结算金额（单位：分）
     */
    private Long settleAmount;
    /**
     * 收款金额（单位：分）(合单支付时，收款金额 > 结算金额)
     */
    private Long payAmount;
    /**
     * 减免金额（单位：分）
     */
    private Long discountAmount;
    /**
     * 减免说明列表
     [{
     "outDiscountId": "外部减免id",
     "discountType": "减免类型",
     "amount": 100, //减免金额（分）
     "discription": "减免说明"
     }]
     */
    private String discounts;
    /**
     * 结算时间
     */
    private LocalDateTime settleTime;
    /**
     * 收费开始时间
     */
    private LocalDateTime chargeStartTime;
    /**
     * 收费结束时间
     */
    private LocalDateTime chargeEndTime;
    /**
     * 收费对象类型
     */
    private Integer payerType;
    /**
     * 付款方id
     */
    private String payerId;
    /**
     * 付款方名称
     */
    private String payerName;

    /**
     * 收款方id
     */
    private String payeeId;

    /**
     * 收款方名称
     */
    private String payeeName;

    /**
     * 备注
     */
    private String remark;
    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    private Integer deleted;

    /**
     * 创建人ID
     */
    private String creator;

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    private String operator;

    /**
     * 修改人姓名
     */
    private String operatorName;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModify;

    /**
     * 交易流水号
     */
    private String tradeNo;


    /**
     * 支付来源:0-PC管理后台 1-业主端app 2-业主端小程序 3-物管端app 4-物管端小程序 " +
     *             "10-亿家生活app，11-亿家生活公众号，12-亿家生活小程序，13-亿管家APP，14-亿管家智能POS机，15-亿家生活公众号物管端
     */
    private Integer paySource;

    /**
     * 是否有效 0有效 1失效
     */
    private Integer available;


    public BillSettleDto generalBillSettleDto(GatherDetail gatherDetail) {
        BillSettleDto billSettleDto = Global.mapperFacade.map(gatherDetail, BillSettleDto.class);
        billSettleDto.setSettleChannel(gatherDetail.getPayChannel());
        billSettleDto.setSettleAmount(gatherDetail.getRecPayAmount());
        billSettleDto.setPayAmount(gatherDetail.getPayAmount());
        billSettleDto.setSettleWay(gatherDetail.getPayWay());
        billSettleDto.setSettleTime(gatherDetail.getPayTime());
        billSettleDto.setBillId(gatherDetail.getRecBillId());
        billSettleDto.setSettleNo(gatherDetail.getGatherBillNo());
        billSettleDto.setGatherBillId(gatherDetail.getGatherBillId());
        billSettleDto.setAvailable(gatherDetail.getAvailable());
        return billSettleDto;
    }
}
