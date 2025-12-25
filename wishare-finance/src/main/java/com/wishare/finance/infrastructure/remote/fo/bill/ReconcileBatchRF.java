package com.wishare.finance.infrastructure.remote.fo.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ApiModel("批量对账请求参数")
public class ReconcileBatchRF {

    @ApiModelProperty("账单id列表")
    private List<Long> billIds;

    @ApiModelProperty("对账结果(false:未对平，true:已对平)")
    private boolean result ;

}
