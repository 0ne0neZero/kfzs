package com.wishare.finance.apps.service.pushbill;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.pushbill.fo.VoucherBillRecMdm63F;
import com.wishare.finance.apps.pushbill.fo.VoucherBillZJRecDetailF;
import com.wishare.finance.apps.pushbill.vo.*;
import com.wishare.finance.apps.scheduler.mdm.Mdm11Handler;
import com.wishare.finance.apps.scheduler.mdm.vo.DictionaryF;
import com.wishare.finance.apps.scheduler.mdm.vo.MDM63F2;
import com.wishare.finance.apps.scheduler.mdm.vo.Mdm63Response;
import com.wishare.finance.apps.scheduler.mdm.vo.ZjDictionaryResponse;
import com.wishare.finance.apps.service.pushbill.mdm63.Mdm63Service;
import com.wishare.finance.domains.configure.chargeitem.entity.FinancialTaxInfo;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxRateE;
import com.wishare.finance.domains.configure.chargeitem.repository.mapper.FinancialTaxInfoMapper;
import com.wishare.finance.domains.configure.chargeitem.repository.mapper.TaxRateMapper;
import com.wishare.finance.domains.configure.subject.service.SubjectMapRulesDomainService;
import com.wishare.finance.domains.mdm.entity.Mdm63E;
import com.wishare.finance.domains.mdm.entity.Mdm63LockE;
import com.wishare.finance.domains.mdm.repository.mapper.Mdm63Mapper;
import com.wishare.finance.domains.pushbill.enums.VoucherChargeTypeEnums;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.PushBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.ZJBillProperties;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.*;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.*;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.Mdm63LockMapper;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.clients.base.ConfigClient;
import com.wishare.finance.infrastructure.remote.clients.base.ContractClient;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.enums.financialcloud.FinancialTaxTypeEnum;
import com.wishare.finance.infrastructure.remote.vo.config.CfgExternalDataV;
import com.wishare.finance.infrastructure.remote.vo.contract.AttachmentE;
import com.wishare.finance.infrastructure.remote.vo.contract.AttachmentV;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractPayPlanInnerInfoV;
import com.wishare.finance.infrastructure.remote.vo.org.SupplierSimpleV;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceCommunityShortV;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherBillDetailAppService implements ApiBase {

    private final VoucherBillDetailZJRepository voucherBillDetailRepository;

    private final VoucherContractInvoiceZJRepository voucherContractInvoiceZJRepository;
    private final VoucherInvoiceZJRepository voucherInvoiceZJRepository;

    private final VoucherContractMeasurementDetailZJRepository voucherContractMeasurementDetailZJRepository;
    private final VoucherBillFileZJRepository voucherBillFileZJRepository;
    private final VoucherPushBillZJRepository voucherPushBillZJRepository;
    private final VoucherBillZJRecDetailRepository recDetailRepository;
    private final VoucherPushBillDxZJRepository voucherPushBillDxZJRepository;
    private final VoucherBillDetailDxZJRepository voucherBillDetailDxZJRepository;

    private final TaxRateMapper taxRateMapper;
    private final ContractClient contractClient;
    private final OrgClient orgClient;
    private final SpaceClient spaceClient;
    private final ConfigClient configClient;

    private final FinancialTaxInfoMapper financialTaxInfoMapper;
    private final Mdm63Mapper mdm63Mapper;

    private final Mdm63Service mdm63Service;
    private final Mdm63LockMapper mdm63LockMapper;
    private final SubjectMapRulesDomainService subjectMapRulesDomainService;

    ObjectMapper objectMapper = new ObjectMapper();
    private static final String CONTENTTYPE = "Content-Type";
    private final ZJBillProperties zjBillProperties;
    static final String DIC_URL = "/ESB/API/ChannelZJFW/YXDMC/QueryFinanceDictData";

    /**
     * 认领明细
     *
     * @param form
     * @return
     */
    public Page<VoucherBillZJFlowDetailV> queryFlowDetailPage(PageF<SearchF<?>> form) {

        Page<VoucherBillZJ> voucherBillZJPage = voucherPushBillZJRepository.pageBySearch(form);
        SearchF<?> searchF = new SearchF<>();
        List<Field> fields = new ArrayList<>();
        List<Long> recordIds = JSON.parseArray(voucherBillZJPage.getRecords().get(0).getRecordIdList(), Long.class);
        fields.add(new Field("fcr.id",recordIds,15));
        searchF.setFields(fields);
        PageF<SearchF<?>> page = new PageF<>();
        page.setConditions(searchF);
        page.setPageNum(form.getPageNum());
        page.setPageSize(form.getPageSize());

        Page<VoucherBillZJFlowDetailV> voucherBillZJFlowDetailVPage = voucherBillDetailRepository.queryFlowDetailPageNew(page);

        return voucherBillZJFlowDetailVPage;
    }

    public List<VoucherBillZJFileSV> queryFileSPage(PageF<SearchF<?>> form) {

        Page<VoucherBillZJ> voucherBillZJPage = voucherPushBillZJRepository.pageBySearch(form);

        List<Long> recordIds = JSON.parseArray(voucherBillZJPage.getRecords().get(0).getRecordIdList(), Long.class);

        List<FlowClaimFilesV> flowClaimFilesVS = voucherBillDetailRepository.queryFlowClaimFilesVNew(recordIds);


        List<VoucherBillZJFileSV> list = new ArrayList<>();
        for (FlowClaimFilesV flowClaimFilesV : flowClaimFilesVS) {
            String flowFiles = flowClaimFilesV.getFlowFiles();
            JSONArray flowFilesObj = JSONObject.parseArray(flowFiles);
            for (int i = 0; i < flowFilesObj.size(); i++) {
                VoucherBillZJFileSV voucherBillZJFileSV = new VoucherBillZJFileSV();
                JSONObject jsonObject = flowFilesObj.getJSONObject(i);
                voucherBillZJFileSV.setFileKey(jsonObject.getString("fileKey"));
                voucherBillZJFileSV.setFileSize(jsonObject.getString("size"));
                voucherBillZJFileSV.setFileName(jsonObject.getString("name"));
                voucherBillZJFileSV.setFileType("银行文件");
                voucherBillZJFileSV.setFileFormat(jsonObject.getString("suffix"));
                voucherBillZJFileSV.setCreatName(flowClaimFilesV.getClaimName());
                voucherBillZJFileSV.setCreatTime(flowClaimFilesV.getGmtCreate());
                list.add(voucherBillZJFileSV);
            }
            String reportFiles = flowClaimFilesV.getReportFiles();
            JSONArray reportFilesObj = JSONObject.parseArray(reportFiles);
            if (null != reportFilesObj) {
                for (int i = 0; i < reportFilesObj.size(); i++) {
                    VoucherBillZJFileSV voucherBillZJFileSV = new VoucherBillZJFileSV();
                    JSONObject jsonObject = reportFilesObj.getJSONObject(i);
                    voucherBillZJFileSV.setFileKey(jsonObject.getString("fileKey"));
                    voucherBillZJFileSV.setFileSize(jsonObject.getString("size"));
                    voucherBillZJFileSV.setFileName(jsonObject.getString("name"));
                    voucherBillZJFileSV.setFileType("收款日报");
                    voucherBillZJFileSV.setFileFormat(jsonObject.getString("suffix"));
                    voucherBillZJFileSV.setCreatName(flowClaimFilesV.getClaimName());
                    voucherBillZJFileSV.setCreatTime(flowClaimFilesV.getGmtCreate());
                    list.add(voucherBillZJFileSV);
                }
            }
        }
        return list;
    }


    public Page<VoucherBillZJConvertDetailV> queryConvertDetailPage(PageF<SearchF<?>> form) {
        return voucherBillDetailRepository.queryConvertDetailPage(form);
    }

    public PageV<VoucherBillZJRecDetailV> queryRecDetailPage(PageF<SearchF<?>> form) {
        PageV<VoucherBillZJRecDetailV> page = voucherBillDetailRepository.queryRecDetailPage(form);
        if (CollectionUtils.isEmpty(page.getRecords())){
            return page;
        }
        List<Long> taxRateIds = page.getRecords().stream()
                .map(VoucherBillZJRecDetailV::getTaxRateId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<TaxRateE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TaxRateE::getId,taxRateIds);
        List<TaxRateE> taxRateEList = taxRateMapper.selectList(queryWrapper);
        Map<Long, TaxRateE> taxRateMap = taxRateEList.stream().collect(Collectors.toMap(TaxRateE::getId, e -> e));
        for (VoucherBillZJRecDetailV record : page.getRecords()) {
            if (StringUtils.isBlank(record.getContractId())){
                if (Objects.isNull(record.getTaxRateId())){
                    continue;
                }
                TaxRateE rateE = taxRateMap.get(record.getTaxRateId());
                if (Objects.nonNull(rateE) && "2".equals(rateE.getTaxType())) {
                    record.setTaxType(FinancialTaxTypeEnum.SIMPLE.getValue());
                }
            } else {
                List<ContractPayPlanInnerInfoV> innerInfoList =
                        contractClient.getInnerInfoByContractIdOnIncome(Arrays.asList(record.getContractId()));
                if (CollectionUtils.isEmpty(innerInfoList)){
                    continue;
                }
                ContractPayPlanInnerInfoV innerInfo = innerInfoList.get(0);
                record.setConMainCode(innerInfo.getConMainCode());
                record.setContractNo(innerInfo.getConMainCode());
                record.setContractName(innerInfo.getContractName());
                record.setContractPay(innerInfo.getOppositeOne());

                if (new BigDecimal("0.05").compareTo(record.getTaxRate()) < 0) {
                    record.setTaxType(FinancialTaxTypeEnum.GENERAL.getValue());
                } else {
                    record.setTaxType(FinancialTaxTypeEnum.SIMPLE.getValue());
                }
            }
        }
        return page;
    }

    public PageV<VoucherBillZJRecCWYDetailV>  queryRecCWYDetailPage(PageF<SearchF<?>> form){
        return  voucherBillDetailRepository.queryRecCWYDetailPage(form);
    }
    public List<VoucherBillZJRecCWYDetailV>  queryRecCWYDetail(PageF<SearchF<?>> form){
        return  voucherBillDetailRepository.queryRecCWYDetail(form);
    }
    public PageV<VoucherBillZJCashFlowV> queryCashFlowPage(PageF<SearchF<?>> form){
        return voucherBillDetailRepository.queryCashFlowPage(form);
    }

    public PageV<VoucherBillCostInfoV> queryCostInfoV(PageF<SearchF<?>> form) {
        return voucherBillDetailRepository.queryVoucherBillCostInfo(form);
    }

    public PageV<VoucherBillPaymentDetailsV> queryPaymentDetails(PageF<SearchF<?>> form) {
        ArrayList<VoucherBillPaymentDetailsV> voucherBillInvoiceVS = new ArrayList<>();
        Page<VoucherContractInvoiceZJ> voucherContractInvoiceZJPage = voucherContractInvoiceZJRepository.pageBySearch(form);
        for (VoucherContractInvoiceZJ record : voucherContractInvoiceZJPage.getRecords()) {
            VoucherBillPaymentDetailsV voucherBillPaymentDetailsV = new VoucherBillPaymentDetailsV();
            JSONArray objects = JSONArray.parseArray(record.getWriteOffInfo());
            voucherBillPaymentDetailsV.setChangeType(VoucherChargeTypeEnums.valueOfByCode(record.getChangeType()).getValue());
            voucherBillPaymentDetailsV.setPaymentName(record.getPaymentName());
            if (null != objects && objects.size() > 1){
                for (int i = 0; i < objects.size(); i++) {
                    voucherBillPaymentDetailsV.setBillNo(objects.getJSONObject(i).getString("billNo"));
                    voucherBillPaymentDetailsV.setAmount(Long.valueOf(objects.getJSONObject(i).getString("amount")));
                    voucherBillPaymentDetailsV.setWriteOffInfoAmount(Long.valueOf(objects.getJSONObject(i).getString("writeOffInfoAmount")));
                }
            }
            voucherBillInvoiceVS.add(voucherBillPaymentDetailsV);
        }
        return PageV.of(voucherContractInvoiceZJPage.getCurrent(), voucherContractInvoiceZJPage.getSize(), voucherContractInvoiceZJPage.getTotal(), voucherBillInvoiceVS);
    }

    public PageV<VoucherBillMeasurementDetailV> queryMeasurementDetail(PageF<SearchF<?>> form) {
        ArrayList<VoucherBillMeasurementDetailV> voucherBillInvoiceVS = new ArrayList<>();
        Page<VoucherContractMeasurementDetailZJ> voucherContractMeasurementDetailZJPage = voucherContractMeasurementDetailZJRepository.pageBySearch(form);
        for (VoucherContractMeasurementDetailZJ record : voucherContractMeasurementDetailZJPage.getRecords()) {
            VoucherBillMeasurementDetailV voucherBillMeasurementDetailV = new VoucherBillMeasurementDetailV();
            voucherBillMeasurementDetailV.setContractItem(record.getContractItem());
            voucherBillMeasurementDetailV.setTaxIncludAmount(new BigDecimal(record.getTaxIncludedAmount().toString()).divide(new BigDecimal("100")).doubleValue());
            voucherBillMeasurementDetailV.setTaxExcludedAmount(new BigDecimal(record.getTaxExcludedAmount().toString()).divide(new BigDecimal("100")).doubleValue());
            voucherBillInvoiceVS.add(voucherBillMeasurementDetailV);
        }

        return PageV.of(voucherContractMeasurementDetailZJPage.getCurrent(), voucherContractMeasurementDetailZJPage.getSize(), voucherContractMeasurementDetailZJPage.getTotal(), voucherBillInvoiceVS);
    }

    public PageV<VoucherBillInvoiceV> queryInvoiceInfo(PageF<SearchF<?>> form) {
        ArrayList<VoucherBillInvoiceV> voucherBillInvoiceVS = new ArrayList<>();
        Page<VoucherInvoiceZJ> voucherInvoiceZJPage = voucherInvoiceZJRepository.pageBySearch(form);
        for (VoucherInvoiceZJ record : voucherInvoiceZJPage.getRecords()) {
            VoucherBillInvoiceV voucherBillInvoiceV = new VoucherBillInvoiceV();
            voucherBillInvoiceV.setInvoiceNo(record.getInvoiceNo());
            voucherBillInvoiceV.setInvoiceCode(record.getInvoiceCode());
            voucherBillInvoiceV.setInvoiceType(record.getInvoiceType());
            voucherBillInvoiceV.setInvoiceDate(record.getInvoiceDate());
            voucherBillInvoiceV.setTaxAmount(new BigDecimal(record.getTaxAmount().toString()).divide(new BigDecimal("100")).doubleValue());
            voucherBillInvoiceV.setPriceTaxAmount(new BigDecimal(record.getPayAmount().toString()).divide(new BigDecimal("100")).doubleValue());
            voucherBillInvoiceVS.add(voucherBillInvoiceV);
        }
        return PageV.of(voucherInvoiceZJPage.getCurrent(), voucherInvoiceZJPage.getSize(), voucherInvoiceZJPage.getTotal(), voucherBillInvoiceVS);
    }

    public List<VoucherBillZJFileSV> queryFileForSettle(PageF<SearchF<?>> form) {
        Page<VoucherContractInvoiceZJ> voucherContractInvoiceZJPage = voucherContractInvoiceZJRepository.pageBySearch(form);
        ArrayList<VoucherBillZJFileSV> voucherBillZJFileSVS = new ArrayList<>();
        for (VoucherContractInvoiceZJ record : voucherContractInvoiceZJPage.getRecords()) {
            JSONArray objects = JSONArray.parseArray(record.getFiles());
            if (null != objects && objects.size() > 0) {
                for (int i = 0; i < objects.size(); i++) {
                    VoucherBillZJFileSV voucherBillZJFileSV = new VoucherBillZJFileSV();
                    voucherBillZJFileSV.setFileKey(objects.getJSONObject(i).getString("fileKey"));
                    voucherBillZJFileSV.setFileName(objects.getJSONObject(i).getString("name"));
                    voucherBillZJFileSV.setFileFormat(objects.getJSONObject(i).getString("suffix"));
                    voucherBillZJFileSV.setCreatTime(record.getGmtCreate());
                    voucherBillZJFileSV.setCreatName(record.getCreatorName());
                    voucherBillZJFileSVS.add(voucherBillZJFileSV);
                }
            }

        }
        return voucherBillZJFileSVS;
    }

    private String getSearchData(PageF<SearchF<?>> form, String name){
        return form.getConditions().getFields().stream()
                .filter(field -> name.equals(field.getName()))
                .findFirst()
                .map(field -> field.getValue() != null ? field.getValue().toString() : null)
                .orElse(null);
    }

    public List<VoucherBillZJFileSV> queryFileForRec(PageF<SearchF<?>> form) {
        //报账单ID
        String voucherBillId = this.getSearchData(form,"voucherBillId");
        //报账单编号
        String voucherBillNo = this.getSearchData(form,"voucherBillNo");
        //报账单编号
        String voucherBillNoZJ = this.getSearchData(form,"voucher_bill_no");
        //合同Id
        String contractId = this.getSearchData(form,"contractId");
        //结算审批单、确收审批单Id
        String settleId = this.getSearchData(form,"settleId");

        List<VoucherBillZJFileSV> voucherBillZJFileSVS = new ArrayList<>();

        //根据报账单编号查询报账单数据
        VoucherBillDxZJ voucherBill = null;
        VoucherBillZJ voucherBillZJ = null;
        if(StringUtils.isNotBlank(voucherBillNo)){
            voucherBill = voucherPushBillDxZJRepository.getVoucherBillDxZJByQuery(voucherBillNo);
        }else{
            voucherBillZJ = voucherPushBillZJRepository.queryByVoucherBillNo(voucherBillNoZJ);
        }

        if (Objects.isNull(voucherBill) && Objects.isNull(voucherBillZJ)) {
            throw new OwlBizException("报账单不存在,请检查");
        }

        Integer billEventType = Objects.nonNull(voucherBill) ? voucherBill.getBillEventType() : voucherBillZJ.getBillEventType();
        //根据报账单ID/编码 获取 影像附件列表
        List<VoucherBillFileZJ> voucherBillFileZJS = new ArrayList<>();
        if(ZJTriggerEventBillTypeEnum.支付申请.getCode() == billEventType){
            voucherBillFileZJS =  voucherBillFileZJRepository.selectByVoucherBillId(Long.valueOf(voucherBillId));
        }else {
            voucherBillFileZJS =  voucherBillFileZJRepository.selectByVoucherBillNo(StringUtils.isNotBlank(voucherBillNo) ? voucherBillNo : voucherBillNoZJ);
        }

        for (VoucherBillFileZJ voucherBillFileZJ : voucherBillFileZJS) {
            VoucherBillZJFileSV voucherBillZJFileSV = new VoucherBillZJFileSV();
            String files = voucherBillFileZJ.getFiles();
            JSONObject jsonObject = JSONObject.parseObject(files);
            voucherBillZJFileSV.setFileKey(jsonObject.getString("fileKey"));
            voucherBillZJFileSV.setFileName(jsonObject.getString("name"));
            voucherBillZJFileSV.setFileFormat(jsonObject.getString("suffix"));
            voucherBillZJFileSV.setFileSize(jsonObject.getString("size"));
            voucherBillZJFileSV.setCreatTime(voucherBillFileZJ.getGmtCreate());
            voucherBillZJFileSV.setCreatName(voucherBillFileZJ.getCreatorName());
            voucherBillZJFileSV.setId(String.valueOf(voucherBillFileZJ.getId()));
            voucherBillZJFileSVS.add(voucherBillZJFileSV);
        }
        if(ZJTriggerEventBillTypeEnum.支付申请.getCode() == billEventType || ZJTriggerEventBillTypeEnum.收入确认.getCode() == billEventType){
            log.info("支付申请、收入确认只查询影像附件，跳出");
            return voucherBillZJFileSVS;
        }

        List<AttachmentV> fielList = new ArrayList<>();
        if (ZJTriggerEventBillTypeEnum.CONTRACT_TYPE_LIST.contains(billEventType) && StringUtils.isNotBlank(contractId)){
            //根据合同ID及文件类型获取 合同与补充协议扫描件
            getContractFileList(contractId, fielList);
        }
        if (ZJTriggerEventBillTypeEnum.收入确认计提.getCode() == billEventType && StringUtils.isNotBlank(contractId)){
            List<String> contractIdList = Arrays.asList(contractId.split("\\s*,\\s*"));
            for(String conId : contractIdList){
                getContractFileList(conId, fielList);
            }
        }
        if(StringUtils.isNotBlank(settleId) && (ZJTriggerEventBillTypeEnum.收入确认实签.getCode() == billEventType || ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode() == billEventType)){
            //根据确收单ID获取 确收审批 附件中 合同确收单附件
            List<AttachmentV> contractFielList = this.getContractFileList(settleId,1003,2);
            if(CollectionUtils.isNotEmpty(contractFielList)){
                fielList.addAll(contractFielList);
            }
        }
        if(StringUtils.isNotBlank(settleId) && (ZJTriggerEventBillTypeEnum.对下结算实签.getCode() == billEventType || ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode() == billEventType)){
            //根据结算单ID获取合同数量签认单
            List<AttachmentV> contractFielList = this.getContractFileList(settleId,1001,2);;
            if(CollectionUtils.isNotEmpty(contractFielList)){
                fielList.addAll(contractFielList);
            }
            //根据结算单ID获取合同结算表
            List<AttachmentV> contractJsFielList = this.getContractFileList(settleId,1001,3);;
            if(CollectionUtils.isNotEmpty(contractJsFielList)){
                fielList.addAll(contractJsFielList);
            }
            if(ZJTriggerEventBillTypeEnum.对下结算实签.getCode() == billEventType){
                //根据结算单ID对应结算审批-财务结算中其他附件
                List<AttachmentV> contractFinanceFielList = this.getContractFileList(settleId,1004,0);;
                if(CollectionUtils.isNotEmpty(contractFinanceFielList)){
                    fielList.addAll(contractFinanceFielList);
                }
            }
        }
        if(CollectionUtils.isEmpty(fielList)){
            log.info("未查询合同扫描件");
            return voucherBillZJFileSVS;
        }
        //封装合同扫描件
        for (AttachmentV attachmentV : fielList) {
            VoucherBillZJFileSV voucherBillZJFileSV = new VoucherBillZJFileSV();
            voucherBillZJFileSV.setFileKey(attachmentV.getFileKey());
            voucherBillZJFileSV.setFileName(attachmentV.getName());
            voucherBillZJFileSV.setFileFormat(attachmentV.getSuffix());
            voucherBillZJFileSV.setFileSize(attachmentV.getFileSizeStr());
            voucherBillZJFileSV.setCreatTime(attachmentV.getGmtCreate());
            voucherBillZJFileSV.setCreatName(attachmentV.getCreatorName());
            voucherBillZJFileSV.setId(attachmentV.getId());
            voucherBillZJFileSV.setFileYxxxID(attachmentV.getFileuuid());
            voucherBillZJFileSVS.add(voucherBillZJFileSV);
        }
        return voucherBillZJFileSVS;
    }

    //根据合同ID及文件类型获取 合同与补充协议扫描件
    private void getContractFileList(String conId, List<AttachmentV> fielList) {
        PageF<SearchF<AttachmentE>> pageF = new PageF<>();
        pageF.setPageNum(1);
        pageF.setPageSize(100);
        Field field = new Field();
        field.setName("businessId");
        field.setMethod(1);
        field.setValue(conId);
        Field fieldBusinessType = new Field();
        fieldBusinessType.setName("businessType");
        fieldBusinessType.setMethod(1);
        fieldBusinessType.setValue(1002);
        List<Field> fields = Arrays.asList(field,fieldBusinessType);
        SearchF<AttachmentE> SearchF = new SearchF<>();
        SearchF.setFields(fields);
        pageF.setConditions(SearchF);
        List<AttachmentV> contractFielList = contractClient.getAllFileList(pageF).getRecords();
        if(CollectionUtils.isNotEmpty(contractFielList)){
            fielList.addAll(contractFielList);
        }
    }

    //根据条件获取对应附件
    private List<AttachmentV> getContractFileList(String businessId, Integer businessType, Integer type){
        PageF<SearchF<AttachmentE>> pageF = new PageF<>();
        pageF.setPageNum(1);
        pageF.setPageSize(100);
        Field field = new Field();
        field.setName("businessId");
        field.setMethod(1);
        field.setValue(businessId);
        Field fieldBusinessType = new Field();
        fieldBusinessType.setName("businessType");
        fieldBusinessType.setMethod(1);
        fieldBusinessType.setValue(businessType);
        Field fieldType = new Field();
        fieldType.setName("type");
        fieldType.setMethod(1);
        fieldType.setValue(type);
        List<Field> fields = Arrays.asList(field,fieldBusinessType,fieldType);
        SearchF<AttachmentE> SearchF = new SearchF<>();
        SearchF.setFields(fields);
        pageF.setConditions(SearchF);
        return  contractClient.frontPage(pageF).getRecords();
    }

    public List<VoucherBillZJRecDetailV2> queryRecDetailPageV2(String voucherBillNo) {
        List<VoucherBillZJRecDetailV2> recDetailV2List = new ArrayList<>();
        //根据 报账单号 获取 汇总单据表
        VoucherBillZJ voucherBill = voucherPushBillZJRepository.queryByVoucherBillNo(voucherBillNo);
        if (Objects.isNull(voucherBill)) {
            return Collections.emptyList();
        }
        //获取科目映射中“代收代付属性费项映射”中所有”其他应收款“ID
        List<String> receivebleList = subjectMapRulesDomainService.getAllTPPReceiveblesList();
        //根据 报账单号 获取 应收款明细列表
        LambdaQueryWrapper<VoucherBillZJRecDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VoucherBillZJRecDetailE::getInnerSheetCode, voucherBillNo);
        queryWrapper.eq(VoucherBillZJRecDetailE::getDeleted, 0);
        List<VoucherBillZJRecDetailE> voucherBillZJRecDetailES = recDetailRepository.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(voucherBillZJRecDetailES)){
            recDetailV2List = Global.mapperFacade.mapAsList(voucherBillZJRecDetailES, VoucherBillZJRecDetailV2.class);
            recDetailV2List.forEach(x-> {
                x.setIsSaveData(Boolean.TRUE);
                if (StringUtils.isNotBlank(x.getPaymentId())) {
                    x.setIsTPPReceivebles(receivebleList.contains(x.getPaymentId()) ? Boolean.TRUE : Boolean.FALSE);
                } else {
                    x.setIsTPPReceivebles(Boolean.FALSE);
                }
            });
            return recDetailV2List;
        }

        //根据 报账单号 获取 报账明细表
        List<VoucherBillZJRecV> recVList = voucherBillDetailRepository.queryRecList(voucherBillNo);
        if (CollectionUtils.isEmpty(recVList)){
            return Collections.emptyList();
        }
        // recVList 按照 contractId taxRate subjectIdExt 分组,其中contractId可能是空的
        Map<String, List<VoucherBillZJRecV>> groupedRecMap = recVList
                        .stream()
                        .collect(Collectors.groupingBy(VoucherBillZJRecV::groupKey));

        //根据项目id获取中交的项目id
        SpaceCommunityShortV spaceCommunityShortVS = spaceClient.get(this.communityId(voucherBill));
        String projectId = "";
        if (Objects.nonNull(spaceCommunityShortVS)) {
            projectId = spaceCommunityShortVS.getSerialNumber();
        }

        Set<BigDecimal> taxRateList = recVList.stream()
                .map(VoucherBillZJRecV::getTaxRate)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        // 暂时写死税种id=增值税 后面不知道是否需要调整
        String taxTypeId = "62f62cde-1396-68be-ac2b-083a01b170bf";
        List<FinancialTaxInfo> taxInfoList = financialTaxInfoMapper.getListByTaxRateAndTaxTypeId(taxRateList, taxTypeId);
        Map<String, String> rateToIdMap = taxInfoList.stream()
                .collect(Collectors.toMap(e -> e.getApplicableTaxRate().stripTrailingZeros().toPlainString(), FinancialTaxInfo::getId, (v1, v2) -> v1));

        List<String> matchedFtIdList = Lists.newArrayList();
        for (Map.Entry<String, List<VoucherBillZJRecV>> entry : groupedRecMap.entrySet()) {
            List<VoucherBillZJRecV> curRecList = entry.getValue();
            VoucherBillZJRecV v = curRecList.get(0);
            // 分组内容
            String contractId = v.getContractId();
            String contractNo = v.getContractNo();
            BigDecimal taxRate = v.getTaxRate();
            String subjectIdExt = v.getSubjectIdExt();
            String subjectName = v.getSubjectName();

            VoucherBillZJRecDetailV2 recV2 = new VoucherBillZJRecDetailV2();
            String innerRecCode = IdentifierFactory.getInstance().serialNumber("pushbillZJ", "SKMX2", 20);
            recV2.setInnerRecCode(innerRecCode);
            recV2.setInnerSheetCode(voucherBillNo);
            LocalDateTime gmtCreate = voucherBill.getGmtCreate();
            // 到期时间
            recV2.setDueDate(gmtCreate.withYear(gmtCreate.getYear() + 1)
                    .with(TemporalAdjusters.lastDayOfMonth())
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            if (Objects.nonNull(voucherBill.getGmtExpire())){
                recV2.setDueDate(voucherBill.getGmtExpire().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            // 税率
            String financialTaxId = rateToIdMap.get(taxRate.stripTrailingZeros().toPlainString());
            if (org.apache.commons.lang3.StringUtils.isBlank(financialTaxId)){
                throw new OwlBizException("税率"+taxRate+"未维护对应增值税id,请联系系统管理员");
            }
            recV2.setFinancialTaxRateId(financialTaxId);
            recV2.setTaxRate(taxRate.setScale(2, RoundingMode.HALF_UP));
            // 金额相关
            BigDecimal reduce = curRecList.stream()
                    .map(VoucherBillZJRecV::getTaxIncludAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalAmount = reduce.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            recV2.setTotalAmountOnCurrency(totalAmount);
            recV2.setTotalAmountOnOrigin(totalAmount);

            BigDecimal taxRateCalNumber = BigDecimal.ONE.add(taxRate);

            // 含税金额 除 1 + 税额   得到不含税金额
            BigDecimal noTaxAmount = reduce.divide(taxRateCalNumber,2, RoundingMode.HALF_UP);
            // 税额
            BigDecimal noTaxAmountDoubleVal = noTaxAmount
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            BigDecimal taxAmountDoubleVal = reduce.divide(new BigDecimal("100"))
                    .subtract(noTaxAmountDoubleVal)
                    .setScale(2 , RoundingMode.HALF_UP);
            recV2.setNoTaxAmountOnCurrency(noTaxAmountDoubleVal);
            recV2.setNoTaxAmountOnOrigin(noTaxAmountDoubleVal);
            recV2.setTaxAmountOnCurrency(taxAmountDoubleVal);
            recV2.setTaxAmountOnOrigin(taxAmountDoubleVal);
            // 业务科目
            recV2.setPaymentId(subjectIdExt);
            recV2.setPaymentName(subjectName);
            recV2.setProjectId(projectId);
            // 合同id为空,那么就是小业主/大业主,否则就是合同的
            if (StringUtils.isBlank(contractNo)){
                recV2.setContractNo("9999999999");
                if (Objects.isNull(v.getPayerType()) || v.getPayerType() != 99){
                    recV2.setContractPayer("BP01505472");
                    recV2.setContractPayerName("交心");
                } else {
                    recV2.setContractPayer(v.getPayerId());
                    recV2.setContractPayerName(v.getPayerName());
                }

                handleTaxTypeOnTaxRateBase(taxRate, recV2);
                // 计税方式
                List<String> taxTypeList = curRecList.stream()
                        .map(VoucherBillZJRecV::getTaxType)
                        .filter(StringUtils::isNotBlank)
                        .filter(e -> !"一般计税".equals(e))
                        .distinct()
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(taxTypeList)){
                    recV2.setTaxType(taxTypeList.get(0));
                }
            } else {
                List<ContractPayPlanInnerInfoV> innerInfoVList =
                        contractClient.getInnerInfoByContractIdOnIncome(Arrays.asList(contractId));
                if (CollectionUtils.isEmpty(innerInfoVList)){
                    throw new OwlBizException("合同内部数据"+contractId+"异常");
                }
                ContractPayPlanInnerInfoV innerInfo = innerInfoVList.get(0);
                recV2.setContractNo(innerInfo.getConMainCode());
                recV2.setDueDate(DateUtil.format(innerInfo.getExpireNextEndDate(), "yyyy-MM-dd"));
                handleTaxTypeOnTaxRateBase(taxRate, recV2);
                if (StringUtils.isBlank(innerInfo.getDraweeId())){
                    throw new OwlBizException("合同往来单位数据异常,请检查");
                }
                List<SupplierSimpleV> simpleList = orgClient.simpleListSupplier(Arrays.asList(innerInfo.getDraweeId()));
                if (CollectionUtils.isEmpty(simpleList)){
                    throw new OwlBizException("合同往来单位内部数据异常,请检查");
                }
                SupplierSimpleV simpleV = simpleList.get(0);
                if (StringUtils.isBlank(simpleV.getMainDataCode())){
                    throw new OwlBizException("合同往来单位缺失主数据信息,请检查");
                }
                recV2.setContractPayer(simpleV.getMainDataCode());
                recV2.setContractPayerName(simpleV.getName());
            }
            if (StringUtils.isNotBlank(recV2.getPaymentId())) {
                recV2.setIsTPPReceivebles(receivebleList.contains(recV2.getPaymentId()) ? Boolean.TRUE : Boolean.FALSE);
            } else {
                recV2.setIsTPPReceivebles(Boolean.FALSE);
            }
            List<VoucherBillZJRecDetailV2> matchedList = mdm63Service.matchForReceiptSheet(recV2,
                    projectId, subjectIdExt, recV2.getContractNo(),
                    recV2.getContractPayer(),
                    !StringUtils.equals("9999999999", recV2.getContractNo()),
                    matchedFtIdList);
            recDetailV2List.addAll(matchedList);
        }
        return recDetailV2List;
    }

    /**
     * 基础的判定
     * 税率和5%比大小
     **/
    private void handleTaxTypeOnTaxRateBase(BigDecimal taxRate, VoucherBillZJRecDetailV2 recV2) {
        if (new BigDecimal("0.05").compareTo(taxRate) < 0) {
            recV2.setTaxType("1");
        } else {
            recV2.setTaxType("2");
        }
    }

    public String communityId(VoucherBillZJ voucherBill) {
        List<VoucherPushBillDetailZJ> pushBillDetailZJS = voucherBillDetailRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                .eq(VoucherPushBillDetailZJ::getVoucherBillNo, voucherBill.getVoucherBillNo()));
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(pushBillDetailZJS)) {
            return pushBillDetailZJS.get(0).getCommunityId();
        }
        return "";
    }

    @Transactional(rollbackFor = Exception.class)
    public String saveOrUpdateRecDetail(VoucherBillZJRecDetailF recDetailF) {
        recDetailRepository.deleteByVoucherBillNo(recDetailF.getVoucherBillNo());
        List<VoucherBillZJRecDetailV2> recDetailList = recDetailF.getRecDetailList();
        List<VoucherBillZJRecDetailE> detailList =
                Global.mapperFacade.mapAsList(recDetailList, VoucherBillZJRecDetailE.class);
        recDetailRepository.saveOrUpdateBatch(detailList);
        VoucherBillZJ voucherBillZJ = voucherPushBillZJRepository.queryByVoucherBillNo(recDetailF.getVoucherBillNo());
        List<String> ftIdList = detailList.stream().map(VoucherBillZJRecDetailE::getFtId).filter(StringUtils::isNotBlank)
                .distinct().collect(Collectors.toList());
        //lockMdm63OnFinanceSheet(voucherBillZJ.getId(), voucherBillZJ.getVoucherBillNo(), ftIdList);
        return "success";
    }

