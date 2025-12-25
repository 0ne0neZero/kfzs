package com.wishare.finance.domains.voucher.support.zhongjiao.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 1:草稿 2:待审批 3:完成审批
 */
public enum ApproveStatusEnum {

    草稿(1, "草稿"),
    待审批(2, "待审批"),
    初审中(3, "初审中"),
    完成初审(4, "完成初审"),
    审批驳回(5, "审批驳回"),
    审批通过(6, "审批通过"),
    OA审批中(7, "OA侧的审批中");
    private int code;
    private String value;

    ApproveStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static ApproveStatusEnum valueOfByCode(int code){
        for (ApproveStatusEnum value : values()) {
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
