package com.wishare.finance.domains.reconciliation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FlowReconciliationDetailDto {

    @ApiModelProperty(value = "认领类型：1:发票认领;2:账单认领;")
    private String claimType;

    @ApiModelProperty(value = "认领ID类型：1:蓝票;2:红票;3:收款单;4:退款单;")
    private String claimIdType;

    @ApiModelProperty(value = "发票id")
    private Long invoiceId;

    @ApiModelProperty(value = "流水id")
    private Long flowId;

    @ApiModelProperty(value = "发票认领金额(单位：分)")
    private Long flowAmount;
}
