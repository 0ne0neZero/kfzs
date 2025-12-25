package com.wishare.finance.apps.service.pushbill;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.vo.TemporaryChargeBillDetailV;
import com.wishare.finance.apps.pushbill.vo.dx.*;
import com.wishare.finance.apps.pushbill.vo.dx.vo.DxCostDetailsV;
import com.wishare.finance.apps.pushbill.vo.dx.vo.DxPaymentDetailsV;
import com.wishare.finance.apps.service.bill.TemporaryChargeBillAppService;
import com.wishare.finance.apps.service.pushbill.mdm63.BaseWriteOffObj;
import com.wishare.finance.apps.service.pushbill.mdm63.Mdm63Service;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.invoicereceipt.consts.enums.TaxpayerTypeEnum;
import com.wishare.finance.domains.pushbill.enums.VoucherChargeTypeEnums;
import com.wishare.finance.domains.voucher.enums.TaxTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillDxZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailDxZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillDetailDxZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherPushBillDxZJRepository;
import com.wishare.finance.infrastructure.remote.clients.base.ConfigClient;
import com.wishare.finance.infrastructure.remote.clients.base.ContractClient;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.enums.financialcloud.FinancialTaxTypeEnum;
import com.wishare.finance.infrastructure.remote.vo.cfg.DictionaryValueV;
import com.wishare.finance.infrastructure.remote.vo.config.CfgExternalDataV;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractPayPlanInnerInfoV;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractSettlementInvoiceDetailF;
import com.wishare.finance.infrastructure.remote.vo.org.CustomerSimpleV;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import com.wishare.finance.infrastructure.remote.vo.org.SupplierSimpleV;
import com.wishare.starter.Global;
import com.wishare.starter.interfaces.ApiBase;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author longhuadmin
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherBillDxDetailAppService implements ApiBase {

    private final VoucherPushBillDxZJRepository voucherPushBillDxZJRepository;
    private final VoucherBillDetailDxZJRepository voucherBillDetailRepository;
    private final ContractClient contractClient;
    private final OrgClient orgClient;
    private final ConfigClient configClient;
    private final TemporaryChargeBillAppService temporaryChargeBillAppService;
    private final ReceivableBillRepository receivableBillRepository;
    private final Mdm63Service mdm63Service;

    /**
     * 通用收入确认明细
     * @param voucherBillNo
     * @return
     */
    public List<GeneralDetails> generalRevenueRecognition(String voucherBillNo) {
        List<VoucherPushBillDetailDxZJ> details = voucherBillDetailRepository.getByVoucherBillNo(voucherBillNo);
        if (CollectionUtils.isEmpty(details)){
            return Collections.emptyList();
        }
        VoucherBillDxZJ voucherBill = voucherPushBillDxZJRepository.queryByVoucherBillNo(voucherBillNo);

        List<GeneralDetails> detailsList = Lists.newArrayList();

        List<DxPaymentDetailsV> detailsDoList = voucherBillDetailRepository.getPaymentDetailsVByVoucherBillNoOnIncome(voucherBillNo, details.get(0).getBillEventType(),details.get(0).getCommunityId());
        if (CollectionUtils.isEmpty(detailsDoList)){
            return Collections.emptyList();
        }

        TemporaryChargeBillDetailV billInfo = temporaryChargeBillAppService.getById(details.get(0).getBillId(), TemporaryChargeBillDetailV.class, details.get(0).getCommunityId());
        OrgFinanceRv orgFinanceById = orgClient.getOrgFinanceById(billInfo.getStatutoryBodyId());
        log.info("根据法定单位id：{}，查询对应法定单位信息：{}", billInfo.getStatutoryBodyId(), orgFinanceById);
        Integer taxpayerType = null;
        if(Objects.nonNull(orgFinanceById)){
            taxpayerType = orgFinanceById.getTaxpayerType();
        }
        handleDetailsOnIncomeSceneForGeneral(detailsList, detailsDoList,details.get(0).getBillEventType(),taxpayerType,voucherBill);
        return detailsList;
    }


    private void handleDetailsOnIncomeSceneForGeneral(List<GeneralDetails> detailsList,
                                                      List<DxPaymentDetailsV> detailsDoList,
                                                      Integer eventType,
                                                      Integer taxpayerType,
                                                      VoucherBillDxZJ voucherBill) {
        List<String> contractIdList = detailsDoList.stream()
                .map(DxPaymentDetailsV::getContractId).distinct()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        List<ContractPayPlanInnerInfoV> innerInfoList = contractClient.getInnerInfoByContractIdOnIncome(contractIdList);
        Map<String, ContractPayPlanInnerInfoV> contractIdToInnerInfoMap = innerInfoList.stream()
                .collect(Collectors.toMap(ContractPayPlanInnerInfoV::getContractId, e -> e, (oldVal, newVal) -> newVal));

        if (eventType == ZJTriggerEventBillTypeEnum.收入确认实签.getCode()){
            detailsDoList.forEach(e -> {
                e.setPaymentId(e.getSignedPaymentId());
                e.setPaymentCode(e.getSignedPaymentCode());
                e.setPaymentName(e.getSignedPaymentName());
            });
        }

        Map<String, List<DxPaymentDetailsV>> groupedMap;
        if (eventType == ZJTriggerEventBillTypeEnum.收入确认计提.getCode()) {
            groupedMap = detailsDoList.stream()
                    .collect(Collectors.groupingBy(DxPaymentDetailsV::groupKeyForIncomeGeneralForJT));
        }else{
            groupedMap = detailsDoList.stream()
                    .collect(Collectors.groupingBy(DxPaymentDetailsV::groupKeyForIncomeGeneralForSQ));
        }
        List<String> draweeIds = innerInfoList.stream()
                .map(ContractPayPlanInnerInfoV::getDraweeId)
                .distinct()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        List<SupplierSimpleV> supplierSimpleList = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(draweeIds)){
            supplierSimpleList = orgClient.simpleListSupplier(draweeIds);
        }
        Map<String, SupplierSimpleV> supplierIdMap = supplierSimpleList.stream()
                .collect(Collectors.toMap(SupplierSimpleV::getId, e -> e));

        List<DictionaryValueV> taxRateDicItems = configClient.getKeyAndValue("FUND_TAX_RATE", null, null);
        Map<String, FinancialTaxTypeEnum> taxRateIdMap = taxRateDicItems.stream()
                .collect(Collectors.toMap(DictionaryValueV::getCode, e -> FinancialTaxTypeEnum.getEnumByTaxRate(e.getName())));
        groupedMap.forEach((key,unitDetailDos) -> {
            DxPaymentDetailsV v = unitDetailDos.get(0);
            GeneralDetails map = Global.mapperFacade.map(v, GeneralDetails.class);
            //map.setTaxTypeName(TaxTypeEnum.getNameByCode(map.getTaxType()));

            // 金额 累加
            map.setTaxIncludedAmount(unitDetailDos.stream().map(DxPaymentDetailsV::getTaxIncludAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(100),8, RoundingMode.HALF_UP));
            map.setTaxExcludedAmount(unitDetailDos.stream().map(DxPaymentDetailsV::getTaxExcludAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(100),8, RoundingMode.HALF_UP));
            map.setTaxAmount(unitDetailDos.stream().map(DxPaymentDetailsV::getTaxAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(100),8, RoundingMode.HALF_UP));


            ContractPayPlanInnerInfoV innerInfo = contractIdToInnerInfoMap.get(v.getContractId());
            if (Objects.nonNull(innerInfo)){
                map.setExchangeUnitName(innerInfo.getDrawee());
                if (StringUtils.isNotBlank(innerInfo.getDraweeId())){
                    SupplierSimpleV simpleV = supplierIdMap.get(innerInfo.getDraweeId());
                    if (Objects.nonNull(simpleV)){
                        map.setExchangeUnitMainCode(simpleV.getMainDataCode());
                    }
                    //兼容数据判断，因历史数据为BP开头导致数据查询为空，传送财务云时为空
                    else if (innerInfo.getDraweeId().startsWith("BP")){
                        map.setExchangeUnitMainCode(innerInfo.getDraweeId());
                    }
                }
                map.setConMainCode(innerInfo.getConMainCode());
            }
            /*FinancialTaxTypeEnum taxTypeEnum = taxRateIdMap.get(v.getTaxRateId());
            if (Objects.nonNull(taxTypeEnum)){
                map.setTaxTypeName(taxTypeEnum.getValue());
                map.setTaxType(taxTypeEnum.getCode().toString());
            }*/

            if(Objects.nonNull(voucherBill.getCalculationMethod())){
                map.setTaxType(voucherBill.getCalculationMethod().toString());
                map.setTaxTypeName(FinancialTaxTypeEnum.GENERAL.getCode().equals(voucherBill.getCalculationMethod()) ? FinancialTaxTypeEnum.GENERAL.getValue() : FinancialTaxTypeEnum.SIMPLE.getValue());
            }else{
                map.setTaxType(FinancialTaxTypeEnum.SIMPLE.getCode().toString());
                map.setTaxTypeName(FinancialTaxTypeEnum.SIMPLE.getValue());
                if(Objects.isNull(taxpayerType) || TaxpayerTypeEnum.一般纳税人.getCode().equals(taxpayerType)){
                    map.setTaxType(FinancialTaxTypeEnum.GENERAL.getCode().toString());
                    map.setTaxTypeName(FinancialTaxTypeEnum.GENERAL.getValue());
                }
            }

            detailsList.add(map);
        });

    }

    /**
     * todo 实签 取的款项id什么的换字段 包括生成的时候
     **/
    public List<DxPaymentDetails> queryDetailsOfPayments(String voucherBillNo) {
        List<VoucherPushBillDetailDxZJ> details = voucherBillDetailRepository.getByVoucherBillNo(voucherBillNo);
        if (CollectionUtils.isEmpty(details)){
            return Collections.emptyList();
        }

        Integer eventType = details.get(0).getBillEventType();
        String communityId = details.get(0).getCommunityId();
        List<DxPaymentDetails> detailsList = Lists.newArrayList();
        VoucherBillDxZJ voucherBill = voucherPushBillDxZJRepository.queryByVoucherBillNo(voucherBillNo);

        if (eventType.equals(ZJTriggerEventBillTypeEnum.对下结算计提.getCode()) ||
                eventType.equals(ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode()) ||
                eventType.equals(ZJTriggerEventBillTypeEnum.对下结算实签.getCode())){
            List<DxPaymentDetailsV> detailsDoList =
                    voucherBillDetailRepository.getPaymentDetailsVByVoucherBillNoOnPay(voucherBillNo, eventType,communityId);
            if (CollectionUtils.isEmpty(detailsDoList)){
                return Collections.emptyList();
            }
            handleDetailsOnPayScene(voucherBill, detailsList, detailsDoList, eventType);
        }
        if (eventType.equals(ZJTriggerEventBillTypeEnum.收入确认计提.getCode()) ||
                eventType.equals(ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode()) ||
                eventType.equals(ZJTriggerEventBillTypeEnum.收入确认实签.getCode())){
            List<DxPaymentDetailsV> detailsDoList = voucherBillDetailRepository.getPaymentDetailsVByVoucherBillNoOnIncome(voucherBillNo, eventType,communityId);
            if (CollectionUtils.isEmpty(detailsDoList)){
                return Collections.emptyList();
            }
            handleDetailsOnIncomeScene(detailsList, detailsDoList,eventType);
        }
        //到期时间替换，使用报账单对应的到期时间
        detailsList.forEach(e -> e.setMaturityDate(DateUtil.date(voucherBill.getGmtCreate().plusYears(1).minusDays(1))));
        return detailsList;
    }

    private void handleDetailsOnIncomeScene(List<DxPaymentDetails> detailsList,
                                            List<DxPaymentDetailsV> detailsDoList,
                                            Integer eventType) {
        List<String> contractIdList = detailsDoList.stream()
                .map(DxPaymentDetailsV::getContractId).distinct()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        List<ContractPayPlanInnerInfoV> innerInfoList = contractClient.getInnerInfoByContractIdOnIncome(contractIdList);
        Map<String, ContractPayPlanInnerInfoV> contractIdToInnerInfoMap = innerInfoList.stream()
                .collect(Collectors.toMap(ContractPayPlanInnerInfoV::getContractId, e -> e, (oldVal, newVal) -> newVal));

        detailsDoList.forEach(e -> {
            e.buildChangeName(eventType);
            e.buildPaymentInfo(eventType);
        });

        Map<String, List<DxPaymentDetailsV>> groupedMap = detailsDoList.stream()
                .collect(Collectors.groupingBy(DxPaymentDetailsV::groupKeyForIncome));
        List<String> draweeIds = innerInfoList.stream()
                .map(ContractPayPlanInnerInfoV::getDraweeId)
                .distinct()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        List<SupplierSimpleV> supplierSimpleList = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(draweeIds)){
            supplierSimpleList = orgClient.simpleListSupplier(draweeIds);
        }
        Map<String, SupplierSimpleV> supplierIdMap = supplierSimpleList.stream()
                .collect(Collectors.toMap(SupplierSimpleV::getId, e -> e));

        List<DictionaryValueV> taxRateDicItems = configClient.getKeyAndValue("FUND_TAX_RATE", null, null);
        Map<String, FinancialTaxTypeEnum> taxRateIdMap = taxRateDicItems.stream()
                .collect(Collectors.toMap(DictionaryValueV::getCode, e -> FinancialTaxTypeEnum.getEnumByTaxRate(e.getName())));

        List<String> matchedFtIdList = Lists.newArrayList();
        groupedMap.forEach((key,unitDetailDos) -> {
            DxPaymentDetailsV v = unitDetailDos.get(0);
            DxPaymentDetails map = Global.mapperFacade.map(v, DxPaymentDetails.class);
            map.setTaxTypeName(TaxTypeEnum.getNameByCode(map.getTaxType()));
            map.setExchangeRate(BigDecimal.ONE.setScale(6,RoundingMode.HALF_UP));

            // 金额 累加
            if (eventType.equals(ZJTriggerEventBillTypeEnum.收入确认实签.getCode())){
                setAmountOnSignedScene(unitDetailDos, map);
                map.setCorrespondingPaymentId(v.getSignedPaymentId());
            }
            if (eventType.equals(ZJTriggerEventBillTypeEnum.收入确认计提.getCode()) ||
                    eventType.equals(ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode())){
                setAmountOnPreScene(unitDetailDos, map);
                map.setCorrespondingPaymentId(v.getPaymentId());
            }
            map.setCorrespondingPaymentId(v.getPaymentId());
            map.setCorrespondingPaymentName(v.getPaymentName());
            ContractPayPlanInnerInfoV innerInfo = contractIdToInnerInfoMap.get(v.getContractId());
            if (Objects.nonNull(innerInfo)){
                map.setMaturityDate(innerInfo.getExpireNextEndDate());
                map.setExchangeUnitName(innerInfo.getDrawee());
                if (StringUtils.isNotBlank(innerInfo.getDraweeId())){
                    SupplierSimpleV simpleV = supplierIdMap.get(innerInfo.getDraweeId());
                    if (Objects.nonNull(simpleV)){
                        map.setExchangeUnitMainCode(simpleV.getMainDataCode());
                    }
                    //兼容数据判断，因历史数据为BP开头导致数据查询为空，传送财务云时为空
                    else if (innerInfo.getDraweeId().startsWith("BP")){
                        map.setExchangeUnitMainCode(innerInfo.getDraweeId());
                    }
                }
                map.setConMainCode(innerInfo.getConMainCode());
            }
            FinancialTaxTypeEnum taxTypeEnum = taxRateIdMap.get(v.getTaxRateId());
            if (Objects.nonNull(taxTypeEnum)){
                map.setTaxTypeName(taxTypeEnum.getValue());
                map.setTaxType(taxTypeEnum.getCode().toString());
            }
            if (ZJTriggerEventBillTypeEnum.收入确认实签.getCode() == eventType){
                if (StringUtils.equals("核销预收款", map.getChangeName())){
                    List<DxPaymentDetails> matchedList = mdm63Service.match(map, eventType, null,matchedFtIdList);
                    detailsList.addAll(matchedList);
                } else {
                    detailsList.add(map);
                }
            }
            if (ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode() == eventType){
                List<DxPaymentDetails> matchedList = mdm63Service.match(map, eventType, null,matchedFtIdList);
                detailsList.addAll(matchedList);
            }
            if (ZJTriggerEventBillTypeEnum.收入确认计提.getCode() == eventType){
                detailsList.add(map);
            }
        });

    }

    private void setAmountOnPreScene(List<DxPaymentDetailsV> unitDetailDos, DxPaymentDetails map) {
        map.setStandardAmount(unitDetailDos.stream().map(DxPaymentDetailsV::getTaxExcludAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP));
        map.setOriginCurrencyAmount(unitDetailDos.stream().map(DxPaymentDetailsV::getTaxExcludAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP));
        map.setTaxIncludedAmount(unitDetailDos.stream().map(DxPaymentDetailsV::getTaxIncludAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP));
    }

    private void handleDetailsOnPayScene(VoucherBillDxZJ voucherBill,
                                         List<DxPaymentDetails> detailsList,
                                         List<DxPaymentDetailsV> detailsDoList,
                                         Integer eventType) {
        List<String> contractIdList = detailsDoList.stream()
                .map(DxPaymentDetailsV::getContractId).distinct()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        List<ContractPayPlanInnerInfoV> innerInfoList = contractClient.getInnerInfoByContractIdOnPay(contractIdList);
        Map<String, ContractPayPlanInnerInfoV> contractIdToInnerInfoMap = innerInfoList.stream()
                .collect(Collectors.toMap(ContractPayPlanInnerInfoV::getContractId, e -> e, (oldVal, newVal) -> newVal));

        detailsDoList.forEach(e -> {
            e.buildChangeName(eventType);
            e.buildPaymentInfo(eventType);
        });

        Map<String, List<DxPaymentDetailsV>> changeNameMap = detailsDoList.stream()
                .collect(Collectors.groupingBy(DxPaymentDetailsV::getChangeName));

        List<String> matchedFtIdList = Lists.newArrayList();
        changeNameMap.forEach((changeName,details) -> {
            Map<String, List<DxPaymentDetailsV>> collect = details.stream()
                    .collect(Collectors.groupingBy(DxPaymentDetailsV::getPaymentId));
            if (MapUtils.isNotEmpty(collect)){
                collect.forEach((paymentId,paymentDetails) -> {
                    DxPaymentDetails map = Global.mapperFacade.map(paymentDetails.get(0), DxPaymentDetails.class);
                    map.setExchangeRate(BigDecimal.ONE.setScale(6,RoundingMode.HALF_UP));
                    // 金额 累加
                    if (eventType.equals(ZJTriggerEventBillTypeEnum.对下结算实签.getCode())){
                        setAmountOnSignedScene(paymentDetails, map);
                    }
                    if (eventType.equals(ZJTriggerEventBillTypeEnum.对下结算计提.getCode()) ||
                            eventType.equals(ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode())){
                        setAmountOnPreScene(paymentDetails, map);
                    }
                    if (StringUtils.equals("核销预付款", map.getChangeName())){
                        map.setCorrespondingPaymentId(paymentDetails.get(0).getPaymentId());
                        map.setCorrespondingPaymentName(paymentDetails.get(0).getPaymentName());
                    } else {
                        map.setCorrespondingPaymentName(paymentDetails.get(0).getPaymentName());
                    }
                    ContractPayPlanInnerInfoV innerInfo = contractIdToInnerInfoMap.get(paymentDetails.get(0).getContractId());
                    if (Objects.nonNull(innerInfo)){
                        map.setMaturityDate(innerInfo.getExpireNextEndDate());
                        map.setConMainCode(innerInfo.getConMainCode());
                    }
                    map.setChangeName(changeName);

                    // 对下结算-实签 若是核销预付款 则需要匹配核销
                    if (eventType.equals(ZJTriggerEventBillTypeEnum.对下结算实签.getCode()) &&
                            StringUtils.equals(VoucherChargeTypeEnums.核销预付款.getValue(), changeName)){
                        List<DxPaymentDetails> matchedList =
                                mdm63Service.match(map, eventType, voucherBill.getSettlementId(), matchedFtIdList);
                        detailsList.addAll(matchedList);
                    } else if(eventType.equals(ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode())){
                        List<DxPaymentDetails> matchedList =
                                mdm63Service.match(map, eventType, voucherBill.getSettlementId(), matchedFtIdList);
                        detailsList.addAll(matchedList);
                    } else {
                        detailsList.add(map);
                    }
                });
            }

        });
    }

    private void setAmountOnSignedScene(List<DxPaymentDetailsV> paymentDetails, DxPaymentDetails map) {
        map.setStandardAmount(paymentDetails.stream().map(DxPaymentDetailsV::getTaxIncludAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP));
        map.setOriginCurrencyAmount(paymentDetails.stream().map(DxPaymentDetailsV::getTaxIncludAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP));
        map.setTaxIncludedAmount(paymentDetails.stream().map(DxPaymentDetailsV::getTaxIncludAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP));
    }

    public List<DxCostDetails> getCostDetails(String voucherBillNo) {
        List<VoucherPushBillDetailDxZJ> details = voucherBillDetailRepository.getByVoucherBillNo(voucherBillNo);
        if (CollectionUtils.isEmpty(details)){
            return Collections.emptyList();
        }

        Integer eventType = details.get(0).getBillEventType();
        String communityId = details.get(0).getCommunityId();
        if (eventType.equals(ZJTriggerEventBillTypeEnum.对下结算计提.getCode()) ||
                eventType.equals(ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode()) ||
                eventType.equals(ZJTriggerEventBillTypeEnum.对下结算实签.getCode())){
            List<DxCostDetailsV> detailsDoList =
                    voucherBillDetailRepository.getCostDetailsVByVoucherBillNoOnPay(voucherBillNo, eventType,communityId);
            List<DxCostDetails> costDetailList = handleDetailsOnPayScene(detailsDoList, communityId);
            // 对下结算-实签单独处理，对比发票金额数据，处理其差额数据
            if(eventType.equals(ZJTriggerEventBillTypeEnum.对下结算实签.getCode())){
                //获取其发票数据
                List<DxInvoiceDetails> invoiceList = this.getInvoiceDetails(voucherBillNo);
                if(CollectionUtils.isEmpty(invoiceList)){
                    log.info("该合同报账单{}，无发票数据，不对成本数据进行操作",voucherBillNo);
                    return costDetailList;
                }
                //【发票】不含税金额
                BigDecimal invoiceTotalTaxExcluded = invoiceList.stream()
                        .map(DxInvoiceDetails::getTaxExcludedAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                //【发票】税额
                BigDecimal invoiceTotalTax = invoiceList.stream()
                        .map(DxInvoiceDetails::getTaxAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                //【发票】价税合计
                BigDecimal invoiceTotalInvoiceTax = invoiceList.stream()
                        .map(DxInvoiceDetails::getInvoiceTaxAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                //【成本明细】不含税金额
                BigDecimal taxExcludedAmount = costDetailList.stream()
                        .map(DxCostDetails::getTaxExcludedAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                //【成本明细】税额
                BigDecimal deductibleTaxAmount = costDetailList.stream()
                        .map(DxCostDetails::getDeductibleTaxAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                //【成本明细】价税合计
                BigDecimal taxInclusiveTotalAmount = costDetailList.stream()
                        .map(DxCostDetails::getTaxInclusiveTotalAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                //通过比较不含税金额，判断成本汇总是否与发票一致，若不一致，则处理其差异数据
                // 不含税金额-计算差值
                BigDecimal difference = invoiceTotalTaxExcluded.subtract(taxExcludedAmount);
                if(difference.compareTo(BigDecimal.ZERO) == 0){
                    log.info("合同报账单{}：对下实签,其成本明细数据，与发票一致，不处理其逻辑",voucherBillNo);
                    return costDetailList;
                }
                log.info("合同报账单{}：对下实签,其成本明细数据，与发票不一致，特殊处理其逻辑",voucherBillNo);
                log.info("合同报账单{}：其成本明细数据：{}，发票数据：{}",voucherBillNo, JSONArray.toJSON(costDetailList), JSONArray.toJSON(invoiceList));
                // 根据差值正负进行处理
                if (difference.compareTo(BigDecimal.ZERO) > 0) {
                    //正差值 - 相加操作
                    costDetailList.get(0).setTaxExcludedAmount(costDetailList.get(0).getTaxExcludedAmount().add(difference));
                    costDetailList.get(0).setTaxExcludedStandardAmount(costDetailList.get(0).getTaxExcludedAmount());
                } else {
                    //负差值 - 相减操作
                    costDetailList.get(0).setTaxExcludedAmount(costDetailList.get(0).getTaxExcludedAmount().subtract(difference.abs()));
                    costDetailList.get(0).setTaxExcludedStandardAmount(costDetailList.get(0).getTaxExcludedAmount());
                }
                // 含税-计算差值
                BigDecimal taxAmountDifference = invoiceTotalTax.subtract(deductibleTaxAmount);
                if (taxAmountDifference.compareTo(BigDecimal.ZERO) > 0) {
                    //正差值 - 相加操作
                    costDetailList.get(0).setDeductibleTaxAmount(costDetailList.get(0).getDeductibleTaxAmount().add(taxAmountDifference));
                    costDetailList.get(0).setDeductibleTaxStandardAmount(costDetailList.get(0).getDeductibleTaxAmount());
                } else {
                    //负差值 - 相减操作
                    costDetailList.get(0).setDeductibleTaxAmount(costDetailList.get(0).getDeductibleTaxAmount().subtract(taxAmountDifference.abs()));
                    costDetailList.get(0).setDeductibleTaxStandardAmount(costDetailList.get(0).getDeductibleTaxAmount());
                }
                // 价税合计-计算差值
                BigDecimal taxInclusiveTotalDifference = invoiceTotalInvoiceTax.subtract(taxInclusiveTotalAmount);
                if (taxInclusiveTotalDifference.compareTo(BigDecimal.ZERO) > 0) {
                    //正差值 - 相加操作
                    costDetailList.get(0).setTaxInclusiveTotalAmount(costDetailList.get(0).getTaxInclusiveTotalAmount().add(taxInclusiveTotalDifference));
                    costDetailList.get(0).setTaxInclusiveTotalStandardAmount(costDetailList.get(0).getTaxInclusiveTotalAmount());
                } else {
                    //负差值 - 相减操作
                    costDetailList.get(0).setTaxInclusiveTotalAmount(costDetailList.get(0).getTaxInclusiveTotalAmount().subtract(taxInclusiveTotalDifference.abs()));
                    costDetailList.get(0).setTaxInclusiveTotalStandardAmount(costDetailList.get(0).getTaxInclusiveTotalAmount());
                }
            }
            return costDetailList;
        }
        return Collections.emptyList();
    }


    private List<DxCostDetails> handleDetailsOnPayScene(List<DxCostDetailsV> details, String communityId){
        List<CfgExternalDataV> community = configClient.getExternalMapByCode("community", communityId);
        // 调用外部数据映射接口
        String departmentName = null;
        String departmentCode = null;
        for (CfgExternalDataV cfgExternalDataV : community) {
            if ("org".equals(cfgExternalDataV.getExternalDataType())){
                departmentName = cfgExternalDataV.getDataName();
                departmentCode = cfgExternalDataV.getDataCode();
            }
        }
        List<DxCostDetails> res = new ArrayList<>();
        Map<String, List<DxCostDetailsV>> costMap = details.stream()
                .filter(e -> StringUtils.isNotBlank(e.getPaymentId()))
                .collect(Collectors.groupingBy(DxCostDetailsV::getPaymentId));
        String finalDepartmentCode = departmentCode;
        String finalDepartmentName = departmentName;
        costMap.forEach((paymentId, costDetails)->{
            DxCostDetailsV v = costDetails.get(0);
            DxCostDetails map = Global.mapperFacade.map(v, DxCostDetails.class);
            BigDecimal deductibleTaxAmount = costDetails.stream()
                    .map(DxCostDetailsV::getDeductibleTaxAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal taxExcludedAmount = costDetails.stream()
                    .map(DxCostDetailsV::getTaxExcludedAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal taxInclusiveTotalAmount = costDetails.stream()
                    .map(DxCostDetailsV::getTaxInclusiveTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
            //税额
            map.setDeductibleTaxAmount(deductibleTaxAmount);
            map.setDeductibleTaxStandardAmount(deductibleTaxAmount);
            //不含税-金额
            map.setTaxExcludedAmount(taxExcludedAmount);
            map.setTaxExcludedStandardAmount(taxExcludedAmount);
            //价税合计
            map.setTaxInclusiveTotalAmount(taxInclusiveTotalAmount);
            map.setTaxInclusiveTotalStandardAmount(taxInclusiveTotalAmount);
            map.setDepartmentCode(finalDepartmentCode);
            map.setDepartmentName(finalDepartmentName);
            res.add(map);
        });

        return res;
    }

    public List<DxInvoiceDetails> getInvoiceDetails(String voucherBillNo) {
        List<VoucherPushBillDetailDxZJ> details = voucherBillDetailRepository.getByVoucherBillNo(voucherBillNo);
        if (CollectionUtils.isEmpty(details)){
            return Collections.emptyList();
        }
        Long id = details.get(0).getBillId();
        String communityId = details.get(0).getCommunityId();
        TemporaryChargeBillDetailV billInfo = temporaryChargeBillAppService.getById(id, TemporaryChargeBillDetailV.class, communityId);
        if(StringUtils.isNotBlank(billInfo.getExtField7())){
            List<ContractSettlementInvoiceDetailF> invoice = contractClient.getInvoice(billInfo.getExtField7());
            if(CollectionUtils.isNotEmpty(invoice)){
                List<DxInvoiceDetails> dxInvoiceDetails = Global.mapperFacade.mapAsList(invoice, DxInvoiceDetails.class);
                if(CollectionUtils.isNotEmpty(dxInvoiceDetails)){
                    dxInvoiceDetails.forEach(detail->{
                        detail.setTaxRate(detail.getTaxRate().setScale(2, RoundingMode.HALF_UP));
                        detail.setTaxAmount(detail.getTaxAmount().setScale(2, RoundingMode.HALF_UP));
                        detail.setTaxExcludedAmount(detail.getInvoiceTaxAmount().subtract(detail.getTaxAmount())
                                .setScale(2, RoundingMode.HALF_UP));
                        detail.setDeductibleTaxAmount(detail.getDeductionAmount().setScale(2, RoundingMode.HALF_UP));
                    });
                }
                return dxInvoiceDetails;
            }
        }

        return Collections.emptyList();
    }

    public List<DxMeasurementDetail> getMeasurementDetails(String voucherBillNo) {
        //获取合同报账单信息
        VoucherBillDxZJ voucherBillDxZJ = voucherPushBillDxZJRepository.queryByVoucherBillNo(voucherBillNo);
        //获取合同报账单明细数据
        List<VoucherPushBillDetailDxZJ> details = voucherBillDetailRepository.getByVoucherBillNo(voucherBillNo);
        if (CollectionUtils.isEmpty(details)){
            return Collections.emptyList();
        }
        //对下结算-实签，获取发票明细，对计量清单逻辑变更
        Map<BigDecimal, DxInvoiceDetails> dxInvoiceListMap;
        if(ZJTriggerEventBillTypeEnum.对下结算实签.getCode() == voucherBillDxZJ.getBillEventType()){
            List<DxInvoiceDetails>  invoiceList = this.getInvoiceDetails(voucherBillNo);
            //通过税率分总，并对不含税金额、税额及价税合计进行汇总
            dxInvoiceListMap = invoiceList.stream().filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(
                            detail -> safeStripTrailingZeros(detail.getTaxRate()),
                            Collectors.reducing(
                                    new DxInvoiceDetails(), // Identity
                                    detail -> {
                                        DxInvoiceDetails d = new DxInvoiceDetails();
                                        d.setTaxRate(detail.getTaxRate());
                                        d.setTaxExcludedAmount(getSafeAmount(detail.getTaxExcludedAmount()));
                                        d.setTaxAmount(getSafeAmount(detail.getTaxAmount()));
                                        d.setInvoiceTaxAmount(getSafeAmount(detail.getInvoiceTaxAmount()));
                                        return d;
                                    },
                                    (d1, d2) -> {
                                        DxInvoiceDetails result = new DxInvoiceDetails();
                                        result.setTaxRate(d1.getTaxRate());
                                        result.setTaxExcludedAmount(addSafe(d1.getTaxExcludedAmount(),d2.getTaxExcludedAmount()));
                                        result.setTaxAmount(addSafe(d1.getTaxAmount(),d2.getTaxAmount()));
                                        result.setInvoiceTaxAmount(addSafe(d1.getInvoiceTaxAmount(),d2.getInvoiceTaxAmount()));
                                        return result;
                                    }
                            )
                    ));
        } else {
            dxInvoiceListMap = new HashMap<>();
        }
        //通过税率对明细数据进行分组
        Map<Long, List<VoucherPushBillDetailDxZJ>> taxRateIdMap = details.stream()
                .collect(Collectors.groupingBy(VoucherPushBillDetailDxZJ::getTaxRateId));
        List<DxMeasurementDetail> measurementDetailList = Lists.newArrayList();
        taxRateIdMap.forEach((taxRateId, detailsList) -> {
            DxMeasurementDetail measurementDetail = new DxMeasurementDetail();
            //含税金额
            BigDecimal amountWithTax = detailsList.stream()
                    .map(VoucherPushBillDetailDxZJ::getTaxIncludAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(100),2 ,RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);
            measurementDetail.setOriginAmountWithTax(amountWithTax);
            measurementDetail.setStandardAmountWithTax(amountWithTax);
            //不含税金额
            BigDecimal amountWithoutTax = detailsList.stream()
                    .map(VoucherPushBillDetailDxZJ::getTaxExcludAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(100),2 ,RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);
            measurementDetail.setOriginAmountWithoutTax(amountWithoutTax);
            measurementDetail.setStandardAmountWithoutTax(amountWithoutTax);
            if (voucherBillDxZJ.getBillEventType() == ZJTriggerEventBillTypeEnum.对下结算计提.getCode() ||
                    voucherBillDxZJ.getBillEventType() == ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode()){
                //含税金额
                measurementDetail.setOriginAmountWithTax(amountWithoutTax);
                measurementDetail.setStandardAmountWithTax(amountWithoutTax);
            }
            //税额
            BigDecimal taxAmount = detailsList.stream()
                    .map(VoucherPushBillDetailDxZJ::getTaxAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(100),2 ,RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);
            measurementDetail.setStandardTaxAmount(taxAmount);
            measurementDetail.setOriginTaxAmount(taxAmount);
            //税率
            measurementDetail.setTaxRate(detailsList.get(0).getTaxRate().stripTrailingZeros());

            //对下结算-实签，直接将发票明细数据赋值到对应清单中，解决发票与系统计算尾差
            DxInvoiceDetails invoiceDetail = dxInvoiceListMap.get(measurementDetail.getTaxRate());
            if(ZJTriggerEventBillTypeEnum.对下结算实签.getCode() == voucherBillDxZJ.getBillEventType() && Objects.nonNull(invoiceDetail)){
                log.info("该合同报账单{}，含有发票数据，用发票数据进行替换数据",voucherBillNo);
                //含税金额
                measurementDetail.setOriginAmountWithTax(invoiceDetail.getInvoiceTaxAmount());
                measurementDetail.setStandardAmountWithTax(invoiceDetail.getInvoiceTaxAmount());
                //不含税金额
                measurementDetail.setOriginAmountWithoutTax(invoiceDetail.getTaxExcludedAmount());
                measurementDetail.setStandardAmountWithoutTax(invoiceDetail.getTaxExcludedAmount());
                //税额
                measurementDetail.setStandardTaxAmount(invoiceDetail.getTaxAmount());
                measurementDetail.setOriginTaxAmount(invoiceDetail.getTaxAmount());
            }
            if (voucherBillDxZJ.getBillEventType() == ZJTriggerEventBillTypeEnum.对下结算计提.getCode() ||
                    voucherBillDxZJ.getBillEventType() == ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode()){
                measurementDetail.setTaxRate(BigDecimal.ZERO);
                measurementDetail.setStandardTaxAmount(BigDecimal.ZERO);
                measurementDetail.setOriginTaxAmount(BigDecimal.ZERO);
            }
            measurementDetailList.add(measurementDetail);
        });

        return measurementDetailList;
    }

    private static BigDecimal safeStripTrailingZeros(BigDecimal value) {
        return value != null ? value.stripTrailingZeros() : BigDecimal.ZERO;
    }

    // 辅助方法：处理null金额
    private static BigDecimal getSafeAmount(BigDecimal amount) {
        return amount != null ? amount : BigDecimal.ZERO;
    }
    private static BigDecimal addSafe(BigDecimal a, BigDecimal b) {
        BigDecimal safeA = a != null ? a : BigDecimal.ZERO;
        BigDecimal safeB = b != null ? b : BigDecimal.ZERO;
        return safeA.add(safeB);
    }

    public List<GeneralDetails> queryCommonIncomeDetail(String voucherBillNo) {
        List<GeneralDetails> generalDetailsList = this.generalRevenueRecognition(voucherBillNo);
        if (CollectionUtils.isEmpty(generalDetailsList)) {
            return Collections.emptyList();
        }
        generalDetailsList.forEach(g->{
            g.setExchangeRate(scaleToSixDecimal(g.getExchangeRate(),6));
            g.setTaxAmount(scaleToSixDecimal(g.getTaxAmount(),2));
            g.setTaxExcludedAmount(scaleToSixDecimal(g.getTaxExcludedAmount(),2));
            g.setTaxIncludedAmount(scaleToSixDecimal(g.getTaxIncludedAmount(),2));
        });
        return generalDetailsList;
    }

    public static BigDecimal scaleToSixDecimal(BigDecimal value,Integer dig) {
        if (value == null) {
            return null;
        }
        return value.setScale(dig, RoundingMode.HALF_UP);
    }
}
