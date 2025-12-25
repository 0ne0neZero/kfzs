package com.wishare.finance.domains.reconciliation.enums;

/**
 * 流水认领状态
 * @author yancao
 */
public enum FlowClaimStatusEnum {

    未认领(0, "未认领"),
    已认领(1, "已认领"),
    已挂起(2, "已挂起"),
    报账审核中(3, "报账审核中"),
    认领审核中(4, "认领审核中")
    ;

    private int code;
    private String value;

    FlowClaimStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static FlowClaimStatusEnum valueOfByCode(int code){
        for (FlowClaimStatusEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        return null;
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
