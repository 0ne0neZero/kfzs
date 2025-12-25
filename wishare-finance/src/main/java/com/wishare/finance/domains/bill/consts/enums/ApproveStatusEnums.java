package com.wishare.finance.domains.bill.consts.enums;

/**
 * 支付申请单审批状态
 * 审批状态 1:草稿 2:待审批 3:完成审批 4:OA审批中 5:OA审批通过 6:OA完结
 */
public enum ApproveStatusEnums {

    草稿(1, "草稿"),
    待审批(2, "待审批"),
    完成审批(3, "完成审批"),
    OA审批中(4, "OA审批中"),
    OA审批通过(5, "OA审批通过"),
    OA完结(6, "OA完结");

    private int code;
    private String value;

    ApproveStatusEnums(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static ApproveStatusEnums valueOfByCode(int code){
        for (ApproveStatusEnums value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        return null;
    }
    public boolean equalsByCode(int code){
        return code == this.code;
    }
}
