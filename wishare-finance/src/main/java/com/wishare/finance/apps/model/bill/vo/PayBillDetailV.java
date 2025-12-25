package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 付款单信息详细信息
 * @author yancao
 */
@Setter
@Getter
@ApiModel("付款单信息详细信息")
public class PayBillDetailV extends PayBillV{

    @ApiModelProperty("结转信息")
    private List<BillCarryoverV> carryovers;

}
