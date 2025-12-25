package com.wishare.finance.domains.invoicereceipt.support;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptDetailRepository;
import com.wishare.finance.infrastructure.support.AbstractQuery;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/9/14
 * @Description:
 */
public class InvoiceReceiptDetailQuery extends AbstractQuery<List<InvoiceReceiptDetailE>> {

    public InvoiceReceiptDetailQuery(InvoiceReceiptDetailRepository invoiceReceiptDetailRepository, List<Long> invoiceReceiptIds) {
        if (!CollectionUtils.isEmpty(invoiceReceiptIds)) {
            List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceReceiptDetailRepository.queryByInvoiceReceiptIds(invoiceReceiptIds);
            if (!CollectionUtils.isEmpty(invoiceReceiptDetailES)) {
                Map<Long, List<InvoiceReceiptDetailE>> billMap = invoiceReceiptDetailES.stream().collect(Collectors.groupingBy(InvoiceReceiptDetailE::getInvoiceReceiptId));
                for (Object key : billMap.keySet()) {
                    map.put((Long) key, billMap.get(key));
                }
            }
        }
    }
}
