package com.wishare.finance.infrastructure.remote.enums;

/**
 * 方圆传反参状态
 * @author dongpeng
 * @version 1.0
 * @since 2023/6/21
 */
public enum FangyuanReturnCodeEnum {

    开票成功("0", "开票成功"),
    开票成功但未签章("00", "开票成功但未签章"),
    单据不存在或未开票("5", "单据不存在或未开票"),
    单据已作废("7", "单据已作废"),
    开票失败("3", "开票失败");

    private String code;

    private String des;


    public static FangyuanReturnCodeEnum valueOfByCode(String code) {
        FangyuanReturnCodeEnum e = null;
        for (FangyuanReturnCodeEnum ee : FangyuanReturnCodeEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    FangyuanReturnCodeEnum(String code, String des) {
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
