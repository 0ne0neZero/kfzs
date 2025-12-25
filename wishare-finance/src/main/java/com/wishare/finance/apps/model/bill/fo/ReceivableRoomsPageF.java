package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 查询应收收费信息入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("查询应收收费信息入参")
public class ReceivableRoomsPageF extends BasePageF{

    @ApiModelProperty(value = "项目ID")
    @NotBlank(message = "项目ID不能为空")
    private String communityId;

    @ApiModelProperty(value = "上级收费单元ID")
    private String supCpUnitId;

    @ApiModelProperty(value = "项目集合ID")
    private List<String> communityIds;

    @ApiModelProperty(value = "费项id")
    private String chargeItemId;

    @ApiModelProperty(value = "费项集合")
    private List<String> chargeItemIds;

    @ApiModelProperty("缴费状态：0未缴，1部分缴费，2已缴")
    private Integer payStatus;

    @ApiModelProperty(value = "房号ID")
    private String roomId;

    @ApiModelProperty(value = "房号集合")
    private List<String> roomIds;

    @ApiModelProperty(value = "收费对象id")
    private List<String> targetObjIds;

    @ApiModelProperty(value = "催缴列表会传1，预收房号查询传2")
    private Integer type;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;


}
