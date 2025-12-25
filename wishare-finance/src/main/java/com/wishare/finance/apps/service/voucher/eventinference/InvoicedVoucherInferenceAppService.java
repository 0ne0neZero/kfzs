package com.wishare.finance.apps.service.voucher.eventinference;

import com.wishare.finance.apps.model.bill.fo.InvoiceBillDto;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.service.voucher.AbstractVoucherInferenceAppService;
import com.wishare.finance.domains.bill.consts.enums.BillStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.voucher.consts.enums.ActionEventEnum;
import com.wishare.finance.domains.voucher.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleE;
import com.wishare.tools.starter.fo.search.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * @description: 收入 账单开票
 * @author: pgq
 * @since: 2022/11/15 15:11
 * @version: 1.0.0
 */
@Service
public class InvoicedVoucherInferenceAppService extends AbstractVoucherInferenceAppService {


    @Override
    public EventTypeEnum getEventType() {
//        return EventTypeEnum.收入_账单开票;
        return EventTypeEnum.账单开票;
    }

    @Override
    public void inference(VoucherRuleE record, boolean isSingle) {
        super.inference(record, isSingle);
    }

    @Override
    public void inferenceByBillType(Integer eventType, List<Field> fieldList,
        String conditions, VoucherRuleE record, boolean isSingle) {
        billInference(eventType, fieldList, conditions, record, BillTypeEnum.预收账单, isSingle);
        billInference(eventType, fieldList, conditions, record, BillTypeEnum.应收账单, isSingle);
    }

    @Override
    public void getBillStatus(VoucherRuleE record, List<Field> fieldList) {
        fieldList.add(new Field("b.state", BillStateEnum.正常.getCode(), 1));
        fieldList.add(new Field("b.invoice_state", 3, 1));
        fieldList.add(new Field("b.approved_state", 2, 1));
        fieldList.add(new Field("b.verify_state", 0, 1));
    }

    @Override
    public Long generateInferenceAmount(BillInferenceV bill, BillTypeEnum billType) {
        return getBillInvoiceAmount(bill, billType);
    }

    @Override
    public Boolean runInferenceByRule(Long ruleId) {
        return super.runInferenceByRule(ruleId);
    }

    @Override
    public String singleInference(Long billId, BillTypeEnum billTypeEnum,
        ActionEventEnum actionEventEnum, Long amount, String supCpUnitId) {
        return super.singleInference(billId, billTypeEnum, actionEventEnum, amount, supCpUnitId);
    }

    @Override
    public Boolean judgeSingleBillStatus(BillInferenceV bill, BillTypeEnum billTypeEnum, String supCpUnitId) {
        return bill.getInvoiceState() != 3;
    }

    /**
     * 获取账单的开票金额
     * @param bill
     * @param billType
     * @return
     */
    private Long getBillInvoiceAmount(BillInferenceV bill, BillTypeEnum billType) {

        Optional<Map<Long, List<InvoiceBillDto>>> billInvoiceMap = invoiceDomainService.getBillInvoiceMap(
            Collections.singletonList(bill.getId()), billType.getCode());
        if (billInvoiceMap.isPresent() && billInvoiceMap.get().containsKey(bill.getId())) {
            List<InvoiceBillDto> list = billInvoiceMap.get().get(bill.getId());
            if (list.isEmpty()) {
                return 0L;
            }
            long amount = list.stream().mapToLong(InvoiceBillDto::getInvoiceAmount).sum();

            List<InvoiceBillDto> partRedList = list.stream().filter(
                invoiceBillDto -> Objects.equals(InvoiceReceiptStateEnum.部分红冲.getCode(),
                    invoiceBillDto.getState())).collect(Collectors.toList());
            if (!partRedList.isEmpty()) {
                List<InvoiceBillDto> redList = invoiceDomainService.listPartRedInvoices(
                    partRedList.stream().map(InvoiceBillDto::getId).collect(Collectors.toList()),
                    bill.getId());
                if (!redList.isEmpty()) {
                    amount = amount - redList.stream().mapToLong(InvoiceBillDto::getInvoiceAmount).sum();
                }
            }
            return amount;
        }
        return 0L;
    }
}
