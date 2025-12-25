package com.wishare.finance.domains.reconciliation;

import com.wishare.finance.domains.reconciliation.entity.FlowDetailE;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "流水认领详情-包含认领记录批次号和报账单号")
public class FlowDetailVo extends FlowDetailE {

    @ApiModelProperty(value = "认领批次号id")
    private Long flowClaimRecordId;

    @ApiModelProperty(value = "认领批次流水号")
    private String flowClaimRecordSerialNumber;

    @ApiModelProperty(value = "报账单id")
    private Long voucherBillId;

    @ApiModelProperty(value = "报账单号")
    private String voucherBillNo;

}
