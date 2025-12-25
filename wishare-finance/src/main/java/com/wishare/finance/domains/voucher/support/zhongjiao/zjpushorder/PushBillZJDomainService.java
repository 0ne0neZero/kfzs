package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.shaded.io.grpc.netty.shaded.io.netty.util.internal.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.vo.TemporaryChargeBillDetailV;
import com.wishare.finance.apps.model.reconciliation.fo.external.OpinionApprovalDataF;
import com.wishare.finance.apps.model.reconciliation.fo.external.OpinionApprovalF;
import com.wishare.finance.apps.model.signature.ExternalMaindataCalmappingListF;
import com.wishare.finance.apps.model.signature.ExternalMaindataCalmappingListV;
import com.wishare.finance.apps.model.voucher.vo.SyncBatchVoucherResultV;
import com.wishare.finance.apps.process.enums.BusinessProcessType;
import com.wishare.finance.apps.process.service.DxJsBillProcessService;
import com.wishare.finance.apps.process.vo.OpinionApprovalV;
import com.wishare.finance.apps.pushbill.fo.RevenueApprove;
import com.wishare.finance.apps.pushbill.fo.SyncBatchPushZJBillF;
import com.wishare.finance.apps.pushbill.fo.UploadLinkZJF;
import com.wishare.finance.apps.pushbill.vo.UploadLinkZJ;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJCashFlowV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJRecCWYDetailV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJRecDetailV2;
import com.wishare.finance.apps.pushbill.vo.dx.DxPaymentDetails;
import com.wishare.finance.apps.pushbill.vo.dx.GeneralDetails;
import com.wishare.finance.apps.service.bill.TemporaryChargeBillAppService;
import com.wishare.finance.apps.service.pushbill.BillRuleAppService;
import com.wishare.finance.apps.service.pushbill.VoucherBillDetailAppService;
import com.wishare.finance.apps.service.pushbill.VoucherBillDxDetailAppService;
import com.wishare.finance.domains.bill.entity.BillAdjustE;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.repository.BillAdjustRepository;
import com.wishare.finance.domains.bill.repository.GatherBillRepository;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.configure.chargeitem.entity.FinancialTaxInfo;
import com.wishare.finance.domains.configure.chargeitem.repository.mapper.FinancialTaxInfoMapper;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.TaxpayerTypeEnum;
import com.wishare.finance.domains.mdm.entity.Mdm63E;
import com.wishare.finance.domains.mdm.entity.Mdm63LockE;
import com.wishare.finance.domains.mdm.repository.mapper.Mdm63Mapper;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimDetailE;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimRecordE;
import com.wishare.finance.domains.reconciliation.entity.FlowDetailE;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimDetailRepository;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimRecordRepository;
import com.wishare.finance.domains.reconciliation.repository.FlowDetailRepository;
import com.wishare.finance.domains.voucher.consts.enums.VoucherApproveStateEnum;
import com.wishare.finance.domains.voucher.dto.ApproveResultDto;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.PushBillTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.service.BillRuleDomainService;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.*;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.DocumentTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.PushBillApproveStateEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.*;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.FinanceProcessRecordZJMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.Mdm63LockMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherBillDetailDxZJMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjbill.ZJBillDataClient;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.dx.FinancialRequestBodyService;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.clients.base.*;
import com.wishare.finance.infrastructure.remote.enums.OperateTypeEnum;
import com.wishare.finance.infrastructure.remote.enums.financialcloud.FinancialTaxTypeEnum;
import com.wishare.finance.infrastructure.remote.fo.bpm.ProcessStartF;
import com.wishare.finance.infrastructure.remote.fo.zj.OrderDealBody;
import com.wishare.finance.infrastructure.remote.fo.zj.OrderStatusBody;
import com.wishare.finance.infrastructure.remote.fo.zj.OrderStatusDel;
import com.wishare.finance.infrastructure.remote.vo.bpm.ProcessProgressV;
import com.wishare.finance.infrastructure.remote.vo.bpm.Progress;
import com.wishare.finance.infrastructure.remote.vo.bpm.WflowModelHistorysV;
import com.wishare.finance.infrastructure.remote.vo.charge.ApproveFilter;
import com.wishare.finance.infrastructure.remote.vo.config.CfgExternalDataV;
import com.wishare.finance.infrastructure.remote.vo.contract.AttachmentE;
import com.wishare.finance.infrastructure.remote.vo.contract.AttachmentV;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractPayPlanInnerInfoV;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractV;
import com.wishare.finance.infrastructure.remote.vo.contract.ZJFileVo;
import com.wishare.finance.infrastructure.remote.vo.external.oa.OpinionApprovalResultV2;
import com.wishare.finance.infrastructure.remote.vo.external.oa.OpinionApprovalV2;
import com.wishare.finance.infrastructure.remote.vo.org.CustomerSimpleV;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceCostRv;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import com.wishare.finance.infrastructure.remote.vo.org.SupplierSimpleV;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceCommunityShortV;
import com.wishare.finance.infrastructure.remote.vo.user.UserInfoRv;
import com.wishare.finance.infrastructure.remote.vo.zj.MDM16V;
import com.wishare.finance.infrastructure.remote.vo.zj.MDM17V;
import com.wishare.finance.infrastructure.remote.vo.zj.ZJDatav;
import com.wishare.finance.infrastructure.remote.vo.zj.ZJSendresultV;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.finance.infrastructure.utils.FileUtil;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.enums.GatewayTagEnum;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum.EXT_UPLOAD_TYPE;
import static com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum.UPLOAD_TYPE;


