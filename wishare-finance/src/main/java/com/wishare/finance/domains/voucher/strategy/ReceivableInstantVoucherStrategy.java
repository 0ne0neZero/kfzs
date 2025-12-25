package com.wishare.finance.domains.voucher.strategy;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRule;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.entity.VoucherRuleRecord;
import com.wishare.finance.domains.voucher.entity.VoucherScheme;
import com.wishare.finance.domains.voucher.repository.VoucherSchemeOrgRepository;
import com.wishare.finance.domains.voucher.strategy.core.InstantVoucherStrategy;
import com.wishare.finance.domains.voucher.strategy.core.InstantVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.utils.ErrorAssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 应收计提即时触发策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/22
 */
@Service
public class ReceivableInstantVoucherStrategy extends InstantVoucherStrategy<InstantVoucherStrategyCommand> {


    @Autowired
    private VoucherSchemeOrgRepository voucherSchemeOrgRepository;

    public ReceivableInstantVoucherStrategy() {
        super(VoucherEventTypeEnum.应收计提);
    }

    @Override
    public VoucherRuleRecord execute(InstantVoucherStrategyCommand command) {
        //1.拿到触发的单据信息
        List<VoucherBusinessBill> voucherBusinessBills = businessBills(command, null);
        ErrorAssertUtil.notEmptyThrow400(voucherBusinessBills, ErrorMessage.VOUCHER_RUN_BUSINESS_ERROR, command.getBusinessBillType().getValue(), command.getBusinessBillId());
        VoucherBusinessBill voucherBusinessBill = voucherBusinessBills.get(0);
        List<Long> orgIds = new ArrayList<>();
        if (Objects.nonNull(voucherBusinessBill.getStatutoryBodyId())){
            orgIds.add(voucherBusinessBill.getBusinessBillId());
        }
        if (Objects.nonNull(voucherBusinessBill.getCostCenterId())){
            orgIds.add(voucherBusinessBill.getCostCenterId());
        }

        ErrorAssertUtil.isEmptyThrow400(orgIds, ErrorMessage.VOUCHER_RUN_SCHEME_ERROR);

        List<VoucherScheme> voucherSchemes = voucherSchemeOrgRepository.schemeListByOrgIds(orgIds);

        if (voucherSchemes != null){

        }


        //根据业务单据拿规则
        VoucherRule voucherRule = null;
        //执行规则
        return doExecute(command, voucherRule);
    }

    @Override
    public List<VoucherBusinessBill> businessBills(InstantVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        return new ArrayList<>();
    }

}
