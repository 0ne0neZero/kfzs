package com.wishare.finance.apps.service.voucher.eventinference;

import com.alibaba.fastjson.JSONArray;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.service.voucher.AbstractVoucherInferenceAppService;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.reconciliation.entity.FlowDetailE;
import com.wishare.finance.domains.reconciliation.service.FlowDetailDomainService;
import com.wishare.finance.domains.voucher.consts.enums.ActionEventEnum;
import com.wishare.finance.domains.voucher.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherE;
import com.wishare.finance.domains.voucher.entity.VoucherInferenceRecordE;
import com.wishare.finance.domains.voucher.entity.VoucherRuleE;
import com.wishare.finance.domains.voucher.model.VoucherRuleCondition;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.fo.OrgFinanceF;
import com.wishare.finance.infrastructure.remote.vo.StatutoryBodyRv;
import com.wishare.finance.infrastructure.remote.vo.yonyounc.SendresultV;
import com.wishare.tools.starter.fo.search.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * @description: 未认领暂收款
 * @author: pgq
 * @since: 2022/12/6 15:41
 * @version: 1.0.0
 */
@Service
public class UnclaimedVoucherInferenceAppService extends AbstractVoucherInferenceAppService {

    @Autowired
    private FlowDetailDomainService flowDetailDomainService;

    @Override
    public EventTypeEnum getEventType() {
        return EventTypeEnum.未认领暂收款;
    }

    @Override
    public void getBillStatus(VoucherRuleE record, List<Field> fieldList) {
    }

    @Override
    public void inference(VoucherRuleE record, boolean isSingle) {
        super.inference(record, isSingle);
    }

    @Override
    public Boolean runInferenceByRule(Long ruleId) {
        return super.runInferenceByRule(ruleId);
    }

    @Override
    @Transactional
    public void inferenceByBillType(Integer eventType, List<Field> fieldList,
        String conditions, VoucherRuleE record, boolean isSingle) {
        List<String> names = new ArrayList<>();
        Integer nameCompare = 1;

        Optional<VoucherRuleCondition> conditionOptional = getCondition(conditions, "1");
        if (conditionOptional.isPresent()) {
            VoucherRuleCondition<JSONArray> condition = conditionOptional.get();
            JSONArray values = condition.getValue();
            nameCompare = getMethodNum(condition.getCompare());
            if (!values.isEmpty()) {
                List<Long> finalStatutoryBodyIds = values.toJavaList(Long.class);
                if (!CollectionUtils.isEmpty(finalStatutoryBodyIds)) {
                    OrgFinanceF orgFinanceF = new OrgFinanceF();
                    orgFinanceF.setFinanceType("CORPORATOR_ITSELF");
                    List<StatutoryBodyRv> list = accountOrgFacade.getOrgFinanceList(orgFinanceF);
                    names = list.stream().filter(rv -> finalStatutoryBodyIds.contains(rv.getId())).map(StatutoryBodyRv::getNameCn).collect(Collectors.toList());
                }
            }
        }
        fieldList.clear();
        getIncomeBankAccount(conditions, fieldList, "f", "our_account");
        getPayBankAccount(conditions, fieldList, "f", "opposite_account");
        Integer flowState = getFlowState(conditions);
        Integer flowStateCompare = getFlowStateCompare(conditions);

        List<FlowDetailE> incomeFlowDetailList = flowDetailDomainService.listByConditions(names, nameCompare, fieldList, 1, flowState, flowStateCompare);
        String name = "";
        Long amount = 0L;
        if (!CollectionUtils.isEmpty(incomeFlowDetailList)) {
            amount = incomeFlowDetailList.stream().mapToLong(FlowDetailE::getSettleAmount).sum();
            name = incomeFlowDetailList.get(0).getOurName();
        }

        fieldList.clear();
        getIncomeBankAccount(conditions, fieldList, "f", "opposite_account");
        getPayBankAccount(conditions, fieldList, "f", "our_account");
        List<FlowDetailE> payFlowDetailList = flowDetailDomainService.listByConditions(names, nameCompare, fieldList, 2, flowState, flowStateCompare);
        if (!CollectionUtils.isEmpty(payFlowDetailList)) {
            amount = payFlowDetailList.stream().mapToLong(FlowDetailE::getSettleAmount).sum();
        }

        VoucherE voucher = generateVoucher(amount, record, name);
        voucherDomainService.insert(voucher);
        VoucherInferenceRecordE voucherInferenceRecordE = dealVoucherInferRecord(record, voucher);
        BillInferenceV billInferenceRV = new BillInferenceV();
        billInferenceRV.setId(voucher.getBillId());
        billInferenceRV.setGmtCreate(LocalDateTime.now());
        SendresultV sendresultV = externalInference(billInferenceRV, record, null, voucher);
        if (null != sendresultV) {
            voucherDomainService.updateVoucherNoByIds(Collections.singletonList(voucher.getId()), sendresultV.getNo(), true);
            voucherInferenceRecordE.setSuccessState(1);
            voucherInferenceRecordDomainService.updateById(voucherInferenceRecordE);
        }

    }

