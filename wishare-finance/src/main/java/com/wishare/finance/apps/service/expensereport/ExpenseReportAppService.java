package com.wishare.finance.apps.service.expensereport;

import java.util.List;

import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.service.InvoiceDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ExpenseReportAppService {

    private final InvoiceDomainService invoiceDomainService;

    /**
     * 开票计提
     * @return
     */
    public Boolean expenseReportFromInvoice(List<InvoiceReceiptDetailE> invoiceReceiptDetails) {


        return true;
    }


}
