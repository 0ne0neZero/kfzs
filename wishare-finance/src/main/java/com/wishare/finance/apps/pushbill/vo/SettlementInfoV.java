package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("结算单详情")
public class SettlementInfoV {
    @ApiModelProperty(value = "结算单id")
    private String settlementId;
    @ApiModelProperty(value = "结算单名称")
    private String settlementName;

}
