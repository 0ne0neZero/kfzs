package com.wishare.finance.apps.pushbill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 收入确认单发起审批
 */
@Setter
@Getter
@ApiModel("收入确认单审核入参")
public class RevenueApprove {

    @ApiModelProperty("成本中心id")
    @NotNull(message = "成本中心id不能为空")
    private Long costCenterId;

    /**
     *报账单id列表
     */
    @ApiModelProperty("报账单id列表")
    @NotNull(message = "报账单id列表不能为空")
    private List<Long>  voucherBillIds;
}
