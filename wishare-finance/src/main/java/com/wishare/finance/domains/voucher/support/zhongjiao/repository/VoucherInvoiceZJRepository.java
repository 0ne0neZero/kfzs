package com.wishare.finance.domains.voucher.support.zhongjiao.repository;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherInvoiceZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherInvoiceZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherInvoiceZJMapper;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherInvoiceZJRepository extends ServiceImpl<VoucherInvoiceZJMapper, VoucherInvoiceZJ> {
    private final VoucherInvoiceZJMapper VoucherInvoiceZJMapper;

    public List<VoucherInvoiceZJ> getByContractInvoiceId(Long contractInvoiceId) {
        List<VoucherInvoiceZJ> measurementDetailZJS = baseMapper.selectList(getWrapper().eq(VoucherInvoiceZJ::getContractInvoiceId, contractInvoiceId));
        return measurementDetailZJS;
    }

    public List<VoucherInvoiceZJ> search(PageF<SearchF<?>> form) {
        QueryWrapper<?> queryModel = form.getConditions().getQueryModel();
        List<VoucherInvoiceZJ> contractInvoiceZJList = baseMapper.search(queryModel.orderByDesc("id"));
        return contractInvoiceZJList;
    }

    public Page<VoucherInvoiceZJ> pageBySearch(PageF<SearchF<?>> searchPageF) {
        Page<VoucherInvoiceZJ> contractInvoiceZJPage = baseMapper.selectBySearch(RepositoryUtil.convertMPPage(searchPageF),
                RepositoryUtil.putLogicDeleted(searchPageF.getConditions().getQueryModel()).orderByDesc("id"));

        return contractInvoiceZJPage;
    }
    
    private static LambdaQueryWrapper<VoucherInvoiceZJ> getWrapper() {
        return Wrappers.<VoucherInvoiceZJ>lambdaQuery();
    }
}
