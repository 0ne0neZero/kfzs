package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 结转状态
 * @Author dxclay
 * @Date 2022/9/13
 * @Version 1.0
 */
public enum CarryoverStateEnum {

    待审核(0, "待审核"),
    审核中(1, "审核中"),
    已生效(2, "已生效"),
    未生效(3, "未生效"),
    ;

    private int code;
    private String value;

    CarryoverStateEnum(int code, String value) {
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


    /**
     * 根据审核状态获取结转状态
     * @param approveState
     * @return
     */
    public static CarryoverStateEnum valueOfApproveState(BillApproveStateEnum approveState){
        switch (approveState){
            case 待审核:
                return 待审核;
            case 审核中:
                return 审核中;
            case 未通过:
                return 未生效;
            case 已审核:
                return 已生效;
        }
        throw BizException.throw400(ErrorMessage.BILL_APPROVE_STATE_NOT_SUPPORT.msg());
    }

}
