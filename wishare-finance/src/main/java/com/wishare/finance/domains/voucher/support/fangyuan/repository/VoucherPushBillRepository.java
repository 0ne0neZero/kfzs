package com.wishare.finance.domains.voucher.support.fangyuan.repository;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.pushbill.vo.VoucherBillMoneyV;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBill;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.mapper.VoucherPushBillMapper;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherPushBillRepository extends ServiceImpl<VoucherPushBillMapper, VoucherBill> {
    private final VoucherPushBillMapper voucherPushBillMapper;

    public Page<VoucherBill> pageBySearch(PageF<SearchF<?>> searchPageF) {
        Page<VoucherBill> billPage = baseMapper.selectBySearch(RepositoryUtil.convertMPPage(searchPageF),
                RepositoryUtil.putLogicDeleted(searchPageF.getConditions().getQueryModel()).orderByDesc("gmt_create"));
        if (CollectionUtils.isNotEmpty(billPage.getRecords())) {
            for (VoucherBill record : billPage.getRecords()) {
                if (StringUtils.isNotBlank(record.getNcFinanceNo())) {
                    String finaceNo = record.getNcFinanceNo().substring(record.getNcFinanceNo().indexOf("_") + 1, record.getNcFinanceNo().lastIndexOf("_"));
                    record.setFinanceNo(finaceNo);
                } else {
                    record.setFinanceNo(null);
                }
            }
        }
        return billPage;
    }

    public void delete(VoucherBill voucherBill) {
        baseMapper.delete(voucherBill.getId());
    }

    public VoucherBillMoneyV getMoney(PageF<SearchF<?>> form) {
        QueryWrapper<?> queryWrapper = form.getConditions().getQueryModel();
        return voucherPushBillMapper.getMoney(RepositoryUtil.putLogicDeleted(queryWrapper));
    }

    public VoucherBill getVoucherBill(String voucherBillNo) {
        return baseMapper.selectOne(new LambdaQueryWrapper<VoucherBill>()
                .eq(VoucherBill::getVoucherBillNo, voucherBillNo));
    }
}
