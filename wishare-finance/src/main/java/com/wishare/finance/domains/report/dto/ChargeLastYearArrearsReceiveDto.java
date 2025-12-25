package com.wishare.finance.domains.report.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 上一年的欠费信息
 *
 * @author yancao
 */
@Setter
@Getter
public class ChargeLastYearArrearsReceiveDto {

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

    @ApiModelProperty("前期应收")
    private Long earlyReceivableAmount;

    @ApiModelProperty("前期应收减免")
    private Long earlyDeductibleAmount;

}
