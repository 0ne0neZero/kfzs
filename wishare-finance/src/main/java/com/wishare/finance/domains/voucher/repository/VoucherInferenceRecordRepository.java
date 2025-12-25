package com.wishare.finance.domains.voucher.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.voucher.entity.VoucherInferenceRecordE;
import com.wishare.finance.domains.voucher.repository.mapper.VoucherInferenceRecordMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/11 19:06
 * @version: 1.0.0
 */
@Service
public class VoucherInferenceRecordRepository extends ServiceImpl<VoucherInferenceRecordMapper, VoucherInferenceRecordE> {

    @Autowired
    private VoucherInferenceRecordMapper voucherInferenceRecordMapper;

    public Page<VoucherInferenceRecordE> queryPage(PageF<SearchF<?>> form) {
        QueryWrapper<?> queryModel = form.getConditions().getQueryModel();
        queryModel.eq("v.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.orderByDesc("v.gmt_create");
        return voucherInferenceRecordMapper.queryPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel);
    }
}
