package com.wishare.finance.domains.voucher.support.fangyuan.repository;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.pushbill.vo.VoucherBillDetailMoneyV;
import com.wishare.finance.domains.voucher.entity.VoucherBillDetail;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherPushBillDetail;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.mapper.VoucherBillDetailMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherBillDetailZJMapper;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherBillDetailRepository extends ServiceImpl<VoucherBillDetailMapper, VoucherPushBillDetail> {
    private final VoucherBillDetailMapper voucherBillDetailMapper;

    public Page<VoucherPushBillDetail> pageBySearch(PageF<SearchF<?>> searchPageF) {
        return baseMapper.selectBySearch(RepositoryUtil.convertMPPage(searchPageF),
                RepositoryUtil.putLogicDeleted(searchPageF.getConditions().getQueryModel())
                        .eq("visible", 0)
                        .orderByDesc("id"));
    }

    // 根据账单id查询凭证信息
    public List<VoucherPushBillDetail> queryVoucherPushBillDetailInfo(List<Long> billIdList) {
        return baseMapper.queryVoucherPushBillDetailInfo(billIdList);
    }

    public void deleteBatch(List<VoucherPushBillDetail> details) {
        List<Long> detailIds = details.stream().map(VoucherPushBillDetail::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(detailIds)) {
            baseMapper.deleteBatch(detailIds);
        }
    }

    public VoucherBillDetailMoneyV queryMoney(PageF<SearchF<?>> form) {
        QueryWrapper<?> queryWrapper = form.getConditions().getQueryModel();
        queryWrapper.eq("visible", 0);
        return voucherBillDetailMapper.queryMoney(RepositoryUtil.putLogicDeleted(queryWrapper));
    }

    public List<VoucherPushBillDetail> getPushDetails(List<Long> ids) {
       return baseMapper.selectList(new LambdaQueryWrapper<VoucherPushBillDetail>()
                .eq(VoucherPushBillDetail::getBillEventType,1)
                .eq(VoucherPushBillDetail::getDeleted,0)
                .in(VoucherPushBillDetail::getBillId,ids));
    }
}
