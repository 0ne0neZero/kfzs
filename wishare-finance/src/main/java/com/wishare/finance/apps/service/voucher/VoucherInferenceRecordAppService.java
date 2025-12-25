package com.wishare.finance.apps.service.voucher;

import com.wishare.finance.apps.model.voucher.fo.VoucherInferenceRecordF;
import com.wishare.finance.apps.model.voucher.vo.VoucherInferenceRecordV;
import com.wishare.finance.domains.voucher.entity.VoucherInferenceRecordE;
import com.wishare.finance.domains.voucher.service.VoucherInferenceRecordDomainService;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/11 18:57
 * @version: 1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherInferenceRecordAppService {

    private final VoucherInferenceRecordDomainService voucherInferenceRecordDomainService;

    /**
     * 获取推推凭记录 （分页）
     * @param form
     * @return
     */
    public PageV<VoucherInferenceRecordV> queryPage(PageF<SearchF<?>> form) {

        return voucherInferenceRecordDomainService.queryPage(form);
    }

    /**
     * 新增推凭记录
     * @param form
     * @return
     */
    public Long add(VoucherInferenceRecordF form) {

        return voucherInferenceRecordDomainService.insert(Global.mapperFacade.map(form, VoucherInferenceRecordE.class));
    }
}
