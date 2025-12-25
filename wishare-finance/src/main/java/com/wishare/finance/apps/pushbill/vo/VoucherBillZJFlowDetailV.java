package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@ApiModel("资金收款单下的认领明细")
public class VoucherBillZJFlowDetailV {

    @ApiModelProperty("流水批次id")
    private Long id;

    @ApiModelProperty("认领批次号、流水号")
    private String serialNumber;

    @ApiModelProperty("认领通知单id")
    private String flowRecordId;

    @ApiModelProperty("认领单据金额")
    private BigDecimal settleAmount;

    @ApiModelProperty("认领流水金额")
    private BigDecimal claimAmount;

    @ApiModelProperty("银行到账时间")
    private String payTime;

    @ApiModelProperty("认领时间")
    private LocalDateTime claimDateTime;

    @ApiModelProperty("项目id")
    private String supCpUnitId;
}
