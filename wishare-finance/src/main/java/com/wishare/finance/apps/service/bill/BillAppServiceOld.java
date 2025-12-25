package com.wishare.finance.apps.service.bill;

import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.fo.BillDetailF;
import com.wishare.finance.apps.model.bill.fo.BillSearchF;
import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.bill.vo.InvoiceV;
import com.wishare.finance.apps.model.bill.vo.ReceiptV;
import com.wishare.finance.domains.bill.facade.BillFacade;
import com.wishare.finance.domains.bill.service.BillDomainServiceOld;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.BillOjv;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.service.InvoiceDomainService;
import com.wishare.finance.domains.invoicereceipt.service.ReceiptDomainService;
import com.wishare.starter.Global;
import com.wishare.starter.interfaces.ApiBase;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/9/5
 * @Description:
 */
@Slf4j
@Service
public class BillAppServiceOld implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ReceiptDomainService receiptDomainService;

    @Setter(onMethod_ = {@Autowired})
    private InvoiceDomainService invoiceDomainService;

    @Setter(onMethod_ = {@Autowired})
    private BillDomainServiceOld billDomainServiceOld;

    @Setter(onMethod_ = {@Autowired})
    private BillFacade  billFacade;

    /**
     * 根据账单id获取账单详情
     *
     * @param form
     * @return
     */
    public BillDetailMoreV detailBill(BillDetailF form) {
        BillDetailMoreV allDetail = billDomainServiceOld.getAllDetail(form.getBillId(), form.getBillType(),null);
        allDetail.setReceiptVos(handleReceipt(allDetail.getBillId()));
        allDetail.setInvoiceVos(handleInvoice(allDetail.getBillId()));
        return allDetail;
    }

    /**
     * 根据账单id查询发票明细
     * @param billId
     * @return
     */
    private List<InvoiceV> handleInvoice(Long billId) {
        List<InvoiceReceiptE> invoiceReceiptES = invoiceDomainService.getByBillId(billId);
        if (CollectionUtils.isNotEmpty(invoiceReceiptES)) {
            return Global.mapperFacade.mapAsList(invoiceReceiptES, InvoiceV.class);
        }
        return null;
    }

    /**
     * 根据账单id查询收据明细
     *
     * @param billId
     * @return
     */
    private List<ReceiptV> handleReceipt(Long billId) {
        List<InvoiceReceiptE> invoiceReceiptES = receiptDomainService.getByBillId(billId);
        if (CollectionUtils.isNotEmpty(invoiceReceiptES)) {
            return Global.mapperFacade.mapAsList(invoiceReceiptES, ReceiptV.class);
        }
        return null;
    }
}
