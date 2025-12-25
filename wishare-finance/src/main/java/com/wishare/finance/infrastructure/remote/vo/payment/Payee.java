package com.wishare.finance.infrastructure.remote.vo.payment;

import lombok.Getter;
import lombok.Setter;

/**
 * 收款人信息
 *
 * @Author dxclay
 * @Date 2022/12/1
 * @Version 1.0
 */
@Getter
@Setter
public class Payee {

    /**
     * 收款人id
     */
    private String payeeId;

    /**
     * 渠道收款人id
     */
    private String channelpayeeId;

    /**
     * 收款人名称
     */
    private String payeeName;

    /**
     * 收款人手机号
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
     * 收款人账号, 银企直连和银行转账必填
     */
    private String payeeAccount;

    /**
     * 收款人开户行, 银企直连和银行转账必填
     */
    private String payeeBank;

}
