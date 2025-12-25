package com.wishare.finance.domains.reconciliation.command;

import com.wishare.finance.domains.reconciliation.enums.FlowClaimDetailStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 票据流水查询信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/24
 */
@Getter
@Setter
@ApiModel("票据流水查询信息")
public class FlowsInvoiceQuery {

    @ApiModelProperty("收款单id")
    private List<Long> invoiceIds;

    @ApiModelProperty("流水认领状态")
    private FlowClaimDetailStatusEnum flowClaimDetailStatus;

    public FlowsInvoiceQuery() {
    }

    public FlowsInvoiceQuery(List<Long> invoiceIds, FlowClaimDetailStatusEnum flowClaimDetailStatus) {
        this.invoiceIds = invoiceIds;
        this.flowClaimDetailStatus = flowClaimDetailStatus;
    }
}
