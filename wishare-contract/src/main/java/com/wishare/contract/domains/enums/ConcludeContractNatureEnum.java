package com.wishare.contract.domains.enums;

import com.wishare.contract.domains.consts.ContractNatureEnum;
import lombok.Getter;

/**
 * @author hhb
 * @describe 合同【推送状态】枚举
 * @date 2025/5/9 10:42
 */
@Getter
public enum ConcludeContractNatureEnum {

    FAIL(0, "失败"),
    SUCCESS(1, "成功"),
    TO_PUSH(2, "待推送"),
    ;

    private Integer code;

    private String des;

    public static ConcludeContractNatureEnum getEnum(Integer code) {
        ConcludeContractNatureEnum e = null;
        for (ConcludeContractNatureEnum ee : ConcludeContractNatureEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    ConcludeContractNatureEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }
}
