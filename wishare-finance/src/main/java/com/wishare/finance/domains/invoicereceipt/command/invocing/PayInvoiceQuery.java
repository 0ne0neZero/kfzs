package com.wishare.finance.domains.invoicereceipt.command.invocing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 付款开票查询信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/24
 */
@Getter
@Setter
@ApiModel("付款开票查询信息")
public class PayInvoiceQuery {

    @ApiModelProperty("付款单id")
    private List<Long> payBillIds;

    public PayInvoiceQuery() {
    }

    public PayInvoiceQuery(List<Long> payBillIds) {
        this.payBillIds = payBillIds;
    }
}
