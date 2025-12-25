package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 增值税特殊管理
 * @author dongpeng
 * @date 2023/10/28 11:41
 */
@Getter
@AllArgsConstructor
public enum SpecialVATEnum {

    免税("免税"),
    百分之100先征后退("100%先征后退"),
    百分之50先征后退("50%先征后退"),
    不征税("不征税"),
    先征后退("先征后退"),
    即征即退百分之100("即征即退100%"),
    即征即退百分之30("即征即退30%"),
    即征即退百分之50("即征即退50%"),
    即征即退百分之70("即征即退70%"),
    按百分之3简易征收("按3%简易征收"),
    按百分之5简易征收("按5%简易征收"),
    按百分之5简易征收减按1点5计征("按5%简易征收减按1.5%计征"),
    稀土产品("稀土产品"),
    简易征收("简易征收"),
    超税负百分之12即征即退("超税负12%即征即退"),
    超税负百分之3即征即退("超税负3%即征即退"),
    超税负百分之6即征即退("超税负6%即征即退"),
    超税负百分之8即征即退("超税负8%即征即退"),
    ;
    private String name;
}
