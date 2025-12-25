package com.wishare.finance.apps.model.bill.fo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("减免管理列表查询dto")
public class ChargeDeductionManageF {

    @ApiModelProperty(value = "房号")
    private String roomName;

    @ApiModelProperty("费项")
    private String chargeItemName;

    @ApiModelProperty("审核状态（0待审核， 1处理中，2已完成，3未通过）")
    private Integer approveState;
}
