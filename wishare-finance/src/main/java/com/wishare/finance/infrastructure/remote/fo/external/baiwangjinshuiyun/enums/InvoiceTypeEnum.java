package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 发票类型
 * @author dongpeng
 * @date 2023/10/27 15:49
 */
@Getter
@AllArgsConstructor
public enum InvoiceTypeEnum {

    增值税专用发票("01", "增值税专用发票"),
    普通发票("02", "普通发票"),
    ;

    private String value;

    private String desc;
}
