package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 调整类型
 * @Author dxclay
 * @Date 2022/8/24
 * @Version 1.0
 */
public enum BillAdjustTypeEnum {

    减免(1, "减免"),
    调高(2, "调高"),
    调低(3, "调低"),
    ;

    private int code;
    private String value;

    BillAdjustTypeEnum(int code, String value) {
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

    public static BillAdjustTypeEnum valueOfByCode(int code){
        for (BillAdjustTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_ADJUST_TYPE_NOT_SUPPORT.msg());
    }

}
