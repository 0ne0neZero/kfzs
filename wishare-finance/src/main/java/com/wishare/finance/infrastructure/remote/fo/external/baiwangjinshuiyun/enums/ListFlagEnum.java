package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dongpeng
 * @date 2023/10/27 16:15
 */
@Getter
@AllArgsConstructor
public enum ListFlagEnum {

    非清单("0", "非清单"),
    清单("1", "清单"),
    ;

    private String code;

    private String name;
}
