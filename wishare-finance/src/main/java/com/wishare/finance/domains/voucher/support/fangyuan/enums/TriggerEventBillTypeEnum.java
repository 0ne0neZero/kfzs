package com.wishare.finance.domains.voucher.support.fangyuan.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

public enum TriggerEventBillTypeEnum {

    对账核销(1, "对账核销"),
    未收款开票(2, "未收款开票"),
    欠费计提(3, "欠费计提"),
    坏账确认(4, "坏账确认"),
    收款结转(5,"收款结转"),
    预收结转(6,"预收结转"),
    款项调整(7,"款项调整"),
    预收结转按钮触发(8,"预收结转按钮触发")
    ;
    private int code;
    private String value;

    TriggerEventBillTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static TriggerEventBillTypeEnum valueOfByCode(int code){
        for (TriggerEventBillTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_BUSINESS_BILL_TYPE_NOT_SUPPORT.msg());
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
}
