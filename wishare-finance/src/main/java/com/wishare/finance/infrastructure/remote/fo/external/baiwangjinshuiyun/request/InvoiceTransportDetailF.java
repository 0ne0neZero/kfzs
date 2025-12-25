package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 货物运输服务明细  特定约束类型代码为04时必填
 * @author dongpeng
 * @date 2023/10/25 20:02
 */
@Data
@ApiModel("货物运输服务明细")
public class InvoiceTransportDetailF {

    @ApiModelProperty(value = "序号",required = true)
    private String xh;

    @ApiModelProperty(value = "运输工具种类(铁路运输, \n" +
            "公路运输,\n" +
            "水路运输,\n" +
            "航空运输,\n" +
            "管道运输)",required = true)
    private String ysgjzl;

    @ApiModelProperty(value = "运输工具牌号",required = true)
    private String ysgjhp;

    @ApiModelProperty(value = "起运地",required = true)
    private String qyd;

    @ApiModelProperty(value = "到达地",required = true)
    private String ddd;

    @ApiModelProperty(value = "运输货物名称",required = true)
    private String yshwmc1;
}
