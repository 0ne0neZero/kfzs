package com.wishare.finance.apps.model.invoice.invoice.vo;

import com.wishare.finance.apps.model.bill.vo.InvoiceV;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("根据金额分组的发票信息")
public class InvoiceGroupByAmountV {
    @ApiModelProperty("金额")
    private String key;
    @ApiModelProperty("金额单位")
    private List<InvoiceAndReceiptV> value;
}
