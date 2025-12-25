package com.wishare.finance.infrastructure.remote.fo.external.baiwang.response;

import com.wishare.finance.infrastructure.remote.fo.external.baiwang.request.InvoiceDetailF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: Linitly
 * @date: 2023/6/20 10:30
 * @descrption:
 */
@Data
@ApiModel(value = "发票开具响应失败信息")
public class InvoiceFailResV {

    @ApiModelProperty(value = "唯一标志开票请求")
    private String serialNo;

    @ApiModelProperty(value = "合计金额")
    private BigDecimal invoiceTotalPrice;

    @ApiModelProperty(value = "合计税额")
    private BigDecimal invoiceTotalTax;

    @ApiModelProperty(value = "价税合计")
    private BigDecimal invoiceTotalPriceTax;

    @ApiModelProperty(value = "商品明细")
    private List<InvoiceDetailF> invoiceDetailsList;
}
