package com.wishare.finance.domains.voucher.support.zhongjiao.repository;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.BillRule;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherContractInvoiceZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherContractInvoiceZJMapper;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherContractInvoiceZJRepository extends ServiceImpl<VoucherContractInvoiceZJMapper, VoucherContractInvoiceZJ> {
    private final VoucherContractInvoiceZJMapper contractInvoiceZJMapper;

    public Page<VoucherContractInvoiceZJ> pageBySearch(PageF<SearchF<?>> searchPageF) {
        Page<VoucherContractInvoiceZJ> contractInvoiceZJPage = baseMapper.selectBySearch(RepositoryUtil.convertMPPage(searchPageF),
                RepositoryUtil.putLogicDeleted(searchPageF.getConditions().getQueryModel()).orderByDesc("id"));

        return contractInvoiceZJPage;
    }


    public VoucherContractInvoiceZJ getByVoucherBillId (Long voucherBillId) {
        return getOne(new LambdaQueryWrapper<VoucherContractInvoiceZJ>().eq(VoucherContractInvoiceZJ::getVoucherBillId, voucherBillId));
    }

    public List<VoucherContractInvoiceZJ> getByVoucherBillIdList(List<Long> voucherBillIdList) {
        return list(getWrapper().in(VoucherContractInvoiceZJ::getVoucherBillId, voucherBillIdList));
    }

    public List<VoucherContractInvoiceZJ> selectBySearchNoPage(PageF<SearchF<?>> searchPageF) {
        List<VoucherContractInvoiceZJ> contractInvoiceZJPage = baseMapper.selectBySearchNoPage(RepositoryUtil.putLogicDeleted(searchPageF.getConditions().getQueryModel()).orderByDesc("id"));
        return contractInvoiceZJPage;
    }

    public void deleteById(Long id) {
        baseMapper.deleteById(id);
    }
    private static LambdaQueryWrapper<VoucherContractInvoiceZJ> getWrapper() {
        return Wrappers.<VoucherContractInvoiceZJ>lambdaQuery();
    }
}
