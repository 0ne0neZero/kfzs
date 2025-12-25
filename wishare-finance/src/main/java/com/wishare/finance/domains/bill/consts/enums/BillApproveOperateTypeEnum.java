package com.wishare.finance.domains.bill.consts.enums;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 审核操作类型
 * @author yancao
 */
public enum BillApproveOperateTypeEnum {

    生成审核(0, "生成审核"),
    调整(1, "调整"),
    作废(2, "作废"),
    结转(3, "结转"),

    退款(4,"退款"),
    冲销(5,"冲销"),
    减免(6,"减免"),
    收款单退款(7,"收款单退款"),
    跳收(8,"跳收"),
    收款单冲销(9,"收款单冲销"),
    ;

    private int code;
    private String value;

    BillApproveOperateTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static BillApproveOperateTypeEnum valueOfByCode(int code){
        for (BillApproveOperateTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_APPROVE_OPERATE_TYPE_NOT_SUPPORT.msg());
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

}
