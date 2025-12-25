package com.wishare.finance.infrastructure.remote.fo.external.baiwang.response;

import com.wishare.finance.infrastructure.remote.fo.external.baiwang.request.InvoiceDetailF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: Linitly
 * @date: 2023/6/20 10:36
 * @descrption:
 */
@Data
@ApiModel(value = "发票开具成功响应")
public class InvoiceSuccessResV {

    @ApiModelProperty(value = "发票代码")
    private String invoiceCode;

    @ApiModelProperty(value = "发票号码")
    private String invoiceNo;

    @ApiModelProperty(value = "全电发票号码")
    private String einvoiceNo;

    @ApiModelProperty(value = "校验码")
    private String invoiceCheckCode;

    @ApiModelProperty(value = "开票日期")
    private String invoiceDate;

    @ApiModelProperty(value = "二维码")
    private String invoiceQrCode;

    @ApiModelProperty(value = "税控码")
    private String taxControlCode;

    @ApiModelProperty(value = "发票类型代码")
    private String invoiceTypeCode;

    @ApiModelProperty(value = "流水号")
    private String serialNo;

    @ApiModelProperty(value = "电子发票地址")
    private String eInvoiceUrl;

    @ApiModelProperty(value = "合计金额")
    private BigDecimal invoiceTotalPrice;

    @ApiModelProperty(value = "价税合计")
    private BigDecimal invoiceTotalPriceTax;

    @ApiModelProperty(value = "合计税额")
    private BigDecimal invoiceTotalTax;

    @ApiModelProperty(value = "发票明细集合")
    private List<InvoiceDetailF> invoiceDetailsList;
}
