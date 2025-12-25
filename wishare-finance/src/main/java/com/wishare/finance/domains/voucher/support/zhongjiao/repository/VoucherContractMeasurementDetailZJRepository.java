package com.wishare.finance.domains.voucher.support.zhongjiao.repository;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.pushbill.vo.FlowClaimFilesV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJCashFlowV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJConvertDetailV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJConvertMoneyV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJDetailMoneyV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJFlowDetailV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJRecDetailV;
import com.wishare.finance.domains.voucher.enums.VoucherBillTypeEnums;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherContractMeasurementDetailZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherBillDetailZJMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherContractMeasurementDetailZJMapper;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherContractMeasurementDetailZJRepository extends ServiceImpl<VoucherContractMeasurementDetailZJMapper, VoucherContractMeasurementDetailZJ> {
    private final VoucherContractMeasurementDetailZJMapper voucherContractMeasurementDetailZJMapper;

    public Page<VoucherContractMeasurementDetailZJ> pageBySearch(PageF<SearchF<?>> searchPageF) {
        Page<VoucherContractMeasurementDetailZJ> measurementDetailZJPage = baseMapper.selectBySearch(RepositoryUtil.convertMPPage(searchPageF),
                RepositoryUtil.putLogicDeleted(searchPageF.getConditions().getQueryModel()).orderByDesc("id"));
        if (CollectionUtils.isNotEmpty(measurementDetailZJPage.getRecords())) {
            for (VoucherContractMeasurementDetailZJ measurementDetailZJ : measurementDetailZJPage.getRecords()) {
                measurementDetailZJ.setTaxRate(measurementDetailZJ.getTaxRate().multiply(new BigDecimal("100")));
            }
        }
        return measurementDetailZJPage;
    }

    public void deleteByContractInvoiceId(Long contractInvoiceId) {
        baseMapper.deleteByContractInvoiceId(contractInvoiceId);
    }

    public List<VoucherContractMeasurementDetailZJ> getByContractInvoiceId(Long contractInvoiceId) {
        List<VoucherContractMeasurementDetailZJ> measurementDetailZJS = baseMapper.selectList(getWrapper().eq(VoucherContractMeasurementDetailZJ::getContractInvoiceId, contractInvoiceId));
        return measurementDetailZJS;
    }

    private static LambdaQueryWrapper<VoucherContractMeasurementDetailZJ> getWrapper() {
        return Wrappers.<VoucherContractMeasurementDetailZJ>lambdaQuery();
    }
}
