package com.wishare.finance.domains.bill.consts.enums;


import cn.hutool.core.util.ObjectUtil;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 账单状态（0，1，2）
 * @Author dxclay
 * @Date 2022/9/13
 * @Version 1.0
 */
public enum BillStateEnum {

    正常(0, "正常"),
    冻结(1, "已冻结"),
    作废(2, "已作废"),
    ;

    private int code;
    private String value;

    BillStateEnum(int code, String value) {
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

    public static BillStateEnum valueOfByCode(int code){
        for (BillStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_INVOICE_STATE_NOT_SUPPORT.msg());
    }


    public static String codeToName(Integer code){
        if (ObjectUtil.isNull(code)){
            return "";
        }
        for (BillStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value.value;
            }
        }
        return "";
    }

}
