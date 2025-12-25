package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 房号账单合计信息
 * @author yancao
 */
@Setter
@Getter
@ApiModel("房号账单合计信息")
public class RoomBillTotalDto {

    @ApiModelProperty(value = "房号id")
    private Long roomId;

    @ApiModelProperty(value = "房号名称")
    private String roomName;

    @ApiModelProperty(value = "应收总额")
    private Long receivableAmountTotal;

    @ApiModelProperty(value = "减免总额")
    private Long deductAmountTotal;

    @ApiModelProperty(value = "今年该房号下所有费项减免总额（单位：分）")
    private Long chargeItemDeductAmountTotal;

}
