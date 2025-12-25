package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.dx;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.signature.ExternalMaindataCalmappingListF;
import com.wishare.finance.apps.model.signature.ExternalMaindataCalmappingListV;
import com.wishare.finance.apps.pushbill.fo.SyncBatchPushZJBillF;
import com.wishare.finance.apps.pushbill.vo.UploadLinkZJ;
import com.wishare.finance.apps.pushbill.vo.dx.DxCostDetails;
import com.wishare.finance.apps.pushbill.vo.dx.DxInvoiceDetails;
import com.wishare.finance.apps.pushbill.vo.dx.DxMeasurementDetail;
import com.wishare.finance.apps.pushbill.vo.dx.DxPaymentDetails;
import com.wishare.finance.apps.service.bill.FinanceCloudService;
import com.wishare.finance.apps.service.pushbill.VoucherBillDxDetailAppService;
import com.wishare.finance.domains.invoicereceipt.consts.enums.TaxpayerTypeEnum;
import com.wishare.finance.domains.mdm.entity.Mdm63E;
import com.wishare.finance.domains.mdm.repository.mapper.Mdm63Mapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.*;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.DocumentTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.facade.PushBillZJFacade;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.PaymentApplicationFormKXMXRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.PaymentApplicationFormRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.PaymentApplicationPayMxRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherBillDetailDxZJMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.*;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.dx.financialcloud.FinancialBottomLineSettlementData;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.clients.base.*;
import com.wishare.finance.infrastructure.remote.enums.financialcloud.FinancialTaxTypeEnum;
import com.wishare.finance.infrastructure.remote.vo.config.CfgExternalDataV;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractPayConcludeF;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractPayConcludeV;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractPayPlanInnerInfoV;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractPaySettlementDetailsV;
import com.wishare.finance.infrastructure.remote.vo.org.CustomerSimpleV;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import com.wishare.finance.infrastructure.remote.vo.org.SupplierSimpleV;
import com.wishare.finance.infrastructure.remote.vo.zj.MDM17V;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.owl.exception.OwlBizException;
import org.springframework.context.annotation.Lazy;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author longhuadmin
 * 单据-中交请求财务云 请求信息组装service
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SuppressWarnings("all")
public class FinancialRequestBodyService {

    private final UserClient userClient;
    private final ExternalClient externalClient;
    private final ConfigClient configClient;
    private final ContractClient contractClient;
    private final SpaceClient spaceClient;
    private final OrgClient orgClient;
    private final VoucherBillDetailDxZJMapper detailDxZJMapper;
    private final PaymentApplicationFormRepository paymentApplicationFormRepository;
    private final PaymentApplicationFormKXMXRepository paymentApplicationFormKXMXRepository;
    private final PaymentApplicationPayMxRepository paymentApplicationPayMxRepository;
    private final FinanceCloudService financeCloudService;
    @Autowired
    @Lazy
    private PushBillZJDomainService pushBillZJDomainService;
    @Autowired
    private VoucherBillDxDetailAppService detailAppService;
    @Autowired
    private Mdm63Mapper mdm63Mapper;

