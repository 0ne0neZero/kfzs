package com.wishare.finance.domains.invoicereceipt.consts.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author xujian
 * @date 2022/9/21
 * @Description: 发票抬头类型：1 个人 2 企业
 */
public enum InvoiceTitleTypeEnum {

    个人(1, "个人"),
    企业(2, "企业"),


    ;

    private Integer code;

    private String des;

    public static InvoiceTitleTypeEnum valueOfByCode(Integer code) {
        InvoiceTitleTypeEnum e = null;
        for (InvoiceTitleTypeEnum ee : InvoiceTitleTypeEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    public static Integer getCodeByDes(String des) {
        Integer code = null;
        for (InvoiceTitleTypeEnum ee : InvoiceTitleTypeEnum.values()) {
            if (StringUtils.equals(ee.getDes(), des)) {
                code = ee.getCode();
                break;
            }
        }
        return code;
    }

    InvoiceTitleTypeEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
