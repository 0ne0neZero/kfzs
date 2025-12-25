package com.wishare.finance.apps.service.pushbill;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.signature.ExternalMaindataCalmappingListF;
import com.wishare.finance.apps.model.signature.ExternalMaindataCalmappingListV;
import com.wishare.finance.apps.model.voucher.vo.SyncBatchVoucherResultV;
import com.wishare.finance.apps.model.voucher.vo.VoucherBusinessReasonsV;
import com.wishare.finance.apps.pushbill.fo.PushInfoF;
import com.wishare.finance.apps.pushbill.fo.SyncBatchPushZJBillF;
import com.wishare.finance.apps.pushbill.fo.UploadLinkZJF;
import com.wishare.finance.apps.pushbill.vo.PushInfoRspV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJDo2;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJRecDetailV2;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJV;
import com.wishare.finance.apps.scheduler.mdm.Mdm11Handler;
import com.wishare.finance.apps.scheduler.mdm.Mdm63Handler;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.repository.BillAdjustRepository;
import com.wishare.finance.domains.bill.repository.GatherBillRepository;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.mdm.entity.Mdm63E;
import com.wishare.finance.domains.mdm.repository.mapper.Mdm63Mapper;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimRecordE;
import com.wishare.finance.domains.reconciliation.enums.FlowClaimRecordApproveStateEnum;
import com.wishare.finance.domains.reconciliation.enums.FlowClaimStatusEnum;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimRecordRepository;
import com.wishare.finance.domains.reconciliation.repository.FlowDetailRepository;
import com.wishare.finance.domains.voucher.consts.enums.VoucherApproveStateEnum;
import com.wishare.finance.domains.voucher.dto.ApproveResultDto;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.PushBillTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.service.BillRuleDomainService;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.*;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.PushBillApproveStateEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.*;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.Mdm63LockMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherBillZJRecDetailMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.OrderDealResult;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.OrderStatusResult;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.PushBillZJDomainService;
import com.wishare.finance.infrastructure.remote.clients.base.*;
import com.wishare.finance.infrastructure.remote.enums.OperateTypeEnum;
import com.wishare.finance.infrastructure.remote.fo.zj.OrderStatusBody;
import com.wishare.finance.infrastructure.remote.fo.zj.OrderStatusDel;
import com.wishare.finance.infrastructure.remote.vo.bpm.ProcessProgressV;
import com.wishare.finance.infrastructure.remote.vo.bpm.Progress;
import com.wishare.finance.infrastructure.remote.vo.config.CfgExternalDataV;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractPayPlanInnerInfoV;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractSettlementF;
import com.wishare.finance.infrastructure.remote.vo.contract.ZJFileVo;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceCostRv;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceCommunityShortV;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.finance.infrastructure.utils.FileUtil;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.enums.GatewayTagEnum;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import groovy.lang.Lazy;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PushBillZJAppService implements ApiBase {

    @Value("${wishare.file.host:}")
    private String fileHost;


    private final PushBillZJDomainService pushBillZJDomainService;
    private final BillRuleDomainService billRuleDomainService;
    private final FlowClaimRecordRepository flowClaimRecordRepository;
    private final VoucherBillDetailZJRepository voucherBillDetailZJRepository;
    private final VoucherBillDetailDxZJRepository voucherBillDetailDxZJRepository;
    private final GatherBillRepository gatherBillRepository;
    private final FlowDetailRepository flowDetailRepository;
    private final ContractClient contractClient;
    private final OrgClient orgClient;
    private final ConfigClient configClient;
    private final BpmClient bpmClient;
    private final VoucherContractInvoiceZJRepository voucherContractInvoiceZJRepository;
    private final ExternalClient externalClient;
    private final VoucherBillFileZJRepository voucherBillFileZJRepository;
    private final ReceivableBillRepository receivableBillRepository;
    private final BillAdjustRepository billAdjustRepository;
    private final SpaceClient spaceClient;
    private final VoucherPushBillZJRepository voucherPushBillZJRepository;
    private final VoucherPushBillDxZJRepository voucherPushBillDxZJRepository;
    @Autowired
    @Lazy
    private PushBillZJAppService pushBillZJAppService;
    private final VoucherBillDetailAppService voucherBillDetailAppService;
    private final VoucherBillZJRecDetailMapper recDetailMapper;

    private final Mdm63Handler mdm63Handler;
    private final Mdm63Mapper mdm63Mapper;
    private final Mdm63LockMapper mdm63LockMapper;

    public Page<VoucherBillZJDo2> pageBySearch(PageF<SearchF<?>> form) {
      return  voucherPushBillZJRepository.pageBySearch2(form);
    }


    public VoucherBillZJV getById (Long id){
        VoucherBillZJ byId = voucherPushBillZJRepository.getById(id);
        VoucherBillZJV map = Global.mapperFacade.map(byId, VoucherBillZJV.class);
        if (byId.getBillEventType().equals(2)) {
            String recordIdList = byId.getRecordIdList();
            List<Long> recordIds = JSON.parseArray(recordIdList, Long.class);
            List<FlowClaimRecordE> flowClaimRecordES = flowClaimRecordRepository.listByIds(recordIds);
            map.setBankAccount(flowClaimRecordES.get(0).getOurAccount());
            // 查询所有的明细
            List<VoucherPushBillDetailZJ> byVoucherBillNo = voucherBillDetailZJRepository.getByVoucherBillNo(map.getVoucherBillNo());
            VoucherBusinessReasonsV byVoucherDetail = getByVoucherDetail(byVoucherBillNo);
            if (Objects.nonNull(byVoucherDetail)) {
                map.setBusinessReasons(byVoucherBillNo.get(0).getCommunityName() +",收款日期:" + byVoucherDetail.getPayTime() +",银行到账日期:"  + byVoucherDetail.getAccountTime());
            }
            if (StringUtils.isNotBlank(byId.getReceiptRemark())){
                map.setBusinessReasons(byId.getReceiptRemark());
            }
            long sum = byVoucherBillNo.stream().filter(s -> s.getTaxIncludAmount() > 0).mapToLong(VoucherPushBillDetailZJ::getTaxIncludAmount).sum();
            map.setTotalAmount(BigDecimal.valueOf(sum).divide(new BigDecimal("100")).setScale(2));
            return map;
        } else if (byId.getBillEventType().equals(3)) {
            PageF<SearchF<?>> pageF = new PageF<>();
            SearchF<?> searchF = new SearchF<>();
            List<Field> fields = new ArrayList<>();
            fields.add(new Field("voucher_bill_id",byId.getId(),1));
            searchF.setFields(fields);
            pageF.setConditions(searchF);
            List<VoucherContractInvoiceZJ> voucherContractInvoiceZJS = voucherContractInvoiceZJRepository.selectBySearchNoPage(pageF);
            map.setTotalAmount(BigDecimal.valueOf(byId.getTotalAmount()).divide(new BigDecimal("100")).setScale(2));
            map.setContractNo(voucherContractInvoiceZJS.get(0).getContractNo());
            map.setSupplier(voucherContractInvoiceZJS.get(0).getSupplier());
            List<VoucherPushBillDetailZJ> byVoucherBillNo = voucherBillDetailZJRepository.getByVoucherBillNoNoSearch(map.getVoucherBillNo());
            String communityName = byVoucherBillNo.get(0).getCommunityName();
            map.setBusinessReasons(communityName + "，结算日期：" + DateTimeUtil.formatDateTime(map.getGmtModify()));
            VoucherContractInvoiceZJ byVoucherBillId = voucherContractInvoiceZJRepository.getByVoucherBillId(map.getId());
            if (null != byVoucherBillId){
                map.setBusinessUnitName(byVoucherBillId.getBusinessName());
                map.setBusinessType(byVoucherBillId.getBusinessName());
            }
            return map;
        } else if (byId.getBillEventType().equals(1)){
            map.setTotalAmount(BigDecimal.valueOf(byId.getTotalAmount()).divide(new BigDecimal("100")).setScale(2));
            VoucherPushBillDetailZJ byVoucherBillNo = voucherBillDetailZJRepository.getByVoucherBillNoNoSearchOne(map.getVoucherBillNo());
            if (null != byVoucherBillNo) {
                List<CfgExternalDataV> community = configClient.getExternalMapByCode("community", byVoucherBillNo.getCommunityId());
                // 调用外部数据映射接口
                // 调用根据行政组织获取核算组织接口
                String dataCode = null;
                for (CfgExternalDataV cfgExternalDataV : community) {
                    if ("org".equals(cfgExternalDataV.getExternalDataType())){
                        map.setUnitName(cfgExternalDataV.getDataName());
                        dataCode = cfgExternalDataV.getDataCode();
                    } else if ("department".equals(cfgExternalDataV.getExternalDataType())){
                        map.setDepartmentName(cfgExternalDataV.getDataName());
                    }
                }
                ExternalMaindataCalmappingListF externalMaindataCalmappingListF = new ExternalMaindataCalmappingListF();
                externalMaindataCalmappingListF.setZorgid(dataCode);
                ExternalMaindataCalmappingListV list1 = externalClient.list(externalMaindataCalmappingListF);
                if (null != list1 && list1.getInfoList().size() > 0) {
                    map.setOrganizationName(list1.getInfoList().get(0).getZaorgno());
                }
            }
            //用户选择部门，默认显示用户部门信息
            if(StringUtils.isNotEmpty(map.getExternalDepartmentCode())){
                map.setDepartmentName(configClient.getDeportNameByCode(map.getExternalDepartmentCode()));
            }
            map.setBusinessType(byId.getBusinessTypeName());
            return map;
        } else {
            return map;
        }
    }
    public SyncBatchVoucherResultV syncBatchPushBill(SyncBatchPushZJBillF syncBatchPushBillF) {
        List<VoucherBillZJ> voucherBills = voucherPushBillZJRepository.list(new LambdaQueryWrapper<VoucherBillZJ>()
                .in(VoucherBillZJ::getId, syncBatchPushBillF.getVoucherIds())
                .in(VoucherBillZJ::getPushState, 1, 3, 4, PushBillTypeEnum.制单失败.getCode(), PushBillTypeEnum.单据驳回.getCode())
                .eq(VoucherBillZJ::getDeleted, 0));
        if (CollectionUtils.isEmpty(voucherBills)){
            throw new BizException(400, "未查询到报账单");
        }
        SpaceCommunityShortV spaceCommunityShortVS = spaceClient.get(pushBillZJDomainService.communityId(voucherBills.get(0)));
        List<CfgExternalDataV> community = configClient.getExternalMapByCode("community", spaceCommunityShortVS.getId());
        String xzzz = null;
        String xzbm = null;
        List<String> collect = community.stream().map(CfgExternalDataV::getExternalDataType).collect(Collectors.toList());
        if (!collect.contains("department")){
            throw new BizException(400, "未维护行政部门");
        }
        if (!collect.contains("org")){
            throw new BizException(400, "未维护行政组织");
        }
        for (CfgExternalDataV cfgExternalDataV : community) {
            if("org".equals(cfgExternalDataV.getExternalDataType())){
                xzzz = cfgExternalDataV.getDataCode();
            }
            if ("department".equals(cfgExternalDataV.getExternalDataType())){
                xzbm = cfgExternalDataV.getDataCode();
            }
        }
        syncBatchPushBillF.setXZZZ(xzzz);
        syncBatchPushBillF.setXZBM(xzbm);
       return pushBillZJDomainService.syncBatchPushBill(syncBatchPushBillF);
    }
    public void queryFinanceOrderDealResult(OrderStatusBody orderStatusBody) {
        pushBillZJDomainService.queryFinanceOrderDealResult(orderStatusBody);
    }

    public OrderDealResult queryOrderStatus(OrderStatusBody orderStatusBody) {
       return pushBillZJDomainService.queryOrderStatus(orderStatusBody);
    }

    public OrderStatusResult delOrderDealResult(OrderStatusDel orderStatusDel) {
       return pushBillZJDomainService.delOrderDealResult(orderStatusDel);
    }

    @Transactional
    public void approveAgree(Long voucherBillId, String procInstId) {
        VoucherBillZJ voucherBillZJ = pushBillZJDomainService.getById(voucherBillId);
        log.info("voucherBillZJ:"+ JSON.toJSON(voucherBillZJ));
        // 主表状态刷新
        pushBillZJDomainService.approveAgree(voucherBillId);
        // 判断报账单类型
        if (voucherBillZJ.getBillEventType().equals(2)){
            // 修改流水认领记录状态 修改流水状态
            String recordIdListString = voucherBillZJ.getRecordIdList();
            List<Long> recordIds = JSON.parseArray(recordIdListString, Long.class);
            billRuleDomainService.updateFlow(recordIds, FlowClaimRecordApproveStateEnum.已审核,
                    FlowClaimStatusEnum.已认领, true);
            //
            billRuleDomainService.updateReconcileStateAfterApprove(recordIds, true);
            // 修改该批次收款单状态 为已推送
//            List<VoucherPushBillDetailZJ> pushBillDetailZJS = voucherBillDetailZJRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
//                    .eq(VoucherPushBillDetailZJ::getVoucherBillNo, voucherBillZJ.getVoucherBillNo())
//                    .eq(VoucherPushBillDetailZJ::getDeleted, 0));
//            gatherBillRepository.update(new UpdateWrapper<GatherBill>().set("inference_state", 1)
//                    .eq("sup_cp_unit_id", pushBillDetailZJS.get(0).getCommunityId())
//                    .in("id", pushBillDetailZJS.stream().map(VoucherPushBillDetailZJ::getGatherBillId).collect(Collectors.toList())));


            // 查询文件上传记录 传到影像系统
            List<FlowClaimRecordE> flowClaimRecordES = flowClaimRecordRepository.listByIds(recordIds);
            List<ZJFileVo> zjFileVos = new ArrayList<>();
            for (FlowClaimRecordE flowClaimRecordE : flowClaimRecordES) {
                String flowFiles = flowClaimRecordE.getFlowFiles();
                JSONArray flowFilesObj = JSONObject.parseArray(flowFiles);
                for (int i = 0; i < flowFilesObj.size(); i++) {
                    JSONObject jsonObject = flowFilesObj.getJSONObject(i);
                    String s = fileHost + jsonObject.getString("fileKey");
                    MultipartFile multipartFile = FileUtil.getMultipartFile(s);
                    ZJFileVo zjFileVo = contractClient.zjUpload(multipartFile, voucherBillZJ.getVoucherBillNo());
                    zjFileVo.setName(jsonObject.getString("name"));
                    zjFileVo.setFileKey(jsonObject.getString("fileKey"));
                    zjFileVos.add(zjFileVo);
                }
                String reportFiles = flowClaimRecordE.getReportFiles();
                JSONArray reportFilesObj = JSONObject.parseArray(reportFiles);
                for (int i = 0; i < reportFilesObj.size(); i++) {
                    JSONObject jsonObject = reportFilesObj.getJSONObject(i);
                    String s = fileHost + jsonObject.getString("fileKey");
                    MultipartFile multipartFile = FileUtil.getMultipartFile(s);
                    ZJFileVo zjFileVo = contractClient.zjUpload(multipartFile, voucherBillZJ.getVoucherBillNo());
                    zjFileVo.setName(jsonObject.getString("name"));
                    zjFileVo.setFileKey(jsonObject.getString("fileKey"));
                    zjFileVos.add(zjFileVo);
                }
            }
            List<VoucherBillFileZJ> voucherBillFileZJS = voucherBillFileZJRepository.selectByVoucherBillId(voucherBillZJ.getId());
            for (VoucherBillFileZJ voucherBillFileZJ : voucherBillFileZJS) {
                String files = voucherBillFileZJ.getFiles();
                JSONObject jsonObject = JSONObject.parseObject(files);
                String s = fileHost + jsonObject.getString("fileKey");
                MultipartFile multipartFile = FileUtil.getMultipartFile(s);
                ZJFileVo zjFileVo = contractClient.zjUpload(multipartFile, voucherBillZJ.getVoucherBillNo());
                zjFileVo.setName(jsonObject.getString("name"));
                zjFileVo.setFileKey(jsonObject.getString("fileKey"));
                zjFileVos.add(zjFileVo);
            }
            for (ZJFileVo zjFileVo : zjFileVos) {
                UploadLinkZJF uploadLinkZJF = new UploadLinkZJF();
                uploadLinkZJF.setImageIdZJ(zjFileVo.getFileId());
                uploadLinkZJF.setName(zjFileVo.getName());
                uploadLinkZJF.setUploadLink("/files/" + zjFileVo.getFileKey());
                uploadLinkZJF.setBillNo(voucherBillZJ.getVoucherBillNo());
                voucherPushBillZJRepository.addLinkZJ(uploadLinkZJF);
            }
            if( null != procInstId) {
                // 判断流程id是否为空
                // 自动推送到财务云
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
                IdentityInfo identityInfoDefault = ThreadLocalUtil.get("IdentityInfo",IdentityInfo.class);
                identityInfoDefault.setTenantId("13554968497211");
                identityInfoDefault.setUserId(voucherBillZJ.getCreator());
                identityInfoDefault.setUserName("BPM-内部审批");
                identityInfoDefault.setGateway(GatewayTagEnum.社区运营平台网关.getTag());
                ThreadLocalUtil.set("IdentityInfo", identityInfoDefault);
                ProcessProgressV processFormAndInstanceProgress = bpmClient.getProcessFormAndInstanceProgress(procInstId, "");
                log.info("调用bpm详情信息接口:" + JSON.toJSON(processFormAndInstanceProgress));

                List<Progress> progress = processFormAndInstanceProgress.getProgress();
                for (Progress progress1 : progress) {
                    if ( null != progress1.getResult() && ("ROOT").equals(progress1.getNodeType().toString())){
                        syncBatchPushZJBillF.setUserId(progress1.getUser().getId());
                        break;
                    }
                }
                pushBillZJDomainService.syncBatchPushBill(syncBatchPushZJBillF);
            }
        } else if(voucherBillZJ.getBillEventType().equals(3)){
            // 告知合同系统审批结果
            contractClient.dealContractInvoice(voucherBillId,2 );
        } else if (voucherBillZJ.getBillEventType().equals(1)){
            // 收入确认单处理
            List<VoucherPushBillDetailZJ> pushBillDetailZJS = voucherBillDetailZJRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                    .eq(VoucherPushBillDetailZJ::getVoucherBillNo, voucherBillZJ.getVoucherBillNo())
                    .eq(VoucherPushBillDetailZJ::getDeleted, 0))
                    ;
//            receivableBillRepository.update(new UpdateWrapper<ReceivableBill>().set("inference_state", 1)
//                    .eq("sup_cp_unit_id", pushBillDetailZJS.get(0).getCommunityId())
//                    .in("id",pushBillDetailZJS.stream().map(VoucherPushBillDetailZJ::getBillId).collect(Collectors.toList())));
//            // 对应调整明细更新为已推凭
//            billAdjustRepository.update(new UpdateWrapper<BillAdjustE>().set("inference_state", 1)
//                    .in("bill_id", pushBillDetailZJS.stream().map(VoucherPushBillDetailZJ::getBillId).collect(Collectors.toList())));


            // 获取影像资料
            List<ZJFileVo> zjFileVos = new ArrayList<>();
            List<VoucherBillFileZJ> voucherBillFileZJS = voucherBillFileZJRepository.selectByVoucherBillId(voucherBillZJ.getId());
            for (VoucherBillFileZJ voucherBillFileZJ : voucherBillFileZJS) {
                String files = voucherBillFileZJ.getFiles();
                JSONObject jsonObject = JSONObject.parseObject(files) ;
                String s = fileHost + jsonObject.getString("fileKey");
                MultipartFile multipartFile = FileUtil.getMultipartFile(s);
                ZJFileVo zjFileVo = contractClient.zjUpload(multipartFile, voucherBillZJ.getVoucherBillNo());
                zjFileVo.setName(jsonObject.getString("name"));
                zjFileVo.setFileKey(jsonObject.getString("fileKey"));
                zjFileVos.add(zjFileVo);
            }
            for (ZJFileVo zjFileVo : zjFileVos) {
                UploadLinkZJF uploadLinkZJF = new UploadLinkZJF();
                uploadLinkZJF.setImageIdZJ(zjFileVo.getFileId());
                uploadLinkZJF.setName(zjFileVo.getName());
                uploadLinkZJF.setUploadLink("/files/" + zjFileVo.getFileKey());
                uploadLinkZJF.setBillNo(voucherBillZJ.getVoucherBillNo());
                voucherPushBillZJRepository.addLinkZJ(uploadLinkZJF);
            }
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
            // set业务类型值
            syncBatchPushZJBillF.setYWLX(voucherBillZJ.getBusinessTypeId().trim());
            IdentityInfo identityInfoDefault = ThreadLocalUtil.get("IdentityInfo",IdentityInfo.class);
            identityInfoDefault.setTenantId("13554968497211");
            identityInfoDefault.setUserId(voucherBillZJ.getCreator());
            identityInfoDefault.setUserName("BPM-内部审批");
            identityInfoDefault.setGateway(GatewayTagEnum.社区运营平台网关.getTag());
            ThreadLocalUtil.set("IdentityInfo", identityInfoDefault);
            ProcessProgressV processFormAndInstanceProgress = bpmClient.getProcessFormAndInstanceProgress(procInstId, "");
            log.info("调用bpm详情信息接口:" + JSON.toJSON(processFormAndInstanceProgress));

            List<Progress> progress = processFormAndInstanceProgress.getProgress();
            for (Progress progress1 : progress) {
                if ( null != progress1.getResult() && ("ROOT").equals(progress1.getNodeType().toString())){
                    syncBatchPushZJBillF.setUserId(progress1.getUser().getId());
                    break;
                }
            }
            syncBatchPushZJBillF.setApproveFlag(1);
            pushBillZJDomainService.syncBatchPushBill(syncBatchPushZJBillF);
        }
    }

    @Transactional
    public void approveRefuse(Long voucherBillId,  String procInstId) {
        VoucherBillZJ voucherBillZJ = pushBillZJDomainService.getById(voucherBillId);
        // 主表子表删除
        IdentityInfo identityInfoDefault = ThreadLocalUtil.get("IdentityInfo",IdentityInfo.class);
        identityInfoDefault.setTenantId("13554968497211");
        identityInfoDefault.setUserId(voucherBillZJ.getCreator());
        identityInfoDefault.setUserName("BPM-内部审批");
        identityInfoDefault.setGateway(GatewayTagEnum.社区运营平台网关.getTag());
        ThreadLocalUtil.set("IdentityInfo", identityInfoDefault);

        ProcessProgressV processFormAndInstanceProgress = bpmClient.getProcessFormAndInstanceProgress(procInstId, "");
        log.info("调用bpm详情信息接口:" + JSON.toJSON(processFormAndInstanceProgress));

        List<Progress> progress = processFormAndInstanceProgress.getProgress();
        for (Progress progress1 : progress) {
            // 遍历到拒绝节点
            if ( null != progress1.getResult() && ("refuse").equals(progress1.getResult().toString())){
                String string = progress1.getComment().toString();
                log.info("progress1.getComment().toString():" + string);
                Pattern pattern = Pattern.compile("text=([^,}]*)");
                Matcher matcher = pattern.matcher(string);
                if (matcher.find()) {
                    String textValue = matcher.group(1);
                    // 移除前导空格
                    textValue = textValue.trim();
                    voucherBillZJ.setRemark(textValue);
                } else {
                    voucherBillZJ.setRemark("");
                }
                break;
            }
        }
        voucherBillZJ.setApproveState(2);
        voucherBillZJ.setPushState(5);
        pushBillZJDomainService.delete(voucherBillZJ);
        // 判断报账单类型
        if (voucherBillZJ.getBillEventType().equals(2)){
            // 修改流水认领记录状态 修改流水状态
            String recordIdListString = voucherBillZJ.getRecordIdList();
            List<Long> recordIds = JSON.parseArray(recordIdListString, Long.class);
            billRuleDomainService.updateFlow(recordIds, FlowClaimRecordApproveStateEnum.已审核,
                    FlowClaimStatusEnum.已认领, false);
            billRuleDomainService.updateReconcileStateAfterApprove(recordIds, false);
        } else if(voucherBillZJ.getBillEventType().equals(3)){
            // 告知合同系统审批结果
//            contractClient.dealContractInvoice(voucherBillId,3);
        }
    }

    public VoucherBusinessReasonsV getByVoucherDetail(List<VoucherPushBillDetailZJ> list) {
        VoucherBusinessReasonsV reasonsV = new VoucherBusinessReasonsV();
        if (CollectionUtils.isNotEmpty(list)) {
            String communityId = list.get(0).getCommunityId();
            List<Long> collect = list.stream().map(VoucherPushBillDetailZJ::getGatherBillId).map(Long :: parseLong).distinct().collect(Collectors.toList());
            List<GatherBill> gatherBill = gatherBillRepository.getGatherBill(collect, communityId).stream().sorted(Comparator.comparing(GatherBill::getPayTime)).collect(Collectors.toList());
            if (gatherBill.get(0).getPayTime().compareTo(gatherBill.get(gatherBill.size()- 1).getPayTime()) == 0) {
                reasonsV.setPayTime(DateTimeUtil.formatDateTime(gatherBill.get(0).getPayTime()));
            } else {
                reasonsV.setPayTime(DateTimeUtil.formatDateTime(gatherBill.get(0).getPayTime()) + "-" +  DateTimeUtil.formatDateTime(gatherBill.get(gatherBill.size()- 1).getPayTime()));
            }
            Set<String> strings = flowDetailRepository.selectPayTime(collect);
            for (String string : strings) {
                reasonsV.setAccountTime(string);
            }
        }
        return reasonsV;
    }

    @Transactional
    public  PushInfoRspV voucherInfo(PushInfoF pushInfoF,Integer pushState){
        String bzdnm = pushInfoF.getBZDNM();
        VoucherBillZJ voucherBillZJ = voucherPushBillZJRepository.queryByVoucherBillNo(bzdnm);
        if (Objects.nonNull(voucherBillZJ)) {
            IdentityInfo identityInfo = new IdentityInfo();
            identityInfo.setTenantId(voucherBillZJ.getTenantId());
            ThreadLocalUtil.set("IdentityInfo", identityInfo);
            voucherBillZJ.setRemark(pushInfoF.getREMARK());
            voucherBillZJ.setPushState(pushState);
            voucherPushBillZJRepository.updateById(voucherBillZJ);
            voucherBillDetailZJRepository.updatePushStateByVoucherBIllNo(bzdnm, pushState);
        }
        handleOnVoucherBillV2(bzdnm,pushInfoF.getREMARK(),pushState);
        return new PushInfoRspV();
    }

    /**
     * 处理新-报账单-因为回调处理的口径一定是同一个
     **/
    public void handleOnVoucherBillV2(String voucherBillNo, String remark, Integer pushState) {
        // 凭证生成 删除mdm63锁定
        mdm63LockMapper.deleteByVoucherBillNo(voucherBillNo);
        // 凭证生成 刷新mdm63数据-小业主
        refreshMdm63OnNoContract(voucherBillNo);

        VoucherBillDxZJ voucherBillDxZJ = voucherPushBillDxZJRepository.queryByVoucherBillNo(voucherBillNo);
        if (Objects.isNull(voucherBillDxZJ)){
            return;
        }
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId(voucherBillDxZJ.getTenantId());
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        voucherBillDxZJ.setRemark(remark);
        voucherBillDxZJ.setPushState(pushState);
        voucherPushBillDxZJRepository.updateById(voucherBillDxZJ);
        voucherBillDetailDxZJRepository.updatePushStateByVoucherBIllNo(voucherBillNo, pushState);
        // 若为财务云审核通过，将账单的计提/结算/确收状态修改为 最终状态
        List<VoucherPushBillDetailDxZJ> details = voucherBillDetailDxZJRepository.getByVoucherBillNo(voucherBillNo);
        if (CollectionUtils.isEmpty(details)){
            return;
        }
        Integer eventType = voucherBillDxZJ.getBillEventType();
        if (Objects.isNull(eventType)){
            log.error("异常报账单,报账单类型为空,{}",voucherBillNo);
            return;
        }
        // 若为财务云审核通过,则为 已xx [0未xx 1xx中 2已xx](计提、结算、确收)
        if (pushState == PushBillTypeEnum.单据审核完成.getCode() && !(ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode() == eventType ||
                ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode() == eventType)){
            List<Long> billIdList = details.stream().map(VoucherPushBillDetailDxZJ::getBillId).collect(Collectors.toList());
            String communityId = details.get(0).getCommunityId();
            receivableBillRepository.updateCertainStatusOnVoucherBillApprovedV2(communityId,billIdList,eventType,2);
        }
        //如实签制单成功 修改为已支付
        if (pushState == PushBillTypeEnum.凭证生成.getCode() && ZJTriggerEventBillTypeEnum.支付申请.getCode() == eventType){
            List<Long> billIdList = details.stream().map(VoucherPushBillDetailDxZJ::getBillId).collect(Collectors.toList());
            String communityId = details.get(0).getCommunityId();
            receivableBillRepository.updateCertainStatusOnVoucherBillApprovedV2(communityId,billIdList,eventType,2);
            contractClient.updateStatus(ContractSettlementF.builder().applyStatus(3).settlementIdList(JSON.parseArray(voucherBillDxZJ.getSettlementId(),String.class)).build());
        }
        // 凭证生成 刷新mdm63数据-合同
        refreshMdm63OnContract(details.get(0).getContractId(), eventType);
    }

    private void refreshMdm63OnNoContract(String voucherBillNo) {
        VoucherBillZJ voucherBillZJ = voucherPushBillZJRepository.queryByVoucherBillNo(voucherBillNo);
        if (Objects.isNull(voucherBillZJ)){
            return;
        }
        List<String> ftIds = recDetailMapper.ftIdByVoucherBillNo(voucherBillNo);
        if (CollectionUtils.isEmpty(ftIds)){
            return;
        }
        List<Mdm63E> mdm63List = mdm63Mapper.selectByFtIds(ftIds);
        if (CollectionUtils.isEmpty(mdm63List)){
            return;
        }
        Date start = mdm63List.stream().map(Mdm63E::getLastModifiedTime).min(Comparator.comparing(Date::getTime)).orElse(null);
        Date end = mdm63List.stream().map(Mdm63E::getLastModifiedTime).max(Comparator.comparing(Date::getTime)).orElse(null);
        if (Objects.isNull(start) || Objects.isNull(end)){
            return;
        }
        start = DateUtil.offsetDay(start, -1);
        end = DateUtil.offsetDay(end, 1);
        String startStr = DateUtil.format(start, "yyyy-MM-dd");
        String endStr = DateUtil.format(end, "yyyy-MM-dd");
        List<String> dateList = Mdm11Handler.splitDatesByInterval(startStr, endStr, 28);
        for (int i = 0; i < dateList.size()-1; i++) {
            String curStart = dateList.get(i);
            String curEnd = dateList.get(i+1);
            mdm63Handler.doSync2("9999999999", curStart, curEnd);
        }
        Date now = new Date();
        start = DateUtil.offsetDay(now, -1);
        end = DateUtil.offsetDay(now, 1);
        mdm63Handler.doSync2("9999999999", DateUtil.format(start, "yyyy-MM-dd"), DateUtil.format(end, "yyyy-MM-dd"));
    }

    private void refreshMdm63OnContract(String contractId, Integer billEventType) {
        if (StringUtils.isBlank(contractId)){
            return;
        }
        List<ContractPayPlanInnerInfoV> innerInfos = null;
        if (ZJTriggerEventBillTypeEnum.PAY_CONTRACT_FROM_EVENT_TYPE.contains(billEventType)){
            innerInfos = contractClient.getInnerInfoByContractIdOnPay(Lists.newArrayList(contractId));
        }
        if (ZJTriggerEventBillTypeEnum.INCOME_CONTRACT_FROM_EVENT_TYPE.contains(billEventType)){
            innerInfos = contractClient.getInnerInfoByContractIdOnIncome(Lists.newArrayList(contractId));
        }
        if (CollectionUtils.isEmpty(innerInfos)){
            return;
        }
        ContractPayPlanInnerInfoV innerInfoV = innerInfos.get(0);
        mdm63Handler.doSync(innerInfoV.getConMainCode(), innerInfoV.getGmtExpireStart(), innerInfoV.getGmtExpireEnd());
    }

    //根据报账单号发起审批
    public Boolean initiateBillApprovel(String voucherBillNo) {
        //根据报账单号查询报账单信息
        VoucherBillZJ voucherBillZJ = voucherPushBillZJRepository.queryByVoucherBillNo(voucherBillNo);
        if(Objects.isNull(voucherBillZJ)){
            throw new OwlBizException("报账单不存在,请检查");
        }
        //校验应收款明细中业务科目为必填但未匹配应核销编码的数据
        List<VoucherBillZJRecDetailV2> queryRecDetailPageV2 = voucherBillDetailAppService.queryRecDetailPageV2(voucherBillNo);
        queryRecDetailPageV2 = queryRecDetailPageV2.stream().filter(detail -> (detail.getIsTPPReceivebles() && StringUtils.isBlank(detail.getFtNo())) || (detail.getIsTPPReceivebles() && StringUtils.isNotBlank(detail.getFtNo()) && !detail.getIsSaveData())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(queryRecDetailPageV2)){
            throw new OwlBizException("应收款明细页面-存在未完全核销的业务科目，请检查并提交");
        }

        Long voucherBillId = voucherBillZJ.getId();
        List<Long> claimRecordList = new ArrayList<>();
        VoucherBillZJ billZj =  voucherPushBillZJRepository.queryByVoucherBillNo(voucherBillNo);
        if(Objects.nonNull(billZj) && StringUtils.isNotBlank(billZj.getRecordIdList())){
            claimRecordList = JSON.parseArray(billZj.getRecordIdList(), Long.class);
        }

        //查询流水认领记录
        List<FlowClaimRecordE> flowClaimRecordES = flowClaimRecordRepository.listByIds(claimRecordList);
        if (CollectionUtils.isEmpty(flowClaimRecordES)) {
            throw new OwlBizException("未找到对应的流水认领记录，请检查报账单批次ID是否正确");
        }
        //查询报账明细
        List<VoucherPushBillDetailZJ> billDetailZJ = voucherBillDetailZJRepository.getByVoucherBillNoNoSearch(voucherBillNo);
        try {
            log.info("资金收款单{}发起审批:", voucherBillNo);

            ApproveResultDto approveResultDto = pushBillZJDomainService.initiateApprove(billDetailZJ.get(0).getCommunityId(),
                    OperateTypeEnum.资金收款, voucherBillId, flowClaimRecordES.get(0));
            if (VoucherApproveStateEnum.无需审批.equalsByCode(approveResultDto.getApproveState())) {
                log.info("无需审批");
                pushBillZJAppService.approveAgree(voucherBillId, null);
                return true;
            } else {
                log.info("发起审批结束，进入审批流程");
                voucherPushBillZJRepository.update(new LambdaUpdateWrapper<VoucherBillZJ>()
                        .set(VoucherBillZJ::getApproveState, PushBillApproveStateEnum.审核中.getCode())
                        .eq(VoucherBillZJ::getId, voucherBillId));
                //  更新流水状态
                //  更新流水记录状态
                return billRuleDomainService.updateFlow(claimRecordList,
                        FlowClaimRecordApproveStateEnum.审核中, FlowClaimStatusEnum.报账审核中, false);
            }
        } catch (Exception e) {
            log.error("报账审批错误:报账单id{}", voucherBillId, e);
            pushBillZJAppService.approveRefuse(voucherBillId, null);
            return false;
        }
    }

}
