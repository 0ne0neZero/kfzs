package com.wishare.finance.apps.service.voucher.eventinference;

import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.service.voucher.AbstractVoucherInferenceAppService;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.ActionEventEnum;
import com.wishare.finance.domains.voucher.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleE;
import com.wishare.tools.starter.fo.search.Field;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 收入 结账销账
 * @author: pgq
 * @since: 2022/11/15 15:11
 * @version: 1.0.0
 */
@Service
public class VerifyVoucherInferenceAppService extends AbstractVoucherInferenceAppService {

    @Override
    public EventTypeEnum getEventType() {
        return EventTypeEnum.预收应收核销;
//        return EventTypeEnum.收入_结账销账;
    }

    @Override
    public void getBillStatus(VoucherRuleE record, List<Field> fieldList) {
        fieldList.add(new Field("b.verify_state", 1, 1));
    }

    @Override
    public void inferenceByBillType(Integer eventType, List<Field> fieldList,
        String conditions, VoucherRuleE record, boolean isSingle) {
        billInference(eventType, fieldList, conditions, record, BillTypeEnum.预收账单, isSingle);
        billInference(eventType, fieldList, conditions, record, BillTypeEnum.应收账单, isSingle);
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
    public Boolean judgeSingleBillStatus(BillInferenceV bill, BillTypeEnum billTypeEnum, String supCpUnitId) {
        return bill.getVerifyState() != 1;
    }
}
