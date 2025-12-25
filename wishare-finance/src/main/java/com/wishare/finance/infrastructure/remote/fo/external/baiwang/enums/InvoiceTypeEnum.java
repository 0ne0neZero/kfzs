package com.wishare.finance.infrastructure.remote.fo.external.baiwang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Linitly
 * @date: 2023/6/19 15:26
 * @descrption: 百望系统发票类型
 */
@Getter
@AllArgsConstructor
public enum InvoiceTypeEnum {

    蓝票("0", "正数发票（蓝票）"),
    红票("1", "负数发票（红票）"),
    ;

    private String code;

    private String name;
}
