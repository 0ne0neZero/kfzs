package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 征收方式
 * @author dongpeng
 * @date 2023/10/27 16:15
 */
@Getter
@AllArgsConstructor
public enum TaxationMethodEnum {


    普通征税("0", "普通征税"),
    减按征税("1", "减按征税"),
    差额征税("2", "差额征税"),
    全额征收("3", "全额征收"),
    ;

    private String code;

    private String name;
}
