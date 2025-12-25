package com.wishare.finance.domains.pushbill.enums;

public enum VoucherInvoiceZJEnums {

    增值税专用发票("01", "增值税专用发票"),
    货物运输业增值税专用("02", "货物运输业增值税专用"),
    机动车销售统一发票("03", "机动车销售统一发票"),
    增值税普通发票("04", "增值税普通发票"),
    增值税电子普通发票("05", "增值税电子普通发票"),
//    增值税普通发票("06", "增值税普通发票"),
    增值税电子专用发票("07", "增值税电子专用发票"),
//    增值税电子普通发票("08", "增值税电子普通发票"),
    二手车销售统一发票("09", "二手车销售统一发票"),
    客票("10", "客票"),
    火车票("11", "火车票"),
    机票("12", "机票"),
    定额发票("13", "定额发票"),
    海关进口增值税专用缴("14", "海关进口增值税专用缴"),
    代扣代缴抵扣清单("15", "代扣代缴抵扣清单"),
    海关进口消费税专用缴("16", "海关进口消费税专用缴"),
    海关进口关税专用缴款("17", "海关进口关税专用缴款"),
    财政电子发票("18", "财政电子发票"),
    普通发票("19", "普通发票"),
    其他发票("99", "其他发票"),
    ;

    private String code;
    private String value;
    VoucherInvoiceZJEnums(String code, String value){
        this.code = code;
        this.value = value;
    }

    public static VoucherInvoiceZJEnums valueOfByCode(String code){
        for (VoucherInvoiceZJEnums value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(String code){
        return code.equals(this.code);
    }


}
