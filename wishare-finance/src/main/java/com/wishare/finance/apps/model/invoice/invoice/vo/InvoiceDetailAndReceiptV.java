package com.wishare.finance.apps.model.invoice.invoice.vo;

import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName invoiceDetailAndReceiptV
 * @description: 发票明细和发票主表信息
 * @author: dp
 * @create: 2023-12-27 09:35
 * @Version 1.0
 **/
@Getter
@Setter
@ApiModel("发票明细和发票主表信息")
public class InvoiceDetailAndReceiptV extends InvoiceReceiptDetailE {
    @ApiModelProperty(value = "项目ID")
    private String communityId;

    @ApiModelProperty(value = "开票状态：1 开票中 2 开票成功 3 开票失败 4 红冲中 5 已红冲 6 已作废 7 开票成功，签章失败 8.部分红冲")
    private Integer state;
}
