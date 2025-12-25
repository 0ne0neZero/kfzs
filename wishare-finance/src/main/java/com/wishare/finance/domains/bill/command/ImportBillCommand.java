package com.wishare.finance.domains.bill.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 账单导入命令
 *
 * @Author dxclay
 * @Date 2022/10/26
 * @Version 1.0
 */
@Getter
@Setter
public class ImportBillCommand<B> {

    /**
     * 账单实体
     */
    private B bill;

    /**
     * 结算金额
     */
    private Long settleAmount;

    /**
     * 结算方式(0线上，1线下)
     */
    private Integer settleWay;

    /**
     * 账单已缴时间
     */
    private LocalDateTime chargeTime;

    /**
     * 收费开始时间
     */
    private LocalDateTime chargeStartTime;

    /**
     * 收费结束时间
     */
    private LocalDateTime chargeEndTime;

    /**
     * 结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）
     */
    private String settleChannel;

    /**
     * 行号标识
     */
    private Integer index;

    /**
     * 导入是否成功
     */
    private boolean success = false;

    @ApiModelProperty("付款方手机号")
    private String payerPhone;

    @ApiModelProperty("收款方手机号")
    private String payeePhone;

}
