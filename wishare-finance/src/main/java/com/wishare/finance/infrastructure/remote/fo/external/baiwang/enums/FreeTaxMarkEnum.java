package com.wishare.finance.infrastructure.remote.fo.external.baiwang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Linitly
 * @date: 2023/6/19 15:55
 * @descrption: 零税率标识
 */
@Getter
@AllArgsConstructor
public enum FreeTaxMarkEnum {


    出口免税和其他免税优惠政策("1", "出口免税和其他免税优惠政策"),
    不征增值税("2", "不征增值税"),
    普通零税率("3", "普通零税率"),
    ;

    private String code;

    private String name;
}
