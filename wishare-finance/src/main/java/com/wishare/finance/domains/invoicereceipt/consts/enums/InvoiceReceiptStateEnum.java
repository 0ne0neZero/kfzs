package com.wishare.finance.domains.invoicereceipt.consts.enums;

/**
 * @author xujian
 * @date 2022/9/21
 * @Description: 开票状态：0 未开票 1 开票中 2 开票成功 3 开票失败 4 红冲中 5 已红冲 6 已作废
 */
public enum InvoiceReceiptStateEnum {

    未开票(0, "未开票"),
    开票中(1, "开票中"),
    开票成功(2, "开票成功"),
    开票失败(3, "开票失败"),
    红冲中(4, "红冲中"),
    已红冲(5, "已红冲"),
    已作废(6, "已作废"),

    开票成功签章失败(7, "开票成功签章失败"),

    部分红冲(8,"部分红冲"),
    /**
     * 红字确认单申请中
     * 在前端页面显示为红冲申请中
     */
    红字确认单申请中(9, "红字确认单申请中");
    ;

    private Integer code;

    private String des;

    public static InvoiceReceiptStateEnum valueOfByCode(Integer code) {
        InvoiceReceiptStateEnum e = null;
        for (InvoiceReceiptStateEnum ee : InvoiceReceiptStateEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    public boolean equalsByCode(int code){
        return this.code == code;
    }

    InvoiceReceiptStateEnum(Integer code, String des) {
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
