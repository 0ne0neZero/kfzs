package com.wishare.finance.apps.service.bill.accounthand;

import com.wishare.finance.apps.model.bill.fo.InvoiceBillDto;
import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceBatchRedF;
import com.wishare.finance.apps.service.invoicereceipt.InvoiceAppService;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.facade.BillFacade;
import com.wishare.finance.domains.configure.accountbook.facade.AmpFinanceFacade;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceLineEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.remote.clients.xxljob.OldXxlJobClient;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: pgq
 * @since: 2022/9/26 17:39
 * @version: 1.0.0
 */
@Service
public abstract class AccountHandAppService implements ApiBase, AccountHandService {

    @Setter(onMethod_ = {@Autowired})
    protected AmpFinanceFacade ampFinanceFacade;

    @Setter(onMethod_ = {@Autowired})
    protected BillFacade billFacade;

    @Setter(onMethod_ = {@Autowired})
    private InvoiceAppService invoiceAppService;

    @Setter(onMethod_ = {@Autowired})
    protected OldXxlJobClient oldXxlJobClient;

    /**
     * 获取账单对应的票据
     *
     * @param billIds
     * @param billType
     * @return
     */
    public Optional<Map<Long, List<InvoiceBillDto>>> getBillInvoice(List<Long> billIds,
        Integer billType) {

        return invoiceAppService.getBillInvoiceMap(billIds, billType);
    }

    /**
     * 红冲
     *
     * @param id
     * @param billType
     */
    protected void invoiceReversal(Long id, Integer billType) {
        Optional<Map<Long, List<InvoiceBillDto>>> billInvoice = getBillInvoice(
            Collections.singletonList(id), billType);
        if (billInvoice.isEmpty()) {
            throw BizException.throw404(ErrorMessage.BILL_HAS_NO_INVOICE.msg());
        }
        List<InvoiceBillDto> invoiceBillDtos = billInvoice.get().get(id);
        if (invoiceBillDtos == null || invoiceBillDtos.isEmpty()) {
            throw BizException.throw404(ErrorMessage.BILL_HAS_NO_INVOICE.msg());
        }
        // 需作废的普通发票
        List<InvoiceBillDto> deleteInvoiceBills = invoiceBillDtos.stream().
            filter(t -> Objects.equals(InvoiceLineEnum.增值税普通发票.getCode(), t.getType())
                || Objects.equals(InvoiceLineEnum.增值税专用发票.getCode(), t.getType())).
            collect(Collectors.toList());
        // 发票作废
        if (!deleteInvoiceBills.isEmpty()) {
            // 因现在只会处理单个异常，票据不多
            for (InvoiceBillDto deleteInvoiceBill : deleteInvoiceBills) {
                invoiceAppService.voidInvoice(deleteInvoiceBill.getInvoiceReceiptId());
            }
        }

        // 需要红冲的发票
        List<InvoiceBillDto> reversalInvoiceBills = invoiceBillDtos.stream().
            filter(t -> Objects.equals(InvoiceLineEnum.增值税电子专票.getCode(), t.getType())
                || Objects.equals(InvoiceLineEnum.增值税电子发票.getCode(), t.getType())).
            collect(Collectors.toList());
        // 票据红冲
        if (!reversalInvoiceBills.isEmpty()) {
            for (InvoiceBillDto reversalInvoiceBill : reversalInvoiceBills) {
                // 因现在只会处理单个异常，票据不多
                InvoiceBatchRedF invoiceBatchRedF = new InvoiceBatchRedF();
                invoiceBatchRedF.setInvoiceReceiptId(reversalInvoiceBill.getInvoiceReceiptId());
                invoiceAppService.invoiceBatchRed(invoiceBatchRedF);
            }
        }

    }

    protected BillDetailMoreV getOneBill(Long id, BillTypeEnum billTypeEnum) {

        BillDetailMoreV allDetail = billFacade.getAllDetail(id, billTypeEnum, null);
        if (Objects.isNull(allDetail)) {
            throw BizException.throw404(ErrorMessage.BILL_NOT_EXIST.msg());
        }
        return allDetail;
    }
}
