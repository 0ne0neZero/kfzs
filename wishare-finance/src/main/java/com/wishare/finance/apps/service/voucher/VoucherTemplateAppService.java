package com.wishare.finance.apps.service.voucher;

import com.wishare.finance.apps.model.voucher.fo.AddVoucherTemplateF;
import com.wishare.finance.apps.model.voucher.fo.DeleteVoucherTemplateF;
import com.wishare.finance.apps.model.voucher.fo.EnableVoucherTemplateF;
import com.wishare.finance.apps.model.voucher.fo.UpdateVoucherTemplateF;
import com.wishare.finance.domains.voucher.entity.VoucherTemplate;
import com.wishare.finance.domains.voucher.service.VoucherTemplateDomainService;
import com.wishare.starter.Global;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 凭证模板应用服务
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/3
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherTemplateAppService {

    private final VoucherTemplateDomainService voucherTemplateDomainService;

    /**
     * 新增凭证模板
     * @param addVoucherTemplateF
     * @return
     */
    @Transactional
    public Long addTemplate(AddVoucherTemplateF addVoucherTemplateF){
        return voucherTemplateDomainService.addTemplate(Global.mapperFacade.map(addVoucherTemplateF, VoucherTemplate.class));
    }


    /**
     * 更新凭证模板
     * @param updateVoucherTemplateF
     * @return
     */
    @Transactional
    public boolean updateTemplate(UpdateVoucherTemplateF updateVoucherTemplateF) {
        return voucherTemplateDomainService.updateTemplate(Global.mapperFacade.map(updateVoucherTemplateF, VoucherTemplate.class));
    }

    /**
     * 启用禁用模板
     * @param enableVoucherTemplateF
     * @return
     */
    @Transactional
    public boolean enableTemplate(EnableVoucherTemplateF enableVoucherTemplateF) {
        return voucherTemplateDomainService.enableTemplate(enableVoucherTemplateF.getVoucherTemplateId(), enableVoucherTemplateF.getDisabled());
    }

    /**
     * 删除凭证模板
     * @param deleteVoucherTemplateF
     * @return
     */
    @Transactional
    public boolean deleteTemplate(DeleteVoucherTemplateF deleteVoucherTemplateF) {
        return voucherTemplateDomainService.deleteTemplate(deleteVoucherTemplateF.getVoucherTemplateId());
    }
}
