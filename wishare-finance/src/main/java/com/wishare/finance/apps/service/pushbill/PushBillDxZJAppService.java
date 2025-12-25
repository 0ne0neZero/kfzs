package com.wishare.finance.apps.service.pushbill;



import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.fo.VoucherBillGenerateForContractSettlementContext;
import com.wishare.finance.apps.model.bill.vo.TemporaryChargeBillDetailV;
import com.wishare.finance.apps.model.signature.ExternalMaindataCalmappingListF;
import com.wishare.finance.apps.model.signature.ExternalMaindataCalmappingListV;
import com.wishare.finance.apps.model.voucher.vo.SyncBatchVoucherResultV;
import com.wishare.finance.apps.model.voucher.vo.VoucherAutoFileSheetItem;
import com.wishare.finance.apps.model.voucher.vo.VoucherBillAutoFileApprovalNode;
import com.wishare.finance.apps.pushbill.fo.SyncBatchPushZJBillF;
import com.wishare.finance.apps.pushbill.fo.UploadLinkZJF;
import com.wishare.finance.apps.pushbill.fo.VoucherBillGenerateOnContractSettlementF;
import com.wishare.finance.apps.pushbill.vo.*;
import com.wishare.finance.apps.pushbill.vo.dx.vo.VoucherBillAutoFileZJVo;
import com.wishare.finance.apps.service.bill.PaymentApplicationFormService;
import com.wishare.finance.apps.service.bill.TemporaryChargeBillAppService;
import com.wishare.finance.domains.bill.repository.TemporaryChargeBillRepository;
import com.wishare.finance.domains.invoicereceipt.consts.enums.TaxpayerTypeEnum;
import com.wishare.finance.domains.refund.RefundManageDTO;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.PushBillTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.service.BillRuleDomainService;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.PaymentApplicationFormZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillDxZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillFileZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailDxZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ApproveStatusEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.PushBillApproveStateEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.PaymentApplicationFormRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillDetailDxZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillFileZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherPushBillDxZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherBillDetailDxZJMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.OrderStatusResult;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.PushBillZJDomainService;
import com.wishare.finance.infrastructure.remote.clients.base.*;
import com.wishare.finance.infrastructure.remote.enums.financialcloud.FinancialTaxTypeEnum;
import com.wishare.finance.infrastructure.remote.fo.zj.OrderStatusBody;
import com.wishare.finance.infrastructure.remote.fo.zj.OrderStatusDel;
import com.wishare.finance.infrastructure.remote.vo.bpm.ProgressNode;
import com.wishare.finance.infrastructure.remote.vo.cfg.CfgExternalDeportData;
import com.wishare.finance.infrastructure.remote.vo.config.CfgExternalDataV;
import com.wishare.finance.infrastructure.remote.vo.contract.*;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceCommunityShortV;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceCommunityV2V;
import com.wishare.finance.infrastructure.utils.FileUtil;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.enums.GatewayTagEnum;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum.EXT_UPLOAD_TYPE;
import static com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum.UPLOAD_TYPE;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PushBillDxZJAppService implements ApiBase {

    @Autowired
    private BillRuleDomainService billRuleDomainService;

    private final VoucherPushBillDxZJRepository voucherPushBillDxZJRepository;
    private final VoucherBillDetailDxZJRepository voucherBillDetailDxZJRepository;
    private final ConfigClient configClient;
    private final ExternalClient externalClient;
    private final BpmClient bpmClient;
    private final TemporaryChargeBillRepository temporaryChargeBillRepository;
    private final TemporaryChargeBillAppService temporaryChargeBillAppService;
    private final ContractClient contractClient;
    @Lazy
    @Autowired
    private VoucherBillFileZJRepository voucherBillFileZJRepository;
    private final SpaceClient spaceClient;
    private final OrgClient orgClient;
    @Lazy
    @Autowired
    private PushBillZJDomainService pushBillZJDomainService;
    private final PaymentApplicationFormRepository paymentApplicationFormRepository;
    private final PaymentApplicationFormService paymentApplicationFormService;
    private final VoucherBillDetailAppService voucherBillDetailAppService;
    @Autowired
    private VoucherBillDetailDxZJMapper voucherBillDetailDxZJMapper;

    @Autowired
    private ChargeClient chargeClient;

    @Value("${wishare.file.host:}")
    private String fileHost;


    /**
     *
     * @param id
     * @param backstageFlag 后端请求手动赋值网关采纳数
     * @return
     */
    public VoucherBillDxZJV getById (Long id, Boolean backstageFlag){
        VoucherBillDxZJ byId = voucherPushBillDxZJRepository.getById(id);

        if (backstageFlag) {
            IdentityInfo identityInfoDefault = ThreadLocalUtil.get("IdentityInfo",IdentityInfo.class);
            identityInfoDefault.setTenantId("13554968497211");
            identityInfoDefault.setUserId(byId.getCreator());
            identityInfoDefault.setUserName("BPM-内部审批");
            identityInfoDefault.setGateway(GatewayTagEnum.社区运营平台网关.getTag());
            ThreadLocalUtil.set("IdentityInfo", identityInfoDefault);
        }

        VoucherBillDxZJV map = Global.mapperFacade.map(byId, VoucherBillDxZJV.class);

        map.setTotalAmount(byId.getTotalAmount().divide(new BigDecimal("100")).setScale(2));
        List<VoucherPushBillDetailDxZJ> byVoucherBillNo = voucherBillDetailDxZJRepository.getByVoucherBillNoNoSearch(map.getVoucherBillNo());
        if (CollectionUtils.isEmpty(byVoucherBillNo)){
            return null;
        }
        if(byId.getBillEventType().equals(ZJTriggerEventBillTypeEnum.收入确认计提.getCode())){
            map.setContractId(byVoucherBillNo.stream().map(VoucherPushBillDetailDxZJ :: getContractId).distinct().collect(Collectors.joining(",")));
        }
        List<CfgExternalDataV> community = configClient.getExternalMapByCode("community", byVoucherBillNo.get(0).getCommunityId());
        // 调用外部数据映射接口
        // 调用根据行政组织获取核算组织接口
        String dataCode = null;
        for (CfgExternalDataV cfgExternalDataV : community) {
            if ("org".equals(cfgExternalDataV.getExternalDataType())){
                map.setUnitName(cfgExternalDataV.getDataName());
                map.setUnitCode(cfgExternalDataV.getDataCode());
                dataCode = cfgExternalDataV.getDataCode();
            } else if ("department".equals(cfgExternalDataV.getExternalDataType())){
                map.setDepartmentName(cfgExternalDataV.getDataName());
                map.setDepartmentCode(cfgExternalDataV.getDataCode());
            }
        }
        //若用户选择部门，则显示用户部门数据
        if(StringUtils.isNotEmpty(byId.getExternalDepartmentCode())){
            map.setDepartmentCode(byId.getExternalDepartmentCode());
            map.setDepartmentName(configClient.getDeportNameByCode(byId.getExternalDepartmentCode()));
        }

        map.setCommunityId(byVoucherBillNo.get(0).getCommunityId());
        ExternalMaindataCalmappingListF externalMaindataCalmappingListF = new ExternalMaindataCalmappingListF();
        externalMaindataCalmappingListF.setZorgid(dataCode);
        ExternalMaindataCalmappingListV list = externalClient.list(externalMaindataCalmappingListF);
        if (null != list && list.getInfoList().size() > 0) {
            map.setOrganizationName(list.getInfoList().get(0).getZaorgno());
        }
        map.setBusinessType(byId.getBusinessTypeName());
        map.setSourceSystem("CCCG-DMC");
        map.setExpectedVoucherDate(map.getGmtCreate());

        List<VoucherBillFileZJ> voucherBillFileZJS = voucherBillFileZJRepository.selectByVoucherBillId(
                ZJTriggerEventBillTypeEnum.支付申请.getCode() == byId.getBillEventType() ? Long.parseLong(byId.getPayAppId()) : byId.getId());
        map.setFileSize(voucherBillFileZJS.size());
        getMergeInfo(byVoucherBillNo, byVoucherBillNo.get(0).getCommunityId(), map);

        return map;
    }

    public PageV<VoucherBillZJV> pageBySearch(PageF<SearchF<?>> form) {
        PageV<VoucherBillZJV> resultList = voucherPushBillDxZJRepository.pageBySearch(form);
        if(CollectionUtils.isEmpty(resultList.getRecords())){
            return resultList;
        }
        List<Long> idList = resultList.getRecords().stream().map(VoucherBillZJV::getId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(idList)){
            idList = new ArrayList<>();
        }
        //根据项目ID获取部门List
        List<String> communityIdList = resultList.getRecords().stream().map(VoucherBillZJV::getCommunityId).distinct().collect(Collectors.toList());
        List<CfgExternalDeportData> communityList = configClient.getDeportList(communityIdList);
        Map<String, List<CfgExternalDeportData>> communityMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(communityList)){
            communityMap = communityList.stream().collect(Collectors.groupingBy(CfgExternalDeportData::getCommunityId));
        }

        //获取所有报账单编号
        List<String> voucherBillNoList = resultList.getRecords().stream().map(VoucherBillZJV::getVoucherBillNo).collect(Collectors.toList());
        idList.addAll(resultList.getRecords().stream().map(VoucherBillZJV::getPayAppId).filter(Objects::nonNull)
                .map(Long::valueOf).collect(Collectors.toList()));
        //根据报账单ID获取附件列表
        List<VoucherBillFileZJ> fileList = voucherBillFileZJRepository.getByBillIdList(idList);
        Set<Long> hasFileIds = fileList.stream()
                .map(VoucherBillFileZJ::getVoucherBillId)
                .collect(Collectors.toSet());
        //获取业务事由默认数据
        HashMap<String,String> receiptRemark = pushBillZJDomainService.getReceiptRemark(voucherBillNoList);
        Map<String, List<CfgExternalDeportData>> finalCommunityMap = communityMap;
        resultList.getRecords().forEach(v ->{
            v.setReceiptRemark(StringUtils.isNotBlank(v.getReceiptRemark()) ? v.getReceiptRemark() : receiptRemark.get(v.getVoucherBillNo()));
            v.setIsHaveFile(ZJTriggerEventBillTypeEnum.对下结算实签.getCode() == v.getBillEventType() || hasFileIds.contains(v.getId()) || (StringUtils.isNotBlank(v.getPayAppId()) && hasFileIds.contains(Long.valueOf(v.getPayAppId()))));
            v.setIsShowUpload(Boolean.FALSE);
            if(ZJTriggerEventBillTypeEnum.支付申请.getCode() != v.getBillEventType() && ZJTriggerEventBillTypeEnum.对下结算实签.getCode() != v.getBillEventType() ){
                v.setIsShowUpload(Boolean.TRUE);
                if((ZJTriggerEventBillTypeEnum.对下结算计提.getCode() == v.getBillEventType() ||  ZJTriggerEventBillTypeEnum.收入确认计提.getCode() == v.getBillEventType() || ZJTriggerEventBillTypeEnum.收入确认实签.getCode() == v.getBillEventType()) && Arrays.asList(PushBillApproveStateEnum.审核中.getCode(),PushBillApproveStateEnum.已审核.getCode()).contains(v.getApproveState())){
                    if(ZJTriggerEventBillTypeEnum.收入确认实签.getCode() == v.getBillEventType() && v.getApproveState() == PushBillApproveStateEnum.已审核.getCode() && v.getPushState() == PushBillTypeEnum.单据驳回.getCode()){
                        v.setIsShowUpload(Boolean.TRUE);
                    }else{
                        v.setIsShowUpload(Boolean.FALSE);
                    }
                } else if ((ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode() == v.getBillEventType() || ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode() == v.getBillEventType())
                        && !PushBillTypeEnum.canPushAgain(v.getPushState()) && !v.getPushState().equals(PushBillTypeEnum.待推送.getCode())) {
                    v.setIsShowUpload(Boolean.FALSE);
                }
            }
            //默认匹配部门数据
            if(ZJTriggerEventBillTypeEnum.对下结算计提.getCode() == v.getBillEventType()
                    || ZJTriggerEventBillTypeEnum.收入确认计提.getCode() == v.getBillEventType()
                    || ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode() == v.getBillEventType()
                    || ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode() == v.getBillEventType()
                    || ZJTriggerEventBillTypeEnum.收入确认实签.getCode() == v.getBillEventType()){
                v.setDepartmentList(finalCommunityMap.get(v.getCommunityId()));
            }
            List<VoucherPushBillDetailDxZJ> details = voucherBillDetailDxZJMapper.selectList(new LambdaQueryWrapper<VoucherPushBillDetailDxZJ>()
                    .eq(VoucherPushBillDetailDxZJ::getDeleted,0)
                    .in(VoucherPushBillDetailDxZJ::getVoucherBillNo,v.getVoucherBillNo()));

            if(ZJTriggerEventBillTypeEnum.对下结算计提.getCode() == v.getBillEventType()
                    || ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode() == v.getBillEventType()){
                if(CollectionUtils.isNotEmpty(details)){
                    if(StringUtils.isNotEmpty(details.get(0).getContractId()) || StringUtils.isNotEmpty(details.get(0).getContractNo())){
                        //查合同信息
                        ContractPayConcludeF contractPayConcludeF = new ContractPayConcludeF();
                        contractPayConcludeF.setId(details.get(0).getContractId());
                        contractPayConcludeF.setPid("0");
                        contractPayConcludeF.setContractNo(details.get(0).getContractNo());
                        ContractPayConcludeV contractPayConcludeV = contractClient.get(contractPayConcludeF);
                        log.info("根据合同id：{}，查询对应支出合同信息：{}", details.get(0).getContractId(), contractPayConcludeV);
                        if(Objects.nonNull(contractPayConcludeV) && StringUtils.isNotBlank(contractPayConcludeV.getOurPartyId())){
                            //根据我方单位ID查询对应法定单位信息
                            OrgFinanceRv orgFinanceById = orgClient.getOrgFinanceById(Long.valueOf(contractPayConcludeV.getOurPartyId()));
                            log.info("根据法定单位id：{}，查询对应法定单位信息：{}", contractPayConcludeV.getOurPartyId(), orgFinanceById);
                            if(Objects.nonNull(orgFinanceById)){
                                v.setCalculationMethod(FinancialTaxTypeEnum.SIMPLE.getCode());
                                if(Objects.isNull(orgFinanceById.getTaxpayerType()) || TaxpayerTypeEnum.一般纳税人.getCode().equals(orgFinanceById.getTaxpayerType())){
                                    v.setCalculationMethod(FinancialTaxTypeEnum.GENERAL.getCode());
                                }
                            }else{
                                v.setCalculationMethod(FinancialTaxTypeEnum.GENERAL.getCode());
                            }
                        }else{
                            v.setCalculationMethod(FinancialTaxTypeEnum.GENERAL.getCode());
                        }
                    }else{
                        v.setCalculationMethod(FinancialTaxTypeEnum.GENERAL.getCode());
                    }

                }
            }
            if(ZJTriggerEventBillTypeEnum.收入确认计提.getCode() == v.getBillEventType()
                    || ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode() == v.getBillEventType()
                    || ZJTriggerEventBillTypeEnum.收入确认实签.getCode() == v.getBillEventType()){
                if(CollectionUtils.isNotEmpty(details)){
                    if(StringUtils.isNotEmpty(details.get(0).getContractId()) || StringUtils.isNotEmpty(details.get(0).getContractNo())){
                        ContractIncomeConcludeF contractIncomeConcludeF = new ContractIncomeConcludeF();
                        contractIncomeConcludeF.setId(details.get(0).getContractId());
                        contractIncomeConcludeF.setPid("0");
                        contractIncomeConcludeF.setContractNo(details.get(0).getContractNo());
                        ContractIncomeConcludeV contractIncomeConcludeV = contractClient.get(contractIncomeConcludeF);
                        log.info("根据合同id：{}，查询对应收入合同信息：{}", details.get(0).getContractId(), contractIncomeConcludeV);
                        if(Objects.nonNull(contractIncomeConcludeV) && StringUtils.isNotBlank(contractIncomeConcludeV.getOurPartyId())){
                            //根据我方单位ID查询对应法定单位信息
                            OrgFinanceRv orgFinanceById = orgClient.getOrgFinanceById(Long.valueOf(contractIncomeConcludeV.getOurPartyId()));
                            log.info("根据法定单位id：{}，查询对应法定单位信息：{}", contractIncomeConcludeV.getOurPartyId(), orgFinanceById);
                            if(Objects.nonNull(orgFinanceById)){
                                v.setCalculationMethod(FinancialTaxTypeEnum.SIMPLE.getCode());
                                if(Objects.isNull(orgFinanceById.getTaxpayerType()) || TaxpayerTypeEnum.一般纳税人.getCode().equals(orgFinanceById.getTaxpayerType())){
                                    v.setCalculationMethod(FinancialTaxTypeEnum.GENERAL.getCode());
                                }
                            }else{
                                v.setCalculationMethod(FinancialTaxTypeEnum.GENERAL.getCode());
                            }
                        }else{
                            v.setCalculationMethod(FinancialTaxTypeEnum.GENERAL.getCode());
                        }
                    }else{
                        v.setCalculationMethod(FinancialTaxTypeEnum.GENERAL.getCode());
                    }
                }
            }

            //重新封装列表附件信息
            /*if(CollectionUtils.isNotEmpty(details) && Objects.nonNull(details.get(0).getBillId())){
                TemporaryChargeBillDetailV billInfo = temporaryChargeBillAppService.getById(details.get(0).getBillId(), TemporaryChargeBillDetailV.class, v.getCommunityId());

                //报账单ID
                String voucherBillId = null;
                //报账单编号
                String voucherBillNo = v.getVoucherBillNo();
                //合同Id
                String contractId = null;
                //结算审批单、确收审批单Id
                String settleId = null;
                if(EXT_UPLOAD_TYPE.contains(v.getBillEventType()) && StringUtils.isNotBlank(billInfo.getExtField7())){
                    settleId = billInfo.getExtField7();
                }
                if(StringUtils.isNotBlank(billInfo.getExtField6())){
                    contractId = billInfo.getExtField6();
                }
                if(ZJTriggerEventBillTypeEnum.支付申请.getCode() ==v.getBillEventType()){
                    voucherBillId = v.getPayAppId();
                }
                // 获取影像资料
                PageF<SearchF<?>> filePageSearch = new PageF<>();
                List<Field> fields = getFields(voucherBillId, voucherBillNo, contractId, settleId);
                SearchF<AttachmentE> SearchF = new SearchF<>();
                SearchF.setFields(fields);
                filePageSearch.setConditions(SearchF);
                List<VoucherBillZJFileSV> forRecFileList =voucherBillDetailAppService.queryFileForRec(filePageSearch);
                List<UploadLinkZJ> uploadLinkList = new ArrayList<>();
                if(CollectionUtils.isNotEmpty(forRecFileList)){
                    for (VoucherBillZJFileSV file : forRecFileList) {
                        UploadLinkZJ zjFileVo = new UploadLinkZJ();
                        zjFileVo.setName(file.getFileName());
                        zjFileVo.setUploadLink("/files/" + file.getFileKey());
                        uploadLinkList.add(zjFileVo);
                    }
                }
                v.setUploadLink(uploadLinkList);
            }*/
            List<VoucherBillZJFileSV> voucherBillZJFileSVS = new ArrayList<>();
            List<VoucherBillFileZJ> bzdFileList = fileList.stream().filter(x -> x.getVoucherBillId().equals(v.getId())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(bzdFileList)){
                for (VoucherBillFileZJ voucherBillFileZJ : bzdFileList) {
                    VoucherBillZJFileSV voucherBillZJFileSV = new VoucherBillZJFileSV();
                    String files = voucherBillFileZJ.getFiles();
                    JSONObject jsonObject = JSONObject.parseObject(files);
                    voucherBillZJFileSV.setFileKey(jsonObject.getString("fileKey"));
                    voucherBillZJFileSV.setFileName(jsonObject.getString("name"));
                    voucherBillZJFileSV.setName(jsonObject.getString("name"));
                    voucherBillZJFileSV.setFileFormat(jsonObject.getString("suffix"));
                    voucherBillZJFileSV.setFileSize(jsonObject.getString("size"));
                    voucherBillZJFileSV.setCreatTime(voucherBillFileZJ.getGmtCreate());
                    voucherBillZJFileSV.setCreatName(voucherBillFileZJ.getCreatorName());
                    voucherBillZJFileSV.setId(String.valueOf(voucherBillFileZJ.getId()));
                    voucherBillZJFileSVS.add(voucherBillZJFileSV);
                }
            }
            v.setFileList(voucherBillZJFileSVS);
        });
        return resultList;
    }

    public VoucherBillZJMoneyV getMoney(PageF<SearchF<?>> form) {
        return voucherPushBillDxZJRepository.getMoney(form);
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderStatusResult delOrderDealResult(OrderStatusDel orderStatusDel) {
        String url = "/ESB/API/ChannelZJFW/YXDMC/QueryFinanceOrderStatusResultList";
        log.info("开始删除报账单，id:{}", orderStatusDel.getId());
        VoucherBillDxZJ zjRepositoryById = voucherPushBillDxZJRepository.getById(orderStatusDel.getId());
        if (Objects.isNull(zjRepositoryById)) {
            throw BizException.throw400("单据不存在不存在");
        }

//        if(!Arrays.asList(1,2,3,5).contains(zjRepositoryById.getBillEventType())){
//            throw BizException.throw400("该单据不允许删除！");
//        }

        OrderStatusResult orderStatusResult = new OrderStatusResult();

        List<VoucherPushBillDetailDxZJ> list = voucherBillDetailDxZJRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailDxZJ>()
                .eq(VoucherPushBillDetailDxZJ::getVoucherBillNo, zjRepositoryById.getVoucherBillNo()));

        voucherPushBillDxZJRepository.removeById(zjRepositoryById);
        if (CollectionUtils.isNotEmpty(list)) {
            voucherBillDetailDxZJRepository.removeBatchByIds(list);
            List<Long> billIds = list.stream().map(VoucherPushBillDetailDxZJ::getBillId).collect(Collectors.toList());
            String communityId = list.get(0).getCommunityId();
            temporaryChargeBillRepository.updateBatchStatus(billIds, communityId, zjRepositoryById.getBillEventType(), 0);
        }
        // todo 若是支付申请，将原始表单状态恢复
        if (ZJTriggerEventBillTypeEnum.支付申请.getCode() == zjRepositoryById.getBillEventType()) {
            PaymentApplicationFormZJ paymentApplicationForm = paymentApplicationFormRepository.getById(Long.parseLong(zjRepositoryById.getPayAppId()));
            if (Objects.nonNull(paymentApplicationForm)) {
                paymentApplicationForm.setApprovalStatus(1);
                paymentApplicationFormRepository.updateById(paymentApplicationForm);
                RefundManageDTO refundManageDTO = new RefundManageDTO(paymentApplicationForm.getCommunityId(),
                        paymentApplicationForm.getPayApplyCode(), paymentApplicationForm.getId(),
                        ApproveStatusEnum.草稿.getCode(), ApproveStatusEnum.草稿.getValue());
                log.info("合同报账单-业务支付申请单删除,修改退款状态入参:{}", JSONObject.toJSONString(refundManageDTO));
                chargeClient.syncRefundManageStatus(refundManageDTO);
            }
        }

        orderStatusResult.setCode(0);
        orderStatusResult.setMessage("删除成功");
        return orderStatusResult;
    }

    private void getMergeInfo(List<VoucherPushBillDetailDxZJ> list, String communityId, VoucherBillDxZJV map){
        try{
            SpaceCommunityV2V communityDetailV2 = spaceClient.getCommunityDetailV2(communityId);
            if(Objects.nonNull(communityDetailV2) && StringUtils.isNotBlank(communityDetailV2.getProjectId())){
                map.setProjectId(communityDetailV2.getProjectId());
                map.setProjectName(communityDetailV2.getProjectName());
            }
        }
        catch (Exception e) {
            log.error("获取项目信息异常", e);
        }


        Long id = list.get(0).getBillId();
        TemporaryChargeBillDetailV billInfo = temporaryChargeBillAppService.getById(id, TemporaryChargeBillDetailV.class, communityId);
        map.setBusinessTypeName(list.get(0).getBusinessTypeName().substring(1,list.get(0).getBusinessTypeName().length()-1));

        OrgFinanceRv orgFinanceById = orgClient.getOrgFinanceById(billInfo.getStatutoryBodyId());
        log.info("根据法定单位id：{}，查询对应法定单位信息：{}", billInfo.getStatutoryBodyId(), orgFinanceById);
        if(Objects.nonNull(orgFinanceById)){
            map.setTaxpayerType(orgFinanceById.getTaxpayerType());
        }

        Integer billEventType = map.getBillEventType();
        //对下结算类型，重新匹配计税方式
        if(billEventType.equals(ZJTriggerEventBillTypeEnum.对下结算实签.getCode()) ||
                billEventType.equals(ZJTriggerEventBillTypeEnum.对下结算计提.getCode()) ||
                billEventType.equals(ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode())){
            if(Objects.nonNull(map.getCalculationMethod())){
                map.setTaxType(map.getCalculationMethod().toString());
            }else{
                map.setTaxType(FinancialTaxTypeEnum.SIMPLE.getCode().toString());
                if(Objects.isNull(map.getTaxpayerType()) || TaxpayerTypeEnum.一般纳税人.getCode().equals(map.getTaxpayerType())){
                    map.setTaxType(FinancialTaxTypeEnum.GENERAL.getCode().toString());
                }
            }
        }

        if (billEventType.equals(ZJTriggerEventBillTypeEnum.对下结算计提.getCode()) ||
                billEventType.equals(ZJTriggerEventBillTypeEnum.对下结算实签.getCode()) ||
                billEventType.equals(ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode()) ||
                billEventType.equals(ZJTriggerEventBillTypeEnum.支付申请.getCode())){

            List<ContractPayPlanInnerInfoV> innerInfoList = contractClient.getInnerInfoByContractIdOnPay(Arrays.asList(billInfo.getExtField6()));
            if(CollectionUtils.isNotEmpty(innerInfoList)){
                ContractPayPlanInnerInfoV contractPayPlanInnerInfoV = innerInfoList.get(0);
                map.setConmainCode(contractPayPlanInnerInfoV.getConMainCode());
                map.setContractId(contractPayPlanInnerInfoV.getContractId());
                map.setOppositeOne(contractPayPlanInnerInfoV.getPayee());
                map.setOppositeOneId(contractPayPlanInnerInfoV.getPayeeId());

//                map.setOppositeOne(contractPayPlanInnerInfoV.getOppositeOne());
//                map.setOppositeOneId(contractPayPlanInnerInfoV.getOppositeOneId());
                //map.setExpireNextEndDate(contractPayPlanInnerInfoV.getExpireNextEndDate());
                //逻辑替换，为创建时间+一年-一天
                map.setExpireNextEndDate(map.getGmtCreate().plusYears(1).minusDays(1));
            }
            if((billEventType.equals(ZJTriggerEventBillTypeEnum.对下结算实签.getCode()) || billEventType.equals(ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode())) && StringUtils.isNotBlank(billInfo.getExtField7())) {
                map.setSettleId(billInfo.getExtField7());
            }
            if(billEventType.equals(ZJTriggerEventBillTypeEnum.对下结算实签.getCode()) && StringUtils.isNotBlank(billInfo.getExtField7())){

                ContractPaySettlementDetailsV contractClientDetail = contractClient.getPayDetailsById(billInfo.getExtField7());
                map.setIsSign(1);
                if (Objects.nonNull(contractClientDetail)) {
                    map.setSettlementType(contractClientDetail.getSettlementType());
                    map.setPayFundNumber(contractClientDetail.getPayFundNumber());
                }

                List<PayPlanPeriodV> contractPaySettlementPeriodVList = contractClientDetail.getContractPaySettlementPeriodVList();
                if(!contractPaySettlementPeriodVList.isEmpty()){
                    Date minStartDate = contractPaySettlementPeriodVList.stream()
                            .min(Comparator.comparing(PayPlanPeriodV::getStartDate))
                            .map(PayPlanPeriodV::getStartDate)
                            .orElse(null);

                    Date maxEndDate = contractPaySettlementPeriodVList.stream()
                            .max(Comparator.comparing(PayPlanPeriodV::getEndDate))
                            .map(PayPlanPeriodV::getEndDate)
                            .orElse(null);
                    map.setSettleStartDate(minStartDate);
                    map.setSettleEndDate(maxEndDate);
                    map.setActualSettlementAmount(contractClientDetail.getActualSettlementAmount());
                    map.setTotalSettlementAmount(contractClientDetail.getTotalSettledAmount());
                }
            }
            if(billEventType.equals(ZJTriggerEventBillTypeEnum.支付申请.getCode())){
                if(StringUtils.isNotEmpty(list.get(0).getContractId()) && !"9999999999".equals(list.get(0).getContractId())){
                    ContractPayConcludeExpandF concludeExpandF = new ContractPayConcludeExpandF();
                    concludeExpandF.setContractId(list.get(0).getContractId());
                    ContractPayConcludeExpandV contractPayConcludeExpandV = contractClient.get(concludeExpandF);
                    List<ContractSrfxxF> srfxx = JSONObject.parseArray(contractPayConcludeExpandV.getSkdwxx(),ContractSrfxxF.class);
                    if(CollectionUtils.isNotEmpty(srfxx)){
                        ContractSrfxxF contractSrfxxF = srfxx.get(0);

                        map.setTruepayee(contractSrfxxF.getPayee());
                        map.setTruepayeeid(contractSrfxxF.getPayeeid());

//                    map.setTruepayee(contractSrfxxF.getTruepayee());
//                    map.setTruepayeeid(contractSrfxxF.getTruepayeeid());
                        map.setTruepayeeaccounbank(contractSrfxxF.getTruepayeeaccounbank());
                        map.setTruepayeeaccountid(contractSrfxxF.getTruepayeeaccountid());
                        map.setTruepayeeaccounname(contractSrfxxF.getTruepayeeaccounname());
                        map.setTruepayeeaccounnumber(contractSrfxxF.getTruepayeeaccounnumber());
                    }

                }

                PaymentApplicationFormZJV detail = paymentApplicationFormRepository.getDetailById(map.getPayAppId());
                if(!Objects.isNull(detail)){
                    map.setContractName(detail.getContractName());
                    map.setRecipientName(detail.getRecipientName());
                    map.setExpectPayDate(detail.getExpectPayDate());
                    map.setTotalPaymentAmount(detail.getTotalPaymentAmount());
                    map.setBusinessTypeName(detail.getBusinessType());

                    map.setReceiptRemark(detail.getBusinessReasons());
                    map.setRemark(detail.getRemarks());
                    map.setFileSize(detail.getAttachmentNum());

                    if(StringUtils.isEmpty(list.get(0).getContractId()) || "9999999999".equals(list.get(0).getContractId())){
                        map.setTruepayee(detail.getRecipientName());
                        map.setTruepayeeid(detail.getRecipientCode());
                        map.setTruepayeeaccountid(detail.getAccountCode());
                        map.setTruepayeeaccounname(detail.getNameOfReceivingAccount());
                        map.setTruepayeeaccounbank(detail.getOpeningBank());
                        map.setTruepayeeaccounnumber(detail.getBankAccountNumber());
                    }

                }

            }

        }

        if (billEventType.equals(ZJTriggerEventBillTypeEnum.收入确认计提.getCode()) ||
                billEventType.equals(ZJTriggerEventBillTypeEnum.收入确认实签.getCode()) ||
                billEventType.equals(ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode())){
            List<ContractPayPlanInnerInfoV> innerInfoList = contractClient.getInnerInfoByContractIdOnIncome(Arrays.asList(billInfo.getExtField6()));
            if(CollectionUtils.isNotEmpty(innerInfoList)){
                ContractPayPlanInnerInfoV contractPayPlanInnerInfoV = innerInfoList.get(0);
                map.setConmainCode(contractPayPlanInnerInfoV.getConMainCode());
                if(!billEventType.equals(ZJTriggerEventBillTypeEnum.收入确认计提.getCode())){
                    map.setContractId(contractPayPlanInnerInfoV.getContractId());
                }
                map.setOppositeOne(contractPayPlanInnerInfoV.getDrawee());
                map.setOppositeOneId(contractPayPlanInnerInfoV.getDraweeId());
//                map.setOppositeOne(contractPayPlanInnerInfoV.getOppositeOne());
//                map.setOppositeOneId(contractPayPlanInnerInfoV.getOppositeOneId());
                //map.setExpireNextEndDate(contractPayPlanInnerInfoV.getExpireNextEndDate());
                //逻辑替换，为创建时间+一年-一天
                map.setExpireNextEndDate(map.getGmtCreate().plusYears(1).minusDays(1));
            }
            if((billEventType.equals(ZJTriggerEventBillTypeEnum.收入确认实签.getCode()) || billEventType.equals(ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode())) && StringUtils.isNotBlank(billInfo.getExtField7())) {
                map.setSettleId(billInfo.getExtField7());
            }
            if(billEventType.equals(ZJTriggerEventBillTypeEnum.收入确认实签.getCode()) && StringUtils.isNotBlank(billInfo.getExtField7())){

                ContractIncomeSettlementDetailsV incomeDetailsById = contractClient.getIncomeDetailsById(billInfo.getExtField7());
                map.setIsSign(1);
                if (Objects.nonNull(incomeDetailsById)) {
                    map.setSettlementType(incomeDetailsById.getSettlementType());
                    map.setPayFundNumber(incomeDetailsById.getPayFundNumber());
                }

                List<IncomePlanPeriodV> periodVList = incomeDetailsById.getContractIncomeSettlementPeriodVList();
                if(CollectionUtils.isNotEmpty(periodVList)){
                    Date minStartDate = periodVList.stream()
                            .min(Comparator.comparing(IncomePlanPeriodV::getStartDate))
                            .map(IncomePlanPeriodV::getStartDate)
                            .orElse(null);

                    Date maxEndDate = periodVList.stream()
                            .max(Comparator.comparing(IncomePlanPeriodV::getEndDate))
                            .map(IncomePlanPeriodV::getEndDate)
                            .orElse(null);
                    map.setSettleStartDate(minStartDate);
                    map.setSettleEndDate(maxEndDate);
                    map.setActualSettlementAmount(incomeDetailsById.getActualSettlementAmount());
                    map.setTotalSettlementAmount(incomeDetailsById.getTotalSettledAmount());
                }
            }
        }

        if(billEventType.equals(ZJTriggerEventBillTypeEnum.对下结算实签.getCode()) ||
                billEventType.equals(ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode()) ||
                billEventType.equals(ZJTriggerEventBillTypeEnum.收入确认实签.getCode()) ||
                billEventType.equals(ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode())){
            List<AttachmentV> searchFPageF = getSearchFPageF(billInfo);
            map.setFileSize(searchFPageF.size());
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public SyncBatchVoucherResultV syncBatchPushBillForSettlement(SyncBatchPushZJBillF syncBatchPushBillF) {
        List<VoucherBillDxZJ> voucherBills = voucherPushBillDxZJRepository.list(new LambdaQueryWrapper<VoucherBillDxZJ>()
                .in(VoucherBillDxZJ::getId, syncBatchPushBillF.getVoucherIds())
                .in(VoucherBillDxZJ::getPushState, 1, 3, 4, PushBillTypeEnum.制单失败.getCode(), PushBillTypeEnum.单据驳回.getCode())
                .eq(VoucherBillDxZJ::getDeleted, 0));
        if (CollectionUtils.isEmpty(voucherBills)){
            throw new BizException(400, "未查询到报账单");
        }
        SpaceCommunityShortV spaceCommunityShortVS = spaceClient.get(pushBillZJDomainService.communityIdForZJ(voucherBills.get(0)));
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

        voucherBills.forEach(voucherBillDxZJ -> {
            if(UPLOAD_TYPE.contains(voucherBillDxZJ.getBillEventType())){
                // 获取影像资料
                List<ZJFileVo> zjFileVos = new ArrayList<>();

                //报账单ID
                String voucherBillId = voucherBillDxZJ.getPayAppId();
                //报账单编号
                String voucherBillNo = voucherBillDxZJ.getVoucherBillNo();
                //合同Id
                String contractId = null;
                //结算审批单、确收审批单Id
                String settleId = null;
                List<VoucherPushBillDetailDxZJ> detailDxZJS = voucherBillDetailDxZJRepository.getByVoucherBillNoNoSearch(voucherBillDxZJ.getVoucherBillNo());
                TemporaryChargeBillDetailV billInfo = temporaryChargeBillAppService.getById(detailDxZJS.get(0).getBillId(), TemporaryChargeBillDetailV.class, detailDxZJS.get(0).getCommunityId());
                if(EXT_UPLOAD_TYPE.contains(voucherBillDxZJ.getBillEventType()) && StringUtils.isNotBlank(billInfo.getExtField7())){
                    settleId = billInfo.getExtField7();
                }
                if(StringUtils.isNotBlank(billInfo.getExtField6())){
                    contractId = billInfo.getExtField6();
                }
                PageF<SearchF<?>> filePageSearch = new PageF<>();
                List<Field> fields = getFields(voucherBillId, voucherBillNo, contractId, settleId);
                SearchF<AttachmentE> SearchF = new SearchF<>();
                SearchF.setFields(fields);
                filePageSearch.setConditions(SearchF);
                List<VoucherBillZJFileSV> fileList =voucherBillDetailAppService.queryFileForRec(filePageSearch);
                for (VoucherBillZJFileSV file : fileList) {
                    ZJFileVo zjFileVo = new ZJFileVo();
                    if(StringUtils.isNotBlank(file.getFileYxxxID())){
                        zjFileVo.setFileId(file.getFileYxxxID());
                    }else{
                        String s = fileHost + file.getFileKey();
                        MultipartFile multipartFile = FileUtil.getMultipartFile(s);
                        zjFileVo = contractClient.zjUpload(multipartFile, voucherBillDxZJ.getVoucherBillNo());
                    }
                    zjFileVo.setFileKey(file.getFileKey());
                    zjFileVo.setName(file.getFileName());
                    zjFileVos.add(zjFileVo);
                }
                //计提附件获取
                /*if(EXT_UPLOAD_TYPE.contains(voucherBillDxZJ.getBillEventType())){
                    List<VoucherPushBillDetailDxZJ> detailDxZJS = voucherBillDetailDxZJRepository.getByVoucherBillNoNoSearch(voucherBillDxZJ.getVoucherBillNo());
                    TemporaryChargeBillDetailV billInfo = temporaryChargeBillAppService.getById(detailDxZJS.get(0).getBillId(), TemporaryChargeBillDetailV.class, detailDxZJS.get(0).getCommunityId());
                    List<AttachmentV> attachmentVList = getSearchFPageF(billInfo);
                    for (AttachmentV attachmentV : attachmentVList) {
                        String s = fileHost + attachmentV.getFileKey();
                        MultipartFile multipartFile = FileUtil.getMultipartFile(s);
                        ZJFileVo zjFileVo = contractClient.zjUpload(multipartFile, voucherBillDxZJ.getVoucherBillNo());
                        zjFileVo.setFileKey(attachmentV.getFileKey());
                        zjFileVo.setName(attachmentV.getName());
                        zjFileVos.add(zjFileVo);
                    }
                }else{
                    List<VoucherBillFileZJ> voucherBillFileZJS = voucherBillFileZJRepository.selectByVoucherBillId(
                            ZJTriggerEventBillTypeEnum.支付申请.getCode() == voucherBillDxZJ.getBillEventType()
                            ? Long.parseLong(voucherBillDxZJ.getPayAppId()) : voucherBillDxZJ.getId());
                    for (VoucherBillFileZJ voucherBillFileZJ : voucherBillFileZJS) {
                        String files = voucherBillFileZJ.getFiles();
                        JSONObject jsonObject = JSONObject.parseObject(files) ;
                        String s = fileHost + jsonObject.getString("fileKey");
                        MultipartFile multipartFile = FileUtil.getMultipartFile(s);
                        ZJFileVo zjFileVo = contractClient.zjUpload(multipartFile, voucherBillDxZJ.getVoucherBillNo());
                        zjFileVo.setFileKey(jsonObject.getString("fileKey"));
                        zjFileVo.setName(jsonObject.getString("name"));
                        zjFileVos.add(zjFileVo);
                    }
                }*/
                if (CollectionUtils.isEmpty(zjFileVos) &&
                        (ZJTriggerEventBillTypeEnum.对下结算计提.getCode() == voucherBillDxZJ.getBillEventType() ||
                         ZJTriggerEventBillTypeEnum.收入确认计提.getCode() == voucherBillDxZJ.getBillEventType())){
                    throw new OwlBizException("请先上传附件并填写业务事由");
                }
                for (ZJFileVo zjFileVo : zjFileVos) {
                    UploadLinkZJF uploadLinkZJF = new UploadLinkZJF();
                    uploadLinkZJF.setName(zjFileVo.getName());
                    uploadLinkZJF.setImageIdZJ(zjFileVo.getFileId());
                    uploadLinkZJF.setUploadLink("/files/" + zjFileVo.getFileKey());
                    uploadLinkZJF.setBillNo(voucherBillDxZJ.getVoucherBillNo());
                    voucherPushBillDxZJRepository.addLinkZJ(uploadLinkZJF);
                }
            }
        });

        return pushBillZJDomainService.syncBatchPushBillForJTAndSQ(syncBatchPushBillF);
    }
    @Transactional(rollbackFor = Exception.class)
    public String getPushCaiWuYunData(SyncBatchPushZJBillF syncBatchPushBillF) {
        List<VoucherBillDxZJ> voucherBills = voucherPushBillDxZJRepository.list(new LambdaQueryWrapper<VoucherBillDxZJ>()
                .in(VoucherBillDxZJ::getId, syncBatchPushBillF.getVoucherIds())
                .in(VoucherBillDxZJ::getPushState, 1, 3, 4, PushBillTypeEnum.制单失败.getCode(), PushBillTypeEnum.单据驳回.getCode())
                .eq(VoucherBillDxZJ::getDeleted, 0));
        if (CollectionUtils.isEmpty(voucherBills)){
            throw new BizException(400, "未查询到报账单");
        }
        SpaceCommunityShortV spaceCommunityShortVS = spaceClient.get(pushBillZJDomainService.communityIdForZJ(voucherBills.get(0)));
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

        voucherBills.forEach(voucherBillDxZJ -> {
            if(UPLOAD_TYPE.contains(voucherBillDxZJ.getBillEventType())){
                // 获取影像资料
                List<ZJFileVo> zjFileVos = new ArrayList<>();

                //报账单ID
                String voucherBillId = voucherBillDxZJ.getPayAppId();
                //报账单编号
                String voucherBillNo = voucherBillDxZJ.getVoucherBillNo();
                //合同Id
                String contractId = null;
                //结算审批单、确收审批单Id
                String settleId = null;
                List<VoucherPushBillDetailDxZJ> detailDxZJS = voucherBillDetailDxZJRepository.getByVoucherBillNoNoSearch(voucherBillDxZJ.getVoucherBillNo());
                TemporaryChargeBillDetailV billInfo = temporaryChargeBillAppService.getById(detailDxZJS.get(0).getBillId(), TemporaryChargeBillDetailV.class, detailDxZJS.get(0).getCommunityId());
                if(EXT_UPLOAD_TYPE.contains(voucherBillDxZJ.getBillEventType()) && StringUtils.isNotBlank(billInfo.getExtField7())){
                    settleId = billInfo.getExtField7();
                }
                if(StringUtils.isNotBlank(billInfo.getExtField6())){
                    contractId = billInfo.getExtField6();
                }
                PageF<SearchF<?>> filePageSearch = new PageF<>();
                List<Field> fields = getFields(voucherBillId, voucherBillNo, contractId, settleId);
                SearchF<AttachmentE> SearchF = new SearchF<>();
                SearchF.setFields(fields);
                filePageSearch.setConditions(SearchF);
                List<VoucherBillZJFileSV> fileList =voucherBillDetailAppService.queryFileForRec(filePageSearch);
                for (VoucherBillZJFileSV file : fileList) {
                    ZJFileVo zjFileVo = new ZJFileVo();
                    if(StringUtils.isNotBlank(file.getFileYxxxID())){
                        zjFileVo.setFileId(file.getFileYxxxID());
                    }else{
                        String s = fileHost + file.getFileKey();
                        MultipartFile multipartFile = FileUtil.getMultipartFile(s);
                        zjFileVo = contractClient.zjUpload(multipartFile, voucherBillDxZJ.getVoucherBillNo());
                    }
                    zjFileVo.setFileKey(file.getFileKey());
                    zjFileVo.setName(file.getFileName());
                    zjFileVos.add(zjFileVo);
                }
                if (CollectionUtils.isEmpty(zjFileVos) &&
                        (ZJTriggerEventBillTypeEnum.对下结算计提.getCode() == voucherBillDxZJ.getBillEventType() ||
                         ZJTriggerEventBillTypeEnum.收入确认计提.getCode() == voucherBillDxZJ.getBillEventType())){
                    throw new OwlBizException("请先上传附件并填写业务事由");
                }
                for (ZJFileVo zjFileVo : zjFileVos) {
                    UploadLinkZJF uploadLinkZJF = new UploadLinkZJF();
                    uploadLinkZJF.setName(zjFileVo.getName());
                    uploadLinkZJF.setImageIdZJ(zjFileVo.getFileId());
                    uploadLinkZJF.setUploadLink("/files/" + zjFileVo.getFileKey());
                    uploadLinkZJF.setBillNo(voucherBillDxZJ.getVoucherBillNo());
                    voucherPushBillDxZJRepository.addLinkZJ(uploadLinkZJF);
                }
            }
        });

        return pushBillZJDomainService.getPushCaiWuYunData(syncBatchPushBillF);
    }

    private static List<Field> getFields(String voucherBillId, String voucherBillNo, String contractId, String settleId) {
        Field fieldBillId = new Field();
        fieldBillId.setName("voucherBillId");
        fieldBillId.setMethod(1);
        fieldBillId.setValue(voucherBillId);
        Field fieldBillNo = new Field();
        fieldBillNo.setName("voucherBillNo");
        fieldBillNo.setMethod(1);
        fieldBillNo.setValue(voucherBillNo);
        Field fieldContractId = new Field();
        fieldContractId.setName("contractId");
        fieldContractId.setMethod(1);
        fieldContractId.setValue(contractId);
        Field fieldSettleId = new Field();
        fieldSettleId.setName("settleId");
        fieldSettleId.setMethod(1);
        fieldSettleId.setValue(settleId);
        return Arrays.asList(fieldBillId,fieldBillNo,fieldContractId,fieldSettleId);
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

    public void generateOnSettlement(VoucherBillGenerateOnContractSettlementF generateF) {
        VoucherBillGenerateForContractSettlementContext context = new VoucherBillGenerateForContractSettlementContext();
        context.setCommunityIdList(Lists.newArrayList(generateF.getCommunityId()));
        context.setBillIdList(generateF.getBillIdList());
        context.setEventType(generateF.getEventType());
        context.setSettlementId(generateF.getSettlementId());
        context.setProcessId(generateF.getProcessId());
        billRuleDomainService.autoExecute(context);
    }

    public void approveAgree(Long voucherBillId) {
        voucherPushBillDxZJRepository.update(new LambdaUpdateWrapper<VoucherBillDxZJ>()
                .set(VoucherBillDxZJ::getGmtApprove, LocalDateTime.now())
                .set(VoucherBillDxZJ::getApproveState, PushBillApproveStateEnum.已审核.getCode())
                .set(VoucherBillDxZJ::getPushState, PushBillTypeEnum.推送中.getCode())
                .eq(VoucherBillDxZJ::getId, voucherBillId));
    }

    public void queryFinanceOrderDealResult(OrderStatusBody orderStatusBody) {
        pushBillZJDomainService.queryFinanceOrderDealResultOnDx(orderStatusBody);
    }

    public void autoFile(String voucherBillNo, HttpServletResponse response){
        VoucherBillDxZJ voucherBill = voucherPushBillDxZJRepository.queryByVoucherBillNo(voucherBillNo);
        if (Objects.isNull(voucherBill)){
            throw new OwlBizException("报账单不存在,请检查");
        }
        Integer billEventType = voucherBill.getBillEventType();
        if (billEventType != ZJTriggerEventBillTypeEnum.对下结算计提.getCode() &&
             billEventType != ZJTriggerEventBillTypeEnum.收入确认计提.getCode() &&
                billEventType != ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode() &&
                billEventType != ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode()){
            throw new OwlBizException("非计提报账单不支持自动生成");
        }
        List<VoucherPushBillDetailDxZJ> detailDxZJList =
                voucherBillDetailDxZJRepository.getByVoucherBillNoNoSearch(voucherBillNo);
        VoucherPushBillDetailDxZJ detail = detailDxZJList.get(0);

        List<VoucherAutoFileSheetItem> sheetItems = buildSheetItems(voucherBillNo, billEventType);
        // List<VoucherBillAutoFileApprovalNode> approvalNodeList = buildApprovalNodes(voucherBill.getProcInstId());

        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();

        ClassPathResource resource;
        if (billEventType == ZJTriggerEventBillTypeEnum.收入确认计提.getCode()){
            resource =  new ClassPathResource("templates/poi/income-voucher-file-template.docx");
        } else if (billEventType == ZJTriggerEventBillTypeEnum.对下结算计提.getCode()) {
            resource =  new ClassPathResource("templates/poi/cost-voucher-file-template.docx");
        } else if (billEventType == ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode()) {
            resource =  new ClassPathResource("templates/poi/income-voucher-write-off-file-template.docx");
        } else {
            resource =  new ClassPathResource("templates/poi/cost-voucher-write-off-file-template.docx");
        }

        Configure configure = Configure.builder()
                .bind("items", policy)
                // .bind("approvalNodes", policy)
                .build();

        Map<String,Object> map = new HashMap<>();
        map.put("communityName",detail.getCommunityName());
        map.put("businessName",detail.getBusinessTypeName());
        map.put("createDate", DateUtil.format(voucherBill.getGmtCreate(), "yyyy-MM-dd"));
        map.put("items", sheetItems);
        // map.put("approvalNodes", approvalNodeList);

        // 此处用路径的话，jar包内的文件会解析出问题，采用流操作即可
        try (InputStream is = resource.getInputStream()) {
            XWPFTemplate template = XWPFTemplate.compile(is, configure).render(map);
            // 将完成数据渲染的文档写出
            String fileName = detail.getCommunityName()+"_"+ LocalDate.now() +".docx";
            responseHeader(response, fileName);
            template.writeAndClose(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void responseHeader(HttpServletResponse response, String fileName) {
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.addHeader("Access-Control-Expose-Headers", "Content-disposition");
    }


    private List<VoucherBillAutoFileApprovalNode> buildApprovalNodes(String procInstId) {
        if (StringUtils.isBlank(procInstId)){
            return Collections.emptyList();
        }
        List<ProgressNode> progressNodeList = bpmClient.getInstanceProgressInfo(procInstId);
        if (CollectionUtils.isEmpty(progressNodeList)){
            return Collections.emptyList();
        }
        return progressNodeList.stream().map(VoucherBillAutoFileApprovalNode::transferFromProgressNode).collect(Collectors.toList());
    }


    private List<VoucherAutoFileSheetItem> buildSheetItems(String voucherBillNo, Integer billEventType) {
        List<VoucherBillAutoFileZJVo> voucherBillFileZJVos =
                voucherPushBillDxZJRepository.selectAutoFileVo(voucherBillNo, billEventType);
        if (CollectionUtils.isEmpty(voucherBillFileZJVos)){
            return Collections.emptyList();
        }
        List<String> contractIds = voucherBillFileZJVos.stream()
                .map(VoucherBillAutoFileZJVo::getContractId)
                .filter(StringUtils::isNotBlank)
                .distinct().collect(Collectors.toList());
        List<ContractPayPlanInnerInfoV> innerInfos = Lists.newArrayList();
        if (billEventType == ZJTriggerEventBillTypeEnum.对下结算计提.getCode() || billEventType == ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode()){
            innerInfos = contractClient.getInnerInfoByContractIdOnPay(contractIds);
        } else if (billEventType == ZJTriggerEventBillTypeEnum.收入确认计提.getCode() || billEventType == ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode()) {
            innerInfos = contractClient.getInnerInfoByContractIdOnIncome(contractIds);
        }
        Map<String, ContractPayPlanInnerInfoV> innerInfoMap = innerInfos.stream()
                .collect(Collectors.toMap(ContractPayPlanInnerInfoV::getContractId, v -> v, (v1, v2) -> v1));
        // 按照contractId+chargeItemId+accountDate分组
        List<VoucherAutoFileSheetItem> sheetItems = Lists.newArrayList();
        Map<String, List<VoucherBillAutoFileZJVo>> groupMap = voucherBillFileZJVos.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getContractId() + v.getChargeItemId() + v.getAccountDate(),
                        TreeMap::new,
                        Collectors.toList()
                ));
        for (List<VoucherBillAutoFileZJVo> voList : groupMap.values()) {
            VoucherBillAutoFileZJVo autoFileZJVo = voList.get(0);
            VoucherAutoFileSheetItem sheetItem = new VoucherAutoFileSheetItem();
            String[] chargeItemSplitStrs = autoFileZJVo.getChargeItemName().split("/");
            sheetItem.setChargeItemName(chargeItemSplitStrs[chargeItemSplitStrs.length-1]);
            sheetItem.setPreFlagName("否");
            BigDecimal curTotalAmount = voList.stream()
                    .map(VoucherBillAutoFileZJVo::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal curNoTaxAmount = voList.stream()
                    .map(VoucherBillAutoFileZJVo::getNoTaxAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal curTaxAmount = voList.stream()
                    .map(VoucherBillAutoFileZJVo::getTaxAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            sheetItem.setCurAmount(curTotalAmount.toString());
            sheetItem.setCurTaxAmount(curTaxAmount.toString());
            sheetItem.setCurNoTaxAmount(curNoTaxAmount.toString());
            ContractPayPlanInnerInfoV innerInfoV = innerInfoMap.get(autoFileZJVo.getContractId());
            if (Objects.isNull(innerInfoV)){
                throw new OwlBizException("合同不存在,请检查");
            }
            sheetItem.setContractName(innerInfoV.getContractName());
            sheetItem.setContractNo(innerInfoV.getConMainCode());
            if (billEventType == ZJTriggerEventBillTypeEnum.对下结算计提.getCode()) {
                sheetItem.setOpposite(innerInfoV.getPayee());
            } else {
                sheetItem.setOpposite(innerInfoV.getDrawee());
            }
            sheetItem.setEndDate(DateUtil.format(innerInfoV.getGmtExpireStart(), "yyyy-MM-dd") + "~"
                    + DateUtil.format(innerInfoV.getGmtExpireEnd(), "yyyy-MM-dd"));
            sheetItem.setStartTime(DateUtil.format(autoFileZJVo.getStartTime(), "yyyy-MM-dd") + "~"
                    + DateUtil.format(autoFileZJVo.getEndTime(), "yyyy-MM-dd"));
            sheetItem.setAccountDate(autoFileZJVo.getAccountDate());
            if (Objects.nonNull(innerInfoV.getChangeContractAmount()) &&
                    innerInfoV.getChangeContractAmount().compareTo(BigDecimal.ZERO) > 0){
                sheetItem.setContractAmount(innerInfoV.getChangeContractAmount().toString());
            } else {
                sheetItem.setContractAmount(innerInfoV.getContractAmountOriginalRate().toString());
            }
            sheetItems.add(sheetItem);
        }
        return sheetItems;
    }

    public void updateBilDxZjFromContract(String settlementId, String otherBusinessReasons, String externalDepartmentCode, Integer calculationMethod){
        //根据结算单id查询报账单数据
        List<VoucherBillDxZJ> voucherBillList = voucherPushBillDxZJRepository.getBillDxZjBySettlementId(settlementId);
        if(CollectionUtils.isNotEmpty(voucherBillList)){
            voucherBillList.forEach(voucherBill -> {
                voucherPushBillDxZJRepository.updateBilDxZjFromContract(voucherBill.getId(), otherBusinessReasons, externalDepartmentCode, calculationMethod);
            });
        }
    }

}
