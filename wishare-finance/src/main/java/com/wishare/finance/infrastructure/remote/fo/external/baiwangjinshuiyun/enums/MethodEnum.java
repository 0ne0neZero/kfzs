package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dongpeng
 * @date 2023/10/27 16:15
 * 接口请求时必传接口标识
 */
@Getter
@AllArgsConstructor
public enum MethodEnum {

    正票申请("QDP-FP-10001", "正票申请"),
    正票开具结果查询("QDP-FP-10003", "正票开具结果查询"),
    红字申请("QDP-FP-10006", "红字申请"),
    红字申请结果查询("QDP-FP-10007", "红字申请结果查询"),
    红字发票确认单状态查询("QDP-FP-10010", "红字发票确认单状态查询"),
    红字开具("QDP-FP-10011", "红字开具"),
    红字开具结果查询("QDP-FP-10012", "红字开具结果查询"),
    版式地址查询("QDP-FP-10015", "版式地址查询"),
    ;

    private String value;

    private String desc;
}
