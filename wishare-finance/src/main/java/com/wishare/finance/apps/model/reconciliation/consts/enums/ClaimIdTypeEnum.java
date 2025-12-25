package com.wishare.finance.apps.model.reconciliation.consts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum ClaimIdTypeEnum {

    蓝票(1, "蓝票"),
    红票(2, "红票"),
    收款单(3, "收款单"),
    退款单(4, "退款单"),
    ;

    private Integer code;

    private String des;

    public static ClaimIdTypeEnum valueOfByCode(Integer code) {
        ClaimIdTypeEnum e = null;
        for (ClaimIdTypeEnum ee : ClaimIdTypeEnum.values()) {
            if (Objects.equals(ee.getCode(), code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }
}
