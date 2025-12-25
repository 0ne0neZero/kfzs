package com.wishare.finance.infrastructure.remote.enums;

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

    public static BillSettleStateEnum valueOfByCode(Integer code) {
        BillSettleStateEnum e = null;
        for (BillSettleStateEnum ee : BillSettleStateEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

}
