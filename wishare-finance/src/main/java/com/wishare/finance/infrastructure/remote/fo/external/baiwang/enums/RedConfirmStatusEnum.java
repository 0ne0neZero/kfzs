package com.wishare.finance.infrastructure.remote.fo.external.baiwang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Linitly
 * @date: 2023/6/19 15:26
 * @descrption: 百望系统 确认单状态
 */
@Getter
@AllArgsConstructor
public enum RedConfirmStatusEnum {

    无需确认("01", "无需确认"),
    销方录入待购方确认("02", "销方录入待购方确认"),
    购方录入待销方确认("03", "购方录入待销方确认"),
    购销双方已确认("04", "购销双方已确认"),
    作废_销方录入购方否认("05", "作废_销方录入购方否认"),
    作废_购方录入销方否认("06", "作废_购方录入销方否认"),
    作废_超72小时未确认("07", "作废_超72小时未确认"),
    作废_发起方已撤销("08", "作废_发起方已撤销"),
    作废_确认后撤销("09", "作废_确认后撤销"),
    作废_异常凭证("10", "作废_异常凭证"),
    ;

    private String code;

    private String name;
}
