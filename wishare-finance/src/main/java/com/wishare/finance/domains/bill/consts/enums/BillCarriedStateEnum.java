package com.wishare.finance.domains.bill.consts.enums;

/**
 * @author xujian
 * @date 2022/9/14
 * @Description: 结转状态：0 未结转，1 待结转，2 部分结转，3 已结转
 */
public enum BillCarriedStateEnum {

    未结转(0, "未结转"),
    待结转(1, "待结转"),
    部分结转(2, "部分结转"),
    已结转(3, "已结转"),
    ;

    private Integer code;
    private String value;

    BillCarriedStateEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
