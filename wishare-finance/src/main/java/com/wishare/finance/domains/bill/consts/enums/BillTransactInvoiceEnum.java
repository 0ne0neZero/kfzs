package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 开票状态
 * @author dxclay
 * @since  2023/3/6
 * @version 1.0
 */
public enum BillTransactInvoiceEnum {

    未开票(0, "未开票"),
    开票中(1, "开票中"),
    部分开票(2, "部分开票"),
    已开票(3, "已开票"),
    开票异常(4, "开票异常"),
    ;

    private int code;
    private String value;

    BillTransactInvoiceEnum(int code, String value) {
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

    public static BillTransactInvoiceEnum valueOfByCode(int code){
        for (BillTransactInvoiceEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.PAYMENT_INVOICE_STATE_NOT_SUPPORT.msg());
    }

}
