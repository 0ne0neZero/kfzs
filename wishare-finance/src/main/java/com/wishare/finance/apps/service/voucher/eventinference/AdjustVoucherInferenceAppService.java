package com.wishare.finance.apps.service.voucher.eventinference;

import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.service.voucher.AbstractVoucherInferenceAppService;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.ActionEventEnum;
import com.wishare.finance.domains.voucher.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleE;
import com.wishare.tools.starter.fo.search.Field;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @description: 收入 账单调整
 * @author: pgq
 * @since: 2022/11/15 15:11
 * @version: 1.0.0
 */
@Service
public class AdjustVoucherInferenceAppService extends AbstractVoucherInferenceAppService {

    @Override
    public EventTypeEnum getEventType() {
//        return EventTypeEnum.收入_账单调整;
        return EventTypeEnum.账单调整;
    }

    @Override
    public void inference(VoucherRuleE record, boolean isSingle) {
        super.inference(record, isSingle);
    }

    @Override
    public void inferenceByBillType(Integer eventType, List<Field> fieldList,
        String conditions, VoucherRuleE record, boolean isSingle) {
        super.billInference(eventType, fieldList, conditions, record, BillTypeEnum.应收账单, isSingle);
        super.billInference(eventType, fieldList, conditions, record, BillTypeEnum.预收账单, isSingle);
        super.billInference(eventType, fieldList, conditions, record, BillTypeEnum.应付账单, isSingle);
    }

    @Override
    public void getBillStatus(VoucherRuleE record, List<Field> fieldList) {

        fieldList.add(new Field("b.verify_state", 0, 1));
    }

    @Override
    public Long generateInferenceAmount(BillInferenceV bill, BillTypeEnum billType) {
        return getAdjustAmount(bill);
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
        return CollectionUtils.isEmpty(getBillSettleList(bill.getId(), billTypeEnum, supCpUnitId));
    }

    /**
     * 获取调整的金额
     * @param bill
     * @return
     */
    private Long getAdjustAmount(BillInferenceV bill) {
        // 再查询时，已经将账单调整的金额累加
        return bill.getReceivableAmount();
    }
}
