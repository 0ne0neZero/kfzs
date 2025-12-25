package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 查询房间应收账单列表入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("查询房间应收账单列表入参")
public class ReceivableBillF {

    @ApiModelProperty(value = "项目ID")
    @NotBlank(message = "项目ID不能为空!")
    private String communityId;

    @ApiModelProperty(value = "费项id")
    private List<String> chargeItemIds;

    @ApiModelProperty(value = "房号ID")
    private String roomId;

    @ApiModelProperty(value = "房号集合")
    private List<String> roomIds;

    @ApiModelProperty(value = "收费对象id")
    private List<String> targetObjIds;

    @ApiModelProperty(value = "缴费状态列表 0：未缴费，1：部分缴费，2：已缴费")
    @NotEmpty(message = "缴费状态不能为空")
    private List<Integer> payState;
}
