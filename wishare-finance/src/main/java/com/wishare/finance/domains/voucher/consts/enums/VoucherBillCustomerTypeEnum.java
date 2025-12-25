package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 业务单据收费对象类型
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherBillCustomerTypeEnum {

    业主(0, "业主"),
    开发商(99, "开发商"),
    租客(2, "租客"),
    客商(3, "客商"),
    法定单位(4, "法定单位"),
    房东(5, "房东"),
    代理人(6, "代理人"),
    家属(7, "家属"),
    伙伴(8, "伙伴"),
    临时客商(98, "临时客商"),
    // 往来单位(99, "开发商");
    ;

    private int code;
    private String value;

    VoucherBillCustomerTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherBillCustomerTypeEnum valueOfByCode(int code){
        for (VoucherBillCustomerTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_RULE_CUSTOMER_TYPE_NOT_SUPPORT.msg());
    }

    public static String getNameByCode(int code) {
        for (VoucherBillCustomerTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value.getValue();
            }
        }
        return "";
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

}
