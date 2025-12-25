package com.wishare.contract.domains.enums.revision.bond;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/27  9:21
 */
public enum BondActionTypeEnum {

    收取类收款("collectSQ","收款"),
    收取类退款("refundSQ","退款"),
    收取类收据("receiptSQ","收据"),
    收取类结转("transferSQ","结转"),

    缴纳类收款("collectJN","收款"),
    缴纳类付款("payJN","付款"),
    缴纳类收据("receiptJN","收据"),
    缴纳类结转("transferJN","结转"),
    转履约("toVolumeUpJN","转履约"),
    ;

    private String code;
    private String name;

    BondActionTypeEnum(String code, String name) {
        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String parseName(String code) {
        for (BondTypeEnum value : BondTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

}
