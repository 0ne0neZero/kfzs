package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 计费方式 (1:单价*面积/月，2:单价/月，3:单价*面积/天，4:单价/天)
 * @Author dxclay
 * @Date 2022/9/13
 * @Version 1.0
 */
public enum BillChargeMethodEnum {

    单价面积月(1, "单价*面积/月"),
    单价月(2, "单价/月"),
    单价面积天(3, "单价*面积/天"),
    单价天(4, "单价/天"),
    ;

    private int code;
    private String value;

    BillChargeMethodEnum(int code, String value) {
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

    public static BillChargeMethodEnum valueOfByCode(int code){
        for (BillChargeMethodEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_CHARGE_METHOD_NOT_SUPPORT.msg());
    }

}
