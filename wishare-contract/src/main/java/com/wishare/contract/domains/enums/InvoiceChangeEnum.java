package com.wishare.contract.domains.enums;


/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2024/5/10/10:41
 */
public enum InvoiceChangeEnum {

    暂时性扣款(01,"暂时性扣款"),
    核销预付款(02,"核销预付款"),
    返还款(03,"返还款"),
    实际应支付(04,"实际应支付"),
    永久性扣款(05,"永久性扣款"),
    支付预付款(06,"支付预付款"),
            ;

    private Integer code;
    private String name;

    InvoiceChangeEnum(Integer code, String name) {
        this.name = name;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String parseName(Integer code) {
        for (InvoiceChangeEnum value : InvoiceChangeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

    public static Integer parseCode(String name) {
        for (InvoiceChangeEnum value : InvoiceChangeEnum.values()) {
            if (value.getName().equals(name)) {
                return value.getCode();
            }
        }
        return null;
    }

}
