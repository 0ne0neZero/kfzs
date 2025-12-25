package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 查询应收账单列表入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("查询应收账单列表入参")
public class HistoryF extends BasePageF{

    @ApiModelProperty(value = "项目ID")
    @NotBlank(message = "项目ID不能为空!")
    private String communityId;

    @ApiModelProperty(value = "费项id")
    private String chargeItemId;

    @ApiModelProperty(value = "房号ID")
    private String roomId;

    @ApiModelProperty(value = "收费对象id")
    private String targetObjId;

}
