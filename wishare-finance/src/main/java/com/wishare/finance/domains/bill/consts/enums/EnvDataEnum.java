package com.wishare.finance.domains.bill.consts.enums;

import lombok.Getter;

@Getter
public enum EnvDataEnum {

    慧享云("hxy","慧享云"),

    方圆("fy","方圆"),

    远洋("yy","远洋"),

    中交("zj","中交")
    ;


    private final String tag;

    private final String tenantName;

    EnvDataEnum(String tag, String tenantName) {
        this.tag = tag;
        this.tenantName = tenantName;
    }
}
