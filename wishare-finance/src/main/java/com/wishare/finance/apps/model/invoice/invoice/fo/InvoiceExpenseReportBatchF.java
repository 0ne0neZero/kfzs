package com.wishare.finance.apps.model.invoice.invoice.fo;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("批量开票计提入参")
public class InvoiceExpenseReportBatchF {

    @NotEmpty(message = "票据id不能为空")
    @ApiModelProperty(value = "票据id")
    List<Long> invoiceReceiptIds;
}
