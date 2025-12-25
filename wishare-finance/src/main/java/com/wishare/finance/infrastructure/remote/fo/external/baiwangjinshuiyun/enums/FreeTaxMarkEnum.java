package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 零税率标识
 * @author dongpeng
 * @date 2023/10/28 11:36
 */
@Getter
@AllArgsConstructor
public enum FreeTaxMarkEnum {

    正常税率("0", "正常税率"),
    免税("1", "免税"),
    不征税("2", "不征税"),
    普通零税率("3", "普通零税率"),
    ;

    private String code;

    private String name;
}