    @Autowired
    protected PushBillZJFacade pushBillZJFacade;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 获取对下结算单的财务云请求信息
     * 参考 com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.PushBillZJDomainService#zjDXJSRequestBody 1063
     **/
    public String getDxRequestBody(VoucherBillDxZJ voucherBill,
                                   List<DxPaymentDetails> dxPaymentDetailsList,
                                   SyncBatchPushZJBillF syncBatchPushBillF,
                                   String projectId,
                                   String empCode,
                                   List<SPRZData> spRZDataList,
                                   List<String> ftIdList) throws JsonProcessingException {
        List<VoucherPushBillDetailDxZJ> details = detailDxZJMapper.selectList(new LambdaQueryWrapper<VoucherPushBillDetailDxZJ>()
                .eq(VoucherPushBillDetailDxZJ::getDeleted,0)
                .in(VoucherPushBillDetailDxZJ::getVoucherBillNo,voucherBill.getVoucherBillNo()));

        ZJRequestBody zjRequestBody = new ZJRequestBody();
        ZJParameter zjParameter = new ZJParameter();
        zjParameter.setBusinessCode(DocumentTypeEnum.valueOfByCode(voucherBill.getBillEventType()).getValue());
        zjParameter.setUnitCode("MDM");
        zjParameter.setSourceSystem("CCCG-DMC");
        zjParameter.setAppInstanceCode("10000");

        List<BILLDATAS> billDataList = Lists.newArrayList();
        BILLDATAS billDatas = new BILLDATAS();

        BILLDATA DXJS_UNIT = new BILLDATA();
        DXJS_UNIT.setCode(DocumentTypeEnum.valueOfByCode(voucherBill.getBillEventType()).getValue());
        DXJS_UNIT.setIsMain("true");
        List<FinancialBottomLineSettlementData> DATA_DXJS =  buildDXJS(voucherBill, syncBatchPushBillF, projectId, empCode, details);
        DXJS_UNIT.setData(DATA_DXJS);

        // 计量明细
        BILLDATA DXJJJLMX_UNIT = new BILLDATA();
        DXJJJLMX_UNIT.setCode("DXJJJLMX");
        DXJJJLMX_UNIT.setIsMain("false");
        List<DXJJJLMXData> DATA_DXJJJLMX =  buildDXJJJLMX(voucherBill, syncBatchPushBillF, projectId, empCode, details);
        DXJJJLMX_UNIT.setData(DATA_DXJJJLMX);

        // 款项明细
        BILLDATA DXJJKJMX_UNIT = new BILLDATA();
        DXJJKJMX_UNIT.setCode("DXJJKJMX");
        DXJJKJMX_UNIT.setIsMain("false");
        List<DXJJKJMXData> DATA_DXJJKJMX =  buildDXJJKJMX(voucherBill, syncBatchPushBillF, projectId, empCode, details, ftIdList);
        DXJJKJMX_UNIT.setData(DATA_DXJJKJMX);

        // 成本明细
        BILLDATA DXJJCBMX_UNIT = new BILLDATA();
        DXJJCBMX_UNIT.setCode("DXJJCBMX");
        DXJJCBMX_UNIT.setIsMain("false");
        List<DXJJCBMXData> DATA_DXJJCBMX =  buildDXJJCBMX(voucherBill, syncBatchPushBillF, projectId, empCode, details);
        DXJJCBMX_UNIT.setData(DATA_DXJJCBMX);

        //  发票明细
        BILLDATA DXJJINVOICE_UNIT = new BILLDATA();
        DXJJINVOICE_UNIT.setCode("DXJJINVOICE");
        DXJJINVOICE_UNIT.setIsMain("false");
        if (voucherBill.getBillEventType() == 4){
            List<DXJJINVOICEData> DATA_DXJJINVOICE = buildDXJJINVOICE(voucherBill, syncBatchPushBillF, projectId, empCode, details);
            DXJJINVOICE_UNIT.setData(DATA_DXJJINVOICE);
        }

        //  影像信息
        BILLDATA YXXXBillData = new BILLDATA();
        List<YXXXData> YXXXDataList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(voucherBill.getUploadLink())) {
            for (UploadLinkZJ uploadLinkZJ : voucherBill.getUploadLink()) {
                //影像信息
                YXXXData yxxxData = new YXXXData();
                yxxxData.setYXBM(uploadLinkZJ.getImageIdZJ());
                YXXXDataList.add(yxxxData);
            }
        }
        YXXXBillData.setCode("YXXX");
        YXXXBillData.setIsMain("false");
        YXXXBillData.setData(YXXXDataList);

        //  审批日志
        BILLDATA SPRZDataBillData = new BILLDATA();
        SPRZDataBillData.setCode("SPRZ");
        SPRZDataBillData.setIsMain("false");
        SPRZDataBillData.setData(spRZDataList);

        ArrayList<BILLDATA> billdatas = Lists.newArrayList();
        billdatas.add(DXJS_UNIT);
        billdatas.add(DXJJJLMX_UNIT);
        billdatas.add(DXJJKJMX_UNIT);
        billdatas.add(DXJJCBMX_UNIT);
        if (voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.对下结算实签.getCode()){
            billdatas.add(DXJJINVOICE_UNIT);
        }
        billdatas.add(YXXXBillData);
        if (CollectionUtils.isNotEmpty(spRZDataList) &&
                voucherBill.getBillEventType() != ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode()){
            billdatas.add(SPRZDataBillData);
        }

        billDatas.setBILLDATA(billdatas);
        billDataList.add(billDatas);

