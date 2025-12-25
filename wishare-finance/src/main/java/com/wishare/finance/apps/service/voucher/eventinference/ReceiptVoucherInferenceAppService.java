package com.wishare.finance.apps.service.voucher.eventinference;

import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.service.voucher.AbstractVoucherInferenceAppService;
import com.wishare.finance.domains.bill.consts.enums.BillInvoiceStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.voucher.consts.enums.ActionEventEnum;
import com.wishare.finance.domains.voucher.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherE;
import com.wishare.finance.domains.voucher.entity.VoucherRuleE;
import com.wishare.tools.starter.fo.search.Field;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * @description: 收票结算
 * @author: pgq
 * @since: 2022/12/2 9:21
 * @version: 1.0.0
 */
@Service
public class ReceiptVoucherInferenceAppService extends AbstractVoucherInferenceAppService {

    @Override
    public EventTypeEnum getEventType() {
//        return EventTypeEnum.支出_进销开票;
        return EventTypeEnum.收票结算;
    }

    @Override
    public void inference(VoucherRuleE record, boolean isSingle) {
        super.inference(record, isSingle);
    }

    @Override
    public void getBillStatus(VoucherRuleE record, List<Field> fieldList) {

        fieldList.add(new Field("b.state", BillStateEnum.正常.getCode(), 1));
        fieldList.add(new Field("b.invoice_state", BillInvoiceStateEnum.已开票.getCode(), 1));
        fieldList.add(new Field("b.verify_state", 0, 1));
    }

    @Override
    public void inferenceByBillType(Integer eventType, List<Field> fieldList,
        String conditions, VoucherRuleE record, boolean isSingle) {
        billInference(eventType, fieldList, conditions, record, BillTypeEnum.应付账单, isSingle);
    }

    @Override
    public Boolean runInferenceByRule(Long ruleId) {
        return super.runInferenceByRule(ruleId);
    }

    @Override
    public String singleInference(Long billId, BillTypeEnum billTypeEnum, ActionEventEnum actionEventEnum, Long amount, String supCpUnitId) {
        return super.singleInference(billId, billTypeEnum, actionEventEnum, amount, supCpUnitId);
    }

    @Override
    public List<String> batchSingleInference(List<Long> billIds, BillTypeEnum billTypeEnum,
        ActionEventEnum actionEventEnum, Long amount, boolean needBack, String supCpUnitId) {
        return super.batchSingleInference(billIds, billTypeEnum, actionEventEnum, amount, needBack,supCpUnitId);
    }

    @Override
    public Long generateInferenceAmount(BillInferenceV bill, BillTypeEnum billType) {
        VoucherE voucher = voucherDomainService.getLastVoucher(bill.getBillNo(), getEventType());
        List<InvoiceReceiptE> invoiceReceiptEList = invoiceDomainService.getByBillId(bill.getId());
//        List<InvoiceBillDto> list = invoiceDomainService.getInvoiceDetailByBillId(bill.getId(), billType);
        List<Long> InvoiceReceiptIds = invoiceReceiptEList.stream()
            .filter(invoiceReceiptE -> invoiceReceiptE.getBillingTime().isAfter(voucher.getGmtCreate()))
            .map(InvoiceReceiptE::getId)
            .collect(Collectors.toList());
        List<InvoiceReceiptDetailE> invoiceReceiptDetailEList = invoiceDomainService.queryDetailByIds(InvoiceReceiptIds);
        // 找到所有符合的票并计算金额
        return invoiceReceiptDetailEList.stream()
            .map(invoiceBillDto -> invoiceBillDto.getInvoiceAmount() * Double.parseDouble(invoiceBillDto.getTaxRate()))
            .count();
    }

    @Override
    public Boolean judgeSingleBillStatus(BillInferenceV bill, BillTypeEnum billTypeEnum, String supCpUnitId) {
        return bill.getInvoiceState() != 3;
    }
}
