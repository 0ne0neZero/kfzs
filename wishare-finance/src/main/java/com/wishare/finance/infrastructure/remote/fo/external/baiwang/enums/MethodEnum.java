package com.wishare.finance.infrastructure.remote.fo.external.baiwang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Linitly
 * @date: 2023/6/19 14:56
 * @descrption: 百望系统接口标识枚举：接口请求时必传接口标识
 */
@Getter
@AllArgsConstructor
public enum MethodEnum {

    发票开具接口标识("baiwang.output.invoice.issue", "发票开具接口标识"),
    发票查询接口标识("baiwang.output.invoice.query", "发票查询接口标识"),
    红字申请接口标识("baiwang.output.redinfo.standardapply", "红字申请接口标识"),
    红字查询接口标识("baiwang.output.redinfo.standardquery", "红字查询接口标识"),
    监控信息查询接口标识("baiwang.output.device.monitor", "监控信息查询接口标识"),

    全电版式文件接口标识("baiwang.output.format.create", "全电版式文件接口标识"),
    全电发票查询接口标识("baiwang.output.invoice.queryeinvoice", "全电发票查询接口标识"),
    红字确认单申请接口标识("baiwang.output.redinvoice.add", "红字确认单申请接口标识"),
    红字确认单查询接口标识("baiwang.output.redinvoice.redforminfo", "红字确认单查询接口标识"),
    ;

    private String value;

    private String desc;
}
