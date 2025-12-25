package com.wishare.contract.domains.enums.revision;

import lombok.Getter;

/**
 * @author hhb
 * @describe
 * @date 2025/11/7 17:14
 */
@Getter
public enum CostApportionTypeEnum {
    可用金额(1,"可用金额","合约规划当前尚可使用金额"),
    累计分摊金额(2,"累计分摊金额","合约规划当前尚可使用金额"),
    分摊金额(3,"分摊金额","当前单据各月占用金额"),
    分摊后剩余(4,"分摊后剩余","当前单据占用后该费项各月剩余金额"),
    扣款金额(5,"扣款金额","当前结算单据中扣款分摊各月的金额"),
    结算差额(6,"结算差额","等于分摊金额-累计分摊金额-扣款金额");

    private Integer code;
    private String name;
    private String prompt;

    CostApportionTypeEnum(Integer code, String name, String prompt) {
        this.name = name;
        this.code = code;
        this.prompt = prompt;
    }


    public static String parseName(Integer code) {
        for (CostApportionTypeEnum value : CostApportionTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }
    public static CostApportionTypeEnum getEnum(Integer code) {
        for (CostApportionTypeEnum value : CostApportionTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
