package com.wishare.finance.domains.bill.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel("对账账单查询信息")
public class ReconciliationBillQuery {

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("项目ID")
    private String communityId;

    @ApiModelProperty("成本中心id")
    private String costCenterId;

    @ApiModelProperty("收款账号id")
    private Long sbAccountId;

    @ApiModelProperty(value = "支付方式(0线上，1线下)")
    private Integer payWay;

    @ApiModelProperty("支付渠道")
    private String payChannel;

    @ApiModelProperty(value = "账单类型")
    private Integer billType;

}
