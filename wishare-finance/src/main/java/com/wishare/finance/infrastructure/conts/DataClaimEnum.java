package com.wishare.finance.infrastructure.conts;

/**
 * 认领状态
 * @author yancao
 */
public enum DataClaimEnum {

    未认领(0, "未认领"),
    已认领(1, "已认领"),
    ;

    private int code;
    private String value;

    DataClaimEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static DataClaimEnum valueOfByCode(int code){
        for (DataClaimEnum value : values()) {
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
