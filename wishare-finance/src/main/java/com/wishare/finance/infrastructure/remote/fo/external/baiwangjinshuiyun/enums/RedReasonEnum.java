package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 冲红原因代码
 * @author dongpeng
 * @date 2023/10/31 19:00
 */
@Getter
@AllArgsConstructor
public enum RedReasonEnum {

    开票有误("01", "开票有误"),
    销货退回("02", "销货退回"),
    服务中止("03", "服务中止"),
    销售折让("04", "销售折让"),
    ;

    private String code;

    private String name;
}
