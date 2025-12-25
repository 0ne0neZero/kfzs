package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 交易类型：1收款，2付款，3退款，4账单支付
 * @author dxclay
 * @since  2023/3/6
 * @version 1.0
 */
public enum BillTransactTypeEnum {

    收款(0, "收款"),
    付款(1, "付款"),
    退款(2, "退款"),
    账单支付(3, "账单支付"),
    ;

    private int code;
    private String value;

    BillTransactTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

    public static BillTransactTypeEnum valueOfByCode(int code){
        for (BillTransactTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.PAYMENT_TYPE_NOT_SUPPORT.msg());
    }

}
