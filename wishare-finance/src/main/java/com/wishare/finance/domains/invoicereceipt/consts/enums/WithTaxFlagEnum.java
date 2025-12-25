package com.wishare.finance.domains.invoicereceipt.consts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author linitly
 * @date 2023/7/22
 * @Description: 含税标志枚举
 */
@Getter
@AllArgsConstructor
public enum WithTaxFlagEnum {

    含税(1, "含税"),
    不含税(0,"不含税"),
    ;

    private Integer code;

    private String des;
}
