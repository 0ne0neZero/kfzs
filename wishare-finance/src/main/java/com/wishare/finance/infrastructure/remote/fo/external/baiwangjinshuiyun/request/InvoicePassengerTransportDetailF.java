package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 旅客运输服务明细
 * @author dongpeng
 * @date 2023/10/25 20:13
 */
@Data
@ApiModel("旅客运输服务明细")
public class InvoicePassengerTransportDetailF {

    @ApiModelProperty(value = "序号",required = true)
    private String xh;

    @ApiModelProperty(value = "出行人",required = true)
    private String cxr;

    @ApiModelProperty(value = "出行日期(2022-11-18)",required = true)
    private String chuxrq;

    @ApiModelProperty(value = "出行人证件类型",required = true)
    private String cxrzjlx;

    @ApiModelProperty(value = "身份证件号码",required = true)
    private String sfzjhm;

    @ApiModelProperty(value = "出发地",required = true)
    private String cfd;

    @ApiModelProperty(value = "到达地",required = true)
    private String ddd;

    @ApiModelProperty(value = "交通工具类型",required = true)
    private String jtgjlx;

    @ApiModelProperty(value = "等级",required = true)
    private String dengj;

}
