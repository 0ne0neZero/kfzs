package com.wishare.finance.domains.voucher.support.fangyuan.strategy;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBillRuleConditionOBV;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherPushBillDetail;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.TriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.ManualBillStrategy;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.ManualBillStrategyCommand;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.PushBusinessBill;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 坏账确认
 */
@Service
public class BadBillConfirmBillStrategy extends ManualBillStrategy<ManualBillStrategyCommand> {

    public BadBillConfirmBillStrategy() {
        super(TriggerEventBillTypeEnum.坏账确认);
    }

    @Override
    public List<PushBusinessBill> businessBills(ManualBillStrategyCommand command, List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        List<PushBusinessBill> badBillConfirmBilList = pushBillFacade.getBadBillConfirmBilList(conditions, communityIds);
        for (PushBusinessBill pushBusinessBill : badBillConfirmBilList) {
            //查询欠费计提推送时间
            VoucherPushBillDetail pushBillDetail = voucherBillDetailRepository.getOne(new LambdaQueryWrapper<VoucherPushBillDetail>()
                    .eq(VoucherPushBillDetail::getBillId, pushBusinessBill.getBillId())
                    .eq(VoucherPushBillDetail::getBillEventType, 3));
            //减免发生在推送未收款开票和欠费计提事件之前 报账单设置为不可见
            if (pushBillDetail.getGmtCreate().isAfter(pushBusinessBill.getBaCreate())) {
                pushBusinessBill.setVisible(1);
            }
        }
        return badBillConfirmBilList;
    }
}
