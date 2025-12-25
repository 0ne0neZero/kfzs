package com.wishare.finance.domains.bill.consts.enums;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillApproveReverseStateEnum
 * @date 2023.12.06  19:15
 * @description 冲销记录状态枚举
 */
public enum BillApproveReverseStateEnum {

    待冲销(0,"待冲销"),
    冲销中(1,"冲销中"),
    已冲销(2,"已冲销"),
    已拒绝(3,"已拒绝"),
    ;

    private Integer code;

    private String des;

    BillApproveReverseStateEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }
}
