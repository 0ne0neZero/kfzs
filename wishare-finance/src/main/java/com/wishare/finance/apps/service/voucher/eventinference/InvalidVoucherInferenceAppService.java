package com.wishare.finance.apps.service.voucher.eventinference;

import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.service.voucher.AbstractVoucherInferenceAppService;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.BillRefundDto;
import com.wishare.finance.domains.voucher.consts.enums.ActionEventEnum;
import com.wishare.finance.domains.voucher.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleE;
import com.wishare.tools.starter.fo.search.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * @description: 收入 冲销作废
 * @author: pgq
 * @since: 2022/11/15 15:11
 * @version: 1.0.0
 */
@Service
public class InvalidVoucherInferenceAppService extends AbstractVoucherInferenceAppService {

    public static final Set<Long> inferRefundIds = new HashSet<>();

    @Override
    public EventTypeEnum getEventType() {
//        return EventTypeEnum.收入_冲销作废;
        return EventTypeEnum.冲销作废;
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
        billInference(eventType, fieldList, conditions, record, BillTypeEnum.应付账单, isSingle);

        fieldList.clear();
        getCommunityId(conditions, fieldList, "bd");
        getStatutoryBodyId(conditions, fieldList, "b");
        getChargeItemId(conditions, record, fieldList, "bd");
        getPayChannel(conditions, fieldList, "bd");
        getCustomer(conditions, fieldList, "b");
        getStartTime(conditions, fieldList, "b");
        getEndTime(conditions, fieldList, "b");
        getPayTime(conditions, fieldList, "bd");
        billInference(eventType, fieldList, conditions, record, BillTypeEnum.付款单, isSingle);
        billInference(eventType, fieldList, conditions, record, BillTypeEnum.收款单, isSingle);
    }

    @Override
    public void getBillStatus(VoucherRuleE record, List<Field> fieldList) {

        fieldList.add(new Field("b.verify_state", 0, 1));
    }

    @Override
    public Long generateInferenceAmount(BillInferenceV bill, BillTypeEnum billType) {
        if (bill.getState() == 2 || bill.getReversed() == 1) {
            return bill.getTotalAmount();
        } else if (bill.getRefundState() == 2 || bill.getRefundState() == 3) {
            return getRefundAmountByBill(bill);
        }
        return 0L;
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
    public Boolean judgeSingleBillStatus(BillInferenceV bill, BillTypeEnum billTypeEnum, String supCpUnitId) {
        return bill.getRefundState() == 0 && bill.getReversed() == 0 && bill.getState() != 2;
    }

    private Long getRefundAmountByBill(BillInferenceV bill) {
        List<BillRefundDto> billRefundRvs = billFacade.listBillRefund(bill.getId());
        List<BillRefundDto> collect = billRefundRvs.stream()
            .filter(billRefundRv -> billRefundRv.getInferenceState() == 1)
            .collect(Collectors.toList());
        if (collect.isEmpty()) {
            return 0L;
        }
        // 记录所有退款记录id 用于后期修改推凭状态
        inferRefundIds.addAll(collect.stream().map(BillRefundDto::getId).collect(Collectors.toSet()));
        return collect.stream().mapToLong(BillRefundDto::getRefundAmount).sum();
    }

    @Override
    public void doAfterInfer() {
        billFacade.batchUpdateRefundInferenceState(inferRefundIds);
        inferRefundIds.clear();
    }
}
