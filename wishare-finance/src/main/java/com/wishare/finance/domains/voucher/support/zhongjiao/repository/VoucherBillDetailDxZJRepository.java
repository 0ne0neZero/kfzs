package com.wishare.finance.domains.voucher.support.zhongjiao.repository;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.pushbill.vo.*;
import com.wishare.finance.apps.pushbill.vo.dx.vo.DxCostDetailsV;
import com.wishare.finance.apps.pushbill.vo.dx.vo.DxPaymentDetailsV;
import com.wishare.finance.domains.voucher.enums.VoucherBillTypeEnums;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailDxZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherBillDetailDxZJMapper;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherBillDetailDxZJRepository extends ServiceImpl<VoucherBillDetailDxZJMapper, VoucherPushBillDetailDxZJ> {
    private final VoucherBillDetailDxZJMapper voucherBillDetailZJMapper;

    public Page<VoucherPushBillDetailDxZJ> pageBySearch(PageF<SearchF<?>> searchPageF) {
        Page<VoucherPushBillDetailDxZJ> voucherPushBillDetailZJPage = baseMapper.selectBySearch(RepositoryUtil.convertMPPage(searchPageF),
                RepositoryUtil.putLogicDeleted(searchPageF.getConditions().getQueryModel()).orderByDesc("id"));
        if (CollectionUtils.isNotEmpty(voucherPushBillDetailZJPage.getRecords())) {
            for (VoucherPushBillDetailDxZJ voucherPushBillDetailZJ : voucherPushBillDetailZJPage.getRecords()) {
                voucherPushBillDetailZJ.setTaxRate(voucherPushBillDetailZJ.getTaxRate().multiply(new BigDecimal("100")));
            }
        }
        return voucherPushBillDetailZJPage;
    }


    public List<VoucherPushBillDetailDxZJ> selectVoucherPushBillDetailDxZJ(PageF<SearchF<?>> searchPageF) {
        List<VoucherPushBillDetailDxZJ> id = baseMapper.selectVoucherPushBillDetailDxZJ(RepositoryUtil.putLogicDeleted(searchPageF.getConditions().getQueryModel())
                .orderByDesc("id"));
        if (CollectionUtils.isNotEmpty(id)){
            for (VoucherPushBillDetailDxZJ voucherPushBillDetailZJ : id) {
                voucherPushBillDetailZJ.setTaxRate(voucherPushBillDetailZJ.getTaxRate().multiply(new BigDecimal("100")));
            }
        }
        return id;
    }

    public VoucherBillZJDetailMoneyV queryMoney(PageF<SearchF<?>> form) {
        return voucherBillDetailZJMapper.queryMoney(RepositoryUtil.putLogicDeleted(form.getConditions().getQueryModel()));
    }
    public VoucherBillZJDetailMoneyV queryMoneyByGroup(PageF<SearchF<?>> form) {
        VoucherBillZJDetailMoneyV voucherBillZJDetailMoneyV = new VoucherBillZJDetailMoneyV();

        QueryWrapper<?> queryModel = form.getConditions().getQueryModel();
        queryModel.gt("tax_includ_amount", 0);
        List<VoucherPushBillDetailDxZJ> voucherPushBillDetailZJS = voucherBillDetailZJMapper.selectVoucherPushBillDetailDxZJ(RepositoryUtil.putLogicDeleted(queryModel));
        Map<BigDecimal, List<VoucherPushBillDetailDxZJ>> collect = voucherPushBillDetailZJS.stream().collect(Collectors.groupingBy(VoucherPushBillDetailDxZJ::getTaxRate));
        BigDecimal taxIncludAmount = voucherPushBillDetailZJS.stream().map(VoucherPushBillDetailDxZJ::getTaxIncludAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        voucherBillZJDetailMoneyV.setTaxIncludAmount(taxIncludAmount.divide(new BigDecimal("100")));
        BigDecimal taxExcludAmount = BigDecimal.ZERO;
        for (BigDecimal bigDecimal : collect.keySet()) {
            List<VoucherPushBillDetailDxZJ> voucherPushBillDetailZJS1 = collect.get(bigDecimal);
            BigDecimal sum = voucherPushBillDetailZJS1.stream().map(VoucherPushBillDetailDxZJ::getTaxIncludAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
            BigDecimal add = new BigDecimal("1").add(bigDecimal);
            BigDecimal divide = sum.divide(add, 8, RoundingMode.HALF_UP)
                    .divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP);
            taxExcludAmount = taxExcludAmount.add(divide);
        }
        voucherBillZJDetailMoneyV.setTaxExcludAmount(taxExcludAmount);
        return voucherBillZJDetailMoneyV;
    }


    public VoucherBillZJConvertMoneyV queryConvertDetailMoney(PageF<SearchF<?>> form){
        return  voucherBillDetailZJMapper.queryConvertDetailMoney(RepositoryUtil.putLogicDeleted(form.getConditions().getQueryModel()));
    }


    public Page<VoucherBillZJFlowDetailV>  queryFlowDetailPage(PageF<SearchF<?>> form) {
       return voucherBillDetailZJMapper.queryFlowDetailPage(RepositoryUtil.convertMPPage(form),form.getConditions().getQueryModel());
    }
    public Page<VoucherBillZJFlowDetailV>  queryFlowDetailPageNew(PageF<SearchF<?>> form) {
        return voucherBillDetailZJMapper.queryFlowDetailPageNew(RepositoryUtil.convertMPPage(form),form.getConditions().getQueryModel());
    }

    public List<FlowClaimFilesV> queryFlowClaimFilesV(PageF<SearchF<?>> form) {
        QueryWrapper<?> queryModel = form.getConditions().getQueryModel();
        queryModel.in("vbdz.bill_type",1, 2, 3);
        return voucherBillDetailZJMapper.queryFlowClaimFilesV(queryModel);
    }
    public List<FlowClaimFilesV> queryFlowClaimFilesVNew(List<Long> ids) {
        return voucherBillDetailZJMapper.queryFlowClaimFilesVNew(ids);
    }

    public Page<VoucherBillZJConvertDetailV>  queryConvertDetailPage(PageF<SearchF<?>> form) {
        return voucherBillDetailZJMapper.queryConvertDetailPage(RepositoryUtil.convertMPPage(form),RepositoryUtil.putLogicDeleted(form.getConditions().getQueryModel()));
    }

    public PageV<VoucherBillZJRecDetailV> queryRecDetailPage(PageF<SearchF<?>> form){
        Page<VoucherBillZJRecDetailV> voucherBillZJRecDetailVPage = voucherBillDetailZJMapper.queryRecDetailPage(RepositoryUtil.convertMPPage(form), RepositoryUtil.putLogicDeleted(form.getConditions().getQueryModel()));
        return  PageV.of(form, voucherBillZJRecDetailVPage.getTotal(), voucherBillZJRecDetailVPage.getRecords());
    }



    public PageV<VoucherBillZJRecCWYDetailV> queryRecCWYDetailPage(PageF<SearchF<?>> form){
        Page<VoucherBillZJRecCWYDetailV> voucherBillZJRecDetailVPage = voucherBillDetailZJMapper.queryRecCWYDetailPage(RepositoryUtil.convertMPPage(form), RepositoryUtil.putLogicDeleted(form.getConditions().getQueryModel()));
        return  PageV.of(form, voucherBillZJRecDetailVPage.getTotal(), voucherBillZJRecDetailVPage.getRecords());
    }


    public List<VoucherBillZJRecCWYDetailV> queryRecCWYDetail(PageF<SearchF<?>> form){
        return voucherBillDetailZJMapper.queryRecCWYDetail(RepositoryUtil.putLogicDeleted(form.getConditions().getQueryModel(),"vbd"));
    }


    public PageV<VoucherBillZJCashFlowV> queryCashFlowPage(PageF<SearchF<?>> form){
        Page<VoucherBillZJCashFlowV> voucherBillZJCashFlowVPage = voucherBillDetailZJMapper.queryCashFlowPage(RepositoryUtil.convertMPPage(form), RepositoryUtil.putLogicDeleted(form.getConditions().getQueryModel()));
        for (VoucherBillZJCashFlowV record : voucherBillZJCashFlowVPage.getRecords()) {
            record.setBillNo(IdentifierFactory.getInstance().serialNumber("cashFlowNoZJ", "CFNO", 20));
        }
        return  PageV.of(form, voucherBillZJCashFlowVPage.getTotal(), voucherBillZJCashFlowVPage.getRecords());
    }
    public PageV<VoucherBillCostInfoV> queryVoucherBillCostInfo(PageF<SearchF<?>> form){
        Page<VoucherBillCostInfoV> voucherBillZJ = voucherBillDetailZJMapper.queryVoucherBillCostInfo(RepositoryUtil.convertMPPage(form), RepositoryUtil.putLogicDeleted(form.getConditions().getQueryModel()));
        return  PageV.of(form, voucherBillZJ.getTotal(), voucherBillZJ.getRecords());
    }


    public void delete(String voucherBillNo) {
        baseMapper.delete(voucherBillNo);
    }
    public List<VoucherPushBillDetailDxZJ> getByVoucherBillNo(String voucherBillNo) {
        return baseMapper.selectList(new LambdaQueryWrapper<VoucherPushBillDetailDxZJ>()
                .eq(VoucherPushBillDetailDxZJ::getDeleted,0)
                .in(VoucherPushBillDetailDxZJ::getVoucherBillNo,voucherBillNo));
    }

    public List<VoucherPushBillDetailDxZJ> getByVoucherBillNoList(List<String> voucherBillNoList) {
        return baseMapper.selectList(new LambdaQueryWrapper<VoucherPushBillDetailDxZJ>()
                .eq(VoucherPushBillDetailDxZJ::getBillEventType,2)
                .eq(VoucherPushBillDetailDxZJ::getDeleted,0)
                .ne(VoucherPushBillDetailDxZJ::getBillType, VoucherBillTypeEnums.手续费.getCode())
                .in(VoucherPushBillDetailDxZJ::getVoucherBillNo,voucherBillNoList));
    }


    public List<VoucherPushBillDetailDxZJ> getByVoucherBillNoNoSearch(String voucherBillNo) {
        return baseMapper.selectList(new LambdaQueryWrapper<VoucherPushBillDetailDxZJ>()
                .eq(VoucherPushBillDetailDxZJ::getDeleted,0)
                .in(VoucherPushBillDetailDxZJ::getVoucherBillNo,voucherBillNo));
    }

    public List<VoucherPushBillDetailDxZJ> getByVoucherBillNoListNoSearch(List<String> voucherBillNos) {
        return baseMapper.selectList(new LambdaQueryWrapper<VoucherPushBillDetailDxZJ>()
                .eq(VoucherPushBillDetailDxZJ::getDeleted,0)
                .in(VoucherPushBillDetailDxZJ::getVoucherBillNo,voucherBillNos));
    }

    public void updatePushStateByVoucherBIllNo(String voucherBillNo, Integer state){
        baseMapper.updatePushStateByVoucherBIllNo(voucherBillNo, state);
    }



    public List<VoucherPushBillDetailDxZJ> getPushDetails (List<Long> ids) {
        return baseMapper.selectList(new LambdaQueryWrapper<VoucherPushBillDetailDxZJ>()
                .eq(VoucherPushBillDetailDxZJ::getBillEventType,1)
                .eq(VoucherPushBillDetailDxZJ::getDeleted,0)
                .in(VoucherPushBillDetailDxZJ::getBillId,ids));
    }

    public List<DxPaymentDetailsV> getPaymentDetailsVByVoucherBillNoOnPay(String voucherBillNo,
                                                                          Integer billEventType,
                                                                          String communityId) {
        return baseMapper.getPaymentDetailsVByVoucherBillNoOnPay(voucherBillNo,billEventType,communityId);
    }

    public List<DxPaymentDetailsV> getPaymentDetailsVByVoucherBillNoOnIncome(String voucherBillNo,
                                                                             Integer billEventType,
                                                                             String communityId) {
        return baseMapper.getPaymentDetailsVByVoucherBillNoOnIncome(voucherBillNo,billEventType,communityId);
    }

    public List<DxCostDetailsV> getCostDetailsVByVoucherBillNoOnPay(String voucherBillNo, Integer billEventType, String communityId) {
        return baseMapper.getCostDetailsVByVoucherBillNoOnPay(voucherBillNo,billEventType,communityId);
    }

}
