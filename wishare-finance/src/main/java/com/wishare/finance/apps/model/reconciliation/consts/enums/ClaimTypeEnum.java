package com.wishare.finance.apps.model.reconciliation.consts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClaimTypeEnum {

    发票认领(1, "发票认领"),
    账单认领(2, "账单认领"),
    ;

    private Integer code;

    private String des;

    public static ClaimTypeEnum valueOfByCode(Integer code) {
        ClaimTypeEnum e = null;
        for (ClaimTypeEnum ee : ClaimTypeEnum.values()) {
            if (ee.getCode() == code) {
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
