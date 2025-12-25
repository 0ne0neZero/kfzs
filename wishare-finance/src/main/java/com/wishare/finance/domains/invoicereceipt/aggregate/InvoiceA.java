package com.wishare.finance.domains.invoicereceipt.aggregate;

import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import lombok.Getter;

import java.util.List;

/**
 * 票据聚合
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/17
 */
@Getter
public class InvoiceA {

    private final InvoiceReceiptE invoiceReceipt;

    private final InvoiceE invoice;

    private final List<InvoiceReceiptDetailE> invoiceReceiptDetails;

    public InvoiceA(InvoiceReceiptE invoiceReceipt, InvoiceE invoice, List<InvoiceReceiptDetailE> invoiceReceiptDetails) {
        this.invoiceReceipt = invoiceReceipt;
        this.invoice = invoice;
        this.invoiceReceiptDetails = invoiceReceiptDetails;
        invoiceReceipt.verify(invoiceReceiptDetails);
    }
}
