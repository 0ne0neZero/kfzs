package com.wishare.finance.infrastructure.remote.enums;

/**
 * 方圆传反参状态
 * @author dongpeng
 * @version 1.0
 * @since 2023/6/21
 */
public enum FangyuanQdReturnCodeEnum {

    开票成功("0000", "开票成功"),
    开票中("2021", "开票中"),
    开票失败("2022", "开票失败"),
    开票成功签章失败("2023", "开票成功签章失败"),
    重置签章待再次发起中("9999", "重置签章，待再次发起中");

    private String code;

    private String des;


    public static FangyuanQdReturnCodeEnum valueOfByCode(String code) {
        FangyuanQdReturnCodeEnum e = null;
        for (FangyuanQdReturnCodeEnum ee : FangyuanQdReturnCodeEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    FangyuanQdReturnCodeEnum(String code, String des) {
        this.code = code;
        this.des = des;
    }

    public String getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

}
