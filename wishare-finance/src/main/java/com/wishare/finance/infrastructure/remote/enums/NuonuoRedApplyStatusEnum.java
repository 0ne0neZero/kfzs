package com.wishare.finance.infrastructure.remote.enums;

/**
 *
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/15
 */
public enum NuonuoRedApplyStatusEnum {

    无需确认("01", "无需确认"),
    销方录入待购方确认("02", "销方录入待购方确认"),
    购方录入待销方确认("03", "购方录入待销方确认"),
    购销双方已确认("04", "购销双方已确认"),
    作废_销方录入购方否认("05", "作废（销方录入购方否认）"),
    作废_购方录入销方否认("06", "作废（购方录入销方否认）"),
    作废_超72小时未确认("07", "作废（超72小时未确认）"),
    作废_发起方已撤销("08", "作废（发起方已撤销）"),
    作废_确认后撤销("09", "作废（确认后撤销）"),
    申请中("15", "申请中"),
    申请失败("16", "申请失败");

    private String code;

    private String des;


    public static NuonuoRedApplyStatusEnum valueOfByCode(String code) {
        NuonuoRedApplyStatusEnum e = null;
        for (NuonuoRedApplyStatusEnum ee : NuonuoRedApplyStatusEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    NuonuoRedApplyStatusEnum(String code, String des) {
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
