package com.wishare.finance.infrastructure.remote.fo.external.baiwang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Linitly
 * @date: 2023/6/19 15:33
 * @descrption:
 */
@Getter
@AllArgsConstructor
public enum TaxationMethodEnum {


    普通征税("0", "普通征税"),
    差额征税("2", "差额征税"),
    ;

    private String code;

    private String name;
}
