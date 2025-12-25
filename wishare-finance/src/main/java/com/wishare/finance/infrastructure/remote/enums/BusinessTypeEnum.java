package com.wishare.finance.infrastructure.remote.enums;

/**
 * 结算状态
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum BusinessTypeEnum {

    未启用(1, "未启用"),
    已启用(2, "已启用"),
    已停用(3, "已停用"),
    其他(99,"其他"),
    ;

    private int code;
    private String des;

    BusinessTypeEnum(int code, String des) {
        this.code = code;
        this.des = des;
    }

    public int getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

    public static BusinessTypeEnum valueOfByCode(Integer code) {
        BusinessTypeEnum e = null;
        for (BusinessTypeEnum ee : BusinessTypeEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    public static BusinessTypeEnum valueOfByCode(String des) {
        BusinessTypeEnum e = null;
        for (BusinessTypeEnum ee : BusinessTypeEnum.values()) {
            if (ee.getDes().equals(des)) {
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
