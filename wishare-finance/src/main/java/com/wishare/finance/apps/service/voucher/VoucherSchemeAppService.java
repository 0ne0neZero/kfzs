package com.wishare.finance.apps.service.voucher;

import com.wishare.finance.apps.model.voucher.fo.AddVoucherSchemeF;
import com.wishare.finance.apps.model.voucher.fo.DeleteVoucherSchemeF;
import com.wishare.finance.apps.model.voucher.fo.EnableVoucherSchemeF;
import com.wishare.finance.apps.model.voucher.fo.RelateSchemeVoucherRuleF;
import com.wishare.finance.domains.voucher.command.UpdateVoucherSchemeCommand;
import com.wishare.finance.domains.voucher.entity.VoucherScheme;
import com.wishare.finance.domains.voucher.service.VoucherSchemeDomainService;
import com.wishare.starter.Global;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 凭证核算方案应用服务
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/3
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherSchemeAppService {


    private final VoucherSchemeDomainService voucherSchemeDomainService;

    /**
     * 新增凭证核算方案
     * @param addVoucherSchemeF
     * @return
     */
    @Transactional
    public Long addVoucherScheme(AddVoucherSchemeF addVoucherSchemeF){
       return voucherSchemeDomainService.addVoucherScheme(Global.mapperFacade.map(addVoucherSchemeF, VoucherScheme.class));
    }

    /**
     * 更新凭证核算方案
     * @param updateVoucherSchemeCommand
     * @return
     */
    @Transactional
    public boolean updateVoucherScheme(UpdateVoucherSchemeCommand updateVoucherSchemeCommand) {
        return voucherSchemeDomainService.updateVoucherScheme(updateVoucherSchemeCommand);
    }

    @Transactional
    public boolean enableScheme(EnableVoucherSchemeF enableVoucherSchemeF) {
        return voucherSchemeDomainService.enableScheme(enableVoucherSchemeF.getVoucherSchemeId(), enableVoucherSchemeF.getDisabled());
    }

    @Transactional
    public Boolean deleteScheme(DeleteVoucherSchemeF deleteVoucherSchemeF) {
        return voucherSchemeDomainService.deleteScheme(deleteVoucherSchemeF.getVoucherSchemeId());
    }

    public boolean relateRule(RelateSchemeVoucherRuleF relateSchemeVoucherRuleF) {
        return voucherSchemeDomainService.relateRule(relateSchemeVoucherRuleF.getVoucherRuleId(), relateSchemeVoucherRuleF.getVoucherSchemeIds());
    }
}
