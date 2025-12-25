package com.wishare.finance.domains.reconciliation.enums;

/**
 * 流水认领状态
 * @author yancao
 */
public enum FlowClaimDetailStatusEnum {

    正常(0, "正常"),
    已撤销(1, "已撤销"),
    ;

    private int code;
    private String value;

    FlowClaimDetailStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static FlowClaimDetailStatusEnum valueOfByCode(int code){
        for (FlowClaimDetailStatusEnum value : values()) {
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
