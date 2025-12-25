package com.wishare.finance.domains.voucher.strategy;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherBusinessDetail;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.strategy.core.TaskVoucherStrategy;
import com.wishare.finance.domains.voucher.strategy.core.TaskVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 应收计提定时任务策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/22
 */
@Slf4j
@Service
public class ReceivableTaskVoucherStrategy extends TaskVoucherStrategy<TaskVoucherStrategyCommand> {


    public ReceivableTaskVoucherStrategy() {
        super(VoucherEventTypeEnum.应收计提);
    }

    @Override
    public List<VoucherBusinessBill> businessBills(TaskVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        return voucherFacade.getReceivableList(conditions, command, VoucherEventTypeEnum.应收计提.getCode());
    }

    @Override
    protected void modifyInferenceStatus(List<VoucherBusinessDetail> voucherBusinessDetails) {
        voucherFacade.receivableModifyInferenceStatus(voucherBusinessDetails);
    }
}
