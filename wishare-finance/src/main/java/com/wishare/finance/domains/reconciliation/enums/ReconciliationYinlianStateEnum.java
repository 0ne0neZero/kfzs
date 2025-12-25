package com.wishare.finance.domains.reconciliation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author szh
 * @date 2024/4/1 13:55
 */

@Getter
@AllArgsConstructor
public enum ReconciliationYinlianStateEnum {
    NO(0, "未核对"),
    YES(1, "已核对"),

    ;
    private int code;
    private String value;

}
