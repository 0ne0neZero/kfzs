package com.wishare.contract.apps.fo.contractset;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
public class BondPlanDeductionAmountF {

    @ApiModelProperty("保证金计划id")
    @NotNull
    private Long bondPlanId;

    @ApiModelProperty("账单id")
    @NotNull
    private Long billId;

    @ApiModelProperty("收/付/扣/退款金额")
    @NotNull
    private BigDecimal collectionAmount;

    @ApiModelProperty("费项id")
    @NotNull
    private Long itemId;
}
