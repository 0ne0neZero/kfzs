package com.wishare.contract.domains.enums.revision;

/**
 * @description: 保证金退款状态枚举类
 * @author: zhangfuyu
 * @Date: 2023/7/28/11:29
 */
public enum BondRefundStatusEnum {

    未退款(1,"未退款"),
    部分退款(2,"部分退款"),
    已退款(3,"已退款");

    private Integer code;
    private String name;

    BondRefundStatusEnum(Integer code, String name) {
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
        for (BondRefundStatusEnum value : BondRefundStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }
}