/*    private void lockMdm63OnFinanceSheet(Long voucherBillId, String voucherBillNo, List<String> ftIdList) {
        mdm63LockMapper.deleteByVoucherBillNo(voucherBillNo);
        // ftIdList去重复
        ftIdList = ftIdList.stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ftIdList)){
            return;
        }
        for (String ftId : ftIdList) {
            Mdm63LockE mdm63LockE = new Mdm63LockE();
            mdm63LockE.setId(IdentifierFactory.getInstance().generateLongIdentifier("mdm63_lock"));
            mdm63LockE.setFtId(ftId);
            mdm63LockE.setVoucherBillId(voucherBillId);
            mdm63LockE.setVoucherBillNo(voucherBillNo);
            mdm63LockMapper.insert(mdm63LockE);
        }
    }*/

    public PageV<Mdm63FrontV> queryMdm63Page(PageF<VoucherBillRecMdm63F> pageF) {
        VoucherBillRecMdm63F mdm63F = pageF.getConditions();
        SpaceCommunityShortV spaceCommunityShortVS = spaceClient.get(mdm63F.getCommunityId());
        if (Objects.isNull(spaceCommunityShortVS)){
            throw new OwlBizException("项目编号不存在,请检查");
        }
        String projectId = spaceCommunityShortVS.getSerialNumber();
        boolean fromContract = !StringUtils.equals("9999999999", mdm63F.getContractNo());
        Page<Mdm63FrontV> mdm63FrontVPage = mdm63Mapper.queryPageOnRec(RepositoryUtil.convertMPPage(pageF),
                Mdm63Service.RECEIVABLE_TYPE,
                projectId,
                mdm63F.getContractPayer(),
                mdm63F.getBillNum(),
                mdm63F.getBizDate(),
                mdm63F.getContractNo(),
                mdm63F.getPaymentId(),
                mdm63F.getQueryVoucherBillNo(),
                fromContract);
        if (CollectionUtils.isEmpty(mdm63FrontVPage.getRecords())){
            return PageV.of(pageF.getPageNum(),pageF.getPageSize(),0,Collections.emptyList());
        }
        for (Mdm63FrontV record : mdm63FrontVPage.getRecords()) {
            record.setProjectInfoName(spaceCommunityShortVS.getName());
            record.setPartnerName(mdm63F.getContractPayerName());
        }
        return PageV.of(mdm63FrontVPage.getCurrent(), mdm63FrontVPage.getSize(),
                mdm63FrontVPage.getTotal(), mdm63FrontVPage.getRecords());
    }

    public SpaceCommunityShortV queryCommunityByVoucherBillNo(String voucherBillNo) {
        VoucherBillZJ voucherBill = voucherPushBillZJRepository.queryByVoucherBillNo(voucherBillNo);
        return spaceClient.get(this.communityId(voucherBill));
    }

    private static final ExecutorService executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 4
    );

    //根据项目ID重新获取MDM63数据
    public List<Mdm63Response> getProjectIdRefresh(String projectId){
        List<Mdm63Response> resultList = new ArrayList<>();
        SpaceCommunityShortV spaceCommunityShortVS = spaceClient.get(projectId);
        if (Objects.isNull(spaceCommunityShortVS)){
            throw new OwlBizException("项目编号不存在,请检查");
        }
        // 获取项目对应的外部数据对照组织ID
        List<CfgExternalDataV> community = configClient.getExternalMapByCode("community", projectId);
        String acctUnitID = community.stream()
                .filter(e -> "org".equals(e.getExternalDataType()))
                .map(CfgExternalDataV::getDataCode)
                .filter(StringUtils::isNotBlank)
                .findFirst()
                .orElse(null);

        String project = spaceCommunityShortVS.getSerialNumber();
                // 获取当前年份并减去一年得到上一年
        Year lastYear = Year.now().minusYears(1);
        // 构建上一年1月1日的开始时间
        LocalDateTime startOfYear = lastYear.atMonth(1).atDay(1).atStartOfDay();
        Date calStart = Date.from(startOfYear.atZone(ZoneId.systemDefault()).toInstant());
        String startStr = DateUtil.format(calStart, "yyyy-MM-dd");
        String endDateStr = DateUtil.format(new Date(), "yyyy-MM-dd");
        List<String> dateList = Mdm11Handler.splitDatesByInterval(startStr, endDateStr,28);
        List<CompletableFuture<List<Mdm63Response>>> futures = new ArrayList<>();
// 根据partnerCode contractCode 最后更新时间在start-end的进行强制更新
        mdm63Mapper.deleteByCtCodeAndPartnerCodeInCertainPeriod("9999999999", "BP01505472", startStr, endDateStr, project);
        for (int i = 0; i < dateList.size()-1; i++) {
            String curStart = dateList.get(i);
            String curEnd = dateList.get(i+1);
            //resultList.addAll(doSync2("9999999999", curStart, curEnd,project));
            CompletableFuture<List<Mdm63Response>> future = CompletableFuture.supplyAsync(() ->
                    doSync2("9999999999", curStart, curEnd, project, acctUnitID), executor);
            futures.add(future);
        }
        // 等待所有任务完成并收集结果
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );
        resultList = allFutures.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .collect(Collectors.toList())
        ).join();
        return resultList;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Mdm63Response> doSync2(String ctCode, String start, String end, String projectId, String acctUnitID) {
        int pageNum = 1;
        List<Mdm63Response> responseDataList = Lists.newArrayList();
        while (true){
            DictionaryF<MDM63F2> dicRequest = buildDictionaryFOnMdm16_2(ctCode, start, end, pageNum,projectId, acctUnitID);
            log.info("项目更新mdm63参数dicRequest为：{}",JSONArray.toJSON(dicRequest));
            String jsonString;
            try {
                jsonString = objectMapper.writeValueAsString(dicRequest);
            } catch (JsonProcessingException e) {
                log.error("objectMapper error:{}", e.getMessage());
                return responseDataList;
            }
            log.info("dicRequest:{}", jsonString);
            //组装查询对象
            String result = postRequestDefault(zjBillProperties.getUrl()+DIC_URL, jsonString);
            //数据转换
            ZjDictionaryResponse<Mdm63Response> response = null;
            response = JSON.parseObject(result, new TypeReference<ZjDictionaryResponse<Mdm63Response>>() {});
            if (Objects.isNull(response) || org.apache.commons.collections4.CollectionUtils.isEmpty(response.getData())){
                log.info("小业主{} 开始 {} 结束 {} 页码 {} 无MDM63数据", ctCode, start, end, pageNum);
                break;
            }
            responseDataList.addAll(response.getData());

            if (response.getTotalPage() > pageNum){
                pageNum++;
            }else {
                break;
            }
        }
        if (CollectionUtils.isEmpty(responseDataList)){
            return responseDataList;
        }
        // 插入新数据
        List<Mdm63E> list = responseDataList.stream().map(Mdm63Response::transferToMdm63E).collect(Collectors.toList());
        // 防止list过大,拆分200个一次进行插入
        List<List<Mdm63E>> partition = Lists.partition(list, 200);
        partition.forEach(mdm63Mapper::insertBatch);
        return responseDataList;
    }

    private DictionaryF<MDM63F2> buildDictionaryFOnMdm16_2(String ctCode, String start, String end, Integer pageNum, String projectId, String acctUnitID) {
        String startUtcStr = start+"T00:00:00.00+00:00";
        String endUtcStr = end+"T00:00:00.00+00:00";
        DictionaryF<MDM63F2> dictionaryF = new DictionaryF<>();
        MDM63F2 mdm16F = MDM63F2.builder()
                .contractCode(ctCode)
                .acctUnitID(acctUnitID)
                .starttime(startUtcStr)
                .PARTNERID("BP01505472")
                .pageNum(pageNum)
                .endtime(endUtcStr)
                .project(projectId).build();
        dictionaryF.setWhereCondition(mdm16F);
        dictionaryF.setAppInstanceCode("10000");
        dictionaryF.setDicCode("MDM63");
        dictionaryF.setSourceSystem("CCCG-DMC");
        dictionaryF.setUnitCode("MDM");
        return dictionaryF;
    }

    static public String postRequestDefault(String url, String jsonString) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader(CONTENTTYPE, "application/json");
        httpPost.setEntity(new StringEntity(jsonString, ContentType.APPLICATION_JSON));
        return post(httpPost);
    }

    static public String post(HttpPost httpPost) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                String responseBody = EntityUtils.toString(response.getEntity());
                return responseBody;
            } catch (Exception e) {
                e.printStackTrace();
                log.error("postRequest:{}", e.getMessage());
            } finally {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("postRequest:{}", e.getMessage());
        }
        throw BizException.throw400("警告！！！postRequest");
    }

}
