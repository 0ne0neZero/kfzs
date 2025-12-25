package com.wishare.finance.infrastructure.remote.fo.external.baiwang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Linitly
 * @date: 2023/6/19 15:52
 * @descrption: 差额征税标签, 发票类型代码为01，02时且征税方式为2必填(全电发票且差额征税)
 */
@Getter
@AllArgsConstructor
public enum TaxationLabelEnum {

    差额开票("01", "差额开票"),
    全额开票("02", "全额开票"),
    ;

    private String code;

    private String name;
}
