package com.wishare.finance.apis.pushbill;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.reconciliation.vo.FlowDetailSimpleVo;
import com.wishare.finance.apps.model.reconciliation.vo.FlowRecordSimpleVo;
import com.wishare.finance.apps.model.voucher.vo.SyncBatchVoucherResultV;
import com.wishare.finance.apps.model.voucher.vo.VoucherBusinessReasonsV;
import com.wishare.finance.apps.pushbill.fo.PushInfoF;
import com.wishare.finance.apps.pushbill.fo.SyncBatchPushZJBillF;
import com.wishare.finance.apps.pushbill.fo.UpLoadFileF;
import com.wishare.finance.apps.pushbill.fo.UploadLinkZJF;
import com.wishare.finance.apps.pushbill.vo.*;
import com.wishare.finance.apps.service.pushbill.PushBillZJAppService;
import com.wishare.finance.apps.service.pushbill.VoucherBillDetailAppService;
import com.wishare.finance.apps.service.pushbill.VoucherBillFileZJService;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.PushBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherContractInvoiceZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.PushBillApproveStateEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillDetailZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillFileZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherContractInvoiceZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherPushBillZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.OrderDealResult;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.OrderStatusResult;
import com.wishare.finance.infrastructure.remote.clients.base.ConfigClient;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.fo.zj.OrderStatusBody;
import com.wishare.finance.infrastructure.remote.fo.zj.OrderStatusDel;
import com.wishare.finance.infrastructure.remote.vo.cfg.CfgExternalDeportData;
import com.wishare.finance.infrastructure.remote.vo.contract.AttachmentE;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceCostV;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Api(tags = {"汇总单据中交"})
@RestController
@RequestMapping("/voucherbillZJ")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherBillZJApi implements ApiBase {

    private final VoucherPushBillZJRepository voucherPushBillRepository;
    private final PushBillZJAppService pushBillZJAppService;
    private final VoucherBillDetailZJRepository voucherBillDetailZJRepository;
    private final VoucherContractInvoiceZJRepository voucherContractInvoiceZJRepository;
    private final VoucherBillFileZJRepository voucherBillFileZJRepository;
    private final VoucherBillFileZJService voucherBillFileZJService;
    private final OrgClient orgClient;
    private final ConfigClient configClient;
    private final VoucherBillDetailAppService voucherBillDetailAppService;

    @PostMapping("/page")
    @ApiOperation(value = "获取单据(分页)", notes = "获取单据(分页)")
    public PageV<VoucherBillZJV2> queryPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        PageV<VoucherBillZJV2> voucherBillZJVPageV = RepositoryUtil.convertMoneyPage(pushBillZJAppService.pageBySearch(form), VoucherBillZJV2.class);

        Map<String, List<VoucherPushBillDetailZJ>> voucherBillNoToPushBillDetailListMap = voucherBillNoToPushBillDetailListMap(voucherBillZJVPageV);
        Map<String, List<VoucherPushBillDetailZJ>> listMap = voucherBillNoToPushBillDetailListMap2(voucherBillZJVPageV);
        Map<Long, List<VoucherContractInvoiceZJ>> voucherBillIdToInvoiceListMap = null;

        if (CollectionUtils.isNotEmpty(voucherBillZJVPageV.getRecords())) {
            Map<String, List<CfgExternalDeportData>> communityMap = new HashMap<>();
            Map<Long, String> idToCommunityIdMap = new HashMap<>();
            //获取成本中心code
            List<Long> costCenterIdList = voucherBillZJVPageV.getRecords().stream().map(VoucherBillZJV2::getCostCenterId).distinct().collect(Collectors.toList());
            //根据成本中心code获取对应项目，从而获取对应部门数据
            if(CollectionUtils.isNotEmpty(costCenterIdList)){
                //根据成本中心code获取对应项目ID
                List<OrgFinanceCostV> orgFinanceCosts = orgClient.getCommunityIds(costCenterIdList);
                if(CollectionUtils.isNotEmpty(orgFinanceCosts)){
                    idToCommunityIdMap = orgFinanceCosts.stream()
                            .collect(Collectors.toMap(
                                    OrgFinanceCostV::getId,
                                    OrgFinanceCostV::getCommunityId,
                                    (oldValue, newValue) -> newValue
                            ));
                    List<String> communityIdList = orgFinanceCosts.stream().map(OrgFinanceCostV::getCommunityId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(communityIdList)){
                        //根据项目ID获取对应部门信息
                        List<CfgExternalDeportData> communityList = configClient.getDeportList(communityIdList);
                        communityMap = communityList.stream().collect(Collectors.groupingBy(CfgExternalDeportData::getCommunityId));
                    }
                }
            }
            for (VoucherBillZJV2 voucherBillZJ : voucherBillZJVPageV.getRecords()) {
                voucherBillZJ.setShowPushAgainBtn(PushBillTypeEnum.canPushAgain(voucherBillZJ.getPushState()));
                if (voucherBillZJ.getBillEventType().equals(2)) {
                    voucherBillZJ.setIsShowApprovel(PushBillApproveStateEnum.审核中.getCode() != voucherBillZJ.getApproveState() && PushBillApproveStateEnum.已审核.getCode() != voucherBillZJ.getApproveState());
                    List<VoucherPushBillDetailZJ> byVoucherBillNo = voucherBillNoToPushBillDetailListMap.get(voucherBillZJ.getVoucherBillNo());
//                            voucherBillDetailZJRepository.getByVoucherBillNo(voucherBillZJ.getVoucherBillNo());
                    VoucherBusinessReasonsV byVoucherDetail = pushBillZJAppService.getByVoucherDetail(byVoucherBillNo);
                    if (Objects.nonNull(byVoucherDetail) && CollectionUtils.isNotEmpty(byVoucherBillNo)) {
                        voucherBillZJ.setBusinessReasons(byVoucherBillNo.get(0).getCommunityName() + ",收款日期:" + byVoucherDetail.getPayTime() + ",银行到账日期:" + byVoucherDetail.getAccountTime());
                    }
                    //是否展示重新推送按钮
                } else if (voucherBillZJ.getBillEventType().equals(3)) {
                    voucherBillZJ.setIsShowApprovel(Boolean.FALSE);
                    List<VoucherPushBillDetailZJ> byVoucherBillNo = listMap.get(voucherBillZJ.getVoucherBillNo());
//                            voucherBillDetailZJRepository.getByVoucherBillNoNoSearch(voucherBillZJ.getVoucherBillNo());
                    String communityName = StringUtils.EMPTY;
                    if (CollectionUtils.isNotEmpty(byVoucherBillNo)){
                        communityName = byVoucherBillNo.get(0).getCommunityName();
                    }
                    voucherBillZJ.setBusinessReasons(communityName + "，结算日期：" + DateTimeUtil.formatDateTime(voucherBillZJ.getGmtModify()));
                    VoucherContractInvoiceZJ byVoucherBillId = voucherContractInvoiceZJRepository.getByVoucherBillId(voucherBillZJ.getId());
                    if (null != byVoucherBillId) {
                        voucherBillZJ.setBusinessUnitName(byVoucherBillId.getBusinessName());
                        voucherBillZJ.setBusinessType(byVoucherBillId.getBusinessName());
                    }
                }
                //收入确认新增部门列表
                else if (voucherBillZJ.getBillEventType().equals(1)){
                    String communityId = idToCommunityIdMap.get(voucherBillZJ.getCostCenterId());
                    if(StringUtils.isNotEmpty(communityId)){
                        voucherBillZJ.setDepartmentList(communityMap.get(communityId));
                    }
                }
                if (StringUtils.isNotBlank(voucherBillZJ.getRecordSimpleStr())){
                    String[] recordSimpleList = voucherBillZJ.getRecordSimpleStr().split(",");
                    List<FlowRecordSimpleVo> flowRecordSimpleVoList = Lists.newArrayList();
                    List<String> serialNumbers = Lists.newArrayList();
                    for (String recordSimpleStr : recordSimpleList) {
                        String[] split = recordSimpleStr.split(" ");
                        FlowRecordSimpleVo flowRecordSimpleVo = new FlowRecordSimpleVo();
                        flowRecordSimpleVo.setFlowRecordId(Long.parseLong(split[0]));
                        flowRecordSimpleVo.setSerialNumber(split[1]);
                        flowRecordSimpleVoList.add(flowRecordSimpleVo);
                        serialNumbers.add(split[1]);
                    }
                    voucherBillZJ.setRecordSimpleVoList(flowRecordSimpleVoList);
                    voucherBillZJ.setRecordSimpleStr(StringUtils.join(serialNumbers, ","));
                }
                if (StringUtils.isNotBlank(voucherBillZJ.getFlowDetailSimpleStr())){
                    String[] detailSimpleList = voucherBillZJ.getFlowDetailSimpleStr().split(",");
                    List<FlowDetailSimpleVo> flowDetailSimpleList = Lists.newArrayList();
                    List<String> serialNumbers = Lists.newArrayList();
                    for (String recordSimpleStr : detailSimpleList) {
                        String[] split = recordSimpleStr.split(" ");
                        FlowDetailSimpleVo flowRecordSimpleVo = new FlowDetailSimpleVo();
                        flowRecordSimpleVo.setFlowDetailId(Long.parseLong(split[0]));
                        flowRecordSimpleVo.setSerialNumber(split[1]);
                        flowDetailSimpleList.add(flowRecordSimpleVo);
                        serialNumbers.add(split[1]);
                    }
                    voucherBillZJ.setFlowDetailSimpleVoList(flowDetailSimpleList);
                    voucherBillZJ.setFlowDetailSimpleStr(StringUtils.join(serialNumbers, ","));
                }

                // 获取影像资料
                PageF<SearchF<?>> filePageSearch = new PageF<>();
                Field fieldBillNo = new Field();
                fieldBillNo.setName("voucher_bill_no");
                fieldBillNo.setMethod(1);
                fieldBillNo.setValue(voucherBillZJ.getVoucherBillNo());
                List<Field> fields = Arrays.asList(fieldBillNo);
                SearchF<AttachmentE> SearchF = new SearchF<>();
                SearchF.setFields(fields);
                filePageSearch.setConditions(SearchF);
                List<VoucherBillZJFileSV> forRecFileList = new ArrayList<>();
                if(ZJTriggerEventBillTypeEnum.收入确认.getCode() == voucherBillZJ.getBillEventType()){
                    forRecFileList = voucherBillDetailAppService.queryFileForRec(filePageSearch);
                }else{
                    forRecFileList = voucherBillDetailAppService.queryFileSPage(filePageSearch);
                }
                List<UploadLinkZJ> uploadLinkList = new ArrayList<>();
                if(CollectionUtils.isNotEmpty(forRecFileList)){
                    for (VoucherBillZJFileSV file : forRecFileList) {
                        UploadLinkZJ zjFileVo = new UploadLinkZJ();
                        zjFileVo.setName(file.getFileName());
                        zjFileVo.setUploadLink("/files/" + file.getFileKey());
                        uploadLinkList.add(zjFileVo);
                    }
                }
                voucherBillZJ.setUploadLink(uploadLinkList);
            }
        }
        return voucherBillZJVPageV;
    }

    private Map<String,List<VoucherPushBillDetailZJ>> voucherBillNoToPushBillDetailListMap(PageV<VoucherBillZJV2> voucherBillZJVPageV) {
        if (CollectionUtils.isEmpty(voucherBillZJVPageV.getRecords())){
            return Collections.emptyMap();
        } else {
            List<String> billNos = voucherBillZJVPageV.getRecords().stream()
                    .map(VoucherBillZJV::getVoucherBillNo)
                    .distinct()
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(billNos)){
                return Collections.emptyMap();
            }
            List<VoucherPushBillDetailZJ> detailZJList = voucherBillDetailZJRepository.getByVoucherBillNoList(billNos);
            if (CollectionUtils.isEmpty(detailZJList)){
                return Collections.emptyMap();
            }
            return detailZJList.stream().collect(Collectors.groupingBy(VoucherPushBillDetailZJ::getVoucherBillNo));
        }
    }

    private Map<String,List<VoucherPushBillDetailZJ>> voucherBillNoToPushBillDetailListMap2(PageV<VoucherBillZJV2> voucherBillZJVPageV) {
        if (CollectionUtils.isEmpty(voucherBillZJVPageV.getRecords())){
            return Collections.emptyMap();
        } else {
            List<String> billNos = voucherBillZJVPageV.getRecords().stream()
                    .filter(voucherBillZJ -> voucherBillZJ.getBillEventType().equals(3))
                    .map(VoucherBillZJV::getVoucherBillNo)
                    .distinct()
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(billNos)){
                return Collections.emptyMap();
            }
            List<VoucherPushBillDetailZJ> detailZJList = voucherBillDetailZJRepository.getByVoucherBillNoListNoSearch(billNos);
            if (CollectionUtils.isEmpty(detailZJList)){
                return Collections.emptyMap();
            }
            return detailZJList.stream().collect(Collectors.groupingBy(VoucherPushBillDetailZJ::getVoucherBillNo));
        }
    }

    private Map<Long, List<VoucherContractInvoiceZJ>>  voucherBillIdToInvoiceListMap(PageV<VoucherBillZJV2> voucherBillZJVPageV) {
        if (CollectionUtils.isEmpty(voucherBillZJVPageV.getRecords())){
            return Collections.emptyMap();
        } else {
            List<Long> ids = voucherBillZJVPageV.getRecords().stream()
                    .filter(voucherBillZJ -> voucherBillZJ.getBillEventType().equals(3))
                    .map(VoucherBillZJV::getId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(ids)){
                return Collections.emptyMap();
            }
            List<VoucherContractInvoiceZJ> invoiceZJList = voucherContractInvoiceZJRepository.getByVoucherBillIdList(ids);
            if (CollectionUtils.isEmpty(invoiceZJList)){
                return Collections.emptyMap();
            }
            return invoiceZJList.stream().collect(Collectors.groupingBy(VoucherContractInvoiceZJ::getVoucherBillId));
        }
    }

    @GetMapping("/getById")
    @ApiOperation(value = "通过单据主表id查询单据信息", notes = "通过单据主表id查询单据信息")
    public VoucherBillZJV getById (@Validated @RequestParam Long id) {
        return pushBillZJAppService.getById(id);
    }

    @PostMapping("/getMoney")
    @ApiOperation(value = "获取单据总金额", notes = "获取单据总金额")
    public VoucherBillZJMoneyV getMoney(@Validated @RequestBody PageF<SearchF<?>> form) {
        return voucherPushBillRepository.getMoney(form);
    }

    @PostMapping("/infer/batch")
    @ApiOperation(value = "批量同步", notes = "批量同步")
    public SyncBatchVoucherResultV syncBatchVoucher(@RequestBody SyncBatchPushZJBillF syncBatchPushBillF) {
        return pushBillZJAppService.syncBatchPushBill(syncBatchPushBillF);
    }
    @PostMapping("/infer/QueryFinanceOrderDealResult")
    @ApiOperation(value = "主动查询推单状态接口", notes = "主动查询推单状态接口")
    public void queryFinanceOrderDealResult(@RequestBody OrderStatusBody orderStatusBody) {
        pushBillZJAppService.queryFinanceOrderDealResult(orderStatusBody);
    }
    @PostMapping("/infer/queryOrderStatus")
    @ApiOperation(value = "主动查询推单状态三方接口", notes = "主动查询推单状态三方接口")
    public OrderDealResult queryOrderStatus(@RequestBody OrderStatusBody orderStatusBody) {
       return pushBillZJAppService.queryOrderStatus(orderStatusBody);
    }

    @PostMapping("/infer/delPushOrder")
    @ApiOperation(value = "中交删单接口", notes = "中交删单接口")
    public OrderStatusResult delPushOrder(@RequestBody OrderStatusDel orderStatusDel) {
       return pushBillZJAppService.delOrderDealResult(orderStatusDel);
    }

    @PostMapping("/addLinkZJ")
    @ApiOperation(value = "附件链接新增", notes = "附件链接新增")
    public boolean addLinkZJ(@RequestBody UploadLinkZJF uploadLinkZJF) {
        return voucherPushBillRepository.addLinkZJ(uploadLinkZJF);
    }
    @PostMapping("/addFileInfo")
    @ApiOperation(value = "报账单新增影像信息", notes = "报账单新增影像信息")
    public boolean addFileInfo(@RequestBody @Validated UpLoadFileF upLoadFileF) {
        return voucherBillFileZJRepository.addFileInfo(upLoadFileF);
    }



    @GetMapping("/deleteFileInfo")
    @ApiOperation(value = "报账单删除影像信息", notes = "报账单删除影像信息")
    public boolean deleteFileInfo(@Param("id") Long id) {
        return voucherBillFileZJService.deleteById(id);
    }


    /**
     * 财务云推送报账单状态接口
     */
    @PostMapping("/pushInfo")
    @ApiOperation(value = "财务云状态流转", notes = "财务云状态流转")
    public PushInfoRspV voucherInfo(@RequestBody PushInfoF pushInfoF){
        log.info("pushInfoF信息:" + JSON.toJSON(pushInfoF));
        // 判断推送过来的状态， 只处理驳回的
        if (pushInfoF.getBZDSTAGECODE().equals("驳回") && pushInfoF.getBZDSTATUSCODE().equals("-1")) {
            pushBillZJAppService.voucherInfo(pushInfoF, PushBillTypeEnum.单据驳回.getCode());
        }
        /**
         * 制单成功示例 标准编码 0
         * {"bZDOPER":"L20100568","bZDPREVSTATUSCODE":"0","bZDZDR":"庞水娟","bZDPREVSTAGECODE":"制单","bZDNM":"BJZJhxbz202410280039",
         * "bIZCODE":"CCCG-DMC","rEMARK":"","bZDSTAGECODE":"财务审核","bZDSTATUSDATE":"2024-10-29 10:19:34","iPSCODE":"7",
         * "bZDSTATUSCODE":"2","cWYNM":"409d43bf-b0d2-47a4-ab4c-e21da6ddbf84"}
         **/
        if (Objects.nonNull(pushInfoF.getBZDSTATUSCODE()) &&
                pushInfoF.getBZDSTATUSCODE().equals("0")){
            pushBillZJAppService.voucherInfo(pushInfoF, PushBillTypeEnum.制单成功.getCode());
        }
        /**
         * 财务云审核通过示例 标准编码 10
         * {"bZDPREVSTAGECODE":"财务审核","rEMARK":"同意","bZDSTAGECODE":"结算办理"}
         **/
        if (Objects.nonNull(pushInfoF.getBZDSTATUSCODE()) &&
                pushInfoF.getBZDSTATUSCODE().equals("10") &&
                StringUtils.equals("财务审核",pushInfoF.getBZDPREVSTAGECODE()) &&
                StringUtils.equals("同意",pushInfoF.getREMARK()) &&
                StringUtils.equals("结算办理",pushInfoF.getBZDSTAGECODE())){
            pushBillZJAppService.voucherInfo(pushInfoF, PushBillTypeEnum.单据审核完成.getCode());
        }
        /**
         凭证生成 标准编码 30
         {"bZDOPER":"R00000261","bZDPREVSTATUSCODE":"30","bZDZDR":"财务云接口专用账号",
         "bZDPREVSTAGECODE":"凭证生成","bZDNM":"BJZJhxbz202505200189","bIZCODE":"CCCG-DMC",
         "rEMARK":"","bZDSTAGECODE":"完成","bZDSTATUSDATE":"2025-05-21 09:30:48","iPSCODE":"7",
         "bZDSTATUSCODE":"6","cWYNM":"aa6f1f3c-a01b-48a7-a2c4-d7de0a6b6e00"}
         **/
        if (Objects.nonNull(pushInfoF.getBZDSTATUSCODE()) &&
                pushInfoF.getBZDSTATUSCODE().equals("30")){
            pushBillZJAppService.handleOnVoucherBillV2(pushInfoF.getBZDNM(),pushInfoF.getREMARK(),PushBillTypeEnum.凭证生成.getCode());
        }
        return new PushInfoRspV();
    }

    @GetMapping("/initiateBillApprovel")
    @ApiOperation(value = "根据报账单号发起审批", notes = "根据报账单号发起审批")
    public Boolean initiateBillApprovel (@Validated @RequestParam String voucherBillNo) {
        return pushBillZJAppService.initiateBillApprovel(voucherBillNo);
    }

}
