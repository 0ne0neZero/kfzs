package com.wishare.finance.infrastructure.remote.fo.external.baiwang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Linitly
 * @date: 2023/6/19 16:15
 * @descrption: 红字申请类型
 */
@Getter
@AllArgsConstructor
public enum ApplyTypeEnum {

    购方已抵扣("1_1", "购方已抵扣"),
    购方未抵扣("1_2", "购方未抵扣"),
    销方申请("2_0", "销方申请"),
    ;

    private String code;

    private String name;
}
