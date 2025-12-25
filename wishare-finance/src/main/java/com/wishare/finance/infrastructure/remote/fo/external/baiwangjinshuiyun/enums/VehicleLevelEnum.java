package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交通工具等级
 * @author dongpeng
 * @date 2023/10/26 11:33
 */
@Getter
@AllArgsConstructor
public enum VehicleLevelEnum {
    公务舱("1","公务舱"),
    头等舱("2","头等舱"),
    经济舱("3","经济舱"),
    一等座("4","一等座"),
    二等座("5","二等座"),
    软席("6","软席（软座、软卧）"),
    硬席("7","硬席（硬座、硬卧）"),
    一等舱("8","一等舱"),
    二等舱("9","二等舱"),
    三等舱("10","三等舱"),
    ;

    private String code;

    private String name;


}
