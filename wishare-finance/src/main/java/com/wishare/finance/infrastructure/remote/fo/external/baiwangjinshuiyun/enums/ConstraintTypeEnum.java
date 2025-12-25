package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 特定约束类型
 * @author dongpeng
 * @date 2023/10/26 11:26
 */
@Getter
@AllArgsConstructor
public enum ConstraintTypeEnum {

    正常发票("00", "正常发票(默认)"),
    成品油("01", "成品油"),
    稀土("02", "稀土"),
    建筑服务("03", "建筑服务"),
    货物运输服务("04", "货物运输服务"),
    不动产销售服务("05", "不动产销售服务"),
    不动产经营租赁服务("06", "不动产经营租赁服务"),
    代收车船税("07", "代收车船税"),
    通行费("08", "通行费"),
    旅客运输服务("09", "旅客运输服务"),
    医疗服务住院("10", "医疗服务(住院)"),
    医疗服务门诊("11", "医疗服务(门诊)"),
    自产农产品销售("12", "自产农产品销售"),
    拖拉机和联合收割机("13", "拖拉机和联合收割机"),
    机动车("14", "机动车"),
    二手车("15", "二手车"),
    农产品收购("16", "农产品收购"),
    光伏收购("17", "光伏收购"),
    卷烟("18", "卷烟"),
    出口("19", "出口"),
    农产品("20", "农产品"),
    ;

    private String code;

    private String name;
}
