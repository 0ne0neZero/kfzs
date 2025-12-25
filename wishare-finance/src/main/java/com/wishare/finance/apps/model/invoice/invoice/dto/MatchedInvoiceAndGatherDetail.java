package com.wishare.finance.apps.model.invoice.invoice.dto;

import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceGatherDetailAmount;
import com.wishare.finance.apps.model.invoice.invoice.fo.QuotaInvoiceBindF;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shenchenxi
 * @date 2024/08/22
 * @Description:
 */
@Getter
@Setter
@AllArgsConstructor
@ApiModel("定额发票分配收款明细")
public class MatchedInvoiceAndGatherDetail {
    private QuotaInvoiceBindF.InvoiceReceiptIdAndAmountF invoice;
    private InvoiceGatherDetailAmount gatherDetail;
    /**
     * 当前发票明细单子所开金额
     */
    private Long matchedAmount;
    /**
     * 当前发票金额
     */
    private Long invoiceAmount;
    /**
     * 发票id
     */
    private Long invoiceReceiptId;
    /**
     * 收款明细id
     */
    private Long gatherDetailId;
    /**
     * 收款明旭需要开票的金额
     */
    private Long gatherDetailNeedAmount;
}
