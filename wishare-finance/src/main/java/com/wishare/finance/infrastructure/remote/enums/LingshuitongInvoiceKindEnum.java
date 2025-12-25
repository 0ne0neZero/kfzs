package com.wishare.finance.infrastructure.remote.enums;

/**
 * @author xujian
 * @date 2022/12/1
 * @Description: 灵税通发票类型
 * 增值税纸质专用发票
 * 增值税电子专用发票
 * 增值税纸质普通发票
 * 增值税电子普通发票
 * 增值税普通发票（卷式）
 * 增值税电子通行费发票
 * 机动车销售统一发票
 * 二手车销售统一发票
 * 火车票
 * 客运汽车
 * 航空运输电子客票行程单
 * 船票
 * 出租车发票
 * 通用机打发票
 * 电子机打发票
 * 定额发票
 * 过路费发票
 * 区块链发票
 * 国际小票
 * 出行行程单
 * 其他发票
 * 全电发票（增值税专用发票）
 * 全电发票（增值税普通发票
 */
public enum LingshuitongInvoiceKindEnum {

    增值税纸质专用发票(2,"增值税纸质专用发票"),
    增值税电子专用发票(4,"增值税电子专用发票"),
    增值税纸质普通发票(1,"增值税纸质普通发票"),
    增值税电子普通发票(3,"增值税电子普通发票"),
    增值税普通发票(0," 增值税普通发票（卷式）"),
    增值税电子通行费发票(0,"增值税电子通行费发票"),
    机动车销售统一发票(0,"机动车销售统一发票"),
    二手车销售统一发票(0,"二手车销售统一发票"),
    火车票(0,"火车票"),
    客运汽车(0,"客运汽车"),
    航空运输电子客票行程单(0,"航空运输电子客票行程单"),
    船票(0,"船票"),
    出租车发票(0,"出租车发票"),
    通用机打发票(0,"通用机打发票"),
    电子机打发票(0,"电子机打发票"),
    定额发票(0,"定额发票"),
    过路费发票(0,"过路费发票"),
    区块链发票(0,"区块链发票"),
    国际小票(0,"国际小票"),
    出行行程单(0,"出行行程单"),
    其他发票(0,"其他发票"),
    全电发票专票(0,"全电发票（增值税专用发票）"),
    全电发票普票(8,"全电发票（增值税普通发票）"),

    ;

    //对应到我方发票的的code
    private Integer code;

    private String des;


    public static LingshuitongInvoiceKindEnum valueOfByCode(Integer code) {
        LingshuitongInvoiceKindEnum e = null;
        for (LingshuitongInvoiceKindEnum ee : LingshuitongInvoiceKindEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    LingshuitongInvoiceKindEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
