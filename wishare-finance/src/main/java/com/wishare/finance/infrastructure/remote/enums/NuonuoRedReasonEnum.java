package com.wishare.finance.infrastructure.remote.enums;

/**
 * 诺诺冲红原因
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/14
 */
public enum NuonuoRedReasonEnum {

    销货退回("1", "销货退回"),
    开票有误("2", "开票有误"),
    服务中止("3", "服务中止"),
    销售折让("4", "销售折让");

    private String code;

    private String des;


    public static NuonuoRedReasonEnum valueOfByCode(String code) {
        NuonuoRedReasonEnum e = null;
        for (NuonuoRedReasonEnum ee : NuonuoRedReasonEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    NuonuoRedReasonEnum(String code, String des) {
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
