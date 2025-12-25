package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 开票状态
 * @Author dxclay
 * @Date 2022/9/13
 * @Version 1.0
 */
public enum BillInvoiceStateEnum {

    未开票(0, "未开票"),
    开票中(1, "开票中"),
    部分开票(2, "部分开票"),
    已开票(3, "已开票"),
    ;

    private int code;
    private String value;

    BillInvoiceStateEnum(int code, String value) {
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

    public static BillInvoiceStateEnum valueOfByCode(int code){
        for (BillInvoiceStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_INVOICE_STATE_NOT_SUPPORT.msg());
    }

}