        zjParameter.setPsData(billDataList);
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("中交推单原始参数 {}", JSON.toJSONString(zjParameter));
        String billdataString = objectMapper.writeValueAsString(zjParameter).replace("\"", "'");
        String replace = "---";
        zjRequestBody.setContext("---");
        String zjRequestBodyString = JSON.toJSONString(zjRequestBody).replace(replace, billdataString);
        log.info("中交推单参数 {}", zjRequestBodyString);
        // 测试代码
//        if (true){
//            throw new RuntimeException("测试中交推单");
//        }
        return zjRequestBodyString;
    }

    private List<DXJJINVOICEData> buildDXJJINVOICE(VoucherBillDxZJ voucherBill,
                                                   SyncBatchPushZJBillF syncBatchPushBillF,
                                                   String projectId,
                                                   String empCode,
                                                   List<VoucherPushBillDetailDxZJ> details) {
        List<DxInvoiceDetails> invoiceDetails = detailAppService.getInvoiceDetails(voucherBill.getVoucherBillNo());
        if (CollectionUtils.isEmpty(invoiceDetails)){
            throw new OwlBizException("请先在 合同管理-结算审批 页面上传发票");
        }
        List<DXJJINVOICEData> dxjjinvoiceDataList = Lists.newArrayList();
        for (DxInvoiceDetails invoice : invoiceDetails) {
            DXJJINVOICEData dxjjinvoiceData = new DXJJINVOICEData();
            dxjjinvoiceData.setJSNM(voucherBill.getVoucherBillNo());
            String FPMXNMNumber = IdentifierFactory.getInstance().serialNumber("pushbillZJ", "FPMXNM", 20);
            dxjjinvoiceData.setFPMXNM(FPMXNMNumber);
            dxjjinvoiceData.setFPHM(invoice.getInvoiceNum());
            dxjjinvoiceData.setFPDM(invoice.getInvoiceCode());
            dxjjinvoiceData.setJYM(invoice.getVerificationCode());
            dxjjinvoiceData.setKPRQ(invoice.getInvoiceDate().toString());
            dxjjinvoiceData.setBHSJE(invoice.getTaxExcludedAmount().doubleValue());
            if (Objects.nonNull(invoice.getTaxRate())){
                dxjjinvoiceData.setSL(invoice.getTaxRate().toString());
            }
            dxjjinvoiceData.setSE(invoice.getTaxAmount().doubleValue());
            dxjjinvoiceData.setJSHJ(invoice.getInvoiceTaxAmount().doubleValue());
            dxjjinvoiceData.setNOTE("");
            // todo prd没说
            dxjjinvoiceData.setSFFB("0");
            dxjjinvoiceData.setFPLX(invoice.getInvoiceType());
            dxjjinvoiceData.setSFCKTS(invoice.getIsExportDrawback());
            if (Objects.nonNull(invoice.getDeductibleTaxAmount())){
                dxjjinvoiceData.setKDKSE(invoice.getDeductibleTaxAmount().doubleValue());
                dxjjinvoiceData.setKDKJEBB(invoice.getDeductibleTaxAmount().doubleValue());
            }
            dxjjinvoiceData.setYBBZ("156");
            dxjjinvoiceData.setHL(1.000000d);
            dxjjinvoiceData.setSM(invoice.getTaxCode());
            dxjjinvoiceData.setYZZT(invoice.getVerifyStatus().toString());

            //购买方名称
            dxjjinvoiceData.setGMFMC(invoice.getInname());
            //购买方税号
            dxjjinvoiceData.setGMFSH(invoice.getGfsbh());
            //销售方名称
            dxjjinvoiceData.setXSFMC(invoice.getOutname());
            //销售方税号
            dxjjinvoiceData.setSH(invoice.getXfsbh());

            dxjjinvoiceDataList.add(dxjjinvoiceData);
        }
        return dxjjinvoiceDataList;
    }

    private List<DXJJCBMXData> buildDXJJCBMX(VoucherBillDxZJ voucherBill,
                                             SyncBatchPushZJBillF syncBatchPushBillF,
                                             String projectId,
                                             String empCode,
                                             List<VoucherPushBillDetailDxZJ> details) {
        List<DxCostDetails> costDetails = detailAppService.getCostDetails(voucherBill.getVoucherBillNo());
        if (CollectionUtils.isEmpty(costDetails)){
            return null;
        }
        List<DXJJCBMXData> dxjjcbmxDataList = Lists.newArrayList();
        for (DxCostDetails costDetail : costDetails) {
            DXJJCBMXData dxjjcbmxData = new DXJJCBMXData();
            String CBMXNMNumber = IdentifierFactory.getInstance().serialNumber("pushbillZJ", "CBMXNM", 20);
            dxjjcbmxData.setCBMXNM(CBMXNMNumber);
            dxjjcbmxData.setJSNM(voucherBill.getVoucherBillNo());
            dxjjcbmxData.setYWKM(costDetail.getPaymentId());
            dxjjcbmxData.setYBID("156");
            dxjjcbmxData.setHL(1.000000d);

            dxjjcbmxData.setJSHJYB(costDetail.getTaxInclusiveTotalAmount());
            dxjjcbmxData.setJSHJBB(costDetail.getTaxInclusiveTotalStandardAmount());
            dxjjcbmxData.setKDKSEYB(costDetail.getDeductibleTaxAmount());
            dxjjcbmxData.setKDKSEBB(costDetail.getDeductibleTaxStandardAmount());
            dxjjcbmxData.setBHSJEYB(costDetail.getTaxExcludedAmount().setScale(2).doubleValue());
            dxjjcbmxData.setBHSJEBB(costDetail.getTaxExcludedStandardAmount().setScale(2).doubleValue());
            if (voucherBill.getBillEventType() == 3){
                dxjjcbmxData.setJSHJYB(costDetail.getTaxExcludedAmount());
                dxjjcbmxData.setJSHJBB(costDetail.getTaxExcludedStandardAmount());
                dxjjcbmxData.setKDKSEYB(BigDecimal.ZERO);
                dxjjcbmxData.setKDKSEBB(BigDecimal.ZERO);
            }
            dxjjcbmxData.setYSKM(costDetail.getBudgetItemId());
            dxjjcbmxDataList.add(dxjjcbmxData);
        }
        return dxjjcbmxDataList;
    }

    private List<DXJJJLMXData> buildDXJJJLMX(VoucherBillDxZJ voucherBill,
                                             SyncBatchPushZJBillF syncBatchPushBillF,
                                             String projectId,
                                             String empCode,
                                             List<VoucherPushBillDetailDxZJ> details) {
        List<DxMeasurementDetail> measurementDetails = detailAppService.getMeasurementDetails(voucherBill.getVoucherBillNo());
        if (CollectionUtils.isEmpty(measurementDetails)){
            return null;
        }
        List<DXJJJLMXData> dxjjjlmxDataList = Lists.newArrayList();
        for (DxMeasurementDetail measurementDetail : measurementDetails) {
            DXJJJLMXData dxjjjlmxData = new DXJJJLMXData();
            String JLMXNMNumber = IdentifierFactory.getInstance().serialNumber("pushbillZJ", "JLMXNM", 20);
            dxjjjlmxData.setJLMXNM(JLMXNMNumber);
            dxjjjlmxData.setJSNM(voucherBill.getVoucherBillNo());
            dxjjjlmxData.setJLHL(1.000000);
            dxjjjlmxData.setBZID("156");
            dxjjjlmxData.setSL(measurementDetail.getTaxRate().doubleValue());
            dxjjjlmxData.setHSJEYB(measurementDetail.getOriginAmountWithTax().doubleValue());
            dxjjjlmxData.setHSJEBB(measurementDetail.getStandardAmountWithTax().doubleValue());
            dxjjjlmxData.setSEYB(measurementDetail.getOriginTaxAmount().doubleValue());
            dxjjjlmxData.setSEBB(measurementDetail.getStandardTaxAmount().doubleValue());
            dxjjjlmxData.setBHSYB(measurementDetail.getOriginAmountWithoutTax().doubleValue());
            dxjjjlmxData.setBHSBB(measurementDetail.getStandardAmountWithoutTax().doubleValue());
            dxjjjlmxDataList.add(dxjjjlmxData);
        }
        return dxjjjlmxDataList;
    }

    private List<DXJJKJMXData> buildDXJJKJMX(VoucherBillDxZJ voucherBill,
                                             SyncBatchPushZJBillF syncBatchPushBillF,
                                             String projectId,
                                             String empCode,
                                             List<VoucherPushBillDetailDxZJ> details,
                                             List<String> ftIdList) {
        List<DxPaymentDetails> dxPaymentDetails = detailAppService.queryDetailsOfPayments(voucherBill.getVoucherBillNo());
        if (CollectionUtils.isEmpty(dxPaymentDetails)){
            return null;
        }
        List<DXJJKJMXData> dxjjkjmxDataList = Lists.newArrayList();
        for (DxPaymentDetails paymentDetail : dxPaymentDetails) {
            DXJJKJMXData dxjjkjmxData = new DXJJKJMXData();
            String KJMXNMNumber = IdentifierFactory.getInstance().serialNumber("pushbillZJ", "KJMXNM", 24);
            dxjjkjmxData.setKJMXNM(KJMXNMNumber);
            dxjjkjmxData.setJSNM(voucherBill.getVoucherBillNo());

            if ("核销预付款".equals(paymentDetail.getChangeName())) {
                dxjjkjmxData.setBD("02");
            }
            if ("实际应付款".equals(paymentDetail.getChangeName())) {
                dxjjkjmxData.setBD("04");
            }

            MDM17V mdm17V = externalClient.queryByCodeForMDM17(paymentDetail.getPaymentCode());
            dxjjkjmxData.setKJXM(mdm17V.getIdExt());
            dxjjkjmxData.setYBID("156");
            dxjjkjmxData.setHL(1.000000);

            dxjjkjmxData.setKJJEYB(String.valueOf(paymentDetail.getOriginCurrencyAmount()));
            dxjjkjmxData.setKJJEBB(String.valueOf(paymentDetail.getStandardAmount()));

            //dxjjkjmxData.setDQRQ(DateUtil.format(paymentDetail.getMaturityDate(), "yyyy-MM-dd"));
            //逻辑替换，到账日期使用报账单创建时间加周期
            dxjjkjmxData.setDQRQ(DateTimeUtil.getYearMonthDay(voucherBill.getGmtCreate().plusYears(1).minusDays(1)));
            dxjjkjmxData.setYSYFBH(paymentDetail.getFtId());
            dxjjkjmxDataList.add(dxjjkjmxData);
        }
        ftIdList.addAll(dxPaymentDetails.stream().map(DxPaymentDetails::getFtId).filter(StringUtil::isNotBlank).distinct().collect(Collectors.toList()));
        return dxjjkjmxDataList;
    }

    private List<FinancialBottomLineSettlementData> buildDXJS(VoucherBillDxZJ voucherBill,
                                                        SyncBatchPushZJBillF syncBatchPushBillF,
                                                        String projectId,
                                                        String empCode,
                                                        List<VoucherPushBillDetailDxZJ> details) {
        // DXJS
        FinancialBottomLineSettlementData dxjsData = new FinancialBottomLineSettlementData();
        dxjsData.setDJNM(voucherBill.getVoucherBillNo());
        dxjsData.setDJBH(voucherBill.getVoucherBillNo());
        dxjsData.setXZZZ(syncBatchPushBillF.getXZZZ());
        dxjsData.setXZBM(StringUtils.isNotEmpty(voucherBill.getExternalDepartmentCode()) ? voucherBill.getExternalDepartmentCode() : syncBatchPushBillF.getXZBM());
        dxjsData.setXMID(projectId);
        dxjsData.setZDR(empCode);
        dxjsData.setDJRQ(voucherBill.getGmtCreate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dxjsData.setLYXT("CCCG-DMC");
        VoucherPushBillDetailDxZJ detail0 = details.get(0);
        Long billId = detail0.getBillId();
        dxjsData.setYWLX(voucherBill.getBusinessType());

        String contractId = detail0.getContractId();
        List<ContractPayPlanInnerInfoV> contractInnerInfos =
                contractClient.getInnerInfoByContractIdOnPay(Lists.newArrayList(contractId));
        if (CollectionUtils.isEmpty(contractInnerInfos) || contractInnerInfos.size() > 1){
            throw new OwlBizException("合同不存在或数据异常");
        }
        ContractPayPlanInnerInfoV contractPayPlanInnerInfoV = contractInnerInfos.get(0);

        dxjsData.setHTBH(contractPayPlanInnerInfoV.getConMainCode());
        if (StringUtils.isBlank(contractPayPlanInnerInfoV.getPayeeId())){
            throw new OwlBizException("合同往来单位信息异常");
        }

        //核算组织名称
        String organizationName;
        List<CfgExternalDataV> community = configClient.getExternalMapByCode("community", contractPayPlanInnerInfoV.getCommunityId());
        // 调用根据行政组织获取核算组织接口
        String dataCode = null;
        for (CfgExternalDataV cfgExternalDataV : community) {
            if ("org".equals(cfgExternalDataV.getExternalDataType())){
                dataCode = cfgExternalDataV.getDataCode();
            }
        }
        ExternalMaindataCalmappingListF externalMaindataCalmappingListF = new ExternalMaindataCalmappingListF();
        externalMaindataCalmappingListF.setZorgid(dataCode);
        ExternalMaindataCalmappingListV list = externalClient.list(externalMaindataCalmappingListF);
        if (null != list && list.getInfoList().size() > 0) {
            organizationName = list.getInfoList().get(0).getZaorgno();
        } else {
            organizationName = null;
        }
        //核算组织编号
        dxjsData.setHSDW(organizationName);

        List<CustomerSimpleV> vList = orgClient.simpleList(Lists.newArrayList(contractPayPlanInnerInfoV.getPayeeId()));
        if (CollectionUtils.isEmpty(vList)){
            throw new OwlBizException("合同往来单位信息异常");
        }
        dxjsData.setZQR(vList.get(0).getMainDataCode());
        if(voucherBill.getBillEventType() == 3){
            dxjsData.setJSDH(null);
        } else {
            ContractPaySettlementDetailsV settlementDetailsV = contractClient.getPayDetailsById(detail0.getSettlementId());
            dxjsData.setJSDH(settlementDetailsV.getPayFundNumber());
        }
        if(voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.对下结算计提.getCode() ||
            voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode()){
            dxjsData.setSFQR("0");
        } else {
            dxjsData.setSFQR("1");
        }

        if(Objects.nonNull(voucherBill.getCalculationMethod())){
            dxjsData.setJSFS(voucherBill.getCalculationMethod().toString());
        }else{
            //查合同信息
            ContractPayConcludeF contractPayConcludeF = new ContractPayConcludeF();
            contractPayConcludeF.setId(contractId);
            ContractPayConcludeV contractPayConcludeV = contractClient.get(contractPayConcludeF);
            log.info("根据合同id：{}，查询对应合同信息：{}", contractId, contractPayConcludeV);
            if(Objects.nonNull(contractPayConcludeV) && StringUtils.isNotBlank(contractPayConcludeV.getOurPartyId())){
                //根据我方单位ID查询对应法定单位信息
                OrgFinanceRv orgFinanceById = orgClient.getOrgFinanceById(Long.valueOf(contractPayConcludeV.getOurPartyId()));
                log.info("根据法定单位id：{}，查询对应法定单位信息：{}", contractPayConcludeV.getOurPartyId(), orgFinanceById);
                if(Objects.nonNull(orgFinanceById)){
                    dxjsData.setJSFS(FinancialTaxTypeEnum.SIMPLE.getCode().toString());
                    if(Objects.isNull(orgFinanceById) || Objects.isNull(orgFinanceById.getTaxpayerType()) || TaxpayerTypeEnum.一般纳税人.getCode().equals(orgFinanceById.getTaxpayerType())){
                        dxjsData.setJSFS(FinancialTaxTypeEnum.GENERAL.getCode().toString());
                    }
                }
            }
        }
        // 求和
        BigDecimal totalTaxIncludeAmount = details.stream()
                .map(VoucherPushBillDetailDxZJ::getTaxIncludAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        dxjsData.setHSJEBB(totalTaxIncludeAmount.setScale(2, RoundingMode.HALF_UP).doubleValue());
        dxjsData.setLJHSJEBB(totalTaxIncludeAmount.setScale(2, RoundingMode.HALF_UP).doubleValue());
        BigDecimal totalTaxExcludeAmount = details.stream()
                .map(VoucherPushBillDetailDxZJ::getTaxExcludAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        dxjsData.setBHSJEBB(totalTaxExcludeAmount.setScale(2, RoundingMode.HALF_UP).doubleValue());
        if (voucherBill.getBillEventType() == 3){
            dxjsData.setHSJEBB(totalTaxExcludeAmount.setScale(2, RoundingMode.HALF_UP).doubleValue());
            dxjsData.setLJHSJEBB(totalTaxExcludeAmount.setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
        BigDecimal totalTaxAmount = totalTaxIncludeAmount.subtract(totalTaxExcludeAmount);
        dxjsData.setSEBB(totalTaxAmount.setScale(2, RoundingMode.HALF_UP).doubleValue());

        dxjsData.setDJFJZS(voucherBill.getUploadNum());
        dxjsData.setBZID("156");
        dxjsData.setBWBBH("156");
        // todo 这是啥 文档没有
        dxjsData.setDJZT("01");
        //业务事由
        dxjsData.setBZSY(StringUtils.isNotBlank(voucherBill.getReceiptRemark()) ? voucherBill.getReceiptRemark() : pushBillZJDomainService.getReceiptRemark(Arrays.asList(voucherBill.getVoucherBillNo())).get(voucherBill.getVoucherBillNo()));
        dxjsData.setPZRQ(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return Lists.newArrayList(dxjsData);
    }

    public String getZFSQRequestBody(VoucherBillDxZJ voucherBill, SyncBatchPushZJBillF syncBatchPushBillF,
                                     String projectId, String empCode, List<SPRZData> sprzDataList,
                                     List<String> ftIdList) throws JsonProcessingException {
        ZJRequestBody zjRequestBody = new ZJRequestBody();
        ZJParameter zjParameter = new ZJParameter();
        zjParameter.setBusinessCode(DocumentTypeEnum.valueOfByCode(voucherBill.getBillEventType()).getValue());
        zjParameter.setUnitCode("MDM");
        zjParameter.setSourceSystem("CCCG-DMC");
        zjParameter.setAppInstanceCode("10000");

        List<VoucherPushBillDetailDxZJ> details = detailDxZJMapper.selectList(new LambdaQueryWrapper<VoucherPushBillDetailDxZJ>()
                .eq(VoucherPushBillDetailDxZJ::getDeleted,0)
                .in(VoucherPushBillDetailDxZJ::getVoucherBillNo,voucherBill.getVoucherBillNo()));

        PaymentApplicationFormZJ paymentApplicationFormZJ = paymentApplicationFormRepository.getById(details.get(0).getPayAppId());

        List<BILLDATAS> billDataList = Lists.newArrayList();
        BILLDATAS billDatas = new BILLDATAS();

        //YWZFSQ（业务支付申请）
        BILLDATA YWZFSQ = new BILLDATA();
        YWZFSQ.setCode("YWZFSQ");
        YWZFSQ.setIsMain("true");
        List<ZFSQData> DATA_DXJS =  buildYWZFSQ(voucherBill, syncBatchPushBillF, projectId, empCode, details,paymentApplicationFormZJ);
        YWZFSQ.setData(DATA_DXJS);

        // KXMX(款项明细)
        BILLDATA ZFSQKJMX_UNIT = new BILLDATA();
        ZFSQKJMX_UNIT.setCode("KXMX");
        ZFSQKJMX_UNIT.setIsMain("false");
        List<ZFSQKJMXData> DATA_ZFSQKJMX =  buildZFSQKJMX(voucherBill, syncBatchPushBillF, projectId, empCode, details, ftIdList,paymentApplicationFormZJ);
        ZFSQKJMX_UNIT.setData(DATA_ZFSQKJMX);

        // 支付明细
        BILLDATA ZFMX_UNIT = new BILLDATA();
        ZFMX_UNIT.setCode("ZFMX");
        ZFMX_UNIT.setIsMain("false");
        List<PayZFMXData> DATA_DXJJCBMX =  buildZFSQZFMX(voucherBill, syncBatchPushBillF, projectId, empCode, details,paymentApplicationFormZJ);
        ZFMX_UNIT.setData(DATA_DXJJCBMX);

        //  影像信息
        BILLDATA YXXXBillData = new BILLDATA();
        List<YXXXData> YXXXDataList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(voucherBill.getUploadLink())) {
            for (UploadLinkZJ uploadLinkZJ : voucherBill.getUploadLink()) {
                //影像信息
                YXXXData yxxxData = new YXXXData();
                yxxxData.setYXBM(uploadLinkZJ.getImageIdZJ());
                YXXXDataList.add(yxxxData);
            }
        }
        YXXXBillData.setCode("YXXX");
        YXXXBillData.setIsMain("false");
        YXXXBillData.setData(YXXXDataList);

        //  审批日志
        BILLDATA SPRZDataBillData = new BILLDATA();
        SPRZDataBillData.setCode("SPRZ");
        SPRZDataBillData.setIsMain("false");
        SPRZDataBillData.setData(sprzDataList);

        ArrayList<BILLDATA> billdatas = Lists.newArrayList();
        billdatas.add(YWZFSQ);
        billdatas.add(ZFSQKJMX_UNIT);
        billdatas.add(ZFMX_UNIT);
        billdatas.add(YXXXBillData);
        billdatas.add(SPRZDataBillData);
        billDatas.setBILLDATA(billdatas);
        billDataList.add(billDatas);

        zjParameter.setPsData(billDataList);
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("中交推单原始参数 {}", JSON.toJSONString(zjParameter));
        String billdataString = objectMapper.writeValueAsString(zjParameter).replace("\"", "'");
        String replace = "---";
        zjRequestBody.setContext("---");
        String zjRequestBodyString = JSON.toJSONString(zjRequestBody).replace(replace, billdataString);
        log.info("中交推单参数 {}", zjRequestBodyString);
        return zjRequestBodyString;
    }


    private List<ZFSQData> buildYWZFSQ(VoucherBillDxZJ voucherBill,
                                       SyncBatchPushZJBillF syncBatchPushBillF,
                                       String projectId,
                                       String empCode,
                                       List<VoucherPushBillDetailDxZJ> details,
                                       PaymentApplicationFormZJ paymentApplicationFormZJ) {
        // YWZFSQ
        ZFSQData dxjsData = new ZFSQData();
        dxjsData.setDJNM(voucherBill.getVoucherBillNo());
        dxjsData.setDJBH(voucherBill.getVoucherBillNo());
        dxjsData.setXZZZ(syncBatchPushBillF.getXZZZ());

        dxjsData.setXZBM(StringUtils.isNotEmpty(voucherBill.getExternalDepartmentCode()) ? voucherBill.getExternalDepartmentCode() : syncBatchPushBillF.getXZBM());
        dxjsData.setXMID(projectId);
        dxjsData.setZDR(empCode);
        dxjsData.setDJRQ(voucherBill.getGmtCreate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dxjsData.setLYXT("CCCG-DMC");
        VoucherPushBillDetailDxZJ detail0 = details.get(0);
        Long billId = detail0.getBillId();
        //dxjsData.setYWLX(voucherBill.getBusinessType());
        //其他业务
        //dxjjkjmxData.setYWLX("002099");
        dxjsData.setYWLX("E12C32FDD50B44BF96C4FC726E87FDFE");

        String contractId = detail0.getContractId();
        List<ContractPayPlanInnerInfoV> contractInnerInfos =
                contractClient.getInnerInfoByContractIdOnPay(Lists.newArrayList(contractId));
        if ((CollectionUtils.isEmpty(contractInnerInfos) || contractInnerInfos.size() == 0 ) && Objects.isNull(paymentApplicationFormZJ)){
            throw new OwlBizException("合同不存在或数据异常");
        }
        ContractPayPlanInnerInfoV contractPayPlanInnerInfoV = new ContractPayPlanInnerInfoV();
        if(CollectionUtils.isNotEmpty(contractInnerInfos)){
            contractPayPlanInnerInfoV = contractInnerInfos.get(0);
        }
        //核算组织名称
        String organizationName;
        List<CfgExternalDataV> community = configClient.getExternalMapByCode("community", Objects.isNull(paymentApplicationFormZJ) ? contractPayPlanInnerInfoV.getCommunityId() : paymentApplicationFormZJ.getCommunityId());
        // 调用根据行政组织获取核算组织接口
        String dataCode = null;
        for (CfgExternalDataV cfgExternalDataV : community) {
            if ("org".equals(cfgExternalDataV.getExternalDataType())){
                dataCode = cfgExternalDataV.getDataCode();
            }
        }
        ExternalMaindataCalmappingListF externalMaindataCalmappingListF = new ExternalMaindataCalmappingListF();
        externalMaindataCalmappingListF.setZorgid(dataCode);
        ExternalMaindataCalmappingListV list = externalClient.list(externalMaindataCalmappingListF);
        if (null != list && list.getInfoList().size() > 0) {
            organizationName = list.getInfoList().get(0).getZaorgno();
        } else {
            organizationName = null;
        }
        //核算组织编号
        dxjsData.setHSDW(organizationName);

        dxjsData.setHTBH(Objects.isNull(paymentApplicationFormZJ) ? contractPayPlanInnerInfoV.getConMainCode() : paymentApplicationFormZJ.getConmaincode());
        if (Objects.isNull(paymentApplicationFormZJ) && StringUtils.isBlank(contractPayPlanInnerInfoV.getPayeeId())){
            throw new OwlBizException("合同往来单位信息异常");
        }
        //收款账户ID
        dxjsData.setSKZH(paymentApplicationFormZJ.getBankIdAccount());
        //实际收款人
        dxjsData.setSJSKR(paymentApplicationFormZJ.getAccountCode());
        //业务事由
        dxjsData.setBZSY(StringUtils.isNotBlank(paymentApplicationFormZJ.getBusinessReasons())? paymentApplicationFormZJ.getBusinessReasons() : pushBillZJDomainService.getReceiptRemark(Arrays.asList(voucherBill.getVoucherBillNo())).get(voucherBill.getVoucherBillNo()));
        dxjsData.setQWFKRQ(paymentApplicationFormZJ.getExpectPayDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dxjsData.setJJBZ(false);
        dxjsData.setYFJEBB(paymentApplicationFormZJ.getTotalPaymentAmount().setScale(2, RoundingMode.HALF_UP).doubleValue());
        dxjsData.setFJZS(null == paymentApplicationFormZJ.getAttachmentNum() ? 0 : paymentApplicationFormZJ.getAttachmentNum());
        String htskrId = null;
        if(Objects.isNull(paymentApplicationFormZJ)){
            List<CustomerSimpleV> vList = orgClient.simpleList(Lists.newArrayList(contractPayPlanInnerInfoV.getPayeeId()));
            htskrId = vList.get(0).getMainDataCode();
        }else{
            htskrId = paymentApplicationFormZJ.getRecipientCode();
        }
        dxjsData.setHTSKRID(htskrId);
        dxjsData.setPZRQ(paymentApplicationFormZJ.getApprovalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dxjsData.setBWBID("156");
        dxjsData.setDJZT("1");
        return Lists.newArrayList(dxjsData);
    }

    private List<ZFSQKJMXData> buildZFSQKJMX(VoucherBillDxZJ voucherBill,
                                             SyncBatchPushZJBillF syncBatchPushBillF,
                                             String projectId,
                                             String empCode,
                                             List<VoucherPushBillDetailDxZJ> details,
                                             List<String> ftIdList,
                                             PaymentApplicationFormZJ paymentApplicationFormZJ) {
        List<PaymentApplicationFormKxmx> zfsqDetails = paymentApplicationFormKXMXRepository.lambdaQuery().eq(PaymentApplicationFormKxmx::getDeleted,0).eq(PaymentApplicationFormKxmx::getPayAppId,voucherBill.getPayAppId()).list();
        if (CollectionUtils.isEmpty(zfsqDetails)){
            return null;
        }
        List<ZFSQKJMXData> kjmxDataList = Lists.newArrayList();
        for (PaymentApplicationFormKxmx paymentDetail : zfsqDetails) {
            ZFSQKJMXData dxjjkjmxData = new ZFSQKJMXData();
            String KJMXNMNumber = IdentifierFactory.getInstance().serialNumber("pushbillZJ", "KXMXNM", 24);
            dxjjkjmxData.setKXMXNM(KJMXNMNumber);
            dxjjkjmxData.setZBNM(voucherBill.getVoucherBillNo());
            dxjjkjmxData.setYWLX(paymentDetail.getSubjectCode());
            if(StringUtils.isEmpty(paymentDetail.getSubjectCode()) && "9999999999".equals(paymentApplicationFormZJ.getContractId())){
                Mdm63E mdm63 = mdm63Mapper.getMdm63ByFtId(paymentDetail.getFtId());
                if(Objects.nonNull(mdm63)){
                    dxjjkjmxData.setYWLX(mdm63.getFundsPropId());
                }
            }
            //dxjjkjmxData.setYWLX("E12C32FDD50B44BF96C4FC726E87FDFE");
            //dxjjkjmxData.setYWLX("CB4C2D77ADE7C867E0530B80C90A8E1F");
            dxjjkjmxData.setZJJHBH(paymentDetail.getFundingPlanNumber());
            //应收应付id
            dxjjkjmxData.setHEYFBH(paymentDetail.getFtId());
            if (StringUtils.isNotBlank(paymentDetail.getFtId())){
                ftIdList.add(paymentDetail.getFtId());
            }
            dxjjkjmxData.setBZID("156");
            dxjjkjmxData.setHL(1.000000);
            dxjjkjmxData.setJEYB(paymentDetail.getAmount().setScale(2, RoundingMode.HALF_UP).doubleValue());
            dxjjkjmxData.setJEBB(paymentDetail.getAmount().setScale(2, RoundingMode.HALF_UP).doubleValue());
            //dxjjkjmxData.setDQRQ(paymentDetail.getDueDate().format(formatter));
            dxjjkjmxData.setDQRQ(DateTimeUtil.getYearMonthDay(voucherBill.getGmtCreate().plusYears(1).minusDays(1)));
            dxjjkjmxData.setQWJSFS(paymentDetail.getPayWay());
            kjmxDataList.add(dxjjkjmxData);
        }
        return kjmxDataList;
    }

    private List<PayZFMXData> buildZFSQZFMX(VoucherBillDxZJ voucherBill,
                                            SyncBatchPushZJBillF syncBatchPushBillF,
                                            String projectId,
                                            String empCode,
                                            List<VoucherPushBillDetailDxZJ> details,
                                            PaymentApplicationFormZJ paymentApplicationFormZJ) {

        List<PaymentApplicationFormPayMx> payDetails = paymentApplicationPayMxRepository.lambdaQuery()
                .eq(PaymentApplicationFormPayMx::getDeleted,0)
                .eq(PaymentApplicationFormPayMx::getPayAppId,voucherBill.getPayAppId())
                .list();
        if (CollectionUtils.isEmpty(payDetails)){
            return null;
        }
        // 对transactionAmount求和
        BigDecimal transactionAmountSum = payDetails.stream()
                .map(payDetail -> payDetail.getTransactionAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        PayZFMXData zfmxData = new PayZFMXData();
        String ZFMXNumber = IdentifierFactory.getInstance().serialNumber("pushbillZJ", "ZFMXNM", 20);
        zfmxData.setZFMXNM(ZFMXNumber);
        zfmxData.setZBNM(voucherBill.getVoucherBillNo());
        zfmxData.setSKDW(paymentApplicationFormZJ.getRecipientCode());
        zfmxData.setSKZH(paymentApplicationFormZJ.getBankIdAccount());
        // todo mdm11 or mdm55?
        zfmxData.setXJLL(paymentApplicationFormZJ.getCashFlow().toString());
        zfmxData.setZFFS(paymentApplicationFormZJ.getPaymentMethod());
        zfmxData.setFKZH(paymentApplicationFormZJ.getPayAccountId());
        zfmxData.setZFBZ("156");
        zfmxData.setHL(1.000000d);

        zfmxData.setZFBB(transactionAmountSum.doubleValue());
        zfmxData.setPZJE(transactionAmountSum.doubleValue());
        zfmxData.setPJSL(StringUtils.isNotEmpty(paymentApplicationFormZJ.getBillsNumbers()) ? Integer.valueOf(paymentApplicationFormZJ.getBillsNumbers()) :0);
        zfmxData.setZZFY(paymentApplicationFormZJ.getTransferRemarks());
        zfmxData.setPJZFFS(0);
        return Lists.newArrayList(zfmxData);
    }
}
