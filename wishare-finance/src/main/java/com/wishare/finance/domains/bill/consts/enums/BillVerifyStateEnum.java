package com.wishare.finance.domains.bill.consts.enums;


/**
 * 核销状态
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum BillVerifyStateEnum {

    未核销(0, "未核销"),
    已核销(1, "已核销"),
    ;

    private int code;
    private String value;

    BillVerifyStateEnum(int code, String value) {
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
