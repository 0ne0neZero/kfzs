package com.wishare.finance.domains.voucher.support.zhongjiao.repository;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.pushbill.vo.*;
import com.wishare.finance.domains.voucher.enums.VoucherBillTypeEnums;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherPushBillDetail;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherBillDetailZJMapper;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class    VoucherBillDetailZJRepository extends ServiceImpl<VoucherBillDetailZJMapper, VoucherPushBillDetailZJ> {
    private final VoucherBillDetailZJMapper voucherBillDetailZJMapper;

    public Page<VoucherPushBillDetailZJ> pageBySearch(PageF<SearchF<?>> searchPageF) {
        Page<VoucherPushBillDetailZJ> voucherPushBillDetailZJPage = baseMapper.selectBySearch(RepositoryUtil.convertMPPage(searchPageF),
                RepositoryUtil.putLogicDeleted(searchPageF.getConditions().getQueryModel()).orderByDesc("id"));
        if (CollectionUtils.isNotEmpty(voucherPushBillDetailZJPage.getRecords())) {
            for (VoucherPushBillDetailZJ voucherPushBillDetailZJ : voucherPushBillDetailZJPage.getRecords()) {
                voucherPushBillDetailZJ.setTaxRate(voucherPushBillDetailZJ.getTaxRate().multiply(new BigDecimal("100")));
            }
        }
        return voucherPushBillDetailZJPage;
    }


    public List<VoucherPushBillDetailZJ> selectVoucherPushBillDetailZJ(PageF<SearchF<?>> searchPageF) {
        List<VoucherPushBillDetailZJ> id = baseMapper.selectVoucherPushBillDetailZJ(RepositoryUtil.putLogicDeleted(searchPageF.getConditions().getQueryModel())
                .orderByDesc("id"));
        if (CollectionUtils.isNotEmpty(id)){
            for (VoucherPushBillDetailZJ voucherPushBillDetailZJ : id) {
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
        //queryModel.gt("tax_includ_amount", 0);
        List<VoucherPushBillDetailZJ> voucherPushBillDetailZJS = voucherBillDetailZJMapper.selectVoucherPushBillDetailZJ(RepositoryUtil.putLogicDeleted(queryModel));
        Map<BigDecimal, List<VoucherPushBillDetailZJ>> collect = voucherPushBillDetailZJS.stream().collect(Collectors.groupingBy(VoucherPushBillDetailZJ::getTaxRate));
        long taxIncludAmount = voucherPushBillDetailZJS.stream().mapToLong(VoucherPushBillDetailZJ::getTaxIncludAmount).sum();
        voucherBillZJDetailMoneyV.setTaxIncludAmount(new BigDecimal(taxIncludAmount).divide(new BigDecimal("100")));
        BigDecimal taxExcludAmount = new BigDecimal("0");
        for (BigDecimal bigDecimal : collect.keySet()) {
            List<VoucherPushBillDetailZJ> voucherPushBillDetailZJS1 = collect.get(bigDecimal);
            long sum = voucherPushBillDetailZJS1.stream().mapToLong(VoucherPushBillDetailZJ::getTaxIncludAmount).sum();
            BigDecimal add = new BigDecimal("1").add(bigDecimal);
            BigDecimal divide = new BigDecimal(sum).divide(add, 2, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal("100"));
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


    public List<VoucherBillZJRecV> queryRecList(String voucherBillNo) {
        return baseMapper.queryRecList(voucherBillNo);
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
    public List<VoucherPushBillDetailZJ> getByVoucherBillNo(String voucherBillNo) {
        return baseMapper.selectList(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                .eq(VoucherPushBillDetailZJ::getBillEventType,2)
                .eq(VoucherPushBillDetailZJ::getDeleted,0)
                .ne(VoucherPushBillDetailZJ::getBillType, VoucherBillTypeEnums.手续费.getCode())
                .in(VoucherPushBillDetailZJ::getVoucherBillNo,voucherBillNo));
    }

    public List<VoucherPushBillDetailZJ> getByVoucherBillNoList(List<String> voucherBillNoList) {
        return baseMapper.selectList(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                .eq(VoucherPushBillDetailZJ::getBillEventType,2)
                .eq(VoucherPushBillDetailZJ::getDeleted,0)
                .ne(VoucherPushBillDetailZJ::getBillType, VoucherBillTypeEnums.手续费.getCode())
                .in(VoucherPushBillDetailZJ::getVoucherBillNo,voucherBillNoList));
    }


    public List<VoucherPushBillDetailZJ> getByVoucherBillNoNoSearch(String voucherBillNo) {
        return baseMapper.selectList(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                .eq(VoucherPushBillDetailZJ::getDeleted,0)
                .eq(VoucherPushBillDetailZJ::getVoucherBillNo,voucherBillNo));
    }

    public VoucherPushBillDetailZJ getByVoucherBillNoNoSearchOne(String voucherBillNo) {
        return baseMapper.selectOne(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                .eq(VoucherPushBillDetailZJ::getVoucherBillNo,voucherBillNo)
                .eq(VoucherPushBillDetailZJ::getDeleted,0)
                .last("LIMIT 1"));
    }

    public List<VoucherPushBillDetailZJ> getByVoucherBillNoListNoSearch(List<String> voucherBillNos) {
        return baseMapper.selectList(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                .eq(VoucherPushBillDetailZJ::getDeleted,0)
                .in(VoucherPushBillDetailZJ::getVoucherBillNo,voucherBillNos));
    }

    public void updatePushStateByVoucherBIllNo(String voucherBillNo, Integer state){
        baseMapper.updatePushStateByVoucherBIllNo(voucherBillNo, state);
    }



    public List<VoucherPushBillDetailZJ> getPushDetails (List<Long> ids) {
        return baseMapper.selectList(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                .eq(VoucherPushBillDetailZJ::getBillEventType,1)
                .eq(VoucherPushBillDetailZJ::getDeleted,0)
                .in(VoucherPushBillDetailZJ::getBillId,ids));
    }

}
