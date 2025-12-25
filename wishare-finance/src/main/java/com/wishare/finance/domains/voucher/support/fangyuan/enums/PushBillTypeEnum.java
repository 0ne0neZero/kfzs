package com.wishare.finance.domains.voucher.support.fangyuan.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

public enum PushBillTypeEnum {

    待推送(1, "待推送"),
    已推送(2, "已推送"),
    推送失败(3, "推送失败"),
    // 原来这两个写反了，已处理
    已驳回(5 , "已驳回"),
    推送中(4, "推送中"),

    单据驳回(6,"单据驳回"),
    制单成功(7,"制单成功"),
    制单失败(8,"制单失败"),
    单据审核完成(9,"单据审核完成"),
    凭证生成(10,"凭证生成");

    private int code;
    private String value;

    PushBillTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static PushBillTypeEnum valueOfByCode(int code){
        for (PushBillTypeEnum value : values()) {
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

    public static boolean canPushAgain(Integer code){
        return code.equals(推送失败.code) || code.equals(制单失败.code) || code.equals(单据驳回.code);
    }
}
