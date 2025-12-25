package com.wishare.finance.domains.invoicereceipt.consts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Linitly
 * @date: 2023/8/8 9:13
 * @descrption: 缴费之后开票标识
 */
@Getter
@AllArgsConstructor
public enum AfterPaymentEnum {


    直接开具(0, "直接开具"),            // 直接开票的需要更新收据的缴费日期
    缴费后开具(1, "缴费后开具"),
    ;

    private Integer code;

    private String desc;
}
