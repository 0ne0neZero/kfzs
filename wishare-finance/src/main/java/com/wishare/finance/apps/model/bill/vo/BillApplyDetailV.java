package com.wishare.finance.apps.model.bill.vo;

import com.wishare.finance.domains.bill.entity.BillJumpE;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("账单审核详情")
public class BillApplyDetailV extends BillDetailV{

    @ApiModelProperty("申请信息")
    private BillApproveApplyV billApprove;

    @ApiModelProperty("调整信息")
    private BillAdjustV billAdjust;

    @ApiModelProperty("退款信息")
    private BillRefundV billRefund;

    @ApiModelProperty("账单结转信息")
    private BillCarryoverV billCarryover;

    @ApiModelProperty("跳收信息记录")
    private BillJumpV billJump;


}
