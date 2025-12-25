package com.wishare.finance.domains.voucher.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.voucher.vo.VoucherInferenceRecordV;
import com.wishare.finance.domains.voucher.entity.VoucherInferenceRecordE;
import com.wishare.finance.domains.voucher.repository.VoucherInferenceRecordRepository;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/11 19:06
 * @version: 1.0.0
 */
@Service
@Deprecated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherInferenceRecordDomainService {

    private final VoucherInferenceRecordRepository voucherInferenceRecordRepository;

    /**
     * 推凭记录列表（分页）
     * @param form
     * @return
     */
    public PageV<VoucherInferenceRecordV> queryPage(PageF<SearchF<?>> form) {
        Page<VoucherInferenceRecordE> page = voucherInferenceRecordRepository.queryPage(form);
        if (Optional.ofNullable(page).isEmpty()) {
            return new PageV<>();
        }
        return PageV.of(form, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), VoucherInferenceRecordV.class));
    }

    /**
     * 插入推凭记录
     * @param form
     * @return
     */
    public Long insert(VoucherInferenceRecordE form) {
        voucherInferenceRecordRepository.save(form);
        return form.getId();
    }

    /**
     * 修改
     * @param record
     */
    public void updateById(VoucherInferenceRecordE record) {
        voucherInferenceRecordRepository.updateById(record);
    }
}
