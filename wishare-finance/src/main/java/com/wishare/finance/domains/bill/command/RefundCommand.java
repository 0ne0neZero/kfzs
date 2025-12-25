package com.wishare.finance.domains.bill.command;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 账单退款命令
 * @author xujian
 * @date 2022/9/8
 * @Description:
 */
@Getter
@Setter
public class RefundCommand {

    /**账单id*/
    private Long billId;
    /**项目id*/
    private String supCpUnitId;
    /**退款金额*/
    private Long refundAmount;

   /**结算渠道 :  ALIPAY：支付宝 WECHATPAY:微信支付，CASH:现金，POS: POS机，UNIONPAY:银联，SWIPE: 刷卡， BANK:银行汇款，CARRYOVER:结转，OTHER: 其他*/
    private String refundChannel;

   /**退款方式(0线上，1线下)*/
    private Integer refundWay;

   /**外部退款编号（支付宝退款单号，银行流水号等）*/
    private String outRefundNo;

   /**收费对象类型*/
    private Integer refunderType;

   /**退款人ID*/
    private String refunderId;

    /**退款人名称",required = true)
    @NotBlank(message = "退款人名称不能为空*/
    private String refunderName;

   /**收费开始时间*/
    private LocalDateTime chargeStartTime;

   /**收费结束时间*/
    private LocalDateTime chargeEndTime;

   /**备注*/
    private String remark;
    
}
