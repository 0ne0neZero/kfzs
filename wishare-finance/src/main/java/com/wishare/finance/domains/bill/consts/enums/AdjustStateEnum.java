package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 调整明细中的状态
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum AdjustStateEnum {

    待审核(0, "未调整"),
    审核中(1, "审核中"),
    已生效(2, "已生效"),
    未生效(3, "未生效"),
    ;

    private int code;
    private String value;

    AdjustStateEnum(int code, String value) {
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
     * 根据审核状态获取调整状态
     * @param approveState
     * @return
     */
    public static AdjustStateEnum valueOfApproveState(BillApproveStateEnum approveState){
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
