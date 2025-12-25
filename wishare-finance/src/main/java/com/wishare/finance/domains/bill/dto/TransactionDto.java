package com.wishare.finance.domains.bill.dto;

import com.wishare.finance.domains.bill.entity.Payee;
import com.wishare.finance.domains.bill.entity.Payer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 交易通知信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/6
 */
@Getter
@Setter
public class TransactionDto {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "交易订单号")
    private String transactionNo;

    @ApiModelProperty(value = "业务系统交易单号")
    private String bizTransactionNo;

    @ApiModelProperty(value = "交易标题")
    private String transactionTitle;

    @ApiModelProperty(value = "支付渠道订单号（如支付宝，微信订单号）")
    private String payChannelNo;

    @ApiModelProperty(value = "结算渠道	   ALIPAY：支付宝，	   WECHATPAY:微信支付，	   CASH:现金，	   POS: POS机，	   UNIONPAY:银联，	   SWIPE: 刷卡，	   BANK:银行汇款，	   CARRYOVER:结转，	   CHEQUE: 支票	   OTHER: 其他	   COMPLEX：组合支付")
    private String payChannel;

    @ApiModelProperty(value = "结算方式(0线上，1线下)")
    private Integer payWay;

    @ApiModelProperty(value = "收款人信息")
    private Payee payee;

    @ApiModelProperty(value = "付款人信息")
    private Payer payer;

    @ApiModelProperty(value = "系统来源编码 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;

    @ApiModelProperty(value = "交易金额")
    private Long amount;

    @ApiModelProperty(value = "交易类型：1收款，2付款，3退款，4账单支付")
    private Integer transactionType;

    @ApiModelProperty(value = "交易状态: 0待交易, 1交易中, 2交易成功, 3交易失败, 4交易已取消, 5推凭中, 6交易已关闭")
    private Integer transactState;

    @ApiModelProperty(value = "扩展参数")
    private String attachParams;

    @ApiModelProperty(value = "交易账单信息")
    private TransactionBillDto billInfo;

    @ApiModelProperty(value = "凭证信息")
    private VoucherDto voucher;

}
