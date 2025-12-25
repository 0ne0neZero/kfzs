package com.wishare.finance.infrastructure.remote.enums;

/**
 * @author xujian
 * @date 2022/12/1
 * @Description:
 */
public enum LingshuitongVoucherTypeEnum {

    取消凭证(0, "0", "取消凭证"),
    入账凭证(1, "1", "入账凭证"),
    ;
    private Integer code;

    private String codeStr;

    private String des;

    public static LingshuitongVoucherTypeEnum valueOfByCode(Integer code) {
        LingshuitongVoucherTypeEnum e = null;
        for (LingshuitongVoucherTypeEnum ee : LingshuitongVoucherTypeEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    LingshuitongVoucherTypeEnum(Integer code, String codeStr, String des) {
        this.code = code;
        this.codeStr = codeStr;
        this.des = des;
    }

    public Integer getCode() {
        return code;
    }

    public String getCodeStr() {
        return codeStr;
    }

    public String getDes() {
        return des;
    }
}
