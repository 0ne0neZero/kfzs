package com.wishare.finance.apps.service.voucher;

import com.wishare.finance.apps.model.voucher.fo.AddVoucherRuleF;
import com.wishare.finance.apps.model.voucher.fo.DeleteVoucherRuleF;
import com.wishare.finance.apps.model.voucher.fo.EnableVoucherRuleF;
import com.wishare.finance.apps.model.voucher.fo.RunVoucherRuleF;
import com.wishare.finance.apps.model.voucher.fo.UpdateVoucherRuleF;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleConditionTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRule;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOptionOBV;
import com.wishare.finance.domains.voucher.service.VoucherRuleDomainService;
import com.wishare.starter.Global;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 凭证规则 应用服务
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/3
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherRuleAppService {

    private final VoucherRuleDomainService voucherRuleDomainService;


    @Transactional
    public Long addRule(AddVoucherRuleF addVoucherRuleF) {
        return voucherRuleDomainService.addRule(Global.mapperFacade.map(addVoucherRuleF, VoucherRule.class));
    }

    @Transactional
    public boolean updateRule(UpdateVoucherRuleF updateVoucherRuleF) {
        return voucherRuleDomainService.updateRule(Global.mapperFacade.map(updateVoucherRuleF, VoucherRule.class));
    }

    public boolean enableVoucherRule(EnableVoucherRuleF enableVoucherRuleF) {
        return voucherRuleDomainService.enable(enableVoucherRuleF.getVoucherRuleId(), enableVoucherRuleF.getDisabled());
    }

    @Transactional
    public boolean deleteVoucherRule(DeleteVoucherRuleF deleteVoucherTemplateF) {
        return voucherRuleDomainService.delete(deleteVoucherTemplateF.getVoucherRuleId());
    }

    public boolean executeVoucherRule(RunVoucherRuleF runVoucherRuleF) {
        return voucherRuleDomainService.manualExecute(runVoucherRuleF.getVoucherRuleId());
    }

    public List<VoucherRuleConditionOptionOBV> getConditionOptions(VoucherRuleConditionTypeEnum conditionType){
        return voucherRuleDomainService.getConditionOptions(conditionType);
    }

}
