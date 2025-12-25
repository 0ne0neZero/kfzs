package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 触发事件类型
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherEventTypeEnum {

    应收计提(1, "应收计提"),
    收款结算(2, "收款结算"),
    预收应收核销(3, "预收应收核销"),
    账单调整(4, "账单调整"),
    账单开票(5, "账单开票"),
//    冲销作废(6, "冲销作废"),
    作废(6, "作废"),
    冲销(18, "冲销"),
    预收应收核销冲销(20, "预收应收核销冲销"),
    未认领暂收款(7, "未认领暂收款"),
    应付计提(8, "应付计提"),
    付款结算(9, "付款结算"),
    收票结算(10, "收票结算"),
    手动生成(11, "手动生成"),
    银行到账(12, "银行到账"),
    收入退款(13, "收入退款"),
    /**
     * 流水认领后
     */
    结算认领(14, "结算认领"),
    账单减免(15, "账单减免"),
    应收计提冲回(16, "应收计提冲回"),
    应付计提冲回(17, "应付计提冲回"),
    款项结转(19, "款项结转"),
    ;

    private int code;
    private String value;

    VoucherEventTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherEventTypeEnum valueOfByCode(int code){
        for (VoucherEventTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_EVENT_TYPE_NOT_SUPPORT.msg());
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
