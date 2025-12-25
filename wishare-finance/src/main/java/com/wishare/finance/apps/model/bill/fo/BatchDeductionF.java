package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@ApiModel("批量减免预收账单金额")
public class BatchDeductionF {

    @ApiModelProperty(value = "账单id", required = true)
    private Long id;

    @ApiModelProperty(value = "应收减免金额(分)")
    private Long deductibleAmount;

    @ApiModelProperty(value = "实收减免金额(分)")
    private Long discountAmount;

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单")
    private Integer billType;

}
