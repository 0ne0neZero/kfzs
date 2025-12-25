package com.wishare.finance.domains.invoicereceipt.consts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvoiceDetailTypeEnum {

    蓝票明细(1, "蓝票明细"),
    部分红冲明细(2, "部分红冲明细"),
    剩余红冲明细(3, "剩余红冲明细"),
    ;

    private Integer code;

    private String des;
}
