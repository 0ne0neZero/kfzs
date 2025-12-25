package com.wishare.finance.domains.report.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ChargeClosingDateDto {

    @ApiModelProperty("唯一值")
    private String roomIdAndChargeItemId;

    @ApiModelProperty("房屋id")
    private Long roomId;

    @ApiModelProperty("房屋名称")
    private String roomName;

    @ApiModelProperty("费项id")
    private Long chargeItemId;

    @ApiModelProperty("费项")
    private String chargeItemName;

    @ApiModelProperty("收款截止日期")
    private LocalDateTime chargeTime;

}
