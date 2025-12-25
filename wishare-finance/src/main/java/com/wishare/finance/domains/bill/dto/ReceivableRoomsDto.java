package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 房号账单合计信息
 *
 * @author yancao
 */
@Setter
@Getter
@ApiModel("应收收费信息")
public class ReceivableRoomsDto {

    @ApiModelProperty(value = "房号id")
    private String roomId;

    @ApiModelProperty(value = "房号名称")
    private String roomName;

    @ApiModelProperty(value = "项目ID")
    private String communityId;

    @ApiModelProperty(value = "项目名称")
    private String communityName;

    @ApiModelProperty(value = "费项id")
    private String chargeItemId;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "收费对象id")
    private String targetObjId;

    @ApiModelProperty(value = "收费对象名字")
    private String targetObjName;

    @ApiModelProperty(value = "收费对象手机号")
    private String phone;

    @ApiModelProperty(value = "收费金额")
    private String totalAmount;

    @ApiModelProperty(value = "bpm跳收状态：0正常，1跳收审核中，2跳收审核通过3跳收拒绝")
    private Integer jumpState;

}
