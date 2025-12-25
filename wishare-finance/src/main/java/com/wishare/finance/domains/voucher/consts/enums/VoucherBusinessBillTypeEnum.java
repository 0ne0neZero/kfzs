package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 业务单据类型
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherBusinessBillTypeEnum {

    应收单(1, "应收单"),
    预收单(2, "预收单"),
    临时单(3, "临时单"),
    应付单(4, "应付单"),
    退款单(5, "退款单"),
    付款单(6, "付款单"),
    收款单(7, "收款单"),
    发票(8, "发票"),
    收据(9, "收据"),
    银行流水(10, "银行流水"),
    清分流水(11, "清分流水"),

    账单调整(13, "账单调整"),
    账单冲销(14, "账单冲销"),
    ;

    private int code;
    private String value;

    VoucherBusinessBillTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherBusinessBillTypeEnum valueOfByCode(int code){
        for (VoucherBusinessBillTypeEnum value : values()) {
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
