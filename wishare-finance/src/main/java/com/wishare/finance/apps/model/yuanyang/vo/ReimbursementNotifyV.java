package com.wishare.finance.apps.model.yuanyang.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wishare.finance.domains.bill.entity.Payee;
import com.wishare.finance.domains.bill.entity.Payer;
import com.wishare.finance.domains.bill.entity.Scene;
import com.wishare.finance.domains.bill.entity.TransactionBillOBV;
import com.wishare.finance.infrastructure.support.JSONTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 远洋通知参数
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/19
 */
@Getter
@Setter
public class ReimbursementNotifyV {

    @ApiModelProperty(value = "交易订单号")
    private String transactionNo;

    @ApiModelProperty(value = "业务系统交易单号")
    private String bizTransactionNo;

    @ApiModelProperty(value = "交易标题")
    private String transactionTitle;

    @ApiModelProperty(value = "支付渠道订单号")
    private String payChannelNo;

    @ApiModelProperty(value = "结算渠道	   ALIPAY：支付宝，	   WECHATPAY:微信支付，	   CASH:现金，	   POS: POS机，	   UNIONPAY:银联，	   SWIPE: 刷卡，	   BANK:银行汇款，	   CARRYOVER:结转，	   CHEQUE: 支票	   OTHER: 其他	   COMPLEX：组合支付")
    private String payChannel;

    @ApiModelProperty(value = "结算方式(0线上，1线下)")
    private Integer payWay;

    @ApiModelProperty(value = "收款人信息")
    private Payee payee;

    @ApiModelProperty(value = "付款人信息")
    private Payer payer;

    @ApiModelProperty(value = "扩展参数")
    private String attachParam;

    @ApiModelProperty(value = "支付参数")
    private String payParam;

    @ApiModelProperty(value = "交易金额")
    private Long amount;

    @ApiModelProperty(value = "推凭状态：0未推凭，1推凭中，2已推凭，3推凭失败")
    private Integer voucherState;

    @ApiModelProperty(value = "开票状态：0未开票，1开票中，2部分开票，3已开票，4开票异常")
    private Integer invoiceState;

    @ApiModelProperty(value = "交易类型：1收款，2付款，3退款，4账单支付")
    private Integer transactionType;

    @ApiModelProperty(value = "交易状态: 0待交易, 1交易中, 2交易成功, 3交易失败, 4交易已取消, 5推凭中, 6交易已关闭")
    private Integer transactState;

    @ApiModelProperty(value = "交易成功时间")
    private LocalDateTime successTime;

    @ApiModelProperty(value = "凭证信息")
    private ReimbursementVoucherV voucher;

}
