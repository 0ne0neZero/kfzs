package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@ApiModel("调整应收账单信息")
public class AdjustReceivableBillF {

    @ApiModelProperty(value = "账单id")
    private Long billId;

}
