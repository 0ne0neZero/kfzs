package com.wishare.finance.domains.bill.consts.enums;

import cn.hutool.core.util.ObjectUtil;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 结算状态
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum BillSettleStateEnum {

    未结算(0, "未结算"),
    部分结算(1, "部分结算"),
    已结算(2, "已结算"),
    结算中(3, "结算中"),
    ;

    private int code;
    private String value;

    BillSettleStateEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static BillSettleStateEnum valueOfByCode(int code){
        for (BillSettleStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_SETTLE_STATE_NOT_SUPPORT.msg());
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }


    public static String codeToName(Integer code){
        if (ObjectUtil.isNull(code)){
            return "";
        }
        for (BillSettleStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value.getValue();
            }
        }
        return "";
    }
}