    public VoucherInferenceRecordE dealVoucherInferRecord(VoucherRuleE voucherRule, VoucherE voucher) {
        VoucherInferenceRecordE record = new VoucherInferenceRecordE();
        record.setChargeItemId(0L);
        record.setChargeItemName("");
        // TODO 数据待填入
        record.setVoucherSystem("用友NCC");

        record.setSuccessState(0);
        record.setVoucherRuleName(voucherRule.getRuleName());
        record.setVoucherRuleId(voucherRule.getId());
        record.setEventType(getEventType().getEvent());
        record.setVoucherIds(voucher.getBillId().toString());

        record.setDebitAmount(voucher.getAmount());
        record.setCreditAmount(voucher.getAmount());

        voucherInferenceRecordDomainService.insert(record);
        return record;
    }

    public VoucherE generateVoucher(Long amount, VoucherRuleE record, String name) {
        VoucherE voucherE = new VoucherE();
        long millis = System.currentTimeMillis();
        voucherE.setBillNo("");
        voucherE.setBillId(millis);
        voucherE.setBillType(0);
        voucherE.setVoucherType("记账凭证");
        voucherE.setVoucherNo(IdentifierFactory.getInstance().serialNumber("voucher", "PZ", 20));
        voucherE.setAmount(amount);
        voucherE.setInferenceState(0);
        voucherE.setStatutoryBodyId(0L);
        voucherE.setStatutoryBodyName(name);

        voucherE.setDetails(getRuleDetails(record, voucherE, BillTypeEnum.预收账单, null, true, null));
        return voucherE;
    }

    /**
     * 流水比较
     * @param jsonStr
     * @return
     */
    private Integer getFlowStateCompare(String jsonStr) {
        Optional<VoucherRuleCondition> optional = getCondition(jsonStr, "15");
        if (optional.isPresent()) {
            VoucherRuleCondition condition = optional.get();
            return getMethodNum(condition.getCompare());
        }
        return null;
    }

    /**
     * 流水
     * @param jsonStr 条件
     */
    private Integer getFlowState(String jsonStr) {
        Optional<VoucherRuleCondition> optional = getCondition(jsonStr, "15");
        if (optional.isPresent()) {
            VoucherRuleCondition<JSONArray> condition = optional.get();
            JSONArray values = condition.getValue();if (!values.isEmpty()) {
                List<Integer> list = values.toJavaList(Integer.class);
                if (list.contains(9)) {
                    return 9;
                }
                if (list.contains(0) && list.contains(1)) {
                    return 9;
                }
                return list.get(0);
            }
        }
        return 9;
    }

    @Override
    public String singleInference(Long billId, BillTypeEnum billTypeEnum, ActionEventEnum actionEventEnum, Long amount, String supCpUintId) {
        return null;
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
