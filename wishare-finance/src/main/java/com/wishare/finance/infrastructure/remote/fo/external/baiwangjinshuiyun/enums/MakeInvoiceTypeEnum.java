package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 开票类型
 * @author dongpeng
 * @date 2023/10/27 15:49
 */
@Getter
@AllArgsConstructor
public enum MakeInvoiceTypeEnum {

    蓝字发票("0", "蓝字发票"),
    红字发票("1", "红字发票"),
    ;

    private String value;

    private String desc;
}
