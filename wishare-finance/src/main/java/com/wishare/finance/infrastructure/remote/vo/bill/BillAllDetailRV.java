package com.wishare.finance.infrastructure.remote.vo.bill;

import com.wishare.finance.apps.model.bill.vo.BillSettleV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("账单所有详细信息")
public class BillAllDetailRV extends BillDetailRV {

    @ApiModelProperty("调整信息")
    private List<BillAdjustRV> adjusts;

    @ApiModelProperty("退款信息")
    private List<BillRefundRV> refunds;

    @ApiModelProperty("结转信息")
    private List<BillCarryoverRV> carryovers;

    @ApiModelProperty("结算明细")
    private List<BillSettleV> settles;

}
