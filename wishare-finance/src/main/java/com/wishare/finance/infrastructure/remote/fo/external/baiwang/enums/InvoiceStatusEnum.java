package com.wishare.finance.infrastructure.remote.fo.external.baiwang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Linitly
 * @date: 2023/6/19 15:26
 * @descrption: 百望系统发票状态
 */
@Getter
@AllArgsConstructor
public enum InvoiceStatusEnum {

    开具成功("00", "开具成功"),
    空白发票作废("02", "空白发票作废"),
    已开发票作废("03", "已开发票作废"),
    正票全额红冲("05", "正票全额红冲"),
    正票部分红冲("06", "正票部分红冲"),
    ;

    private String code;

    private String name;
}
