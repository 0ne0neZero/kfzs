package com.wishare.finance.infrastructure.remote.fo.external.fangyuan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 开具不动产经营租赁服务特定要素
 */
@Setter
@Getter
@ApiModel("开具不动产经营租赁服务特定要素")
public class RealPropertyRentInfoF {

    @ApiModelProperty("不动产地址（传对应省市区中文名称--需与行政区划名称一致）")
    private String bdcdz;

    @ApiModelProperty("详细地址（不动产地址+详细地址 总长度最大120字符，且必须包含 街、路、村、乡、镇、道 关键词）")
    private String xxdz;

    @ApiModelProperty("租赁开始日期（不能晚于租赁结束日期）")
    private String zlqq;

    @ApiModelProperty("租赁结束日期（不能早于租赁开始日期）")
    private String zlqz;

    @ApiModelProperty("跨地（市）标志（0-否 1-是）")
    private String kdsbz = "0";

    @ApiModelProperty("产权证书/不动产权证号")
    private String zsbh;

    @ApiModelProperty("面积单位（只能选其中一种：1 平方千米、2 平方米、3 公顷、4 亩、5 hm²、6 km²、7 m²）")
    private String mjdw = "2";

}
