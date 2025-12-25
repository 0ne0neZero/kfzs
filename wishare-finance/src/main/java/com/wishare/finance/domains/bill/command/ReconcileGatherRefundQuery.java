package com.wishare.finance.domains.bill.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 账单退款查询信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/24
 */
@Getter
@Setter
@ApiModel("账单退款查询信息")
public class ReconcileGatherRefundQuery {

    @ApiModelProperty("收款单id")
    private List<Long> gatherBillIds;

    public ReconcileGatherRefundQuery() {
    }

    public ReconcileGatherRefundQuery(List<Long> gatherBillIds) {
        this.gatherBillIds = gatherBillIds;
    }
}
