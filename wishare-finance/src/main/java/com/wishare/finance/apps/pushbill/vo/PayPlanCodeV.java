package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 资金计划编号
 * @Author dengjie03
 * @Description
 * @Date 2025-01-10
 */
@Data
@ApiModel(value="支付申请单-资金计划编号")
public class PayPlanCodeV implements Serializable {

    @ApiModelProperty(value = "现金流量编号")
    private String XJLLXMMC;
    @ApiModelProperty(value = "计划类型名称")
    private String JHLXMC;
    @ApiModelProperty(value = "计划编号")
    private String JHBH;
    @ApiModelProperty(value = "计划编号ID")
    private String JHBDID;
    @ApiModelProperty(value = "资金计划编号")
    private String JHNM;

}
