package com.wishare.finance.infrastructure.remote.fo.external.baiwang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Linitly
 * @date: 2023/6/19 15:26
 * @descrption: 百望系统发票类型代码
 */
@Getter
@AllArgsConstructor
public enum InvoiceTypeCodeEnum {

    增值税专用发票("004", "增值税专用发票"),
    增值税普通发票("007", "增值税普通发票"),
    增值税电子发票("026", "增值税电子发票"),
    增值税卷式发票("025", "增值税卷式发票"),
    增值税电子专用发票("028", "增值税电子专用发票"),
    全电发票_增值税专用发票("01", "全电发票(增值税专用发票)"),
    全电发票_普通发票("02", "全电发票(普通发票)"),
    ;

    private String code;

    private String name;
}
