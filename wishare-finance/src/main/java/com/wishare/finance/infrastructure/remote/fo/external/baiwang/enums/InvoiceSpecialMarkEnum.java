package com.wishare.finance.infrastructure.remote.fo.external.baiwang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Linitly
 * @date: 2023/6/19 15:34
 * @descrption:
 */
@Getter
@AllArgsConstructor
public enum InvoiceSpecialMarkEnum {


    普通发票("00", "普通发票"),
    农产品销售("01", "农产品销售"),
    农产品收购("02", "农产品收购"),
    成品油("08", "成品油"),
    机动车("12", "机动车"),
    // 全电类发票特殊票种标志
    建筑服务发票("03", "建筑服务发票"),
    货物运输服务发票("04", "货物运输服务发票"),
    不动产销售服务发票("05", "不动产销售服务发票"),
    不动产租赁服务发票("06", "不动产租赁服务发票"),
    ;

    private String code;

    private String name;
}
