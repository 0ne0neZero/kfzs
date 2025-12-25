package com.wishare.finance.domains.invoicereceipt.consts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Linitly
 * @date: 2023/8/8 9:13
 * @descrption: 票据模板是否启用电子签章
 */
@Getter
@AllArgsConstructor
public enum EnableElectSignEnum {


    启用(1, "启用"),
    不启用(0, "不启用"),
    ;

    private Integer code;

    private String desc;
}
