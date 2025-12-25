package com.wishare.finance.apps.service.pushbill.mdm63;

import com.alibaba.fastjson.JSONArray;
import com.wishare.finance.apps.pushbill.vo.PaymentApplicationKXDetailV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJRecDetailV2;
import com.wishare.finance.apps.pushbill.vo.dx.DxPaymentDetails;
import com.wishare.finance.domains.mdm.entity.Mdm63E;
import com.wishare.finance.domains.mdm.repository.mapper.Mdm63Mapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.PaymentApplicationFormZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.PaymentApplicationFormRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.PaymentApplicationZJMapper;
import com.wishare.starter.Global;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author longhuadmin
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class Mdm63Service {

    public static final String PAYABLE_TYPE = "AP";
    public static final String RECEIVABLE_TYPE = "AR";

    private final Mdm63Mapper mdm63Mapper;
    private final PaymentApplicationZJMapper paymentApplicationFormZJMapper;


    /**
     * 自动匹配应收应付并返回匹配之后的结果
     * @param dxPaymentDetails 款项明细(对下结算/收入确认)-unit数据
     * @param curBillEventType 当前的单据类型
     * @param settlementId 结算单id[单个]-对下结算用
     **/
    public List<DxPaymentDetails> match(DxPaymentDetails dxPaymentDetails,
                                        Integer curBillEventType,
                                        String settlementId,
                                        List<String> matchFtIdList){

        List<Mdm63E> mdm63List = queryMdm63ListOnCertainScene(dxPaymentDetails, curBillEventType, settlementId, matchFtIdList);
        log.info("根据条件获取mdm63List列表：{}", JSONArray.toJSON(mdm63List));
        if (CollectionUtils.isEmpty(mdm63List)){
            return Lists.newArrayList(dxPaymentDetails);
        }
        BigDecimal baseAmount = dxPaymentDetails.getStandardAmount();
        BigDecimal baseShowAmount = dxPaymentDetails.getStandardAmount();
        // 若当前单据类型为对下结算计提冲销或者收入确认计提冲销，金额是个负数，取下绝对值
        if (curBillEventType == ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode()
                || curBillEventType == ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode()){
            baseAmount = baseAmount.abs();
        }
        // mdm63List 按照 dhx_je 倒序排序
        mdm63List.sort(Comparator.comparing(Mdm63E::getDhxJe).reversed());
        // 若存在待核销金额大于等于baseAmount的
        List<DxPaymentDetails> filledList = Lists.newArrayList();
        doMatchAndFillWithCal(dxPaymentDetails, mdm63List, baseAmount, baseAmount, filledList, matchFtIdList,baseShowAmount);
        if (CollectionUtils.isEmpty(filledList)){
            return Lists.newArrayList(dxPaymentDetails);
        }
        return filledList;
    }

    private void doMatchAndFillWithCal(DxPaymentDetails dxPaymentDetails,
                                       List<Mdm63E> mdm63List,
                                       BigDecimal baseAmount,
                                       BigDecimal finalBaseAmount,
                                       List<DxPaymentDetails> filledList,
                                       List<String> matchFtIdList,
                                       BigDecimal baseShowAmount) {
        if (mdm63List.stream().anyMatch(mdm63E -> mdm63E.getDhxJe().compareTo(finalBaseAmount) >= 0)){
            // 取出第一个待核销金额大于等于baseAmount的
            Mdm63E mdm63E = mdm63List.stream().filter(mdm63 -> mdm63.getDhxJe().compareTo(finalBaseAmount) >= 0).findFirst().get();
            dxPaymentDetails.setFtId(mdm63E.getFtId());
            dxPaymentDetails.setNumberOfPayableReceivable(mdm63E.getBillNum());
            dxPaymentDetails.setNotSettlementAmount(mdm63E.getDhxJe());
            matchFtIdList.add(mdm63E.getFtId());
            filledList.add(dxPaymentDetails);
            return;
        }
        // 若待核销金额都小于baseAmount的,,按照dhx_je金额从大到小,核销baseAmount,并保留ft_id
        for (Mdm63E mdm63E : mdm63List) {
            if (baseAmount.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
            // 复制dxPaymentDetails,并清空金额
            DxPaymentDetails curDxPaymentDetail = Global.mapperFacade.map(dxPaymentDetails, DxPaymentDetails.class);
            curDxPaymentDetail.setStandardAmount(BigDecimal.ZERO);
            curDxPaymentDetail.setOriginCurrencyAmount(BigDecimal.ZERO);
            if (baseAmount.compareTo(mdm63E.getDhxJe()) >= 0) {
                curDxPaymentDetail.setFtId(mdm63E.getFtId());
                curDxPaymentDetail.setStandardAmount(baseShowAmount.compareTo(BigDecimal.ZERO) < 0 && mdm63E.getDhxJe().compareTo(BigDecimal.ZERO) > 0 ? mdm63E.getDhxJe().negate() : mdm63E.getDhxJe());
                curDxPaymentDetail.setOriginCurrencyAmount(baseShowAmount.compareTo(BigDecimal.ZERO) < 0 && mdm63E.getDhxJe().compareTo(BigDecimal.ZERO) > 0 ? mdm63E.getDhxJe().negate() : mdm63E.getDhxJe());
                curDxPaymentDetail.setNumberOfPayableReceivable(mdm63E.getBillNum());
                curDxPaymentDetail.setNotSettlementAmount(mdm63E.getDhxJe());
                filledList.add(curDxPaymentDetail);
                baseAmount = baseAmount.subtract(mdm63E.getDhxJe());
            } else {
                curDxPaymentDetail.setFtId(mdm63E.getFtId());
                curDxPaymentDetail.setStandardAmount(baseShowAmount.compareTo(BigDecimal.ZERO) < 0 ? baseAmount.negate() : baseAmount);
                curDxPaymentDetail.setOriginCurrencyAmount(baseShowAmount.compareTo(BigDecimal.ZERO) < 0 ? baseAmount.negate() : baseAmount);
                curDxPaymentDetail.setNumberOfPayableReceivable(mdm63E.getBillNum());
                curDxPaymentDetail.setNotSettlementAmount(mdm63E.getDhxJe());
                filledList.add(curDxPaymentDetail);
                baseAmount = BigDecimal.ZERO;
            }
            matchFtIdList.add(mdm63E.getFtId());
        }
    }

    @Nullable
    private List<Mdm63E> queryMdm63ListOnCertainScene(DxPaymentDetails dxPaymentDetails,
                                                      Integer curBillEventType,
                                                      String settlementId,
                                                      List<String> matchFtIdList) {
        List<Mdm63E> mdm63List = null;
        if (ZJTriggerEventBillTypeEnum.对下结算实签.getCode() == curBillEventType){
            // 结算单匹配, 无脑匹配
            PaymentApplicationFormZJ payApplicationFormZJ =
                    paymentApplicationFormZJMapper.selectByCertainSettlementId(settlementId);
            if (Objects.isNull(payApplicationFormZJ)){
                return null;
            }
            // 反查dx_zj
            mdm63List = mdm63Mapper.queryOnPaymentAppId(payApplicationFormZJ.getId(), PAYABLE_TYPE,
                    ZJTriggerEventBillTypeEnum.支付申请.getCode());
        }
        if (ZJTriggerEventBillTypeEnum.收入确认实签.getCode() == curBillEventType){
            // 规则匹配[old table match]
            mdm63List = mdm63OnContract2(dxPaymentDetails.getConMainCode(), dxPaymentDetails.getPaymentId(),
                    RECEIVABLE_TYPE, ZJTriggerEventBillTypeEnum.资金收款.getCode());
        }
        if (ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode() == curBillEventType){
            // 规则匹配
            mdm63List = queryOnContractByFtId(dxPaymentDetails.getVoucherBillNo());
            if(CollectionUtils.isEmpty(mdm63List)){
                mdm63List = mdm63OnContract(dxPaymentDetails.getConMainCode(), dxPaymentDetails.getPaymentId(),
                        RECEIVABLE_TYPE, ZJTriggerEventBillTypeEnum.收入确认计提.getCode());
            }
        }
        if (ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode() == curBillEventType){
            // 规则匹配
            mdm63List = mdm63OnContract(dxPaymentDetails.getConMainCode(), dxPaymentDetails.getPaymentId(),
                    PAYABLE_TYPE, ZJTriggerEventBillTypeEnum.对下结算计提.getCode());
        }
        if (CollectionUtils.isNotEmpty(mdm63List)){
            mdm63List.removeIf(mdm63E -> matchFtIdList.contains(mdm63E.getFtId()));
        }
        return mdm63List;
    }


    /**
     * 合同口径匹配应收应付
     * @param contractCtCode 合同CT码
     * @param paymentId 科目编码(款项配置的)
     * @param sourceBillEventType 来源单据类型
     **/
    private List<Mdm63E> mdm63OnContract(String contractCtCode,
                                        String paymentId,
                                        String apArType,
                                        Integer sourceBillEventType){
        return mdm63Mapper.queryOnContract(contractCtCode, paymentId, apArType, sourceBillEventType);
    }
    private List<Mdm63E> queryOnContractByFtId(String voucherBillNo){
        return mdm63Mapper.queryOnContractByFtId(voucherBillNo);
    }

    private List<Mdm63E> mdm63OnContract2(String contractCtCode,
                                        String paymentCode,
                                        String apArType,
                                        Integer sourceBillEventType){
        return mdm63Mapper.queryOnContract2(contractCtCode, paymentCode, apArType, sourceBillEventType);
    }


    /**
     * 业务支付申请单自动匹配
     **/
    public List<PaymentApplicationKXDetailV> matchForPaySheet(BigDecimal totalAmount,
                                                              String settlementId,
                                                              String paymentId,
                                                              String paymentName,
                                                              List<String> matchedFtIdList) {
        List<Mdm63E> mdm63List = mdm63Mapper.queryOnSettlementIdAndPaymentId(settlementId,
                paymentId,PAYABLE_TYPE,ZJTriggerEventBillTypeEnum.对下结算实签.getCode());
        if (CollectionUtils.isNotEmpty(mdm63List)){
            mdm63List.removeIf(mdm63E -> matchedFtIdList.contains(mdm63E.getFtId()));
        }
        if (CollectionUtils.isEmpty(mdm63List)){
            PaymentApplicationKXDetailV v = new PaymentApplicationKXDetailV();
            v.setSubjectCode(paymentId);
            v.setSubjectName(paymentName);
            v.setAmount(totalAmount);
            return Lists.newArrayList(v);
        }

        mdm63List.sort(Comparator.comparing(Mdm63E::getDhxJe).reversed());
        List<PaymentApplicationKXDetailV> filledList = Lists.newArrayList();
        BigDecimal finalTotalAmount = totalAmount;
        if (mdm63List.stream().anyMatch(mdm63E -> mdm63E.getDhxJe().compareTo(finalTotalAmount) >= 0)){
            // 取出第一个待核销金额大于等于baseAmount的
            Mdm63E mdm63E = mdm63List.stream().filter(mdm63 -> mdm63.getDhxJe().compareTo(finalTotalAmount) >= 0).findFirst().get();
            PaymentApplicationKXDetailV v = new PaymentApplicationKXDetailV();
            v.setSubjectCode(paymentId);
            v.setSubjectName(paymentName);
            v.setAmount(totalAmount);
            v.setWriteOffInfoAmount(mdm63E.getDhxJe());
            v.setFtId(mdm63E.getFtId());
            v.setHeyfbh(mdm63E.getBillNum());
            v.setSummary(mdm63E.getSummary());
            filledList.add(v);
            matchedFtIdList.add(mdm63E.getFtId());
            return filledList;
        }
        // 若待核销金额都小于baseAmount的,,按照dhx_je金额从大到小,核销baseAmount,并保留ft_id
        for (Mdm63E mdm63E : mdm63List) {
            if (totalAmount.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
            PaymentApplicationKXDetailV v = new PaymentApplicationKXDetailV();
            v.setSubjectCode(paymentId);
            v.setSubjectName(paymentName);
            if (totalAmount.compareTo(mdm63E.getDhxJe()) >= 0) {
                v.setFtId(mdm63E.getFtId());
                v.setAmount(mdm63E.getDhxJe());
                v.setWriteOffInfoAmount(mdm63E.getDhxJe());
                v.setHeyfbh(mdm63E.getBillNum());
                v.setSummary(mdm63E.getSummary());
                filledList.add(v);
                totalAmount = totalAmount.subtract(mdm63E.getDhxJe());
            } else {
                v.setFtId(mdm63E.getFtId());
                v.setAmount(totalAmount);
                v.setWriteOffInfoAmount(mdm63E.getDhxJe());
                v.setHeyfbh(mdm63E.getBillNum());
                v.setSummary(mdm63E.getSummary());
                filledList.add(v);
                totalAmount = BigDecimal.ZERO;
            }
            matchedFtIdList.add(mdm63E.getFtId());
        }

        return filledList;
    }

    /**
     * 资金收款单自动匹配
     **/
    public List<VoucherBillZJRecDetailV2> matchForReceiptSheet(VoucherBillZJRecDetailV2 recDetailV2,
                                                               String projectId,
                                                              String paymentId,
                                                              String contractNo,
                                                              String partnerCode,
                                                              boolean fromContract,
                                                               List<String> matchedFtIdList){

        List<Mdm63E> mdm63List = mdm63Mapper.queryForReceiptAutoMatch(RECEIVABLE_TYPE,
                fromContract, contractNo, projectId, paymentId, partnerCode);
        if (CollectionUtils.isNotEmpty(mdm63List)){
            mdm63List = mdm63List.stream()
                    .filter(mdm -> mdm.getDhxJe().compareTo(BigDecimal.ZERO) > 0)
                    .collect(Collectors.toList());
            mdm63List.removeIf(mdm63E -> matchedFtIdList.contains(mdm63E.getFtId()));
        }
        if (CollectionUtils.isEmpty(mdm63List)){
            return Lists.newArrayList(recDetailV2);
        }
        mdm63List.sort(Comparator.comparing(Mdm63E::getDhxJe).reversed());
        mdm63List.sort(Comparator.comparing(Mdm63E::getBizDate, Comparator.nullsFirst(Comparator.naturalOrder())));
        List<VoucherBillZJRecDetailV2> filledList = Lists.newArrayList();
        if (mdm63List.stream().anyMatch(mdm63E -> mdm63E.getDhxJe().compareTo(recDetailV2.getTotalAmountOnCurrency()) >= 0)){
            // todo 取时间最小的
            Mdm63E mdm63E = mdm63List.stream().filter(mdm63 -> mdm63.getDhxJe().compareTo(recDetailV2.getTotalAmountOnCurrency()) >= 0).findFirst().get();
            recDetailV2.setFtId(mdm63E.getFtId());
            recDetailV2.setFtNo(mdm63E.getBillNum());
            recDetailV2.setFtSummary(mdm63E.getSummary());
            filledList.add(recDetailV2);
            matchedFtIdList.add(mdm63E.getFtId());
            return filledList;
        }
        BigDecimal totalAmount = recDetailV2.getTotalAmountOnCurrency();
        BigDecimal taxAmount = recDetailV2.getTaxAmountOnCurrency();
        BigDecimal noTaxAmount = recDetailV2.getNoTaxAmountOnCurrency();
        BigDecimal taxRate = recDetailV2.getTaxRate();
        BigDecimal taxRateCalNumber = BigDecimal.ONE.add(taxRate);
        for (Mdm63E mdm63E : mdm63List) {
            if (totalAmount.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
            VoucherBillZJRecDetailV2 curRecDetail = Global.mapperFacade.map(recDetailV2, VoucherBillZJRecDetailV2.class);
            // 清空所有金额
            curRecDetail.setTotalAmountOnCurrency(null);
            curRecDetail.setTotalAmountOnOrigin(null);
            curRecDetail.setTaxAmountOnCurrency(null);
            curRecDetail.setTaxAmountOnOrigin(null);
            curRecDetail.setNoTaxAmountOnCurrency(null);
            curRecDetail.setNoTaxAmountOnOrigin(null);
            if (totalAmount.compareTo(mdm63E.getDhxJe()) >= 0) {
                BigDecimal curNoTaxAmount = mdm63E.getDhxJe().divide(taxRateCalNumber, 2, RoundingMode.HALF_UP);
                BigDecimal curTaxAmount = mdm63E.getDhxJe().subtract(curNoTaxAmount);
                curRecDetail.setFtId(mdm63E.getFtId());
                curRecDetail.setFtNo(mdm63E.getBillNum());
                curRecDetail.setFtSummary(mdm63E.getSummary());
                curRecDetail.setTotalAmountOnCurrency(mdm63E.getDhxJe());
                curRecDetail.setTotalAmountOnOrigin(mdm63E.getDhxJeYb());
                curRecDetail.setTaxAmountOnCurrency(curTaxAmount);
                curRecDetail.setTaxAmountOnOrigin(curTaxAmount);
                curRecDetail.setNoTaxAmountOnCurrency(curNoTaxAmount);
                curRecDetail.setNoTaxAmountOnOrigin(curNoTaxAmount);
                totalAmount = totalAmount.subtract(mdm63E.getDhxJe());
                taxAmount = taxAmount.subtract(curTaxAmount);
                noTaxAmount = noTaxAmount.subtract(curNoTaxAmount);
            } else {
                curRecDetail.setFtId(mdm63E.getFtId());
                curRecDetail.setFtNo(mdm63E.getBillNum());
                curRecDetail.setFtSummary(mdm63E.getSummary());
                curRecDetail.setTotalAmountOnCurrency(totalAmount);
                curRecDetail.setTotalAmountOnOrigin(totalAmount);
                curRecDetail.setTaxAmountOnCurrency(taxAmount);
                curRecDetail.setTaxAmountOnOrigin(taxAmount);
                curRecDetail.setNoTaxAmountOnCurrency(noTaxAmount);
                curRecDetail.setNoTaxAmountOnOrigin(noTaxAmount);
                totalAmount = BigDecimal.ZERO;
                taxAmount = BigDecimal.ZERO;
                noTaxAmount = BigDecimal.ZERO;
            }
            matchedFtIdList.add(mdm63E.getFtId());
            filledList.add(curRecDetail);
        }
        //若filledList所有核销金额加起来还是小于总金额，在新增一条空白数据
        if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
            VoucherBillZJRecDetailV2 curRecDetail = Global.mapperFacade.map(recDetailV2, VoucherBillZJRecDetailV2.class);
            curRecDetail.setFtId(null);
            curRecDetail.setFtNo(null);
            curRecDetail.setFtSummary(null);
            curRecDetail.setTotalAmountOnCurrency(totalAmount);
            curRecDetail.setTotalAmountOnOrigin(totalAmount);
            curRecDetail.setTaxAmountOnCurrency(taxAmount);
            curRecDetail.setTaxAmountOnOrigin(taxAmount);
            curRecDetail.setNoTaxAmountOnCurrency(noTaxAmount);
            curRecDetail.setNoTaxAmountOnOrigin(noTaxAmount);
            filledList.add(curRecDetail);
        }
        return filledList;
    }
}