@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PushBillZJDomainService {
    private final ZJBillDataClient zjBillDataClient;
    private final UserClient userClient;
    private final ExternalClient externalClient;
    private final ContractClient contractClient;
    private final SpaceClient spaceClient;
    private final OrgClient orgClient;
    private final ConfigClient configClient;
    private final VoucherPushBillZJRepository voucherPushBillZJRepository;
    private final VoucherBillDetailZJRepository voucherBillDetailRepository;
    private final VoucherContractMeasurementDetailZJRepository measurementDetailZJRepository;
    private final VoucherInvoiceZJRepository invoiceZJRepository;
    private final VoucherContractInvoiceZJRepository contractInvoiceZJRepository;
    private final BpmClient bpmClient;
    private final ChargeClient chargeClient;
    private final BillRuleDomainService billRuleDomainService;
    private final VoucherBillDetailAppService voucherBillDetailAppService;
    private final FlowClaimDetailRepository flowClaimDetailRepository;
    private final FlowDetailRepository flowDetailRepository;
    private final FlowClaimRecordRepository flowClaimRecordRepository;
    private final VoucherPushBillZJRepository voucherPushBillRepository;
    private final BillAdjustRepository billAdjustRepository;
    private final GatherBillRepository gatherBillRepository;
    private final ReceivableBillRepository receivableBillRepository;
    private final GatherDetailRepository gatherDetailRepository;
    private final VoucherBillDetailDxZJRepository voucherBillDetailDxZJRepository;
    private final VoucherPushBillDxZJRepository voucherPushBillDxZJRepository;
    private final VoucherBillDxDetailAppService voucherBillDxDetailAppService;
    private final FinancialRequestBodyService financialRequestBodyService;
    private final FinancialTaxInfoMapper financialTaxInfoMapper;
    private final Mdm63Mapper mdm63Mapper;
    private final FinanceProcessRecordZJMapper processRecordZJMapper;
    @Lazy
    @Autowired
    private TemporaryChargeBillAppService temporaryChargeBillAppService;
    private final PaymentApplicationFormRepository paymentApplicationFormRepository;
    private final Mdm63LockMapper mdm63LockMapper;
    @Autowired
    private DxJsBillProcessService processService;
    @Autowired
    private FinanceProcessRecordZJRepository recordZJRepository;
    private final VoucherBillZJRecDetailRepository voucherBillZJRecDetailRepository;
    @Autowired
    private VoucherBillDetailDxZJMapper voucherBillDetailDxZJMapper;

    @Value("${wishare.file.host:}")
    private String fileHost;



    public VoucherBillZJ getById(Long voucherBillId) {
        return voucherPushBillZJRepository.getById(voucherBillId);
    }

    /**
     * oa审批通过，自动推送财务云-对下结算单实签
     **/
    @Transactional(rollbackFor = Exception.class)
    public void approveAgreeOnOa(Long voucherBillId,String initiatorUserId) {
        log.info("对下结算单-实签 通过!");

        VoucherBillDxZJ voucherBillZJ = voucherPushBillDxZJRepository.getById(voucherBillId);
        log.info("voucherBillZJ:"+ JSON.toJSON(voucherBillZJ));
        // 主表状态刷新
        voucherPushBillDxZJRepository.update(new LambdaUpdateWrapper<VoucherBillDxZJ>()
                .set(VoucherBillDxZJ::getApproveState, 2)
                .set(VoucherBillDxZJ::getPushState, PushBillTypeEnum.待推送.getCode())
                .eq(VoucherBillDxZJ::getId, voucherBillId));
        // 自动推送至财务云
        SyncBatchPushZJBillF syncBatchPushZJBillF = new SyncBatchPushZJBillF();
        syncBatchPushZJBillF.setVoucherIds(Arrays.asList(voucherBillId));
        syncBatchPushZJBillF.setVoucherSystem(2);
        // 调用成本中心 获取项目接口
        OrgFinanceCostRv orgFinanceCostById = orgClient.getOrgFinanceCostById(voucherBillZJ.getCostCenterId());
        List<CfgExternalDataV> community = configClient.getExternalMapByCode("community", orgFinanceCostById.getCommunityId());
        // 调用外部数据映射接口
        // 调用根据行政组织获取核算组织接口
        for (CfgExternalDataV cfgExternalDataV : community) {
            if ("department".equals(cfgExternalDataV.getExternalDataType())){
                syncBatchPushZJBillF.setXZBM(cfgExternalDataV.getDataCode());
            } else if ("org".equals(cfgExternalDataV.getExternalDataType())){
                syncBatchPushZJBillF.setXZZZ(cfgExternalDataV.getDataCode());
            }
        }
        if(StringUtils.isNotEmpty(voucherBillZJ.getExternalDepartmentCode())){
            syncBatchPushZJBillF.setXZBM(voucherBillZJ.getExternalDepartmentCode());
        }
        IdentityInfo identityInfoDefault = ThreadLocalUtil.get("IdentityInfo",IdentityInfo.class);
        identityInfoDefault.setTenantId("13554968497211");
        UserInfoRv userInfoByUserId = userClient.getUserInfoByUserId(initiatorUserId);
        log.info("对下结算单{}-发起人信息:{}", voucherBillZJ.getVoucherBillNo(), userInfoByUserId);
        identityInfoDefault.setUserId(initiatorUserId);
        identityInfoDefault.setUserName(userInfoByUserId.getUserName());
        identityInfoDefault.setGateway(GatewayTagEnum.社区运营平台网关.getTag());
        ThreadLocalUtil.set("IdentityInfo", identityInfoDefault);

        syncBatchPushZJBillF.setUserId(initiatorUserId);
        syncBatchPushZJBillF.setApproveFlag(1);

        if(UPLOAD_TYPE.contains(voucherBillZJ.getBillEventType())){
            // 获取影像资料
            List<ZJFileVo> zjFileVos = new ArrayList<>();
            //计提附件获取
            List<VoucherPushBillDetailDxZJ> detailDxZJS = voucherBillDetailDxZJRepository.getByVoucherBillNoNoSearch(voucherBillZJ.getVoucherBillNo());
            TemporaryChargeBillDetailV billInfo = temporaryChargeBillAppService.getById(detailDxZJS.get(0).getBillId(), TemporaryChargeBillDetailV.class, detailDxZJS.get(0).getCommunityId());
            List<AttachmentV> attachmentVList = getSearchFPageF(billInfo);
            for (AttachmentV attachmentV : attachmentVList) {
                String s = fileHost + attachmentV.getFileKey();
                MultipartFile multipartFile = FileUtil.getMultipartFile(s);
                ZJFileVo zjFileVo = contractClient.zjUpload(multipartFile, voucherBillZJ.getVoucherBillNo());
                zjFileVo.setFileKey(attachmentV.getFileKey());
                zjFileVo.setName(attachmentV.getName());
                zjFileVos.add(zjFileVo);
            }

            for (ZJFileVo zjFileVo : zjFileVos) {
                UploadLinkZJF uploadLinkZJF = new UploadLinkZJF();
                uploadLinkZJF.setName(zjFileVo.getName());
                uploadLinkZJF.setImageIdZJ(zjFileVo.getFileId());
                uploadLinkZJF.setUploadLink("/files/" + zjFileVo.getFileKey());
                uploadLinkZJF.setBillNo(voucherBillZJ.getVoucherBillNo());
                voucherPushBillDxZJRepository.addLinkZJ(uploadLinkZJF);
            }
        }
        syncBatchPushBillForJTAndSQ(syncBatchPushZJBillF);
    }

    @NotNull
    public List<AttachmentV> getSearchFPageF(TemporaryChargeBillDetailV billInfo) {
        PageF<SearchF<AttachmentE>> pageF = new PageF<>();
        pageF.setPageNum(1);
        pageF.setPageSize(100);
        Field field = new Field();
        field.setName("businessId");
        field.setMethod(1);
        field.setValue(billInfo.getExtField7());
        List<Field> fields = Arrays.asList(field);
        SearchF<AttachmentE> SearchF = new SearchF<>();
        SearchF.setFields(fields);
        pageF.setConditions(SearchF);
        return  contractClient.frontPage(pageF).getRecords();
    }

    public SyncBatchVoucherResultV syncBatchPushBill(SyncBatchPushZJBillF syncBatchPushBillF) {
        List<VoucherBillZJ> voucherBills = voucherPushBillZJRepository.list(new LambdaQueryWrapper<VoucherBillZJ>()
                .in(VoucherBillZJ::getId, syncBatchPushBillF.getVoucherIds())
                .in(VoucherBillZJ::getPushState, 1, 3, 4,PushBillTypeEnum.制单失败.getCode(), PushBillTypeEnum.单据驳回.getCode())
                .eq(VoucherBillZJ::getDeleted, 0));
        if (null == syncBatchPushBillF.getUserId() || StringUtils.isEmpty(syncBatchPushBillF.getUserId())){
            IdentityInfo identityInfo = ThreadLocalUtil.curIdentityInfo();
            syncBatchPushBillF.setUserId(identityInfo.getUserId());
        }
        UserInfoRv userInfo = userClient.getUserInfo(syncBatchPushBillF.getUserId(), null);
        String empCode = externalClient.getIphoneByEmpCode(userInfo.getMobileNum());
        log.info("中交EmpCode {} 参数 {}", empCode, syncBatchPushBillF.getUserId());
        List<Long> errorList = new ArrayList<>();
        int successCount = voucherBills.size();
        for (VoucherBillZJ voucherBill : voucherBills) {
            SpaceCommunityShortV spaceCommunityShortVS = spaceClient.get(this.communityId(voucherBill));
            String projectId = "";
            if (Objects.nonNull(spaceCommunityShortVS)) {
                projectId = spaceCommunityShortVS.getSerialNumber();
            }
            List<SPRZData> SPRZDataList = new ArrayList<>();
            // 判断是否审批
            if (StringUtils.isNotBlank(voucherBill.getProcInstId())) {
                ProcessProgressV processFormAndInstanceProgress = bpmClient.getProcessFormAndInstanceProgress(voucherBill.getProcInstId(), "");
                List<Progress> progress = processFormAndInstanceProgress.getProgress();
                for (Progress progress1 : progress) {
                    if ( null != progress1.getResult() && ("ROOT").equals(progress1.getNodeType().toString())){
                        SPRZData sprzData = new SPRZData();
                        sprzData.setSPRZID(voucherBill.getProcInstId());
                        sprzData.setRWMC(processFormAndInstanceProgress.getProcessDefName());
                        sprzData.setJDMC(progress1.getName());
                        UserInfoRv userInfo1 = userClient.getUserInfo(progress1.getUser().getId(), null);
                        String byEmpCode = externalClient.getIphoneByEmpCode(userInfo1.getMobileNum());
                        sprzData.setSPR(byEmpCode);
                        sprzData.setSPDZ("提交");
                        sprzData.setSPYJ("提交审批");
                        sprzData.setKSRQ(String.valueOf(progress1.getStartTime()));
                        sprzData.setSPRQ(String.valueOf(progress1.getFinishTime()));
                        SPRZDataList.add(sprzData);
                    } else if ( "agree".equals(progress1.getResult()) && ("APPROVAL").equals(progress1.getNodeType().toString())){
                        SPRZData sprzData = new SPRZData();
                        sprzData.setSPRZID(voucherBill.getProcInstId());
                        sprzData.setRWMC(processFormAndInstanceProgress.getProcessDefName());
                        sprzData.setJDMC(progress1.getName());
                        sprzData.setSPR(externalClient.getIphoneByEmpCode(userClient.getUserInfo(progress1.getUser().getId(), null).getMobileNum()));
                        sprzData.setSPDZ("同意");
                        String string = progress1.getComment().toString();
                        Pattern pattern = Pattern.compile("text=([^,}]*)");
                        Matcher matcher = pattern.matcher(string);
                        if (matcher.find()) {
                            String textValue = matcher.group(1);
                            // 移除前导空格
                            textValue = textValue.trim();
                            sprzData.setSPYJ(textValue);
                        } else {
                            sprzData.setSPYJ("同意");
                        }
                        sprzData.setKSRQ(String.valueOf(progress1.getStartTime()));
                        sprzData.setSPRQ(String.valueOf(progress1.getFinishTime()));
                        SPRZDataList.add(sprzData);
                    }
                }
            }

            log.info("中交项目id {},参数 {}", projectId, this.communityId(voucherBill));
            try {
                List<String> detailNumberS = Lists.newArrayList();
                String zjRequestBodyString = "";
                if (voucherBill.getBillEventType() == 1) {
                    // 判断是否需要审批
                    ApproveFilter approveFilter = chargeClient.getApprovePushBillFilter(spaceCommunityShortVS.getId(),
                            OperateTypeEnum.收入确认.getCode());
                    if (StringUtils.isBlank(syncBatchPushBillF.getYWLX())){
                        syncBatchPushBillF.setYWLX(voucherBill.getBusinessTypeId().trim());
                    }
                    // 1 收入确认单
                    if (voucherBill.getApproveState() == 2 || approveFilter.getApproveWay() == 2  || (null !=  syncBatchPushBillF.getApproveFlag() && syncBatchPushBillF.getApproveFlag().equals(1))) {
                        zjRequestBodyString = this.zjSRQRRequestBody(voucherBill, projectId, syncBatchPushBillF, empCode, detailNumberS, SPRZDataList);
                    } else {
                        RevenueApprove revenueApprove = new RevenueApprove();
                        revenueApprove.setCostCenterId(voucherBill.getCostCenterId());
                        revenueApprove.setVoucherBillIds(Arrays.asList(voucherBill.getId()));
                        Global.ac.getBean(BillRuleAppService.class).revenueApprove(revenueApprove);
                        return new SyncBatchVoucherResultV();
                    }
                } else if (voucherBill.getBillEventType() == 2) {
                    // 2 资金收款单
                    zjRequestBodyString = this.zjZJSKRequestBody(voucherBill, projectId, syncBatchPushBillF, empCode, detailNumberS, SPRZDataList);
                } else if (voucherBill.getBillEventType() == 3) {
                    // 3 对下结算
                    zjRequestBodyString = this.zjDXJSRequestBody(voucherBill, projectId,syncBatchPushBillF, empCode, detailNumberS);
                } else if (voucherBill.getBillEventType() == 4) {
                    // 4 支付申请
                    zjRequestBodyString = this.zjZFSQRequestBody(voucherBill, projectId, syncBatchPushBillF, empCode, detailNumberS);
                }
                String businessCode = DocumentTypeEnum.valueOfByCode(voucherBill.getBillEventType()).getValue();
                ZJSendresultV zjSendresultV = zjBillDataClient.getPushOrder(zjRequestBodyString, voucherBill.getVoucherBillNo(), businessCode);

                if (Objects.nonNull(zjSendresultV) && 0 == zjSendresultV.getCode()) {
                    voucherBill.setDetailNumber(detailNumberS.toString());
                    voucherBill.setPushState(PushBillTypeEnum.已推送.getCode());
                    voucherBillDetailRepository.update(new LambdaUpdateWrapper<VoucherPushBillDetailZJ>()
                            .eq(VoucherPushBillDetailZJ::getVoucherBillNo, voucherBill.getVoucherBillNo())
                            .set(VoucherPushBillDetailZJ::getPushBillState, 2));
                    if (voucherBill.getBillEventType() == 2){
                        String recordIdList = voucherBill.getRecordIdList();
                        List<Long> recordIds = JSON.parseArray(recordIdList, Long.class);
                        billRuleDomainService.updateFlowAfterPush(recordIds, true);
                        // 修改该批次收款单状态 为已推送
                        List<VoucherPushBillDetailZJ> pushBillDetailZJS = voucherBillDetailRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                                .eq(VoucherPushBillDetailZJ::getVoucherBillNo, voucherBill.getVoucherBillNo())
                                .eq(VoucherPushBillDetailZJ::getDeleted, 0));
                        gatherBillRepository.update(new UpdateWrapper<GatherBill>().set("inference_state", 1)
                                .eq("sup_cp_unit_id", pushBillDetailZJS.get(0).getCommunityId())
                                .in("id", pushBillDetailZJS.stream().map(VoucherPushBillDetailZJ::getGatherBillId).collect(Collectors.toList())));

                        List<Long> gatherBillDetailIdList = pushBillDetailZJS.stream().map(VoucherPushBillDetailZJ::getGatherBillDetailId).filter(Objects::nonNull).collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(gatherBillDetailIdList)){
                            List<Long> billIdList = pushBillDetailZJS.stream().map(VoucherPushBillDetailZJ::getBillId).collect(Collectors.toList());
                            gatherDetailRepository.update(new UpdateWrapper<GatherDetail>().set("inference_state", 1)
                                    .eq("sup_cp_unit_id", pushBillDetailZJS.get(0).getCommunityId())
                                    .in("rec_bill_id", billIdList));
                        } else {
                            gatherDetailRepository.update(new UpdateWrapper<GatherDetail>().set("inference_state", 1)
                                    .eq("sup_cp_unit_id", pushBillDetailZJS.get(0).getCommunityId())
                                    .in("id", gatherBillDetailIdList));
                        }


                    } else if (voucherBill.getBillEventType() == 1) {
                        // 收入确认单处理
                        List<VoucherPushBillDetailZJ> pushBillDetailZJS = voucherBillDetailRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                                .eq(VoucherPushBillDetailZJ::getVoucherBillNo, voucherBill.getVoucherBillNo())
                                .eq(VoucherPushBillDetailZJ::getDeleted, 0));
                        receivableBillRepository.update(new UpdateWrapper<ReceivableBill>().set("inference_state", 1)
                                .eq("sup_cp_unit_id", pushBillDetailZJS.get(0).getCommunityId())
                                .in("id",pushBillDetailZJS.stream().map(VoucherPushBillDetailZJ::getBillId).collect(Collectors.toList())));
                        // 对应调整明细更新为已推凭
                        billAdjustRepository.updateBillInferenceStateByGmtModify(
                                pushBillDetailZJS.stream().map(VoucherPushBillDetailZJ::getBillId).collect(Collectors.toList()),
                                1,
                                voucherBill.getGmtCreate());
//                        billAdjustRepository.update(new UpdateWrapper<BillAdjustE>().set("inference_state", 1)
//                                .in("bill_id", pushBillDetailZJS.stream().map(VoucherPushBillDetailZJ::getBillId).collect(Collectors.toList()))
//                                .le("gmt_modify", voucherBill.getGmtCreate()));
                    }
                } else {
                    voucherBill.setPushState(PushBillTypeEnum.推送失败.getCode());
                    ZJDatav zjDatav = JSONObject.parseObject(zjSendresultV.getData(), ZJDatav.class);
                    voucherBill.setRemark(zjDatav.getMessage());
                    errorList.add(voucherBill.getId());
                    successCount--;
                    if (voucherBill.getBillEventType() == 2) {
                        String recordIdList = voucherBill.getRecordIdList();
                        List<Long> recordIds = JSON.parseArray(recordIdList, Long.class);
                        billRuleDomainService.updateFlowAfterPush(recordIds, false);

                        voucherBillDetailRepository.update(new LambdaUpdateWrapper<VoucherPushBillDetailZJ>()
                                .eq(VoucherPushBillDetailZJ::getVoucherBillNo, voucherBill.getVoucherBillNo())
                                .set(VoucherPushBillDetailZJ::getPushBillState, 3));
                    }
                }
            } catch (Exception e) {
                log.error("凭证推送失败 ---------> : ", e);
                voucherBill.setPushState(PushBillTypeEnum.推送失败.getCode());
                voucherBill.setRemark(e.getMessage());
                errorList.add(voucherBill.getId());
                successCount--;
                successCount--;
                voucherBillDetailRepository.update(new LambdaUpdateWrapper<VoucherPushBillDetailZJ>()
                        .eq(VoucherPushBillDetailZJ::getVoucherBillNo, voucherBill.getVoucherBillNo())
                        .set(VoucherPushBillDetailZJ::getPushBillState, 3));
            }
            voucherBill.setFinanceId(null);
            voucherBill.setFinanceNo(null);
            voucherPushBillZJRepository.updateById(voucherBill);

        }
        SyncBatchVoucherResultV result = new SyncBatchVoucherResultV();
        String level = "success";
        if (successCount > 0) {
            level = "warn";
        } else if (successCount <= 0) {
            level = "error";
        }
        result.setLevel(level);
        result.setErrorTotal(voucherBills.size() - successCount);
        result.setSuccessTotal(successCount);
        result.setErrorList(errorList);
        return result;
    }


    private void countAmount(BigDecimal taxrate, SRQRMXData srqrmxData, KJMXData kjmxData, Long settleAmount) {
        //(1+税率)
        BigDecimal decimal = getDecimalByTaxRate(taxrate);

        BigDecimal divideSettleAmount = getDivideSettleAmountBySettleAmount(settleAmount);
        BigDecimal taxAmount = getTaxAmount(taxrate, divideSettleAmount, decimal);
        //不含税金额
        Double taxExcludAmount = getTaxExcludeAmount(divideSettleAmount, taxAmount);
        if (Objects.nonNull(srqrmxData)) {
            //含税金额
            srqrmxData.setHSJEYB(divideSettleAmount.doubleValue());
            srqrmxData.setHSJEBB(divideSettleAmount.doubleValue());
            //税额
            srqrmxData.setSEYB(taxAmount.doubleValue());
            srqrmxData.setSEBB(taxAmount.doubleValue());
            //不含税金额
            srqrmxData.setBHSYB(taxExcludAmount);
            srqrmxData.setBHSBB(taxExcludAmount);
        }
        if (Objects.nonNull(kjmxData)) {
            kjmxData.setKJJEYB(settleAmount.doubleValue());
            kjmxData.setKJJEBB(settleAmount.doubleValue());
        }
    }


    private void countAmountZJ(BigDecimal taxrate, SRQRMXData srqrmxData, KJMXData kjmxData, BigDecimal divideSettleAmount) {
        //(1+税率)
        BigDecimal decimal = getDecimalByTaxRate(taxrate);

        BigDecimal taxAmount = getTaxAmount(taxrate, divideSettleAmount, decimal);
        //不含税金额
        Double taxExcludAmount = getTaxExcludeAmount(divideSettleAmount, taxAmount);
        if (Objects.nonNull(srqrmxData)) {
            //含税金额
            srqrmxData.setHSJEYB(divideSettleAmount.doubleValue());
            srqrmxData.setHSJEBB(divideSettleAmount.doubleValue());
            //税额
            srqrmxData.setSEYB(taxAmount.doubleValue());
            srqrmxData.setSEBB(taxAmount.doubleValue());
            //不含税金额
            srqrmxData.setBHSYB(taxExcludAmount);
            srqrmxData.setBHSBB(taxExcludAmount);
        }
        if (Objects.nonNull(kjmxData)) {
            kjmxData.setKJJEYB(divideSettleAmount.doubleValue());
            kjmxData.setKJJEBB(divideSettleAmount.doubleValue());
        }
    }

    private static double getTaxExcludeAmount(BigDecimal divideSettleAmount, BigDecimal taxAmount) {
        return divideSettleAmount.subtract(taxAmount).doubleValue();
    }

    /** 根据税率获取含税金额
     * @param taxrate
     * @param divideSettleAmount
     * @param decimal
     * @return
     */
    @NotNull
    private static BigDecimal getTaxAmount(BigDecimal taxrate, BigDecimal divideSettleAmount, BigDecimal decimal) {
        BigDecimal taxAmount = taxrate
                .multiply(divideSettleAmount)
                .divide(decimal, 0, RoundingMode.HALF_UP);
        return taxAmount;
    }

    private static BigDecimal getDecimalByTaxRate(BigDecimal taxrate) {
        return taxrate.add(new BigDecimal("1"));
    }

    /**
     * 获取单位为分的金额
     *
     * @param settleAmount
     * @return
     */
    private static BigDecimal getDivideSettleAmountBySettleAmount(Long settleAmount) {
        return new BigDecimal(settleAmount).divide(new BigDecimal("100"));
    }

    private Map<BigDecimal, Long> taxGroup(List<VoucherPushBillDetailZJ> pushBillDetailZJS) {
        Map<BigDecimal, Long> taxList = pushBillDetailZJS.stream()
                .collect(Collectors.groupingBy(VoucherPushBillDetailZJ::getTaxRate, Collectors.summingLong(VoucherPushBillDetailZJ::getTaxIncludAmount)));
        return taxList;
    }
    private Map<BigDecimal, List<VoucherPushBillDetailZJ>> taxDetailListGroup(List<VoucherPushBillDetailZJ> pushBillDetailZJS) {
        Map<BigDecimal, List<VoucherPushBillDetailZJ>> collect = pushBillDetailZJS.stream().collect(Collectors.groupingBy(VoucherPushBillDetailZJ::getTaxRate));
        return collect;
    }

    private Map<BigDecimal, List<VoucherPushBillDetailDxZJ>> taxDetailListGroupForZJ(List<VoucherPushBillDetailDxZJ> pushBillDetailZJS) {
        Map<BigDecimal, List<VoucherPushBillDetailDxZJ>> collect = pushBillDetailZJS.stream().collect(Collectors.groupingBy(VoucherPushBillDetailDxZJ::getTaxRate));
        return collect;
    }
    private Map<String, List<VoucherPushBillDetailZJ>> sysSourceGroup(List<VoucherPushBillDetailZJ> pushBillDetailZJS) {
        return pushBillDetailZJS.stream()
                .collect(Collectors.groupingBy(VoucherPushBillDetailZJ::getContractId, Collectors.toList()));
    }

    private Map<String, List<VoucherPushBillDetailDxZJ>> sysSourceGroupZJ(List<VoucherPushBillDetailDxZJ> pushBillDetailZJS) {
        return pushBillDetailZJS.stream()
                .collect(Collectors.groupingBy(VoucherPushBillDetailDxZJ::getContractId, Collectors.toList()));
    }

    private String zjSRQRRequestBody(VoucherBillZJ voucherBill, String projectId, SyncBatchPushZJBillF syncBatchPushBillF,
                                 String empCode, List<String> detailNumberS, List<SPRZData> SPRZDataList ) throws JsonProcessingException {
        ZJRequestBody zjRequestBody = new ZJRequestBody();
        //appInstanceCode你们默认10000     unitCode默认MDM   sourceSystem默认CCCG-DMC
        ZJParameter zjParameter = new ZJParameter();
        zjParameter.setBusinessCode(DocumentTypeEnum.valueOfByCode(voucherBill.getBillEventType()).getValue());
        zjParameter.setUnitCode("MDM");
        zjParameter.setSourceSystem("CCCG-DMC");
        zjParameter.setAppInstanceCode("10000");
        List<BILLDATAS> billdatasList = Lists.newArrayList();
        BILLDATAS zjDatas = new BILLDATAS();
        BILLDATA zjData = new BILLDATA();
        zjData.setCode(DocumentTypeEnum.valueOfByCode(voucherBill.getBillEventType()).getValue());
        zjData.setIsMain("true");


        // TYSRQR（通用收入确认）说明
        List<TYSRQRData> tysrqrDataList = Lists.newArrayList();
        TYSRQRData tysrqrData = new TYSRQRData();
        tysrqrData.setDJNM(voucherBill.getVoucherBillNo());
        tysrqrData.setDJBH(voucherBill.getVoucherBillNo());
        tysrqrData.setXZZZ(syncBatchPushBillF.getXZZZ());
        tysrqrData.setXZBM(StringUtils.isNotEmpty(voucherBill.getExternalDepartmentCode()) ? voucherBill.getExternalDepartmentCode() : syncBatchPushBillF.getXZBM());
        tysrqrData.setZDR(empCode);
        tysrqrData.setDJRQ(voucherBill.getGmtCreate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        tysrqrData.setLYXT("CCCG-DMC");
        tysrqrData.setSFZY(1);
        tysrqrData.setYWLX(syncBatchPushBillF.getYWLX());
        BigDecimal divide = new BigDecimal(voucherBill.getTotalAmount()).divide(new BigDecimal("100"));
        tysrqrData.setSRQRJEBB(divide.doubleValue());
        tysrqrData.setSFQR("1");
        // tysrqrData.setJSFS("1");
        tysrqrData.setBWBID("156");
        tysrqrData.setDJZT("01");
        tysrqrData.setNSRLX("0");
        tysrqrData.setDJFJZS(voucherBill.getUploadNum());

        tysrqrData.setBZSY("计提应收" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))
                + "应收" + voucherBill.getCostCenterName());
        tysrqrData.setDJZY("收入确认");
        tysrqrData.setPZRQ(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));


        //SRQRMX（通用收入确认）说明
        List<SRQRMXData> srqrmxDatas = Lists.newArrayList();
        BILLDATA billdata = new BILLDATA();
        billdata.setCode("SRQRMX");
        billdata.setIsMain("false");

        //KJMX(款项明细)说明
        List<KJMXData> kjmxDatas = Lists.newArrayList();
        BILLDATA billKJMXdata = new BILLDATA();
        billKJMXdata.setCode("KJMX");
        billKJMXdata.setIsMain("false");
        List<VoucherPushBillDetailZJ> pushBillDetailZJS = voucherBillDetailRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                .eq(VoucherPushBillDetailZJ::getVoucherBillNo, voucherBill.getVoucherBillNo()));

        String SRQRMXNMNumber = IdentifierFactory.getInstance().serialNumber("pushbillZJ", "SRQRMXNM", 20);
        detailNumberS.add(SRQRMXNMNumber);

        String KJMXNumber = IdentifierFactory.getInstance().serialNumber("pushbillZJ", "KJMX", 20);
        detailNumberS.add(KJMXNumber);

        List<String> payerIdList = pushBillDetailZJS.stream()
                .map(VoucherPushBillDetailZJ::getPayerId)
                .distinct()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        List<SupplierSimpleV> supplierSimpleList = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(payerIdList)){
            supplierSimpleList = orgClient.simpleListSupplier(payerIdList);
        }
        Map<String, SupplierSimpleV> supplierIdMap = supplierSimpleList.stream()
                .collect(Collectors.toMap(SupplierSimpleV::getId, e -> e));
