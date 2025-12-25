package com.wishare.finance.infrastructure.remote.enums;

/**
 * 诺诺蓝票发票种类
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/14
 */
public enum NuonuoBlueInvoiceLineEnum {

    bs("bs", "电子发票(增值税专用发票)"),
    pc("pc", "电子发票(普通发票)"),
    es("es", "全电纸质发票(增值税专用发票)"),
    ec("ec", "全电纸质发票(普通发票)");

    private String code;

    private String des;


    public static NuonuoBlueInvoiceLineEnum valueOfByCode(String  code) {
        NuonuoBlueInvoiceLineEnum e = null;
        for (NuonuoBlueInvoiceLineEnum ee : NuonuoBlueInvoiceLineEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    NuonuoBlueInvoiceLineEnum(String code, String des) {
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
