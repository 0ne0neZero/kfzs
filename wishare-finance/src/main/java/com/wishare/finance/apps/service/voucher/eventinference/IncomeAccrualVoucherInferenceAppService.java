package com.wishare.finance.apps.service.voucher.eventinference;

import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.service.voucher.AbstractVoucherInferenceAppService;
import com.wishare.finance.domains.bill.consts.enums.BillStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.ActionEventEnum;
import com.wishare.finance.domains.voucher.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleE;
import com.wishare.tools.starter.fo.search.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @description: 收入 应收计提
 * @author: pgq
 * @since: 2022/11/15 15:11
 * @version: 1.0.0
 */
@Service
public class IncomeAccrualVoucherInferenceAppService extends AbstractVoucherInferenceAppService {

    @Override
    public EventTypeEnum getEventType() {
//        return EventTypeEnum.收入_应收计提;
        return EventTypeEnum.应收计提;
    }

    @Override
    public void inference(VoucherRuleE record, boolean isSingle) {
        super.inference(record, isSingle);
    }

    @Override
    public void inferenceByBillType(Integer eventType, List<Field> fieldList,
        String conditions, VoucherRuleE record, boolean isSingle) {
        super.getPayTime(conditions, fieldList, "b");
        super.getIncomeBankAccount(conditions, fieldList, "b", "sb_account_id");
        super.billInference(eventType, fieldList, conditions, record, BillTypeEnum.应收账单, isSingle);
    }

    @Override
    public void getBillStatus(VoucherRuleE record, List<Field> fieldList) {
        fieldList.add(new Field("b.state", BillStateEnum.正常.getCode(), 1));
        fieldList.add(new Field("b.approved_state", 2, 1));
        fieldList.add(new Field("b.reconcile_state", Arrays.asList(0, 1, 2), 15));
        fieldList.add(new Field("b.verify_state", 0, 1));
        LocalDateTime lastMonthDay = LocalDateTime.of(
            LocalDate.from(LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth())),
            LocalTime.MAX);
        fieldList.add(new Field("b.end_time", lastMonthDay, 6));
    }

    @Override
    public Long generateInferenceAmount(BillInferenceV bill, BillTypeEnum billType) {
        return bill.getReceivableAmount();
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
    public Boolean judgeSingleBillStatus(BillInferenceV bill,
        BillTypeEnum billTypeEnum, String supCpUnitId) {
        return bill.getInferenceStatus() == 1 || bill.getApprovedState() != 2;
    }

}
