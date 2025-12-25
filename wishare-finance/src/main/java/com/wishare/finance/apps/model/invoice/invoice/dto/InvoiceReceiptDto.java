package com.wishare.finance.apps.model.invoice.invoice.dto;

import java.util.Map;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceReceiptDto {
    @ApiModelProperty("map<收款明细id,最新收据>")
    Map<Long, ReceiptDto> longReceiptDtoMap;

    @ApiModelProperty("map<收款明细id,最新发票>")
    Map<Long, InvoiceDto> longInvoiceDtoMap;
}
