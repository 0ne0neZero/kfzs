package com.wishare.finance.infrastructure.remote.fo.external.baiwang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Linitly
 * @date: 2023/6/19 15:55
 * @descrption: 含税标志
 */
@Getter
@AllArgsConstructor
public enum PriceTaxMarkEnum {


    不含税("0", "不含税"),
    含税("1", "含税"),
    ;

    private String code;

    private String name;
}
