package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据类型
 * @author dongpeng
 * @date 2023/10/28 16:02
 */
@Getter
@AllArgsConstructor
public enum DataTypeEnum {
    批发系统("1", "批发系统"),
    零售系统("2", "零售系统"),
    ;

    private String code;

    private String name;

}
