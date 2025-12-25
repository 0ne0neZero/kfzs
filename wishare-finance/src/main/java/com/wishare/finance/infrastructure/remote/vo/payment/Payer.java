package com.wishare.finance.infrastructure.remote.vo.payment;

import lombok.Getter;
import lombok.Setter;

/**
 * 付款人信息
 *
 * @Author dxclay
 * @Date 2022/12/1
 * @Version 1.0
 */
@Getter
@Setter
public class Payer {

    /**
     * 付款人id
     */
    private String payerId;
    /**
     * 渠道付款人id
     */
    private String channelPayerId;
    /**
     * 付款人名称
     */
    private String payerName;
    /**
     * 付款人手机号
     */
    private String phone;

    /**
     * 银行类型, 银企直连和银行转账必填,银行联行号填了可不填
     */
    private String backType;

    /**
     * 银行联行号, 银企直连和银行转账必填
     */
    private String bankNo;

    /**
     * 付款人收款账号，银行转账必传
     */
    private String payerAccount;

    /**
     * 付方客户号 付款账号所属企业号
     */
    private String payNumber;

}