//        Map<String, List<VoucherPushBillDetailZJ>> subjectExtIdMap = pushBillDetailZJS.stream().collect(Collectors.groupingBy(VoucherPushBillDetailZJ::getSubjectExtId));
        Map<String, List<VoucherPushBillDetailZJ>> groupedDetailMap = pushBillDetailZJS.stream().collect(Collectors.groupingBy(VoucherPushBillDetailZJ::groupForPushKxmx));
        for (String s : groupedDetailMap.keySet()) {
            //KJMX(款项明细)说明
            VoucherPushBillDetailZJ first = groupedDetailMap.get(s).get(0);
            KJMXData kjmxData = new KJMXData();
            kjmxData.setKJMXNM(KJMXNumber);
            kjmxData.setZBNM(voucherBill.getVoucherBillNo());
            kjmxData.setBD(first.getChangeCode());
            kjmxData.setKXID(first.getSubjectExtId());
            kjmxData.setXMID(projectId);
            if (new BigDecimal("0.05").compareTo(first.getTaxRate()) < 0) {
                kjmxData.setJSFS("1");
            } else {
                kjmxData.setJSFS("2");
            }
            Integer payerType = first.getPayerType();
            if (Objects.isNull(payerType) || payerType != 99){
                kjmxData.setWLDWID("BP01505472");
            } else {
                SupplierSimpleV simpleV = supplierIdMap.get(first.getPayerId());
                if (Objects.nonNull(simpleV)){
                    kjmxData.setWLDWID(simpleV.getMainDataCode());
                }
                //兼容数据判断，因历史数据为BP开头导致数据查询为空，传送财务云时为空
                else if (first.getPayerId().startsWith("BP")){
                    kjmxData.setWLDWID(first.getPayerId());
                }
            }

            kjmxData.setYBID("156");
            kjmxData.setHL(1.000000);
            Long collect = groupedDetailMap.get(s).stream().collect(Collectors.summingLong(VoucherPushBillDetailZJ::getTaxIncludAmount));
            kjmxData.setKJJEYB(new BigDecimal(String.valueOf(collect)).divide(new BigDecimal("100")).doubleValue());
            kjmxData.setKJJEBB(new BigDecimal(String.valueOf(collect)).divide(new BigDecimal("100")).doubleValue());
            kjmxData.setHTID("9999999999");

            // 变动03 负数 自动匹配应收应付
            if (StringUtils.equals(first.getChangeCode(), "03") && collect < 0){
                LambdaQueryWrapper<Mdm63E> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Mdm63E::getContractCode, "9999999999");
                queryWrapper.eq(Mdm63E::getPartnerId, kjmxData.getWLDWID());
                queryWrapper.eq(Mdm63E::getFundsPropId, kjmxData.getKXID());
                queryWrapper.eq(Mdm63E::getArapModule, "AR");
                queryWrapper.ge(Mdm63E::getDhxJe, -collect);
                queryWrapper.last("limit 1");
                List<Mdm63E> mdm63EList = mdm63Mapper.selectList(queryWrapper);
                if (CollectionUtils.isEmpty(mdm63EList)){
                    throw new OwlBizException("未找到匹配的应收应付");
                }
                kjmxData.setYSYFID(mdm63EList.get(0).getFtId());
            }
            LocalDateTime gmtCreate = voucherBill.getGmtCreate();
            kjmxData.setDQRQ(gmtCreate.withYear(gmtCreate.getYear() + 1).with(TemporalAdjusters.lastDayOfMonth()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//            kjmxData.setDQRQ(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            kjmxDatas.add(kjmxData);
        }


        if (CollectionUtils.isNotEmpty(pushBillDetailZJS) && (SysSourceEnum.收费系统.getCode() == pushBillDetailZJS.get(0).getSysSource()
                || SysSourceEnum.工单系统.getCode() == pushBillDetailZJS.get(0).getSysSource())) {
            tysrqrData.setHTBH("9999999999");
            Map<BigDecimal, List<VoucherPushBillDetailZJ>> bigDecimalListMap = taxDetailListGroup(pushBillDetailZJS);
            String taxTypeId = "62f62cde-1396-68be-ac2b-083a01b170bf";
            Set<BigDecimal> taxRateList = bigDecimalListMap.keySet();
            List<FinancialTaxInfo> taxInfoList = financialTaxInfoMapper.getListByTaxRateAndTaxTypeId(taxRateList, taxTypeId);
            Map<String, String> rateToIdMap = taxInfoList.stream()
                    .collect(Collectors.toMap(e -> e.getApplicableTaxRate().stripTrailingZeros().toPlainString(), FinancialTaxInfo::getId));
            for (BigDecimal taxrate : bigDecimalListMap.keySet()) {
                List<VoucherPushBillDetailZJ> voucherPushBillDetailZJS = bigDecimalListMap.get(taxrate);
                // 内部再按照收费对象类型分组
                Map<String, List<VoucherPushBillDetailZJ>> payerTypeMap = voucherPushBillDetailZJS.stream().collect(Collectors.groupingBy(VoucherPushBillDetailZJ::groupForPushSrmx));
                for (String payerTypeStr : payerTypeMap.keySet()) {
                    List<VoucherPushBillDetailZJ> payerTypeDetails = payerTypeMap.get(payerTypeStr);
                    VoucherPushBillDetailZJ first = payerTypeDetails.get(0);
                    SRQRMXData srqrmxData = new SRQRMXData();
                    // 这个地方虽然是税率循环， 但是计税方式只有一个类别， 要么简易， 要么一般
                    // 临时方案，时间来不及
                    // 按照税率对计税方式赋值
                    if (new BigDecimal("0.05").compareTo(taxrate) < 0) {
                        tysrqrData.setJSFS("1");
                        srqrmxData.setJSFS("1");
                    } else {
                        tysrqrData.setJSFS("2");
                        srqrmxData.setJSFS("2");
                    }
                    //SRQRMX（通用收入确认）说明
                    srqrmxData.setSRQRMXNM(SRQRMXNMNumber);
                    srqrmxData.setZBNM(voucherBill.getVoucherBillNo());
                    srqrmxData.setXMMC(projectId);
                    if (Objects.isNull(first.getPayerType()) || first.getPayerType() != 99){
                        srqrmxData.setWLDW("BP01505472");
                    } else {
                        srqrmxData.setWLDW(first.getPayerId());
                    }
                    srqrmxData.setBZID("156");
                    srqrmxData.setHL(1.000000);
                    // srqrmxData.setJSFS("1");
                    srqrmxData.setHTBH("9999999999");

                    String financialTaxId = rateToIdMap.get(taxrate.stripTrailingZeros().toPlainString());
                    if (StringUtils.isBlank(financialTaxId)){
                        throw new OwlBizException("税率"+taxrate+"未维护对应增值税id,请联系系统管理员");
                    }
                    srqrmxData.setSL(financialTaxId);
                    long taxIncludAmountSum = payerTypeDetails.stream().mapToLong(VoucherPushBillDetailZJ::getTaxIncludAmount).sum();

                    srqrmxData.setHSJEYB(new BigDecimal(taxIncludAmountSum).divide(new BigDecimal("100")).doubleValue());
                    srqrmxData.setHSJEBB(new BigDecimal(taxIncludAmountSum).divide(new BigDecimal("100")).doubleValue());

                    BigDecimal add = new BigDecimal("1").add(taxrate);
                    // 含税金额 除 1 + 税额   得到不含税金额
                    BigDecimal bigDecimal = new BigDecimal(taxIncludAmountSum).divide(add, 2,RoundingMode.HALF_UP);
                    // 税额
                    BigDecimal subtract = new BigDecimal(taxIncludAmountSum).subtract(bigDecimal);
                    //税额
                    srqrmxData.setSEYB(subtract.divide(new BigDecimal("100"),2,RoundingMode.HALF_UP).doubleValue());
                    srqrmxData.setSEBB(subtract.divide(new BigDecimal("100"),2,RoundingMode.HALF_UP).doubleValue());
                    //不含税金额
                    srqrmxData.setBHSYB(bigDecimal.divide(new BigDecimal("100"),2,RoundingMode.HALF_UP).doubleValue());
                    srqrmxData.setBHSBB(bigDecimal.divide(new BigDecimal("100"),2,RoundingMode.HALF_UP).doubleValue());
                    srqrmxDatas.add(srqrmxData);
                }
            }
        }
        // 理论上不应该走到这里，不改，走到这里了就是系统有bug
        if (CollectionUtils.isNotEmpty(pushBillDetailZJS) && SysSourceEnum.合同系统.getCode() == pushBillDetailZJS.get(0).getSysSource()) {
            Map<String, List<VoucherPushBillDetailZJ>> sourceGroup = this.sysSourceGroup(pushBillDetailZJS);
            List<String> contractIdS = Lists.newArrayList(sourceGroup.keySet());
            List<ContractV> infoListByIds = contractClient.getInfoListByIds(contractIdS);
            for (ContractV infoListById : infoListByIds) {
                SRQRMXData srqrmxData = new SRQRMXData();
                srqrmxData.setSRQRMXNM(SRQRMXNMNumber);
                srqrmxData.setZBNM(voucherBill.getVoucherBillNo());
                srqrmxData.setXMMC(projectId);
                srqrmxData.setWLDW("BP01505472");
                srqrmxData.setBZID("156");
                srqrmxData.setHL(1.000000);
                srqrmxData.setJSFS("1");
                List<VoucherPushBillDetailZJ> voucherPushBillDetailZJS = sourceGroup.get(infoListById.getId());
                BigDecimal taxRate = voucherPushBillDetailZJS.get(0).getTaxRate();
                Long taxIncludTotalAmount = voucherPushBillDetailZJS.stream().mapToLong(VoucherPushBillDetailZJ::getTaxIncludAmount).sum();
                String taxTypeId = "62f62cde-1396-68be-ac2b-083a01b170bf";
                List<FinancialTaxInfo> taxInfoList = financialTaxInfoMapper.getListByTaxRateAndTaxTypeId(Lists.newArrayList(taxRate), taxTypeId);
                Map<String, String> rateToIdMap = taxInfoList.stream()
                        .collect(Collectors.toMap(e -> e.getApplicableTaxRate().stripTrailingZeros().toPlainString(), FinancialTaxInfo::getId));
                srqrmxData.setSL(rateToIdMap.get(taxRate.stripTrailingZeros().toPlainString()));
                this.countAmount(taxRate, srqrmxData, null, taxIncludTotalAmount);
                // todo
                // 合同部分， 等对接合同在修改吧    需要按照系统来源、 税率、 项目名称分组。
                // this.countAmount(taxRate, null, kjmxData, taxIncludTotalAmount);
                // kjmxData.setHTID(infoListById.getFromid());
                tysrqrData.setHTBH(infoListById.getFromid());
                srqrmxData.setHTBH(infoListById.getFromid());
                srqrmxDatas.add(srqrmxData);

            }
        }
        List<YXXXData> yxxxDataList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(voucherBill.getUploadLink())) {
            for (UploadLinkZJ uploadLinkZJ : voucherBill.getUploadLink()) {
                //影像信息
                YXXXData yxxxData = new YXXXData();
                yxxxData.setYXBM(uploadLinkZJ.getImageIdZJ());
                yxxxDataList.add(yxxxData);
            }
        }
        BILLDATA billYXXXdata = new BILLDATA();
        billYXXXdata.setCode("YXXX");
        billYXXXdata.setIsMain("false");
        billYXXXdata.setData(yxxxDataList);

        tysrqrDataList.add(tysrqrData);
        zjData.setData(tysrqrDataList);

        billdata.setData(srqrmxDatas);
        billKJMXdata.setData(kjmxDatas);

        // 节点添加审批日志信息
        BILLDATA SPRZDataBillData = new BILLDATA();
        SPRZDataBillData.setCode("SPRZ");
        SPRZDataBillData.setIsMain("false");
        SPRZDataBillData.setData(SPRZDataList);

        ArrayList<BILLDATA> billdatas = Lists.newArrayList();
        billdatas.add(billKJMXdata);
        billdatas.add(billdata);
        billdatas.add(zjData);
        billdatas.add(billYXXXdata);
        zjDatas.setBILLDATA(billdatas);
        if (CollectionUtils.isNotEmpty(SPRZDataList)) {
            billdatas.add(SPRZDataBillData);
        }
        billdatasList.add(zjDatas);
        zjParameter.setPsData(billdatasList);
        ObjectMapper objectMapper = new ObjectMapper();
        String billdataString = objectMapper.writeValueAsString(zjParameter).replace("\"", "'");

        log.info("中交组装参数 {}", billdataString);
        String replace = "---";
        zjRequestBody.setContext("---");
        String zjRequestBodyString = JSON.toJSONString(zjRequestBody).replace(replace, billdataString);
        log.info("中交推单参数 {}", zjRequestBodyString);
        return zjRequestBodyString;
    }

    private String zjSRQRRequestBodyForPayIncome(String projectId, ZJTriggerEventBillTypeEnum billTypeEnum,
                                                 VoucherBillDxZJ voucherBill, SyncBatchPushZJBillF syncBatchPushBillF,
                                                 String empCode, List<String> detailNumberS, List<SPRZData> SPRZDataList,
                                                 List<DxPaymentDetails> pushBillDetailZJS,
                                                 List<GeneralDetails> generalRevenueRecognition,
                                                 Integer billEventType, List<String> ftIdList) throws JsonProcessingException {
        ZJRequestBody zjRequestBody = new ZJRequestBody();
        //appInstanceCode你们默认10000     unitCode默认MDM   sourceSystem默认CCCG-DMC
        ZJParameter zjParameter = new ZJParameter();
        zjParameter.setBusinessCode(DocumentTypeEnum.valueOfByCode(voucherBill.getBillEventType()).getValue());
        zjParameter.setUnitCode("MDM");
        zjParameter.setSourceSystem("CCCG-DMC");
        zjParameter.setAppInstanceCode("10000");
        List<BILLDATAS> billdatasList = Lists.newArrayList();
        BILLDATAS zjDatas = new BILLDATAS();
        BILLDATA zjData = new BILLDATA();
        zjData.setCode(DocumentTypeEnum.valueOfByCode(voucherBill.getBillEventType()).getValue());
        zjData.setIsMain("true");

        String SRQRMXNMNumber = IdentifierFactory.getInstance().serialNumber("pushbillZJ", "SRQRMXNM", 20);

        List<TYSRQRData> tysrqrDataList = Lists.newArrayList();
        //SRQRMX（通用收入确认）说明
        List<SRQRMXData> srqrmxDatas = Lists.newArrayList();
        BILLDATA billdata = new BILLDATA();


        Set<BigDecimal> taxRateList = new HashSet<>();
        generalRevenueRecognition.forEach(generalDetail -> {
            taxRateList.add(generalDetail.getTaxRate());
        });
        // todo 暂时写死税种id=增值税 后面不知道是否需要调整
        String taxTypeId = "62f62cde-1396-68be-ac2b-083a01b170bf";
        List<FinancialTaxInfo> taxInfoList = financialTaxInfoMapper.getListByTaxRateAndTaxTypeId(taxRateList, taxTypeId);
        Map<String, String> rateToIdMap = taxInfoList.stream()
                .collect(Collectors.toMap(e -> e.getApplicableTaxRate().stripTrailingZeros().toPlainString(), FinancialTaxInfo::getId,(v1,v2)->v2));
        //核算组织名称
        String organizationName;
        Integer taxpayerType = null;
        List<VoucherPushBillDetailDxZJ> byVoucherBillNo = voucherBillDetailDxZJRepository.getByVoucherBillNoNoSearch(voucherBill.getVoucherBillNo());
        if(CollectionUtils.isNotEmpty(byVoucherBillNo)){
            List<CfgExternalDataV> community = configClient.getExternalMapByCode("community", byVoucherBillNo.get(0).getCommunityId());
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
            TemporaryChargeBillDetailV billInfo = temporaryChargeBillAppService.getById(byVoucherBillNo.get(0).getBillId(), TemporaryChargeBillDetailV.class, byVoucherBillNo.get(0).getCommunityId());
            OrgFinanceRv orgFinanceById = orgClient.getOrgFinanceById(billInfo.getStatutoryBodyId());
            log.info("根据法定单位id：{}，查询对应法定单位信息：{}", billInfo.getStatutoryBodyId(), orgFinanceById);

            if(Objects.nonNull(orgFinanceById)){
                taxpayerType = orgFinanceById.getTaxpayerType();
            }
        } else {
            organizationName = null;
        }
        //推送财务云数据重复，提取一条
        TYSRQRData tysrqrData = new TYSRQRData();

        Integer finalTaxpayerType = taxpayerType;
        generalRevenueRecognition.forEach(generalDetail -> {
            // TYSRQR（通用收入确认）说明

            tysrqrData.setDJNM(voucherBill.getVoucherBillNo());
            tysrqrData.setDJBH(voucherBill.getVoucherBillNo());
            //核算组织编号
            tysrqrData.setHSDW(organizationName);
            tysrqrData.setXZZZ(syncBatchPushBillF.getXZZZ());
            tysrqrData.setXZBM(StringUtils.isNotEmpty(voucherBill.getExternalDepartmentCode()) ? voucherBill.getExternalDepartmentCode() : syncBatchPushBillF.getXZBM());
            tysrqrData.setZDR(empCode);
            tysrqrData.setDJRQ(voucherBill.getGmtCreate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            tysrqrData.setLYXT("CCCG-DMC");
            tysrqrData.setSFZY(1);
            tysrqrData.setYWLX(voucherBill.getBusinessType().trim());
            //test
            //tysrqrData.setYWLX("D5774BBC73034493B2FFC12B95CD931F");

            BigDecimal divide = voucherBill.getTotalAmount().divide(new BigDecimal("100"));
            tysrqrData.setSRQRJEBB(divide.doubleValue());
            tysrqrData.setSFQR("1");
            //new 计税方式
            tysrqrData.setJSFS(generalDetail.getTaxType());
            tysrqrData.setBWBID("156");
            tysrqrData.setDJZT("01");
            tysrqrData.setNSRLX("0");
            tysrqrData.setDJFJZS(voucherBill.getUploadNum());
            //业务事由
            tysrqrData.setBZSY(StringUtils.isNotBlank(voucherBill.getReceiptRemark()) ? voucherBill.getReceiptRemark() : this.getReceiptRemark(Arrays.asList(voucherBill.getVoucherBillNo())).get(voucherBill.getVoucherBillNo()));
            tysrqrData.setDJZY(billTypeEnum.getValue());
            tysrqrData.setPZRQ(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            String financialTaxId = rateToIdMap.get(generalDetail.getTaxRate().stripTrailingZeros().toPlainString());
            //tysrqrData.setSL(financialTaxId);

            //通用收入确认明细
            SRQRMXData srqrmxData = new SRQRMXData();
            // 计量明细内码
            srqrmxData.setSRQRMXNM(SRQRMXNMNumber);
            srqrmxData.setZBNM(voucherBill.getVoucherBillNo());
            srqrmxData.setXMMC(projectId);
            srqrmxData.setHTBH(generalDetail.getConMainCode());
            //test
            //srqrmxData.setHTBH("9999999999");
            //new 往来单位主数据编号，BP开头
            srqrmxData.setWLDW(generalDetail.getExchangeUnitMainCode());
            srqrmxData.setBZID("156");
            srqrmxData.setHL(pushBillDetailZJS.get(0).getExchangeRate().doubleValue());
            srqrmxData.setHSJEYB(generalDetail.getTaxIncludedAmount().doubleValue());
            srqrmxData.setHSJEBB(generalDetail.getTaxIncludedAmount().doubleValue());
            /*if (ZJTriggerEventBillTypeEnum.收入确认计提.getCode() == billEventType) {
                srqrmxData.setHSJEYB(generalDetail.getTaxExcludedAmount().doubleValue());
                srqrmxData.setHSJEBB(generalDetail.getTaxExcludedAmount().doubleValue());
            }*/

            if (StringUtils.isBlank(financialTaxId)){
                throw new OwlBizException("税率"+generalDetail.getTaxRate()+"未维护对应增值税id,请联系系统管理员");
            }
            srqrmxData.setSL(financialTaxId);

            srqrmxData.setSEYB(generalDetail.getTaxAmount().doubleValue());
            srqrmxData.setSEBB(generalDetail.getTaxAmount().doubleValue());
            srqrmxData.setBHSYB(generalDetail.getTaxExcludedAmount().doubleValue());
            srqrmxData.setBHSBB(generalDetail.getTaxExcludedAmount().doubleValue());
            //new 计税方式
            //srqrmxData.setJSFS(generalDetail.getTaxType());
            if(Objects.nonNull(voucherBill.getCalculationMethod())){
                srqrmxData.setJSFS(voucherBill.getCalculationMethod().toString());
            }else{
                srqrmxData.setJSFS(FinancialTaxTypeEnum.SIMPLE.getCode().toString());
                if(Objects.isNull(finalTaxpayerType) || TaxpayerTypeEnum.一般纳税人.getCode().equals(finalTaxpayerType)){
                    srqrmxData.setJSFS(FinancialTaxTypeEnum.GENERAL.getCode().toString());
                }
            }
            srqrmxDatas.add(srqrmxData);
        });


        tysrqrDataList.add(tysrqrData);

        billdata.setData(srqrmxDatas);
        billdata.setCode("SRQRMX");
        billdata.setIsMain("false");

        //KJMX(款项明细)说明
        List<KJMXData> kjmxDatas = Lists.newArrayList();
        BILLDATA billKJMXdata = new BILLDATA();
        billKJMXdata.setCode("KJMX");
        billKJMXdata.setIsMain("false");



        detailNumberS.add(SRQRMXNMNumber);

        String KJMXNumber = IdentifierFactory.getInstance().serialNumber("pushbillZJ", "KJMX", 20);
        detailNumberS.add(KJMXNumber);
        //Map<String, List<VoucherPushBillDetailDxZJ>> subjectExtIdMap = pushBillDetailZJS.stream().collect(Collectors.groupingBy(VoucherPushBillDetailDxZJ::getSubjectExtId));
        for (DxPaymentDetails dxPaymentDetail : pushBillDetailZJS) {
            //KJMX(款项明细)说明
            KJMXData kjmxData = new KJMXData();
            kjmxData.setKJMXNM(KJMXNumber);
            kjmxData.setZBNM(voucherBill.getVoucherBillNo());
            //new 变动
            kjmxData.setBD(dxPaymentDetail.getChangeCode());
            //kjmxData.setBD(dxPaymentDetail.getChangeName());
            //new 款项名称
            //kjmxData.setKXID(dxPaymentDetail.getCorrespondingPaymentName());
            kjmxData.setKXID(dxPaymentDetail.getCorrespondingPaymentId());
            //new 项目id
            kjmxData.setXMID(projectId);
            //new new 计税方式
            kjmxData.setJSFS(dxPaymentDetail.getTaxType());
            //new 支持不适用合同，主数据编码，BP开头
            kjmxData.setWLDWID(dxPaymentDetail.getExchangeUnitMainCode());
            //new 往来单位主数据编号，BP开头
            kjmxData.setWLDW(dxPaymentDetail.getExchangeUnitMainCode());
            //new 到期日期
            //kjmxData.setDQRQ(convertDate(dxPaymentDetail.getMaturityDate()));
            //逻辑替换，到账日期使用报账单创建时间加周期
            kjmxData.setDQRQ(DateTimeUtil.getYearMonthDay(voucherBill.getGmtCreate().plusYears(1).minusDays(1)));

            kjmxData.setYBID("156");
            //new 预计收付款日期
            kjmxData.setYJSKRQ(convertDate(dxPaymentDetail.getExpectedPayDate()));
            //new 金额（原币）
            kjmxData.setKJJEYB(null != dxPaymentDetail.getTaxIncludedAmount() ? dxPaymentDetail.getTaxIncludedAmount().doubleValue() : null);
            //new 金额（本位币）
            kjmxData.setKJJEBB(null != dxPaymentDetail.getTaxIncludedAmount() ? dxPaymentDetail.getTaxIncludedAmount().doubleValue() : null);
            //new 合同id
            kjmxData.setHTID(dxPaymentDetail.getConMainCode());
            //test
            //kjmxData.setHTID("9999999999");
            //new 汇率
            kjmxData.setHL(dxPaymentDetail.getExchangeRate().doubleValue());
            //new 未核销金额（原币）
            kjmxData.setWHXJEYB(dxPaymentDetail.getNotSettlementAmount().doubleValue());
/*            if (ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode() == billEventType) {
                MDM17V mdm17V = externalClient.queryByCodeForMDM17(dxPaymentDetail.getPaymentId());
                List<Mdm63E> mdm63EList = mdm63Mapper.selectByCondition(dxPaymentDetail.getConMainCode(),mdm17V.getIdExt());
                if (CollectionUtils.isNotEmpty(mdm63EList)) {
                    List<String> ftIdsList = writeOffAndReturnFtIds(mdm63EList,dxPaymentDetail.getNotSettlementAmount());
                    //TODO 接口不支持多笔核销
                    kjmxData.setYSYFID(dxPaymentDetail.getNumberOfPayableReceivable());
                }
            }*/
            kjmxData.setYSYFID(dxPaymentDetail.getFtId());
            if (StringUtils.isNotBlank(dxPaymentDetail.getFtId())){
                ftIdList.add(dxPaymentDetail.getFtId());
            }
            kjmxDatas.add(kjmxData);

        }
        List<YXXXData> yxxxDataList = Lists.newArrayList();

        if (CollectionUtils.isNotEmpty(voucherBill.getUploadLink())) {
            for (UploadLinkZJ uploadLinkZJ : voucherBill.getUploadLink()) {
                //影像信息
                YXXXData yxxxData = new YXXXData();
                yxxxData.setYXBM(uploadLinkZJ.getImageIdZJ());
                yxxxDataList.add(yxxxData);
            }
        }
        BILLDATA billYXXXdata = new BILLDATA();
        billYXXXdata.setCode("YXXX");
        billYXXXdata.setIsMain("false");
        billYXXXdata.setData(yxxxDataList);


        zjData.setData(tysrqrDataList);


        billKJMXdata.setData(kjmxDatas);

        // 节点添加审批日志信息
        BILLDATA SPRZDataBillData = new BILLDATA();
        SPRZDataBillData.setCode("SPRZ");
        SPRZDataBillData.setIsMain("false");
        SPRZDataBillData.setData(SPRZDataList);

        ArrayList<BILLDATA> billdatas = Lists.newArrayList();
        billdatas.add(billKJMXdata);
        billdatas.add(billdata);
        billdatas.add(zjData);
        //TODO
        billdatas.add(billYXXXdata);
        zjDatas.setBILLDATA(billdatas);
        if (CollectionUtils.isNotEmpty(SPRZDataList)) {
            billdatas.add(SPRZDataBillData);
        }
        billdatasList.add(zjDatas);
        zjParameter.setPsData(billdatasList);
        ObjectMapper objectMapper = new ObjectMapper();
        String billdataString = objectMapper.writeValueAsString(zjParameter).replace("\"", "'");

        log.info("income_pay_push_param 中交组装参数 {}", billdataString);
        String replace = "---";
        zjRequestBody.setContext("---");
        String zjRequestBodyString = JSON.toJSONString(zjRequestBody).replace(replace, billdataString);
        log.info("income_pay_push_param_json中交推单参数 {}", zjRequestBodyString);
        return zjRequestBodyString;
    }

    /**
     *
     * @param mdm63EList         应收应付明细列表
     * @param notSettlementAmount 本次需要核销的金额（待核销金额）
     * @return 已核销行的ftId列表
     */
    private List<String> writeOffAndReturnFtIds(List<Mdm63E> mdm63EList, BigDecimal notSettlementAmount) {
        if (mdm63EList == null
                || mdm63EList.isEmpty()
                || notSettlementAmount == null
                || notSettlementAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return Collections.emptyList();
        }

        List<String> writtenOffFtIds = new ArrayList<>();
        mdm63EList.sort(Comparator.comparing(Mdm63E::getBizDate));
        for (Mdm63E detail : mdm63EList) {
            if (notSettlementAmount.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
            BigDecimal currentDhxJe = detail.getDhxJe();
            if (currentDhxJe == null || currentDhxJe.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            if (notSettlementAmount.compareTo(currentDhxJe) >= 0) {
                writtenOffFtIds.add(detail.getFtId());
                notSettlementAmount = notSettlementAmount.subtract(currentDhxJe);
            } else {
                writtenOffFtIds.add(detail.getFtId());
                notSettlementAmount = BigDecimal.ZERO;
            }
        }

        return writtenOffFtIds;
    }



    private String convertDate(Date target) {
        if (null == target) {
            return null;
        }
        Instant instant = target.toInstant();
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(formatter);
    }

    private String zjZFSQRequestBody(VoucherBillZJ voucherBill, String projectId, SyncBatchPushZJBillF syncBatchPushBillF,
                                     String empCode, List<String> detailNumberS) throws JsonProcessingException {
        return null;
    }
    private String zjZJSKRequestBody(VoucherBillZJ voucherBill, String projectId, SyncBatchPushZJBillF syncBatchPushBillF,
                                     String empCode, List<String> detailNumberS, List<SPRZData> SPRZDataList ) throws JsonProcessingException {
        PageF<SearchF<?>> form = new PageF<SearchF<?>>();
        form.setConditions(new SearchF<>());
        SearchF<?> conditions = form.getConditions();
        conditions.setFields(new ArrayList<>());
        conditions.getFields().add(new Field("voucher_bill_no", voucherBill.getVoucherBillNo(), 1));
        form.setPageNum(1);
        form.setPageSize(100);
        ZJRequestBody zjRequestBody = new ZJRequestBody();
        ZJParameter zjParameter = new ZJParameter();
        zjParameter.setBusinessCode(DocumentTypeEnum.valueOfByCode(voucherBill.getBillEventType()).getValue());
        zjParameter.setUnitCode("MDM");
        zjParameter.setSourceSystem("CCCG-DMC");
        zjParameter.setAppInstanceCode("10000");
        // 封装参数

        List<BILLDATAS> billdatasList = Lists.newArrayList();
        BILLDATAS zjDatas = new BILLDATAS();
        BILLDATA zjZJSKBillData = new BILLDATA();
        zjZJSKBillData.setCode(DocumentTypeEnum.valueOfByCode(voucherBill.getBillEventType()).getValue());
        zjZJSKBillData.setIsMain("true");
        // ZJSK（资金收款）说明
        List<ZJSKData> zjskDataList = Lists.newArrayList();
        ZJSKData zjskData = new ZJSKData();
        zjskData.setDJNM(voucherBill.getVoucherBillNo());
        zjskData.setDJBH(voucherBill.getVoucherBillNo());
        zjskData.setXZZZ(syncBatchPushBillF.getXZZZ());
        zjskData.setXZBM(StringUtils.isNotEmpty(voucherBill.getExternalDepartmentCode()) ? voucherBill.getExternalDepartmentCode() : syncBatchPushBillF.getXZBM());
        zjskData.setBWBBZ("156");
        zjskData.setHSBM("");
        zjskData.setHSDW("");
        zjskData.setFKZHNB("");
        zjskData.setNWZBZ("");
        zjskData.setFKZH("");
        zjskData.setBZ("");
        zjskData.setZDR(empCode);
        zjskData.setDJRQ(voucherBill.getGmtCreate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        zjskData.setLYXT("CCCG-DMC");
        // 默认为001006（服务类业务）
        zjskData.setYWLX("D5774BBC73034493B2FFC12B95CD931F");
        // 写死
        zjskData.setSJFKR("BP01505472");
        // 收款銀行账户
        String recordIdListString = voucherBill.getRecordIdList();
        List<Long> recordIds = JSON.parseArray(recordIdListString, Long.class);
        List<FlowClaimRecordE> flowClaimRecordES = flowClaimRecordRepository.listByIds(recordIds);
        // 2024-10需求 根据认领流水记录的收款方式来区分 线下[604e3a92-38fa-11eb-a48c-db86e1d51865]=现金
        // 其他[14f4ff68-a1ce-679c-c5f9-8fe20e598000]=网银付款
        zjskData.setSKFS("14f4ff68-a1ce-679c-c5f9-8fe20e598000");
        FlowClaimRecordE recordE = flowClaimRecordES.get(0);
        if (Objects.nonNull(recordE.getPayChannelType()) && recordE.getPayChannelType() == 0){
            zjskData.setSKFS("604e3a92-38fa-11eb-a48c-db86e1d51865");
        }

        zjskData.setSKYHZH(recordE.getOurAccount());
        zjskData.setYBBZ("156");
        zjskData.setHL(1.000000);
        zjskData.setFJZS(voucherBill.getUploadNum());
        zjskData.setYWSY("资金收款:" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")) + voucherBill.getCostCenterName());
        // 2024-10-30 推送备注修改 by @qd_shenbaocun1
        if (StringUtils.isNotBlank(voucherBill.getReceiptRemark())){
            zjskData.setYWSY("资金收款:" + voucherBill.getReceiptRemark());
        }
        zjskData.setPZRQ(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        UserInfoRv userInfo = userClient.getUserInfo(recordE.getCreator(), null);
        zjskData.setRLR(externalClient.getIphoneByEmpCode(userInfo.getMobileNum()));
        zjskData.setSKJEBB(Double.valueOf(String.valueOf(BigDecimal.valueOf(voucherBill.getTotalAmount()).divide(new BigDecimal("100")).setScale(2))));
        zjskData.setSKJEYB(Double.valueOf(String.valueOf(BigDecimal.valueOf(voucherBill.getTotalAmount()).divide(new BigDecimal("100")).setScale(2))));
        zjskData.setSKRQ(voucherBill.getGmtCreate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        zjskDataList.add(zjskData);
        zjZJSKBillData.setData(zjskDataList);


        // SKMX2（应收款明细）说明
        BILLDATA SKMX2DataBillData = new BILLDATA();
        SKMX2DataBillData.setCode("SKMX2");
        SKMX2DataBillData.setIsMain("false");
        // 2025-03-13 切换为页面交互一致的(页面交互已修正逻辑)
        List<VoucherBillZJRecDetailV2> recDetailV2List = voucherBillDetailAppService.queryRecDetailPageV2(voucherBill.getVoucherBillNo());
        // 获取mdm63 数据
        List<String> ftIdList = recDetailV2List.stream().map(v -> v.getFtId()).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        List<Mdm63E> mdm63List = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(ftIdList)){
            mdm63List = mdm63Mapper.selectBatchIds(ftIdList);
        }
        Map<String, BigDecimal> ftAmountMap = mdm63List.stream()
                .collect(Collectors.toMap(Mdm63E::getFtId, Mdm63E::getDhxJe, (v1, v2) -> v1));
        List<SKMX2Data> SKMX2DataList = recDetailV2List.stream().map(VoucherBillZJRecDetailV2::transfer).collect(Collectors.toList());
        for (SKMX2Data skmx2Data : SKMX2DataList) {
            if (StringUtils.isNotBlank(skmx2Data.getHXYSBH())){
                skmx2Data.setWHXJEYB(ftAmountMap.get(skmx2Data.getHXYSBH()).setScale(2, RoundingMode.HALF_UP).toString());
            }
        }
        SKMX2DataBillData.setData(SKMX2DataList);

        // SKMX3（认领明细）说明
        BILLDATA SKMX3DataBillData = new BILLDATA();
        SKMX3DataBillData.setCode("SKMX3");
        SKMX3DataBillData.setIsMain("false");
        List<SKMX3Data> SKMX3DataList = Lists.newArrayList();

        List<FlowClaimDetailE> flowClaimDetailES = flowClaimDetailRepository.queryByFlowClaimRecordId(recordIds);
        List<Long> collect = flowClaimDetailES.stream().map(FlowClaimDetailE::getFlowId).collect(Collectors.toList());
        List<FlowDetailE> flowDetailES = flowDetailRepository.listByIds(collect);
        long sum = flowDetailES.stream().mapToLong(FlowDetailE::getSettleAmount).sum();
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        for (FlowDetailE flowDetailE : flowDetailES) {
            stringBuilder.append(flowDetailE.getIdExt()).append(",");
            builder.append(BigDecimal.valueOf(flowDetailE.getSettleAmount()).divide(new BigDecimal("100")).setScale(2)).append(",");
        }
        StringBuilder delete = stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        StringBuilder jebb = builder.delete(builder.length() - 1, builder.length());
        SKMX3Data skmx3Data = new SKMX3Data();
        skmx3Data.setRLMXNM(flowDetailES.get(0).getSerialNumber());
        skmx3Data.setDJNM(voucherBill.getVoucherBillNo());
        skmx3Data.setRLTZID(delete.toString());
        skmx3Data.setRLBBJE(jebb.toString());
        skmx3Data.setRLJE(Double.valueOf(String.valueOf(BigDecimal.valueOf(sum).divide(new BigDecimal("100")).setScale(2))));
        SKMX3DataList.add(skmx3Data);
        SKMX3DataBillData.setData(SKMX3DataList);

        // SKXJLL（现金流量）
        BILLDATA SKXJLLDataBillData = new BILLDATA();
        SKXJLLDataBillData.setCode("SKXJLL");
        SKXJLLDataBillData.setIsMain("false");
        List<SKXJLLData> SKXJLLDataList = Lists.newArrayList();
        PageV<VoucherBillZJCashFlowV> voucherBillZJCashFlowVPageV = voucherBillDetailAppService.queryCashFlowPage(form);
        for (VoucherBillZJCashFlowV record : voucherBillZJCashFlowVPageV.getRecords()) {
            String KJMXNumber = IdentifierFactory.getInstance().serialNumber("pushbillZJ", "SKXJLL", 20);
            SKXJLLData skxjllData = new SKXJLLData();
            skxjllData.setXJLLMXNM(KJMXNumber);
            skxjllData.setDJNM(voucherBill.getVoucherBillNo());
            // 现金流量id
            skxjllData.setXJLL(record.getCashFlowNameExtId());
            skxjllData.setYBBZ("156");
            skxjllData.setHL(1.000000);
            skxjllData.setJEBB(Double.valueOf(record.getAmount().toString()));
            skxjllData.setJEYB(Double.valueOf(record.getAmount().toString()));
            SKXJLLDataList.add(skxjllData);

        }
        SKXJLLDataBillData.setData(SKXJLLDataList);

        // 影像信息
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

        // SPRZ（审批日志）说明
        BILLDATA SPRZDataBillData = new BILLDATA();
        SPRZDataBillData.setCode("SPRZ");
        SPRZDataBillData.setIsMain("false");

        SPRZDataBillData.setData(SPRZDataList);




        ArrayList<BILLDATA> billdatas = Lists.newArrayList();
        billdatas.add(zjZJSKBillData);
        billdatas.add(SKMX2DataBillData);
        billdatas.add(SKMX3DataBillData);
        billdatas.add(SKXJLLDataBillData);
        billdatas.add(YXXXBillData);
        if (CollectionUtils.isNotEmpty(SPRZDataList)) {
            billdatas.add(SPRZDataBillData);
        }
        zjDatas.setBILLDATA(billdatas);
        billdatasList.add(zjDatas);
        zjParameter.setPsData(billdatasList);
        ObjectMapper objectMapper = new ObjectMapper();
        String billdataString = objectMapper.writeValueAsString(zjParameter).replace("\"", "'");
        String replace = "---";
        zjRequestBody.setContext("---");
        String zjRequestBodyString = JSON.toJSONString(zjRequestBody).replace(replace, billdataString);
        log.info("中交推单参数 {}", zjRequestBodyString);
        return zjRequestBodyString;
    }

    /** 对下结算请求体
     * @param voucherBill
     * @param projectId
     * @param syncBatchPushBillF
     * @param empCode
     * @param detailNumberS
     * @return
     */
    private String zjDXJSRequestBody(VoucherBillZJ voucherBill, String projectId, SyncBatchPushZJBillF syncBatchPushBillF, String empCode, List<String> detailNumberS) throws JsonProcessingException {
        ZJRequestBody zjRequestBody = new ZJRequestBody();
        ZJParameter zjParameter = new ZJParameter();
        zjParameter.setBusinessCode(DocumentTypeEnum.valueOfByCode(voucherBill.getBillEventType()).getValue());
        zjParameter.setUnitCode("MDM");
        zjParameter.setSourceSystem("CCCG-DMC");
        zjParameter.setAppInstanceCode("10000");
        // 封装参数
        List<BILLDATAS> billdatasList = Lists.newArrayList();
        BILLDATAS zjDatas = new BILLDATAS();
        BILLDATA zjDXJSBillData = new BILLDATA();
        zjDXJSBillData.setCode(DocumentTypeEnum.valueOfByCode(voucherBill.getBillEventType()).getValue());
        zjDXJSBillData.setIsMain("true");
        List<DXJSData> dxjsDataList = Lists.newArrayList();
        BILLDATA DXJS1DataBillData = new BILLDATA();
        BILLDATA DXJS2DataBillData = new BILLDATA();
        BILLDATA DXJS3DataBillData = new BILLDATA();
        BILLDATA DXJS4DataBillData = new BILLDATA();

        // 根据报账单id查询
        VoucherContractInvoiceZJ contractInvoiceZJ = contractInvoiceZJRepository.getOne(Wrappers.<VoucherContractInvoiceZJ>lambdaQuery().eq(VoucherContractInvoiceZJ::getVoucherBillId, voucherBill.getId()).last("limit 1"));
        log.info("对下结算-获取VoucherContractInvoiceZJ：{}",JSONObject.toJSONString(contractInvoiceZJ));
        if (contractInvoiceZJ != null) {
            List<VoucherContractMeasurementDetailZJ> measureDetailList = measurementDetailZJRepository.list(Wrappers.<VoucherContractMeasurementDetailZJ>lambdaQuery().eq(VoucherContractMeasurementDetailZJ::getVoucherBillId, voucherBill.getId()));
            log.info("对下结算-获取计量明细：{}",JSONObject.toJSONString(measureDetailList));

            List<VoucherInvoiceZJ> invoiceZJS = invoiceZJRepository.list(Wrappers.<VoucherInvoiceZJ>lambdaQuery().eq(VoucherInvoiceZJ::getVoucherBillId, voucherBill.getId()));
            log.info("对下结算-获取发票明细：{}",JSONObject.toJSONString(invoiceZJS));
            // DXJS（对下结算）说明
            buildDXJSData(voucherBill, projectId, syncBatchPushBillF, empCode, contractInvoiceZJ, dxjsDataList,measureDetailList);
            if (CollectionUtils.isNotEmpty(measureDetailList)) {

                // DXJJJLMX（计量明细）

                DXJS1DataBillData.setCode("DXJJJLMX");
                DXJS1DataBillData.setIsMain("false");
                List<DXJJJLMXData> JLMX2DataList = Lists.newArrayList();

                // DXJJKJMX(扣减明细/款项明细)说明
                DXJS2DataBillData.setCode("DXJJKJMX");
                DXJS2DataBillData.setIsMain("false");
                List<DXJJKJMXData> KJMX2DataList = Lists.newArrayList();

                // DXJJCBMX（成本明细）说明
                DXJS3DataBillData.setCode("DXJJCBMX");
                DXJS3DataBillData.setIsMain("false");
                List<DXJJCBMXData> CBMX2DataList = Lists.newArrayList();
                // DXJJINVOICE（发票明细）说明，业务系统收到发票的话需要传递数据
                DXJS4DataBillData.setCode("DXJJINVOICE");
                DXJS4DataBillData.setIsMain("false");
                List<DXJJINVOICEData> INVOICE2DataList = Lists.newArrayList();
                // DXJJINVOICE（发票明细）说明，业务系统收到发票的话需要传递数据
                // 查询报账明细
                List<VoucherPushBillDetailZJ> pushBillDetailZJS = voucherBillDetailRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                        .eq(VoucherPushBillDetailZJ::getVoucherBillNo, voucherBill.getVoucherBillNo()));
                log.info("对下结算-获取报账明细：{}",JSONObject.toJSONString(pushBillDetailZJS));
                Map<BigDecimal, Long> taxrateMap = this.taxGroup(pushBillDetailZJS);
                for (VoucherPushBillDetailZJ billDetailZJ : pushBillDetailZJS) {
                    BigDecimal taxRate = billDetailZJ.getTaxRate();
                    // DXJJKJMX(扣减明细/款项明细)说明
                    buildKJMX4DXJS(voucherBill, contractInvoiceZJ, taxrateMap, taxRate, KJMX2DataList);

                    // DXJJCBMX（成本明细）说明
                    buildCBMX4DXJS(voucherBill, billDetailZJ, CBMX2DataList);

                }

                for (VoucherContractMeasurementDetailZJ measurementDetailZJ : measureDetailList) {
                    // DXJJJLMX（计量明细）
                    buildJLMX4DXJS(voucherBill, measurementDetailZJ, JLMX2DataList);
                }

                for (VoucherInvoiceZJ invoiceZJ : invoiceZJS) {
                    // DXJJINVOICE（发票明细）说明，业务系统收到发票的话需要传递数据
                    buildINVOICE4DXJS(voucherBill, invoiceZJ, contractInvoiceZJ, INVOICE2DataList);
                }

                zjDXJSBillData.setData(dxjsDataList);
                DXJS1DataBillData.setData(JLMX2DataList);
                DXJS2DataBillData.setData(KJMX2DataList);
                DXJS3DataBillData.setData(CBMX2DataList);
                DXJS4DataBillData.setData(INVOICE2DataList);
            }
        }

        // 影像信息
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

        // SPRZ（审批日志）说明
        BILLDATA SPRZDataBillData = new BILLDATA();
        SPRZDataBillData.setCode("SPRZ");
        SPRZDataBillData.setIsMain("false");
        List<SPRZData> SPRZDataList = Lists.newArrayList();
        SPRZData sprzData = new SPRZData();
        SPRZDataList.add(sprzData);

        ArrayList<BILLDATA> billdatas = Lists.newArrayList();
        billdatas.add(zjDXJSBillData);
        billdatas.add(DXJS1DataBillData);
        billdatas.add(DXJS2DataBillData);
        billdatas.add(DXJS3DataBillData);
        billdatas.add(DXJS4DataBillData);

        billdatas.add(YXXXBillData);
        billdatas.add(SPRZDataBillData);

        zjDatas.setBILLDATA(billdatas);
        billdatasList.add(zjDatas);
        zjParameter.setPsData(billdatasList);
        ObjectMapper objectMapper = new ObjectMapper();
        String billdataString = objectMapper.writeValueAsString(zjParameter).replace("\"", "'");
        String replace = "---";
        zjRequestBody.setContext("---");
        String zjRequestBodyString = JSON.toJSONString(zjRequestBody).replace(replace, billdataString);
        log.info("中交推单参数 {}", zjRequestBodyString);
        return zjRequestBodyString;
    }

    public void buildDXJSData(VoucherBillZJ voucherBill, String projectId, SyncBatchPushZJBillF syncBatchPushBillF, String empCode, VoucherContractInvoiceZJ contractInvoiceZJ, List<DXJSData> dxjsDataList, List<VoucherContractMeasurementDetailZJ> measureDetailList) {
        DXJSData dxjsData = new DXJSData();
        dxjsData.setDJNM(voucherBill.getVoucherBillNo());
        dxjsData.setDJBH(voucherBill.getVoucherBillNo());
        dxjsData.setXZZZ(syncBatchPushBillF.getXZZZ());
        dxjsData.setXZBM(StringUtils.isNotEmpty(voucherBill.getExternalDepartmentCode()) ? voucherBill.getExternalDepartmentCode() : syncBatchPushBillF.getXZBM());
        dxjsData.setXMID(projectId);
        dxjsData.setZDR(empCode);
        dxjsData.setDJRQ(voucherBill.getGmtCreate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dxjsData.setLYXT("CCCG-DMC");
        MDM16V mdm16V = externalClient.queryByCode(contractInvoiceZJ.getBusinessCode());
        dxjsData.setYWLX(mdm16V.getIdExt());
        dxjsData.setHTBH(contractInvoiceZJ.getContractNo());
        dxjsData.setZQR(contractInvoiceZJ.getSupplier());
        dxjsData.setJSDH(contractInvoiceZJ.getSettlementNo());
        dxjsData.setSFQR("1");
        dxjsData.setJSFS("1");
        double taxAmountTotal = measureDetailList.stream().mapToDouble(VoucherContractMeasurementDetailZJ::getTaxIncludedAmount).sum();
        dxjsData.setHSJEBB(Double.valueOf(String.valueOf(BigDecimal.valueOf(taxAmountTotal).setScale(2, RoundingMode.HALF_UP))));
        dxjsData.setLJHSJEBB(Double.valueOf(String.valueOf(BigDecimal.valueOf(taxAmountTotal).setScale(2, RoundingMode.HALF_UP))));
        dxjsData.setBZSY(contractInvoiceZJ.getCommunityName() + "结算日期：" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dxjsData.setPZRQ(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dxjsData.setDJZT("1");
        dxjsDataList.add(dxjsData);
    }

    /** 构建对下结算发票明细请求体
     * @param voucherBill
     * @param contractInvoiceZJ
     * @param INVOICE2DataList
     */
    public void buildINVOICE4DXJS(VoucherBillZJ voucherBill, VoucherInvoiceZJ invoiceZJ, VoucherContractInvoiceZJ contractInvoiceZJ, List<DXJJINVOICEData> INVOICE2DataList) {
        DXJJINVOICEData dxjjinvoiceData = new DXJJINVOICEData();
        dxjjinvoiceData.setFPMXNM(voucherBill.getVoucherBillNo());
        dxjjinvoiceData.setJSNM(voucherBill.getVoucherBillNo());
        dxjjinvoiceData.setFPHM(invoiceZJ.getInvoiceNo());
        dxjjinvoiceData.setFPDM(invoiceZJ.getInvoiceCode());
        dxjjinvoiceData.setKPRQ(invoiceZJ.getInvoiceDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if(null != invoiceZJ.getTaxAmount()) {
            dxjjinvoiceData.setSE(BigDecimal.valueOf(invoiceZJ.getTaxAmount()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
        if (null != invoiceZJ.getPayAmount()) {
            dxjjinvoiceData.setJSHJ(Double.valueOf(String.valueOf(BigDecimal.valueOf(invoiceZJ.getPayAmount()).setScale(2, RoundingMode.HALF_UP).doubleValue())));
        }
        dxjjinvoiceData.setSFFB("0");
        dxjjinvoiceData.setFPLX(invoiceZJ.getInvoiceType());
        dxjjinvoiceData.setSFCKTS("0");
        dxjjinvoiceData.setYBBZ("156");
        dxjjinvoiceData.setHL(1.000000);
        dxjjinvoiceData.setYZZT("1");
        INVOICE2DataList.add(dxjjinvoiceData);
    }

    /** 对下结算-成本明细
     * @param voucherBill
     * @param billDetailZJ
     * @param CBMX2DataList
     */
    public void buildCBMX4DXJS(VoucherBillZJ voucherBill, VoucherPushBillDetailZJ billDetailZJ, List<DXJJCBMXData> CBMX2DataList) {
        DXJJCBMXData dxjjcbmxData = new DXJJCBMXData();
        String CBMXNMNumber = IdentifierFactory.getInstance().serialNumber("pushbillZJ", "CBMXNM", 20);
        dxjjcbmxData.setCBMXNM(CBMXNMNumber);
        dxjjcbmxData.setJSNM(voucherBill.getVoucherBillNo());
        dxjjcbmxData.setYWKM(String.valueOf(billDetailZJ.getSubjectExtId()));
        dxjjcbmxData.setYBID("156");
        dxjjcbmxData.setHL(1.000000);

        dxjjcbmxData.setJSHJYB( new BigDecimal(String.valueOf(billDetailZJ.getTaxIncludAmount())).divide(new BigDecimal("100")));
        dxjjcbmxData.setJSHJBB(new BigDecimal(String.valueOf(billDetailZJ.getTaxIncludAmount())).divide(new BigDecimal("100")));
        dxjjcbmxData.setBHSJEYB(Double.valueOf(String.valueOf(BigDecimal.valueOf(billDetailZJ.getTaxExcludAmount()).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP).doubleValue())));
        dxjjcbmxData.setBHSJEBB(Double.valueOf(String.valueOf(BigDecimal.valueOf(billDetailZJ.getTaxExcludAmount()).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP).doubleValue())));
        dxjjcbmxData.setJSHJBB(new BigDecimal(String.valueOf(billDetailZJ.getTaxIncludAmount())).divide(new BigDecimal("100")));
        dxjjcbmxData.setKDKSEYB(new BigDecimal("0"));
        dxjjcbmxData.setKDKSEBB(new BigDecimal("0"));
        CBMX2DataList.add(dxjjcbmxData);
    }

    /** 对下结算-扣减明细
     * @param voucherBill
     * @param contractInvoiceZJ
     * @param taxrateMap
     * @param taxRate
     * @param KJMX2DataList
     */
    public void buildKJMX4DXJS(VoucherBillZJ voucherBill, VoucherContractInvoiceZJ contractInvoiceZJ, Map<BigDecimal, Long> taxrateMap, BigDecimal taxRate, List<DXJJKJMXData> KJMX2DataList) {
        DXJJKJMXData dxjjkjmxData = new DXJJKJMXData();
        dxjjkjmxData.setBD(contractInvoiceZJ.getChangeType());
        String KJMXNMNumber = IdentifierFactory.getInstance().serialNumber("pushbillZJ", "KJMXNM", 24);
        dxjjkjmxData.setKJMXNM(KJMXNMNumber);
        dxjjkjmxData.setJSNM(voucherBill.getVoucherBillNo());
        MDM17V mdm17V = externalClient.queryByCodeForMDM17(contractInvoiceZJ.getPaymentId());
        dxjjkjmxData.setKJXM(mdm17V.getIdExt());
        dxjjkjmxData.setYBID("156");
        dxjjkjmxData.setHL(1.000000);
        if (StringUtils.isNotBlank(contractInvoiceZJ.getWriteOffInfo())){
            JSONObject param = JSONObject.parseObject(contractInvoiceZJ.getWriteOffInfo());
            String ysyfbh = param.getString("YSYFBH");
            dxjjkjmxData.setYSYFBH(ysyfbh);
        }
        dxjjkjmxData.setKJJEYB(String.valueOf(taxrateMap.get(taxRate)));
        dxjjkjmxData.setKJJEBB(String.valueOf(taxrateMap.get(taxRate)));
        KJMX2DataList.add(dxjjkjmxData);
    }

    /** 对下结算-计量明细
     * @param voucherBill
     * @param JLMX2DataList
     */
    public void buildJLMX4DXJS(VoucherBillZJ voucherBill,VoucherContractMeasurementDetailZJ measurementDetailZJ, List<DXJJJLMXData> JLMX2DataList) {
        DXJJJLMXData dxjjjlmxData = new DXJJJLMXData();
        String JLMXNMNumber = IdentifierFactory.getInstance().serialNumber("pushbillZJ", "JLMXNM", 20);
        dxjjjlmxData.setJLMXNM(JLMXNMNumber);
        dxjjjlmxData.setJSNM(voucherBill.getVoucherBillNo());
        dxjjjlmxData.setJLHL(1.000000);
        BigDecimal taxRate = measurementDetailZJ.getTaxRate();
        Long taxIncludedAmount = measurementDetailZJ.getTaxIncludedAmount();
        Long taxExcludedAmount = measurementDetailZJ.getTaxExcludedAmount();
        BigDecimal taxAmount = getDivideSettleAmountBySettleAmount(taxIncludedAmount);
        BigDecimal taxExcludeAmount = getDivideSettleAmountBySettleAmount(taxExcludedAmount);
        //不含税金额
        dxjjjlmxData.setHSJEYB(taxAmount.doubleValue());
        dxjjjlmxData.setHSJEBB(taxAmount.doubleValue());
        dxjjjlmxData.setBHSYB(taxExcludeAmount.doubleValue());
        dxjjjlmxData.setBHSBB(taxExcludeAmount.doubleValue());
        JLMX2DataList.add(dxjjjlmxData);
    }

    public String communityId(VoucherBillZJ voucherBill) {
        List<VoucherPushBillDetailZJ> pushBillDetailZJS = voucherBillDetailRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                .eq(VoucherPushBillDetailZJ::getVoucherBillNo, voucherBill.getVoucherBillNo()));
        if (CollectionUtils.isNotEmpty(pushBillDetailZJS)) {
            return pushBillDetailZJS.get(0).getCommunityId();
        }
        return "";
    }

    public String communityIdForZJ(VoucherBillDxZJ voucherBill) {
        List<VoucherPushBillDetailDxZJ> pushBillDetailZJS = voucherBillDetailDxZJRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailDxZJ>()
                .eq(VoucherPushBillDetailDxZJ::getVoucherBillNo, voucherBill.getVoucherBillNo()));
        if (CollectionUtils.isNotEmpty(pushBillDetailZJS)) {
            return pushBillDetailZJS.get(0).getCommunityId();
        }
        return "";
    }

    /**
     * 拉取推送的财务云单据状态和信息-新报账单
     **/
    public void queryFinanceOrderDealResultOnDx(OrderStatusBody orderStatusBody) {
        String url = "/ESB/API/ChannelZJFW/YXDMC/QueryFinanceOrderDealResultList";
        List<VoucherBillDxZJ> voucherBills = voucherPushBillDxZJRepository.list(new LambdaQueryWrapper<VoucherBillDxZJ>()
//                .eq(VoucherBillZJ::getBillEventType, 2)
                .eq(VoucherBillDxZJ::getPushState, 2)
                .eq(VoucherBillDxZJ::getDeleted, 0)
                .eq(VoucherBillDxZJ::getInferenceState, 0));
        if (CollectionUtils.isEmpty(voucherBills)) {
            log.info("暂无制单信息需要查询 ");
            return;
        }
        int i = 0;
        List<VoucherBillDxZJ> updateList = Lists.newArrayList();
        for (VoucherBillDxZJ voucherBill : voucherBills) {
            System.out.println("当前拉取"+(i++));
            orderStatusBody.setDjbh(voucherBill.getVoucherBillNo());
            orderStatusBody.setDjnm(voucherBill.getVoucherBillNo());
            orderStatusBody.setLyxt("CCCG-DMC");
            log.info("中交推单orderStatusBody入参信息 ：{}", JSON.toJSONString(orderStatusBody));
            String body = zjBillDataClient.getOrderStatus(JSON.toJSONString(orderStatusBody), url);
            log.info("中交推单body返回信息 ：{}", body);
            OrderDealResult orderDeal = JSONObject.parseObject(body, OrderDealResult.class);
            log.info("中交推单orderDeal返回信息 ：{}", JSON.toJSONString(orderDeal));
            List<OrderDealData> orderDealData = JSONObject.parseArray(orderDeal.getData(), OrderDealData.class);
            if (CollectionUtils.isNotEmpty(orderDealData)) {
                for (OrderDealData orderDealDatum : orderDealData) {
                    if (orderDealDatum.getISNEWINVOKE().equals("0") && orderDealDatum.getRESULT().equals("0")) {
                        voucherBill = voucherPushBillDxZJRepository.getById(voucherBill.getId());
                        if (voucherBill.getPushState().equals(PushBillTypeEnum.已推送.getCode())){
                            voucherBill.setPushState(PushBillTypeEnum.制单成功.getCode());
                        }
                        voucherBill.setInferenceState(1);
                        voucherBill.setWanderAboutState(1);
                        voucherBill.setFinanceId(orderDealDatum.getCWYDICID());
                        voucherBill.setFinanceNo(orderDealDatum.getCWYDOCCODE());
                    } else {
                        voucherBill.setPushState(PushBillTypeEnum.制单失败.getCode());
                        voucherBill.setWanderAboutState(2);
                        voucherBill.setRemark(orderDealDatum.getDESCRIPTION());
                    }
                    updateList.add(voucherBill);
                }
            } else {
                log.info("调用财务云接口失败 未获取到制单信息 参数:{}", JSON.toJSONString(orderStatusBody));
                voucherBill.setPushState(PushBillTypeEnum.推送失败.getCode());
                voucherBill.setRemark("调用财务云接口失败 未获取到相关信息");
                updateList.add(voucherBill);
            }
        }
        if (!voucherPushBillDxZJRepository.updateBatchById(updateList)) {
            log.error("更新推单信息失败 信息:{}", JSON.toJSONString(updateList));
        }
    }

    public void queryFinanceOrderDealResult(OrderStatusBody orderStatusBody) {
        String url = "/ESB/API/ChannelZJFW/YXDMC/QueryFinanceOrderDealResultList";
        List<VoucherBillZJ> voucherBills = voucherPushBillZJRepository.list(new LambdaQueryWrapper<VoucherBillZJ>()
                .in(VoucherBillZJ::getBillEventType, 1, 2)
                .eq(VoucherBillZJ::getPushState, 2)
                .eq(VoucherBillZJ::getDeleted, 0)
                .eq(VoucherBillZJ::getInferenceState, 0));
        if (CollectionUtils.isEmpty(voucherBills)) {
            log.info("暂无制单信息需要查询 ");
            return;
        }
        int i = 0;
        List<VoucherBillZJ> updateList = Lists.newArrayList();
        for (VoucherBillZJ voucherBill : voucherBills) {
            System.out.println("当前拉取"+(i++));
            orderStatusBody.setDjbh(voucherBill.getVoucherBillNo());
            orderStatusBody.setDjnm(voucherBill.getVoucherBillNo());
            orderStatusBody.setLyxt("CCCG-DMC");
            log.info("中交推单orderStatusBody入参信息 ：{}", JSON.toJSONString(orderStatusBody));
            String body = zjBillDataClient.getOrderStatus(JSON.toJSONString(orderStatusBody), url);
            log.info("中交推单body返回信息 ：{}", body);
            OrderDealResult orderDeal = JSONObject.parseObject(body, OrderDealResult.class);
            log.info("中交推单orderDeal返回信息 ：{}", JSON.toJSONString(orderDeal));
            List<OrderDealData> orderDealData = JSONObject.parseArray(orderDeal.getData(), OrderDealData.class);
            if (CollectionUtils.isNotEmpty(orderDealData)) {
                for (OrderDealData orderDealDatum : orderDealData) {
                    if (orderDealDatum.getISNEWINVOKE().equals("0") && orderDealDatum.getRESULT().equals("0")) {
                        voucherBill = voucherPushBillZJRepository.getById(voucherBill.getId());
                        if (voucherBill.getPushState().equals(PushBillTypeEnum.已推送.getCode())){
                            voucherBill.setPushState(PushBillTypeEnum.制单成功.getCode());
                        }
                        voucherBill.setInferenceState(1);
                        voucherBill.setWanderAboutState(1);
                        voucherBill.setFinanceId(orderDealDatum.getCWYDICID());
                        voucherBill.setFinanceNo(orderDealDatum.getCWYDOCCODE());
                    } else {
                        voucherBill.setPushState(PushBillTypeEnum.制单失败.getCode());
                        voucherBill.setWanderAboutState(2);
                        voucherBill.setRemark(orderDealDatum.getDESCRIPTION());
                    }
                    updateList.add(voucherBill);
                }
            } else {
                log.info("调用财务云接口失败 未获取到制单信息 参数:{}", JSON.toJSONString(orderStatusBody));
                voucherBill.setPushState(PushBillTypeEnum.推送失败.getCode());
                voucherBill.setRemark("调用财务云接口失败 未获取到相关信息");
                updateList.add(voucherBill);
            }
        }
        if (!voucherPushBillZJRepository.updateBatchById(updateList)) {
            log.error("更新推单信息失败 信息:{}", JSON.toJSONString(updateList));
        }
    }

    public OrderDealResult queryOrderStatus(OrderStatusBody orderStatusBody) {
        // String url = "/ChannelZJFW/YXDMC/QueryFinanceOrderDealResultList";
        String url = "/ESB/API/ChannelZJFW/YXDMC/QueryFinanceOrderDealResultList";
        log.info("中交推单orderStatusBody入参信息 ：{}", JSON.toJSONString(orderStatusBody));
        String body = zjBillDataClient.getOrderStatus(JSON.toJSONString(orderStatusBody), url);
        log.info("中交推单body返回信息 ：{}", body);
        OrderDealResult orderDeal = JSONObject.parseObject(body, OrderDealResult.class);
        log.info("中交推单orderDeal返回信息 ：{}", JSON.toJSONString(orderDeal));
        return orderDeal;
    }

    public void queryFinanceOrderStatus(OrderDealBody orderDealBody) {
        String url = "ChannelZJFW/YXDMC/QueryFinanceOrderStatusResultList";
        List<VoucherBillZJ> voucherBills = voucherPushBillZJRepository.list(new LambdaQueryWrapper<VoucherBillZJ>()
                .in(VoucherBillZJ::getPushState, 1, 3)
                .eq(VoucherBillZJ::getDeleted, 0));
        if (CollectionUtils.isEmpty(voucherBills)) {
            log.info("暂无制单信息需要查询 ");
            return;
        }
        for (VoucherBillZJ voucherBill : voucherBills) {
            orderDealBody.setDjbh(voucherBill.getVoucherBillNo());
            orderDealBody.setDjnm(voucherBill.getVoucherBillNo());
            orderDealBody.setLyxt("CCCG-DMC");
            orderDealBody.setBusinessCode(DocumentTypeEnum.valueOfByCode(voucherBill.getBillEventType()).getValue());

            String body = zjBillDataClient.getOrderStatus(JSON.toJSONString(orderDealBody), url);
            OrderStatusResult orderStatusResult = JSONObject.parseObject(body, OrderStatusResult.class);
            log.info("中交推单orderStatusResult返回信息 ：{}", JSON.toJSONString(orderStatusResult));
            List<OrderDealData> orderDealData = JSONObject.parseArray(orderStatusResult.getData(), OrderDealData.class);
            if (CollectionUtils.isNotEmpty(orderDealData)) {
                for (OrderDealData orderDealDatum : orderDealData) {
                    if (orderDealDatum.getISNEWINVOKE().equals("0") && orderDealDatum.getRESULT().equals("0")) {
                        voucherBill.setPushState(2);
                        voucherBill.setInferenceState(1);
                        voucherBill.setWanderAboutState(3);
                        voucherBill.setFinanceId(orderDealDatum.getCWYDICID());
                        voucherBill.setFinanceNo(orderDealDatum.getCWYDOCCODE());
                    } else {
                        voucherBill.setPushState(PushBillTypeEnum.推送失败.getCode());
                        voucherBill.setWanderAboutState(2);
                        voucherBill.setRemark(orderDealDatum.getDESCRIPTION());
                    }
                }
            }
            if (CollectionUtils.isEmpty(orderDealData)) {
                log.info("调用财务云接口失败 未获取到制单信息 参数:{}", JSON.toJSONString(orderStatusResult));
                return;
            }
        }
        if (!voucherPushBillZJRepository.updateBatchById(voucherBills)) {
            log.error("更新推单信息失败 信息:{}", JSON.toJSONString(voucherBills));
        }
    }

    public OrderStatusResult delOrderDealResult(OrderStatusDel orderStatusDel) {
        String url = "/ESB/API/ChannelZJFW/YXDMC/QueryFinanceOrderStatusResultList";
        VoucherBillZJ zjRepositoryById = voucherPushBillZJRepository.getById(orderStatusDel.getId());
        OrderStatusResult orderStatusResult = new OrderStatusResult();
        if (Objects.isNull(zjRepositoryById)) {
            throw BizException.throw400("单据不存在不存在");
        }
        if (StringUtils.isEmpty(zjRepositoryById.getFinanceId())) {
            List<VoucherPushBillDetailZJ> list = voucherBillDetailRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                    .eq(VoucherPushBillDetailZJ::getVoucherBillNo, zjRepositoryById.getVoucherBillNo()));
            // 判断收款单类型  如果是资金收款单   更新流水状态  更新对应的推送状态 未 未推送
            if (zjRepositoryById.getBillEventType() == 2) {
                //删除锁定的mdm63数据
                mdm63LockMapper.updateDeletedByVoucherBIllId(orderStatusDel.getId());
                String recordIdList = zjRepositoryById.getRecordIdList();
                List<Long> recordIds = JSON.parseArray(recordIdList, Long.class);
                billRuleDomainService.updateFlowAfterDel(recordIds);
                gatherBillRepository.update(new UpdateWrapper<GatherBill>().set("inference_state", 0)
                        .eq("sup_cp_unit_id", list.get(0).getCommunityId())
                        .in("id", list.stream().map(VoucherPushBillDetailZJ::getGatherBillId).collect(Collectors.toList())));

                gatherDetailRepository.update(new UpdateWrapper<GatherDetail>().set("inference_state", 0)
                        .eq("sup_cp_unit_id", list.get(0).getCommunityId())
                        .in("rec_bill_id", list.stream().map(VoucherPushBillDetailZJ::getBillId).collect(Collectors.toList())));

            }else if (zjRepositoryById.getBillEventType()==3){
                contractClient.dealContractInvoice(zjRepositoryById.getId(),4);
            } else if (zjRepositoryById.getBillEventType() == 1) {
                List<Long> collect = list.stream().filter(s -> s.getPushBillState() == 2 || s.getPushBillState() == 6).filter(s -> s.getReverseFlag() == 0).map(VoucherPushBillDetailZJ::getBillId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)){
                    receivableBillRepository.update(new UpdateWrapper<ReceivableBill>().set("inference_state", 0)
                            .eq("sup_cp_unit_id", list.get(0).getCommunityId())
                            .in("id", collect)
                            .eq("inference_state", 1));
                }

            }
            voucherPushBillZJRepository.removeById(zjRepositoryById);

            // 收入确认单逆向流程标记取消
            List<Long> collect = list.stream().filter(s -> s.getReverseFlag() == 3).map(VoucherPushBillDetailZJ::getSceneId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                billAdjustRepository.updateBillInferenceState(collect, 0);
            }
            List<Long> collect1 = list.stream().filter(s -> s.getReverseFlag() == 4).map(VoucherPushBillDetailZJ::getSceneId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect1)) {
                billAdjustRepository.updateBillInferenceState(collect1, 0);
            }
            if (CollectionUtils.isNotEmpty(list)) {
                voucherBillDetailRepository.removeBatchByIds(list);
            }
            orderStatusResult.setCode(0);
            orderStatusResult.setMessage("删除成功");
            return orderStatusResult;
        }
        OrderDealBody orderDealBody = new OrderDealBody();
        orderDealBody.setDjbh(zjRepositoryById.getVoucherBillNo());
        orderDealBody.setDjnm(zjRepositoryById.getVoucherBillNo());
        orderDealBody.setLyxt("CCCG-DMC");
        orderDealBody.setBusinessCode(DocumentTypeEnum.valueOfByCode(zjRepositoryById.getBillEventType()).getValue());

        String body = zjBillDataClient.getOrderStatus(JSON.toJSONString(orderDealBody), url);
        orderStatusResult = JSONObject.parseObject(body, OrderStatusResult.class);
        log.info("中交推单orderStatusResult返回信息 ：{}", JSON.toJSONString(orderStatusResult));
        if (Objects.nonNull(orderStatusResult) && orderStatusResult.getCode() == 1) {
            return orderStatusResult;
        }
        /**
         * 资金收款单和收入确认单的返回结果是不一样的，而且返回结果中，一个单子会有多个环节
         **/
        List<VoucherPushBillDetailZJ> list1 = voucherBillDetailRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                .eq(VoucherPushBillDetailZJ::getVoucherBillNo, zjRepositoryById.getVoucherBillNo()));
        if (zjRepositoryById.getBillEventType() == 2){
            //删除锁定的mdm63数据
            mdm63LockMapper.updateDeletedByVoucherBIllId(orderStatusDel.getId());
            List<OrderStatusData> orderDealData = JSONObject.parseArray(orderStatusResult.getData(), OrderStatusData.class);
            if (CollectionUtils.isNotEmpty(orderDealData)){
                List<OrderStatusData> rejectList = orderDealData.stream().filter(e -> "-1".equals(e.getDQHJ())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(rejectList)){
                    orderStatusResult.setCode(1);
                    orderStatusResult.setMessage("财务云未驳回，请检查");
                    return orderStatusResult;
                }
                String recordIdList = zjRepositoryById.getRecordIdList();
                List<Long> recordIds = JSON.parseArray(recordIdList, Long.class);
                billRuleDomainService.updateFlowAfterDel(recordIds);
                gatherBillRepository.update(new UpdateWrapper<GatherBill>().set("inference_state", 0)
                        .eq("sup_cp_unit_id", list1.get(0).getCommunityId())
                        .in("id", list1.stream().map(VoucherPushBillDetailZJ::getGatherBillId).collect(Collectors.toList())));
                gatherDetailRepository.update(new UpdateWrapper<GatherDetail>().set("inference_state", 0)
                        .eq("sup_cp_unit_id", list1.get(0).getCommunityId())
                        .in("rec_bill_id", list1.stream().map(VoucherPushBillDetailZJ::getBillId).collect(Collectors.toList())));
            } else {
                orderStatusResult.setCode(1);
                orderStatusResult.setMessage("财务云暂无流转状态");
                return orderStatusResult;
            }
        } else if (zjRepositoryById.getBillEventType() == 1){
            List<OrderStatusData> orderDealData = JSONObject.parseArray(orderStatusResult.getData(), OrderStatusData.class);
            if (CollectionUtils.isNotEmpty(orderDealData)){
                List<OrderStatusData> rejectList = orderDealData.stream().filter(e -> "-1".equals(e.getDQHJ())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(rejectList)){
                    orderStatusResult.setCode(1);
                    orderStatusResult.setMessage("财务云未驳回，请检查");
                    return orderStatusResult;
                }
                List<Long> collect = list1.stream().filter(s -> s.getPushBillState() == 2 || s.getPushBillState() == 6).filter(s -> s.getReverseFlag() == 0).map(VoucherPushBillDetailZJ::getBillId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)){
                    receivableBillRepository.update(new UpdateWrapper<ReceivableBill>().set("inference_state", 0)
                            .eq("sup_cp_unit_id", list1.get(0).getCommunityId())
                            .in("id", collect)
                            .eq("inference_state", 1));
                }
            } else {
                orderStatusResult.setCode(1);
                orderStatusResult.setMessage("财务云暂无流转状态");
                return orderStatusResult;
            }
        } else if (zjRepositoryById.getBillEventType() == 3) {
            /**
             * todo 这里怎么序列化?文档也是缺失的
             **/
            contractClient.dealContractInvoice(zjRepositoryById.getId(), 4);
        }

        voucherPushBillZJRepository.removeById(zjRepositoryById);
        List<Long> collect = list1.stream().filter(s -> s.getReverseFlag() == 3).map(VoucherPushBillDetailZJ::getSceneId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            billAdjustRepository.update(new UpdateWrapper<BillAdjustE>().set("inference_state", 0).in("id", collect));
        }
        List<Long> collect1 = list1.stream().filter(s -> s.getReverseFlag() == 4).map(VoucherPushBillDetailZJ::getSceneId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect1)) {
            billAdjustRepository.update(new UpdateWrapper<BillAdjustE>().set("inference_state", 0).in("id", collect1));
        }
        if (CollectionUtils.isNotEmpty(list1)) {
            voucherBillDetailRepository.removeBatchByIds(list1);
        }
        orderStatusResult.setCode(0);
        orderStatusResult.setMessage("删除成功");
        return orderStatusResult;
    }

    /**
     * 发起审批流程
     * @param communityId
     * @return
     */
    public ApproveResultDto initiateApprove(String communityId, OperateTypeEnum operateType  , Long voucherBillId, FlowClaimRecordE flowClaimRecordE) {
        ApproveResultDto approveResultDto = new ApproveResultDto();
        ApproveFilter approveFilter = chargeClient.getApprovePushBillFilter(communityId,
                operateType.getCode());
        if (approveFilter.getApproveWay() == 1) {
            WflowModelHistorysV wflowModelHistorysV = bpmClient.getProcessModelByFormId(approveFilter.getApproveRule());
            log.info("ChargeApproveAppService.batchBpmAdjust获取审批流程入参,wflowModelHistorysV:{},结果:{}", approveFilter.getApproveRule(), JSON.toJSONString(wflowModelHistorysV));
            if (ObjectUtil.isNull(wflowModelHistorysV)) {
                throw BizException.throw301("获取审批流程为空,请确认流程是否存在");
            }
            ProcessStartF processStartF = new ProcessStartF();
            Map<String, Object> formData = new HashMap<>();
            formData.put("flowType", operateType.getDes());
            formData.put("flowId", voucherBillId);

            BigDecimal se = new BigDecimal(flowClaimRecordE.getClaimAmount());
            BigDecimal res = se.divide(new BigDecimal(100), 2, RoundingMode.CEILING);

            processStartF.setFormData(formData);
            processStartF.setBusinessKey(String.valueOf(voucherBillId));
            processStartF.setBusinessType(OperateTypeEnum.资金收款.getDes());
            processStartF.setSuitableTargetType("PROJECT");
            processStartF.setSuitableTargetId(communityId);
            processStartF.setSuitableTargetName(flowClaimRecordE.getSupCpUnitName());
            processStartF.setReceiptRemark(flowClaimRecordE.getReceiptRemark());
            processStartF.setClaimAmount(res.toString());
            String s = null;
            try {
                s = bpmClient.processStart(wflowModelHistorysV.getProcessDefId(), processStartF);
            } catch (Exception e) {
                log.info("流程发起异常：{}",e);
                log.error("流程发起异常：{}",e);
                throw new OwlBizException("流程发起超时，请稍后重试！");
            }
            VoucherBillZJ byId = voucherPushBillRepository.getById(voucherBillId);
            byId.setProcInstId(s);
            voucherPushBillRepository.updateById(byId);
            approveResultDto.setApproveState(VoucherApproveStateEnum.审批中.getCode());
            return approveResultDto;
        }
        approveResultDto.setApproveState(VoucherApproveStateEnum.无需审批.getCode());
        return approveResultDto;
    }


    public void initiateApprove(VoucherBillDxZJ voucherBillDxZJ, String communityId, String communityName, OperateTypeEnum operateType) {
        ApproveFilter approveFilter = chargeClient.getApprovePushBillFilter(communityId,
                operateType.getCode());
        if (approveFilter.getApproveWay() == 1) {
            WflowModelHistorysV wflowModelHistorysV = bpmClient.getProcessModelByFormId(approveFilter.getApproveRule());
            log.info("ChargeApproveAppService.batchBpmAdjust获取审批流程入参,wflowModelHistorysV:{},结果:{}", approveFilter.getApproveRule(), JSON.toJSONString(wflowModelHistorysV));
            if (ObjectUtil.isNull(wflowModelHistorysV)) {
                throw BizException.throw301("获取审批流程为空,请确认流程是否存在");
            }
            ProcessStartF processStartF = new ProcessStartF();
            Map<String, Object> formData = new HashMap<>();
            formData.put("flowType", operateType.getDes());
            formData.put("flowId", voucherBillDxZJ.getId());

            BigDecimal res = voucherBillDxZJ.getTotalAmount().divide(new BigDecimal(100), 2, RoundingMode.CEILING);

            processStartF.setFormData(formData);
            processStartF.setBusinessKey(String.valueOf(voucherBillDxZJ.getId()));
            processStartF.setBusinessType(operateType.getDes());
            processStartF.setSuitableTargetType("PROJECT");
            processStartF.setSuitableTargetId(communityId);
            processStartF.setSuitableTargetName(communityName);
            processStartF.setClaimAmount(res.toString());

//            IdentityInfo identityInfo = ThreadLocalUtil.curIdentityInfo();
//            identityInfo.setUserId();
//            identityInfo.setUserName()
            String s = null;
            try {
                s = bpmClient.processStart(wflowModelHistorysV.getProcessDefId(), processStartF);
            } catch (Exception e) {
                log.info("流程发起异常：{}",e);
                log.error("流程发起异常：{}",e);
                throw new OwlBizException("流程发起超时，请稍后重试！");
            }
            voucherBillDxZJ.setProcInstId(s);
            voucherBillDxZJ.setApproveState(PushBillApproveStateEnum.审核中.getCode());
            voucherBillDxZJ.setPushState(4);
            voucherPushBillDxZJRepository.updateById(voucherBillDxZJ);
        }else{
            voucherBillDxZJ.setApproveState(PushBillApproveStateEnum.已审核.getCode());
            voucherBillDxZJ.setPushState(1);
            voucherBillDxZJ.setGmtApprove(LocalDateTime.now());
            voucherPushBillDxZJRepository.updateById(voucherBillDxZJ);
        }
    }

    public void approveAgree(Long voucherBillId) {
        voucherPushBillZJRepository.update(new LambdaUpdateWrapper<VoucherBillZJ>()
                .set(VoucherBillZJ::getApproveState, PushBillApproveStateEnum.已审核.getCode())
                .eq(VoucherBillZJ::getId, voucherBillId));
    }

    public void delete(VoucherBillZJ voucherBillZJ) {
//        VoucherBillZJ voucherBillZJ = voucherPushBillZJRepository.getById(voucherBillId);
//        voucherBillZJ.setApproveState(2);
//        voucherPushBillZJRepository.updateById(voucherBillZJ);
//        voucherBillDetailRepository.delete(voucherBillZJ.getVoucherBillNo());
        voucherPushBillZJRepository.updateById(voucherBillZJ);
    }


    public SyncBatchVoucherResultV syncBatchPushBillForJTAndSQ(SyncBatchPushZJBillF syncBatchPushBillF) {
        log.info("推送财务云调用推送逻辑syncBatchPushBillForJTAndSQ");
        List<VoucherBillDxZJ> voucherBills = voucherPushBillDxZJRepository.list(new LambdaQueryWrapper<VoucherBillDxZJ>()
                .in(VoucherBillDxZJ::getId, syncBatchPushBillF.getVoucherIds())
                .in(VoucherBillDxZJ::getPushState, 1, 3, 4,PushBillTypeEnum.制单失败.getCode(), PushBillTypeEnum.单据驳回.getCode())
                .eq(VoucherBillDxZJ::getDeleted, 0));
        if (null == syncBatchPushBillF.getUserId() || StringUtils.isEmpty(syncBatchPushBillF.getUserId())){
            IdentityInfo identityInfo = ThreadLocalUtil.curIdentityInfo();
            syncBatchPushBillF.setUserId(identityInfo.getUserId());
        }
        log.info("推送财务云调用推送逻辑syncBatchPushBillForJTAndSQ用户封装完毕：{}",syncBatchPushBillF.getUserId());
        UserInfoRv userInfo = userClient.getUserInfo(syncBatchPushBillF.getUserId(), null);
        log.info("推送财务云调用推送逻辑syncBatchPushBillForJTAndSQ用户封装userInfo：{}",JSONArray.toJSON(userInfo));
        String empCode = externalClient.getIphoneByEmpCode(userInfo.getMobileNum());
        log.info("推送财务云调用推送逻辑syncBatchPushBillForJTAndSQ用户封装empCode：{}",JSONArray.toJSON(empCode));
        List<Long> errorList = new ArrayList<>();
        int successCount = voucherBills.size();
        String oaUrl = null;
        for (VoucherBillDxZJ voucherBill : voucherBills) {
            String projectId = "";
            try  {
                SpaceCommunityShortV spaceCommunityShortVS = spaceClient.get(this.communityIdForZJ(voucherBill));
                if (Objects.nonNull(spaceCommunityShortVS)) {
                    projectId = spaceCommunityShortVS.getSerialNumber();
                }
            } catch (Exception e) {
                log.error("推送财务云syncBatchPushBillForJTAndSQ获取数据异常 {}", JSONArray.toJSON(voucherBill));
            }

            List<SPRZData> SPRZDataList = new ArrayList<>();
            // 判断是否审批
            assemblyApproveInfoOnBpm(voucherBill, SPRZDataList);
            assemblyApproveInfoOnOa(voucherBill, SPRZDataList);

            log.info("推送财务云中交项目id {},参数 {}", projectId, this.communityIdForZJ(voucherBill));
            try {
                List<String> detailNumberS = Lists.newArrayList();
                String zjRequestBodyString = "";
                List<String> ftIdList = Lists.newArrayList();
                if(voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.对下结算计提.getCode() ||
                   voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode() ||
                    voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.对下结算实签.getCode()){
                    // 1 收入确认单
                        List<DxPaymentDetails> dxPaymentDetailsList = voucherBillDxDetailAppService.queryDetailsOfPayments(voucherBill.getVoucherBillNo());
                        // 先做报文组装，内部有校验，没问题才能往后走
                        zjRequestBodyString = financialRequestBodyService.getDxRequestBody(voucherBill,
                                dxPaymentDetailsList, syncBatchPushBillF, projectId, empCode, SPRZDataList, ftIdList);
                        // 实签走oa审批
                        if (voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.对下结算实签.getCode()){
                            if (Objects.nonNull(voucherBill.getApproveState())){
                                if (voucherBill.getApproveState() == 1){
                                    throw new OwlBizException("请先等待OA审批完成!");
                                }
                                if (voucherBill.getApproveState() == 0 || voucherBill.getApproveState() == 3){
                                    // 未发起或驳回走发起流程
                                    oaUrl = processService.createProcess(voucherBill.getId(), BusinessProcessType.DX_JS_FORM);
                                    voucherPushBillDxZJRepository.update(new LambdaUpdateWrapper<VoucherBillDxZJ>()
                                            .eq(VoucherBillDxZJ::getVoucherBillNo, voucherBill.getVoucherBillNo())
                                            .set(VoucherBillDxZJ::getApproveState, 1)
                                            .set(VoucherBillDxZJ::getPushState, PushBillTypeEnum.推送中.getCode()));
                                    break;
                                }
                            }
                        }
                } else if (voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.收入确认实签.getCode() || voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.收入确认计提.getCode()  ||
                        voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode()) {
                    //收入确认单单实签
                    // 判断是否需要审批
                    ApproveFilter approveFilter = new ApproveFilter();
                    approveFilter.setApproveWay(2);
                    // 1 收入确认单
                    log.info("推送财务云approveFilter approveWay is:{}",approveFilter.getApproveWay());
                    if (approveFilter.getApproveWay() == 2  || (null !=  syncBatchPushBillF.getApproveFlag() && syncBatchPushBillF.getApproveFlag().equals(1))) {
                        List<DxPaymentDetails> dxPaymentDetailsList = voucherBillDxDetailAppService.queryDetailsOfPayments(voucherBill.getVoucherBillNo());
                        List<GeneralDetails> generalRevenueRecognition = voucherBillDxDetailAppService.generalRevenueRecognition(voucherBill.getVoucherBillNo());
                        zjRequestBodyString =
                                this.zjSRQRRequestBodyForPayIncome(projectId,
                                        ZJTriggerEventBillTypeEnum.valueOfByCode(voucherBill.getBillEventType()),
                                        voucherBill, syncBatchPushBillF, empCode, detailNumberS, SPRZDataList,
                                        dxPaymentDetailsList,generalRevenueRecognition,voucherBill.getBillEventType(),
                                        ftIdList);
                    } else {
                        log.info("推送财务云approveFilter is not approve:{}",approveFilter.getApproveWay());
                        RevenueApprove revenueApprove = new RevenueApprove();
                        revenueApprove.setCostCenterId(voucherBill.getCostCenterId());
                        revenueApprove.setVoucherBillIds(Arrays.asList(voucherBill.getId()));
                        Global.ac.getBean(BillRuleAppService.class).revenueApproveNew(revenueApprove,OperateTypeEnum.收入确认计提单);
                        return new SyncBatchVoucherResultV();
                    }

                }
                else if (voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.支付申请.getCode()) {
                    ApproveFilter approveFilter = new ApproveFilter();
                    approveFilter.setApproveWay(2);
                    log.info("pay application approveFilter approveWay is:{}",approveFilter.getApproveWay());
                    if (approveFilter.getApproveWay() == 2  || (null !=  syncBatchPushBillF.getApproveFlag() && syncBatchPushBillF.getApproveFlag().equals(1))) {
                        zjRequestBodyString = financialRequestBodyService.getZFSQRequestBody(voucherBill, syncBatchPushBillF,
                                projectId, empCode, SPRZDataList, ftIdList);
                    } else {
                        log.info("approveFilter is not approve:{}",approveFilter.getApproveWay());
                        RevenueApprove revenueApprove = new RevenueApprove();
                        revenueApprove.setCostCenterId(voucherBill.getCostCenterId());
                        revenueApprove.setVoucherBillIds(Arrays.asList(voucherBill.getId()));
                        Global.ac.getBean(BillRuleAppService.class).revenueApproveNew(revenueApprove,OperateTypeEnum.业务支付申请单);
                        return new SyncBatchVoucherResultV();
                    }

                }
                log.info("推送财务云zjRequestBodyStringStart:{}",zjRequestBodyString);
                String businessCode = DocumentTypeEnum.valueOfByCode(voucherBill.getBillEventType()).getValue();
                ZJSendresultV zjSendresultV = zjBillDataClient.getPushOrder(zjRequestBodyString, voucherBill.getVoucherBillNo(), businessCode);
                log.info("推送财务云zjRequestBodyStringEnd:{}",JSONArray.toJSON(zjSendresultV));
                if (Objects.nonNull(zjSendresultV) && 0 == zjSendresultV.getCode()) {
                    voucherBill.setDetailNumber(detailNumberS.toString());
                    voucherBill.setPushState(PushBillTypeEnum.已推送.getCode());
                    voucherBillDetailDxZJRepository.update(new LambdaUpdateWrapper<VoucherPushBillDetailDxZJ>()
                            .eq(VoucherPushBillDetailDxZJ::getVoucherBillNo, voucherBill.getVoucherBillNo())
                            .set(VoucherPushBillDetailDxZJ::getPushBillState, 2));
                    // 锁定mdm63
                    /*if (CollectionUtils.isNotEmpty(ftIdList)){
                        lockMdm63(voucherBill.getId(), voucherBill.getVoucherBillNo(), ftIdList);
                    }*/
                     if (voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.收入确认计提.getCode()) {
                        List<VoucherPushBillDetailDxZJ> pushBillDetailZJS = voucherBillDetailDxZJRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailDxZJ>()
                                .eq(VoucherPushBillDetailDxZJ::getVoucherBillNo, voucherBill.getVoucherBillNo())
                                .eq(VoucherPushBillDetailDxZJ::getDeleted, 0));
                        receivableBillRepository.update(new UpdateWrapper<ReceivableBill>()
                                .set("inference_state", 1)
                                .set("provision_status", 1)
                                .set("provision_voucher_pushing_status", 1)
                                .eq("sup_cp_unit_id", pushBillDetailZJS.get(0).getCommunityId())
                                .in("id",pushBillDetailZJS.stream().map(VoucherPushBillDetailDxZJ::getBillId).collect(Collectors.toList())));
                    }else if (voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.收入确认实签.getCode()) {
                        List<VoucherPushBillDetailDxZJ> pushBillDetailZJS = voucherBillDetailDxZJRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailDxZJ>()
                                .eq(VoucherPushBillDetailDxZJ::getVoucherBillNo, voucherBill.getVoucherBillNo())
                                .eq(VoucherPushBillDetailDxZJ::getDeleted, 0));
                        receivableBillRepository.update(new UpdateWrapper<ReceivableBill>()
                                .set("inference_state", 1)
                                .set("receipt_confirmation_status", 1)
                                .set("receipt_confirmation_voucher_pushing_status", 1)
                                .eq("sup_cp_unit_id", pushBillDetailZJS.get(0).getCommunityId())
                                .in("id",pushBillDetailZJS.stream().map(VoucherPushBillDetailDxZJ::getBillId).collect(Collectors.toList())));
                    } else if (voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.对下结算计提.getCode()) {
                         List<VoucherPushBillDetailDxZJ> pushBillDetailZJS = voucherBillDetailDxZJRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailDxZJ>()
                                 .eq(VoucherPushBillDetailDxZJ::getVoucherBillNo, voucherBill.getVoucherBillNo())
                                 .eq(VoucherPushBillDetailDxZJ::getDeleted, 0));
                         receivableBillRepository.update(new UpdateWrapper<ReceivableBill>()
                                 .set("inference_state", 1)
                                 .set("provision_status", 1)
                                 .set("provision_voucher_pushing_status", 1)
                                 .eq("sup_cp_unit_id", pushBillDetailZJS.get(0).getCommunityId())
                                 .in("id",pushBillDetailZJS.stream().map(VoucherPushBillDetailDxZJ::getBillId).collect(Collectors.toList())));
                     }else if (voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.对下结算实签.getCode()) {
                         List<VoucherPushBillDetailDxZJ> pushBillDetailZJS = voucherBillDetailDxZJRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailDxZJ>()
                                 .eq(VoucherPushBillDetailDxZJ::getVoucherBillNo, voucherBill.getVoucherBillNo())
                                 .eq(VoucherPushBillDetailDxZJ::getDeleted, 0));
                         receivableBillRepository.update(new UpdateWrapper<ReceivableBill>()
                                 .set("inference_state", 1)
                                 .set("settlement_status", 1)
                                 .set("settlement_voucher_pushing_status", 1)
                                 .eq("sup_cp_unit_id", pushBillDetailZJS.get(0).getCommunityId())
                                 .in("id",pushBillDetailZJS.stream().map(VoucherPushBillDetailDxZJ::getBillId).collect(Collectors.toList())));
                     }else if (voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.支付申请.getCode()) {
                         List<VoucherPushBillDetailDxZJ> pushBillDetailZJS = voucherBillDetailDxZJRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailDxZJ>()
                                 .eq(VoucherPushBillDetailDxZJ::getVoucherBillNo, voucherBill.getVoucherBillNo())
                                 .eq(VoucherPushBillDetailDxZJ::getDeleted, 0));
                         receivableBillRepository.update(new UpdateWrapper<ReceivableBill>()
                                 .set("inference_state", 1)
                                 .set("pay_app_status", 2)
                                 .set("pay_app_push_status", 2)
                                 .eq("sup_cp_unit_id", pushBillDetailZJS.get(0).getCommunityId())
                                 .in("id",pushBillDetailZJS.stream().map(VoucherPushBillDetailDxZJ::getBillId).collect(Collectors.toList())));
                     }
                } else {
                    log.error("推送财务云推送失败");
                    voucherBill.setPushState(PushBillTypeEnum.推送失败.getCode());
                    ZJDatav zjDatav = JSONObject.parseObject(zjSendresultV.getData(), ZJDatav.class);
                    voucherBill.setRemark(Objects.nonNull(zjDatav) ? zjDatav.getMessage() : null);
                    errorList.add(voucherBill.getId());
                    successCount--;
                    if (voucherBill.getBillEventType() == 2) {
                        String recordIdList = voucherBill.getRecordIdList();
                        List<Long> recordIds = JSON.parseArray(recordIdList, Long.class);
                        billRuleDomainService.updateFlowAfterPush(recordIds, false);

                        voucherBillDetailRepository.update(new LambdaUpdateWrapper<VoucherPushBillDetailZJ>()
                                .eq(VoucherPushBillDetailZJ::getVoucherBillNo, voucherBill.getVoucherBillNo())
                                .set(VoucherPushBillDetailZJ::getPushBillState, 3));
                    }
                }
            } catch (OwlBizException ex){
                log.error("推送财务云凭证推送失败OwlBizException ---------> : ", ex);
              throw ex;
            } catch (Exception e) {
                log.error("推送财务云凭证推送失败 ---------> : ", e);
                voucherBill.setPushState(PushBillTypeEnum.推送失败.getCode());
                voucherBill.setRemark(e.getMessage());
                errorList.add(voucherBill.getId());
                successCount--;
                successCount--;
                voucherBillDetailRepository.update(new LambdaUpdateWrapper<VoucherPushBillDetailZJ>()
                        .eq(VoucherPushBillDetailZJ::getVoucherBillNo, voucherBill.getVoucherBillNo())
                        .set(VoucherPushBillDetailZJ::getPushBillState, 3));
            }
            voucherBill.setFinanceId(null);
            voucherBill.setFinanceNo(null);
            log.info("推送财务云推送完成后更改主表逻辑数据：{}", JSONArray.toJSON(voucherBill));
            voucherPushBillDxZJRepository.updateById(voucherBill);

        }
        SyncBatchVoucherResultV result = new SyncBatchVoucherResultV();
        String level = "success";
        if (successCount > 0) {
            level = "warn";
        } else if (successCount <= 0) {
            level = "error";
        }
        result.setLevel(level);
        result.setErrorTotal(voucherBills.size() - successCount);
        result.setSuccessTotal(successCount);
        result.setErrorList(errorList);
        result.setOaUrl(oaUrl);
        return result;
    }

    public String getPushCaiWuYunData(SyncBatchPushZJBillF syncBatchPushBillF) {
        log.info("推送财务云调用推送逻辑syncBatchPushBillForJTAndSQ");
        List<VoucherBillDxZJ> voucherBills = voucherPushBillDxZJRepository.list(new LambdaQueryWrapper<VoucherBillDxZJ>()
                .in(VoucherBillDxZJ::getId, syncBatchPushBillF.getVoucherIds())
                .in(VoucherBillDxZJ::getPushState, 1, 3, 4,PushBillTypeEnum.制单失败.getCode(), PushBillTypeEnum.单据驳回.getCode())
                .eq(VoucherBillDxZJ::getDeleted, 0));
        if (null == syncBatchPushBillF.getUserId() || StringUtils.isEmpty(syncBatchPushBillF.getUserId())){
            IdentityInfo identityInfo = ThreadLocalUtil.curIdentityInfo();
            syncBatchPushBillF.setUserId(identityInfo.getUserId());
        }
        log.info("推送财务云调用推送逻辑syncBatchPushBillForJTAndSQ用户封装完毕：{}",syncBatchPushBillF.getUserId());
        UserInfoRv userInfo = userClient.getUserInfo(syncBatchPushBillF.getUserId(), null);
        log.info("推送财务云调用推送逻辑syncBatchPushBillForJTAndSQ用户封装userInfo：{}",JSONArray.toJSON(userInfo));
        String empCode = externalClient.getIphoneByEmpCode(userInfo.getMobileNum());
        log.info("推送财务云调用推送逻辑syncBatchPushBillForJTAndSQ用户封装empCode：{}",JSONArray.toJSON(empCode));
        List<Long> errorList = new ArrayList<>();
        int successCount = voucherBills.size();
        String oaUrl = null;
        for (VoucherBillDxZJ voucherBill : voucherBills) {
            String projectId = "";
            try  {
                SpaceCommunityShortV spaceCommunityShortVS = spaceClient.get(this.communityIdForZJ(voucherBill));
                if (Objects.nonNull(spaceCommunityShortVS)) {
                    projectId = spaceCommunityShortVS.getSerialNumber();
                }
            } catch (Exception e) {
                log.error("推送财务云syncBatchPushBillForJTAndSQ获取数据异常 {}", JSONArray.toJSON(voucherBill));
            }

            List<SPRZData> SPRZDataList = new ArrayList<>();
            // 判断是否审批
            assemblyApproveInfoOnBpm(voucherBill, SPRZDataList);
            assemblyApproveInfoOnOa(voucherBill, SPRZDataList);

            log.info("推送财务云中交项目id {},参数 {}", projectId, this.communityIdForZJ(voucherBill));
            try {
                List<String> detailNumberS = Lists.newArrayList();
                String zjRequestBodyString = "";
                List<String> ftIdList = Lists.newArrayList();
                if(voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.对下结算计提.getCode() ||
                        voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode() ||
                        voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.对下结算实签.getCode()){
                    // 1 收入确认单
                    List<DxPaymentDetails> dxPaymentDetailsList = voucherBillDxDetailAppService.queryDetailsOfPayments(voucherBill.getVoucherBillNo());
                    // 先做报文组装，内部有校验，没问题才能往后走
                    zjRequestBodyString = financialRequestBodyService.getDxRequestBody(voucherBill,
                            dxPaymentDetailsList, syncBatchPushBillF, projectId, empCode, SPRZDataList, ftIdList);
                } else if (voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.收入确认实签.getCode() || voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.收入确认计提.getCode()  ||
                        voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode()) {
                    //收入确认单单实签
                    // 判断是否需要审批
                    ApproveFilter approveFilter = new ApproveFilter();
                    approveFilter.setApproveWay(2);
                    // 1 收入确认单
                    log.info("推送财务云approveFilter approveWay is:{}",approveFilter.getApproveWay());
                    if (approveFilter.getApproveWay() == 2  || (null !=  syncBatchPushBillF.getApproveFlag() && syncBatchPushBillF.getApproveFlag().equals(1))) {
                        List<DxPaymentDetails> dxPaymentDetailsList = voucherBillDxDetailAppService.queryDetailsOfPayments(voucherBill.getVoucherBillNo());
                        List<GeneralDetails> generalRevenueRecognition = voucherBillDxDetailAppService.generalRevenueRecognition(voucherBill.getVoucherBillNo());
                        zjRequestBodyString =
                                this.zjSRQRRequestBodyForPayIncome(projectId,
                                        ZJTriggerEventBillTypeEnum.valueOfByCode(voucherBill.getBillEventType()),
                                        voucherBill, syncBatchPushBillF, empCode, detailNumberS, SPRZDataList,
                                        dxPaymentDetailsList,generalRevenueRecognition,voucherBill.getBillEventType(),
                                        ftIdList);
                    } else {
                        log.info("推送财务云approveFilter is not approve:{}",approveFilter.getApproveWay());
                    }

                }
                else if (voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.支付申请.getCode()) {
                    ApproveFilter approveFilter = new ApproveFilter();
                    approveFilter.setApproveWay(2);
                    log.info("pay application approveFilter approveWay is:{}",approveFilter.getApproveWay());
                    if (approveFilter.getApproveWay() == 2  || (null !=  syncBatchPushBillF.getApproveFlag() && syncBatchPushBillF.getApproveFlag().equals(1))) {
                        zjRequestBodyString = financialRequestBodyService.getZFSQRequestBody(voucherBill, syncBatchPushBillF,
                                projectId, empCode, SPRZDataList, ftIdList);
                    } else {
                        log.info("approveFilter is not approve:{}",approveFilter.getApproveWay());
                    }

                }
                log.info("推送财务云zjRequestBodyStringStart:{}",zjRequestBodyString);
                return zjRequestBodyString;
            } catch (OwlBizException ex){
                log.error("推送财务云凭证推送失败OwlBizException ---------> : ", ex);
                throw ex;
            } catch (Exception e) {
                log.error("推送财务云凭证推送失败 ---------> : ", e);
                voucherBill.setPushState(PushBillTypeEnum.推送失败.getCode());
                voucherBill.setRemark(e.getMessage());
                errorList.add(voucherBill.getId());
                successCount--;
                successCount--;
                voucherBillDetailRepository.update(new LambdaUpdateWrapper<VoucherPushBillDetailZJ>()
                        .eq(VoucherPushBillDetailZJ::getVoucherBillNo, voucherBill.getVoucherBillNo())
                        .set(VoucherPushBillDetailZJ::getPushBillState, 3));
            }
        }
        return null;
    }


    //获取业务事由默认数据
    public HashMap<String,String> getReceiptRemark(List<String> voucherBillNoList){
        HashMap<String,String> remarkList = new HashMap<>();
        remarkList.put("默认","默认");
        if(CollectionUtils.isEmpty(voucherBillNoList)){
            return remarkList;
        }
        //根据报账单号获取去重后封装数据
        List<VoucherPushBillDetailDxZJ> detailList = voucherBillDetailDxZJMapper.getbillDetailDxZjList(voucherBillNoList);
        for(VoucherPushBillDetailDxZJ det : detailList){
            //项目名称
            String remark = "【业财合同】" + det.getCommunityName();
            //合同名称
            if(ZJTriggerEventBillTypeEnum.收入确认计提.getCode() != det.getBillEventType()){
                remark = remark + (StringUtils.isNotBlank(det.getContractName())  ? "-" + det.getContractName() : "");
            }
            //归属月
            if(Objects.nonNull(det.getAccountDate())) {
                remark = remark + "-"+ det.getAccountDate().format(DateTimeFormatter.ofPattern("yyyy年MM月"));
            }
            if(Arrays.asList(ZJTriggerEventBillTypeEnum.对下结算计提.getCode(),ZJTriggerEventBillTypeEnum.对下结算实签.getCode(),ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode()).contains(det.getBillEventType())){
                remark = remark + "-成本";
            }else if(Arrays.asList(ZJTriggerEventBillTypeEnum.收入确认计提.getCode(),ZJTriggerEventBillTypeEnum.收入确认实签.getCode(),ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode()).contains(det.getBillEventType())){
                remark = remark + "-收入";
            }else{
                remark = remark + "-业务申请-¥";
            }
            if(ZJTriggerEventBillTypeEnum.对下结算计提.getCode() == det.getBillEventType()){
                remark = remark +"计提-¥";
            }else if(ZJTriggerEventBillTypeEnum.对下结算实签.getCode() == det.getBillEventType()){
                remark = remark +"确认-¥";
            }else if(ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode() == det.getBillEventType()){
                remark = remark +"冲销-¥";
            }else if(ZJTriggerEventBillTypeEnum.收入确认计提.getCode() == det.getBillEventType()){
                remark = remark +"计提-¥";
            }else if(ZJTriggerEventBillTypeEnum.收入确认实签.getCode() == det.getBillEventType()){
                remark = remark +"确认¥-";
            }else if(ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode() == det.getBillEventType()){
                remark = remark +"冲销-¥";
            }
            if(ZJTriggerEventBillTypeEnum.对下结算计提.getCode() == det.getBillEventType()){
                remark = remark + det.getTaxExcludAmount().movePointLeft(2).stripTrailingZeros().toPlainString()+"元";
            }else{
                remark = remark + det.getTaxIncludAmount().movePointLeft(2).stripTrailingZeros().toPlainString()+"元";
            }

            remarkList.put(det.getVoucherBillNo(),remark);
        }
        return remarkList;
    }

    /*private void lockMdm63(Long voucherBillId, String voucherBillNo, List<String> ftIdList) {
        // ftIdList去重复
        ftIdList = ftIdList.stream().distinct().collect(Collectors.toList());
        for (String ftId : ftIdList) {
            Mdm63LockE mdm63LockE = new Mdm63LockE();
            mdm63LockE.setId(IdentifierFactory.getInstance().generateLongIdentifier("mdm63_lock"));
            mdm63LockE.setFtId(ftId);
            mdm63LockE.setVoucherBillId(voucherBillId);
            mdm63LockE.setVoucherBillNo(voucherBillNo);
            //先删除原数据在保存
            mdm63LockMapper.deleteMdm63Lock(ftId, voucherBillNo);
            mdm63LockMapper.insert(mdm63LockE);
        }
    }*/

    /**
     * 获取OA审批信息
     **/
    private void assemblyApproveInfoOnOa(VoucherBillDxZJ voucherBill, List<SPRZData> sprzDataList) {
        if (voucherBill.getBillEventType() != ZJTriggerEventBillTypeEnum.对下结算实签.getCode() &&
            voucherBill.getBillEventType() != ZJTriggerEventBillTypeEnum.支付申请.getCode()){
            return;
        }
        Long mainDataId = voucherBill.getId();
        Integer oaApproveType = BusinessProcessType.DX_JS_FORM.getCode();
        String receiptRemark = voucherBill.getReceiptRemark();
        if (voucherBill.getBillEventType() == ZJTriggerEventBillTypeEnum.支付申请.getCode()) {
            mainDataId = Long.valueOf(voucherBill.getPayAppId());
            oaApproveType = BusinessProcessType.PAYMENT_APP_FORM.getCode();
            PaymentApplicationFormZJ paymentApplicationFormZJ = paymentApplicationFormRepository.getById(voucherBill.getPayAppId());
            if (Objects.isNull(paymentApplicationFormZJ)){
                log.error("支付申请单{}不存在", voucherBill.getPayAppId());
                return;
            }
            receiptRemark = paymentApplicationFormZJ.getBusinessReasons();
        }
        FinanceProcessRecordZJ record = recordZJRepository.getOne(Wrappers.<FinanceProcessRecordZJ>lambdaQuery()
                .eq(FinanceProcessRecordZJ::getMainDataId, mainDataId)
                .eq(FinanceProcessRecordZJ::getType, oaApproveType));
        if (ObjectUtils.isEmpty(record) || StringUtils.isBlank(record.getProcessId())) {
            log.error("报账单{}没有流程信息", voucherBill.getVoucherBillNo());
            return;
        }
        OpinionApprovalV2 opinionApprovalV2 = recordZJRepository.opinionApprovalV2(record.getProcessId());
        log.info("报账单{}的OA审批信息：{}", voucherBill.getVoucherBillNo(), opinionApprovalV2);
        if (ObjectUtils.isEmpty(opinionApprovalV2) || CollectionUtils.isEmpty(opinionApprovalV2.getET_RESULT())){
            log.error("报账单{}的OA审批信息异常", voucherBill.getVoucherBillNo());
            return;
        }
        // OA反馈，此处只使用 提交、退回、转办 ,同意
        List<OpinionApprovalResultV2> etResult = opinionApprovalV2.getET_RESULT();
        etResult.removeIf(v2 -> !StringUtils.equals("2",v2.getOPERATETYPE()) &&
                !StringUtils.equals("3",v2.getOPERATETYPE()) &&
                !StringUtils.equals("h",v2.getOPERATETYPE()) &&
                !StringUtils.equals("0",v2.getOPERATETYPE()));
        if (CollectionUtils.isEmpty(etResult)){
            return;
        }
        // 倒序遍历
        for (int i = etResult.size() - 1; i >= 0; i--) {
            OpinionApprovalResultV2 v2 = etResult.get(i);
            SPRZData sprzData = new SPRZData();
            sprzData.setSPRZID(mainDataId.toString());
            sprzData.setRWMC(receiptRemark);
            sprzData.setJDMC(v2.getEXAMROLE());
            sprzData.setSPR(v2.getOPERATOR4ACODE());
            // 2 提交 3 退回 h 转办
            if (StringUtils.equals("2",v2.getOPERATETYPE())){
                sprzData.setSPDZ("提交");
            } else if (StringUtils.equals("3",v2.getOPERATETYPE())){
                sprzData.setSPDZ("退回");
            } else if (StringUtils.equals("h",v2.getOPERATETYPE())){
                sprzData.setSPDZ("转办");
            } else if (StringUtils.equals("0", v2.getOPERATETYPE())){
                sprzData.setSPDZ("同意");
            }
            if (StringUtils.equals("1",v2.getISSTART())){
                sprzData.setSPYJ("发起审批");
            } else {
                sprzData.setSPYJ(StringUtils.isBlank(v2.getEXAMOPINION()) ? "同意": v2.getEXAMOPINION());
            }
            sprzData.setKSRQ(v2.getRECEIVEDATE());
            sprzData.setSPRQ(v2.getEXAMDATE());
            sprzDataList.add(sprzData);
        }
    }

    private void assemblyApproveInfoOnBpm(VoucherBillDxZJ voucherBill, List<SPRZData> SPRZDataList) {
        if (StringUtils.isBlank(voucherBill.getProcInstId())){
            return;
        }
        ProcessProgressV processFormAndInstanceProgress = bpmClient.getProcessFormAndInstanceProgress(voucherBill.getProcInstId(), "");
        List<Progress> progress = processFormAndInstanceProgress.getProgress();
        for (Progress progress1 : progress) {
            if (null != progress1.getResult() && ("ROOT").equals(progress1.getNodeType().toString())) {
                SPRZData sprzData = new SPRZData();
                sprzData.setSPRZID(voucherBill.getProcInstId());
                sprzData.setRWMC(processFormAndInstanceProgress.getProcessDefName());
                sprzData.setJDMC(progress1.getName());
                UserInfoRv userInfo1 = userClient.getUserInfo(progress1.getUser().getId(), null);
                String byEmpCode = externalClient.getIphoneByEmpCode(userInfo1.getMobileNum());
                sprzData.setSPR(byEmpCode);
                sprzData.setSPDZ("提交");
                sprzData.setSPYJ("提交审批");
                sprzData.setKSRQ(String.valueOf(progress1.getStartTime()));
                sprzData.setSPRQ(String.valueOf(progress1.getFinishTime()));
                SPRZDataList.add(sprzData);
            } else if ("agree".equals(progress1.getResult()) && ("APPROVAL").equals(progress1.getNodeType().toString())) {
                SPRZData sprzData = new SPRZData();
                sprzData.setSPRZID(voucherBill.getProcInstId());
                sprzData.setRWMC(processFormAndInstanceProgress.getProcessDefName());
                sprzData.setJDMC(progress1.getName());
                try {
                    sprzData.setSPR(externalClient.getIphoneByEmpCode(userClient.getUserInfo(progress1.getUser().getId(), null).getMobileNum()));
                } catch (Exception e) {
                    // 请求失败，跳过当前迭代
                    continue;
                }
                 sprzData.setSPDZ("同意");
                String string = progress1.getComment().toString();
                Pattern pattern = Pattern.compile("text=([^,}]*)");
                Matcher matcher = pattern.matcher(string);
                if (matcher.find()) {
                    String textValue = matcher.group(1);
                    // 移除前导空格
                    textValue = textValue.trim();
                    sprzData.setSPYJ(textValue);
                } else {
                    sprzData.setSPYJ("同意");
                }
                sprzData.setKSRQ(String.valueOf(progress1.getStartTime()));
                sprzData.setSPRQ(String.valueOf(progress1.getFinishTime()));
                SPRZDataList.add(sprzData);
            }
        }

    }


}

