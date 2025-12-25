package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 跳收明细中的状态
 * @Author zhenghui
 * @Date 2023/7/14
 * @Version 1.0
 */
public enum JumpStateEnum {

    正常(0, "正常"),
    跳收审核中(1, "跳收审核中"),
    跳收审核通过(2, "跳收审核通过"),
    跳收拒绝(3, "跳收拒绝"),
    ;

    private int code;
    private String value;

    JumpStateEnum(int code, String value) {
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



}
