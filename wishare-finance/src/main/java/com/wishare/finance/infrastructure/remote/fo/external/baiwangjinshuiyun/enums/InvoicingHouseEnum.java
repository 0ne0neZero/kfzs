package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dongpeng
 * @date 2023/10/28 11:33
 * 发票行性质
 */
@Getter
@AllArgsConstructor
public enum InvoicingHouseEnum {

    正常行("0", "正常行"),
    折扣行("1", "折扣行(折扣行金额负)"),
    被折扣行("2", "被折扣行(被折扣行金额正)"),
    ;

    private String code;

    private String name;
}
