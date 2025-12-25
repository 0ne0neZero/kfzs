package com.wishare.finance.infrastructure.remote.fo.external.baiwang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 百旺系统发票来源枚举
 * @author: Linitly
 * @date: 2023/8/22 15:48
 * @descrption: 发票来源枚举
 */
@Getter
@AllArgsConstructor
public enum BaiWangInvoiceSourceEnum {

    增值税发票管理系统("1", "增值税发票管理系统"),
    电子发票服务平台("2", "电子发票服务平台"),
    ;

    private String code;

    private String name;
}
