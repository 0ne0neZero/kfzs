package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 收款单信息详细信息
 * @author yancao
 */
@Setter
@Getter
@ApiModel("收款单信息详细信息")
public class GatherBillDetailV extends GatherBillV{

    @ApiModelProperty("结转信息")
    private List<BillCarryoverV> carryovers;

    @ApiModelProperty("退款信息")
    private List<BillRefundV> refunds;

}
