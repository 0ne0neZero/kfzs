package com.wishare.finance.apps.service.voucher.eventinference;

import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.model.bill.vo.BillSettleV;
import com.wishare.finance.apps.service.voucher.AbstractVoucherInferenceAppService;
import com.wishare.finance.domains.bill.consts.enums.BillStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.ActionEventEnum;
import com.wishare.finance.domains.voucher.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherE;
import com.wishare.finance.domains.voucher.entity.VoucherRuleE;
import com.wishare.tools.starter.fo.search.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @description: 收入 收款结算
 * @author: pgq
 * @since: 2022/11/15 15:11
 * @version: 1.0.0
 */
@Service
public class IncomeSettleVoucherInferenceAppService extends AbstractVoucherInferenceAppService {

    @Override
    public EventTypeEnum getEventType() {
//        return EventTypeEnum.收入_收款结算;
        return EventTypeEnum.收款结算;
    }

    @Override
    public void inference(VoucherRuleE record, boolean isSingle) {
        super.inference(record, isSingle);
    }

    @Override
    public void getBillStatus(VoucherRuleE record, List<Field> fieldList) {
        fieldList.add(new Field("b.state", BillStateEnum.正常.getCode(), 1));
        fieldList.add(new Field("b.settle_state", Arrays.asList(1, 2), 15));
        fieldList.add(new Field("b.account_handed", Arrays.asList(1, 2), 15));
        fieldList.add(new Field("b.verify_state", 0, 1));
    }

    @Override
    public void inferenceByBillType(Integer eventType, List<Field> fieldList,
        String conditions, VoucherRuleE record, boolean isSingle) {
        fieldList.clear();
        getCommunityId(conditions, fieldList, "bd");
        getStatutoryBodyId(conditions, fieldList, "b");
        getChargeItemId(conditions, record, fieldList, "bd");
        getPayChannel(conditions, fieldList, "bd");
        getCustomer(conditions, fieldList, "b");
        getStartTime(conditions, fieldList, "b");
        getEndTime(conditions, fieldList, "b");
        getPayTime(conditions, fieldList, "bd");
        super.inferenceDetailByBillType(eventType, fieldList, conditions, record, isSingle, BillTypeEnum.收款单);
    }

    @Override
    public Long generateInferenceAmount(BillInferenceV bill, BillTypeEnum billType) {
        // 在查询时已加入结算金额
        return bill.getReceivableAmount();
//        return getSettleAmount(bill, getEventType(), billType);
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
        return true;
    }

    /**
     * 获取本次结算金额
     * @param bill
     * @param eventTypeEnum
     * @param billType
     * @return
     */
    private Long getSettleAmount(BillInferenceV bill, EventTypeEnum eventTypeEnum, BillTypeEnum billType) {
        VoucherE voucher = voucherDomainService.getLastVoucher(bill.getBillNo(), eventTypeEnum);
        List<BillSettleV> billSettles = billFacade.getBillSettle(Collections.singletonList(bill.getId()), billType, bill.getCommunityId());
        if (voucher == null || voucher.getAmount() == null) {
            return bill.getSettleAmount();
        }
        if (CollectionUtils.isEmpty(billSettles)) {
            return 0L;
        }
        List<BillSettleV> billSettleList = billSettles.stream()
            .filter(settle -> settle.getGmtCreate().isAfter(voucher.getGmtCreate()))
            .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(billSettleList)) {
            return 0L;
        }
        return billSettleList.stream().map(BillSettleV::getSettleAmount).count();
    }
}
