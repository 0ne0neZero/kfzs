package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 根据房号统计账单
 * @author yancao
 */
@Setter
@Getter
@ApiModel("根据房号统计账单")
public class StatisticsRoomBillAmountF {

    @ApiModelProperty(value = "账单类型（1:应收账单，2:预收账单，3:临时收费账单）", required = true)
    @NotNull(message = "账单类型不能为空")
    private Integer billType;

    @ApiModelProperty(value = "房号id集合")
    private List<Long> roomIdList;

    @ApiModelProperty(value = "费项id集合")
    private List<Long> chargeItemIdList;

    @ApiModelProperty(value = "上级收费单元ID")
    private String supCpUnitId;
}
