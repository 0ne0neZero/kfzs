package com.wishare.finance.domains.invoicereceipt.command.invocing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 收款开票查询信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/24
 */
@Getter
@Setter
@ApiModel("收款开票查询信息")
public class ReceivableInvoiceQuery {

    @ApiModelProperty("收款单id")
    private List<Long> receivableBillIds;

    public ReceivableInvoiceQuery() {
    }

    public ReceivableInvoiceQuery(List<Long> receivableBillIds) {
        this.receivableBillIds = receivableBillIds;
    }
}
