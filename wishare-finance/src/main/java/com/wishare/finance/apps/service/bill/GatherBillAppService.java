package com.wishare.finance.apps.service.bill;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.fo.AddGatherBillDetailF;
import com.wishare.finance.apps.model.bill.fo.AddGatherBillF;
import com.wishare.finance.apps.model.bill.fo.ApproveBatchGatherBillF;
import com.wishare.finance.apps.model.bill.fo.ApproveGatherBillF;
import com.wishare.finance.apps.model.bill.fo.BillApplyBatchF;
import com.wishare.finance.apps.model.bill.fo.BillApplyF;
import com.wishare.finance.apps.model.bill.fo.BillFlagF;
import com.wishare.finance.apps.model.bill.fo.BillInferenceF;
import com.wishare.finance.apps.model.bill.fo.DeleteBatchPayBillF;
import com.wishare.finance.apps.model.bill.fo.GatherApplyUpdateF;
import com.wishare.finance.apps.model.bill.fo.GatherBillCarryoverF;
import com.wishare.finance.apps.model.bill.fo.GatherBillInvalidF;
import com.wishare.finance.apps.model.bill.fo.GatherCollectF;
import com.wishare.finance.apps.model.bill.fo.GatherInvoiceF;
import com.wishare.finance.apps.model.bill.fo.GetPaySourceByBillNoF;
import com.wishare.finance.apps.model.bill.fo.ListBillInferenceF;
import com.wishare.finance.apps.model.bill.fo.PayInvoiceListF;
import com.wishare.finance.apps.model.bill.fo.PayListF;
import com.wishare.finance.apps.model.bill.vo.BatchBillInferenceF;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.model.bill.vo.BillSimpleInfoV;
import com.wishare.finance.apps.model.bill.vo.GatherBillDetailV;
import com.wishare.finance.apps.model.bill.vo.GatherBillIgnoreV;
import com.wishare.finance.apps.model.bill.vo.GatherBillV;
import com.wishare.finance.apps.model.bill.vo.GatherDetailV;
import com.wishare.finance.apps.model.bill.vo.GetPaySourceByBillNoV;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceDto;
import com.wishare.finance.apps.model.invoice.invoice.vo.GatherInvoiceRefreshV;
import com.wishare.finance.apps.model.invoice.invoice.vo.RefreshStateV;
import com.wishare.finance.domains.bill.command.AddGatherBillCommand;
import com.wishare.finance.domains.bill.command.AddGatherBillDetailCommand;
import com.wishare.finance.domains.bill.command.ApproveCommand;
import com.wishare.finance.domains.bill.command.BatchApproveBillCommand;
import com.wishare.finance.domains.bill.command.BillApplyBatchCommand;
import com.wishare.finance.domains.bill.command.BillApplyCommand;
import com.wishare.finance.domains.bill.command.InvalidCommand;
import com.wishare.finance.domains.bill.consts.enums.BillApproveOperateTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.BillBatchResultDto;
import com.wishare.finance.domains.bill.dto.GatherAndPayStatisticsDto;
import com.wishare.finance.domains.bill.dto.GatherDto;
import com.wishare.finance.domains.bill.dto.PayListDto;
import com.wishare.finance.domains.bill.entity.AdvanceBill;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.entity.TemporaryChargeBill;
import com.wishare.finance.domains.bill.service.AdvanceBillDomainService;
import com.wishare.finance.domains.bill.service.GatherBillDomainService;
import com.wishare.finance.domains.bill.service.ReceivableBillDomainService;
import com.wishare.finance.domains.bill.service.TemporaryChargeBillDomainService;
import com.wishare.finance.domains.invoicereceipt.dto.GatherDetailInfo;
import com.wishare.finance.domains.invoicereceipt.service.InvoiceDomainService;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.event.EventTransactional;
import com.wishare.finance.infrastructure.support.lock.BusinessLockHelper;
import com.wishare.finance.infrastructure.support.lock.LockException;
import com.wishare.finance.infrastructure.support.lock.LockerEnum;
import com.wishare.finance.infrastructure.support.lock.MultiLocker;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yancao
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GatherBillAppService {

    private final GatherBillDomainService gatherBillDomainService;

    private final ReceivableBillDomainService receivableBillDomainService;

    private final AdvanceBillDomainService advanceBillDomainService;

    private final TemporaryChargeBillDomainService temporaryChargeBillDomainService;

    @Lazy
    @Resource
    private InvoiceDomainService invoiceDomainService;


    /**
     * 批量新增收款单
     *
     * @param addGatherBillFList
     * @return
     */
    public List<GatherBillV> addBatch(List<AddGatherBillF> addGatherBillFList) {
        List<AddGatherBillCommand> addGatherBillCommandList = Lists.newArrayList();
        for (AddGatherBillF addGatherBillF : addGatherBillFList) {
            AddGatherBillCommand addGatherBillCommand = Global.mapperFacade.map(addGatherBillF, AddGatherBillCommand.class);
            List<AddGatherBillDetailF> addGatherBillDetails = addGatherBillF.getAddGatherBillDetails();
            List<AddGatherBillDetailCommand> addGatherBillDetailCommands = Global.mapperFacade.mapAsList(addGatherBillDetails, AddGatherBillDetailCommand.class);
            addGatherBillCommand.setAddGatherBillDetailCommandList(addGatherBillDetailCommands);
            addGatherBillCommandList.add(addGatherBillCommand);
        }
        return gatherBillDomainService.addBatch(addGatherBillCommandList);
    }

    /**
     * 批量同步收款单
     *
     * @param gatherBillList
     * @return
     */
    public Boolean addGatherBatch(List<GatherBill> gatherBillList) {
        return gatherBillDomainService.addGatherBatch(gatherBillList);
    }

    /**
     * 批量同步收款单明细
     *
     * @param gatherDetailList
     * @return
     */
    public Boolean addGatherDetailBatch(List<GatherDetail> gatherDetailList) {
        return gatherBillDomainService.addGatherDetailBatch(gatherDetailList);
    }
    /**
     * 获取收款单信息
     * @param hncId
     * @param communityId
     */
    public List<GatherBill> getByOutBusId(String hncId,String communityId) {
        return gatherBillDomainService.getByOutBusId(hncId,communityId);
    }


    /**
     * 发起审核申请
     *
     * @param billApplyF 申请参数
     * @return Boolean
     */
    public Long apply(BillApplyF billApplyF) {
        List<String> strBillIds = List.of(String.valueOf(billApplyF.getBillId()));
        try {
            BusinessLockHelper.tryMultiLock(MultiLocker.of(LockerEnum.GATHER_LOCK, strBillIds,
                    LockerEnum.GATHER_LOCK.defLeaseTime()));
        } catch (LockException e) {
            ErrorAssertUtil.throw400(ErrorMessage.PRE_GATHER_OPERATING);
        }
        try {
            return gatherBillDomainService.apply(new BillApplyCommand(billApplyF.getBillId(), billApplyF.getReason(),
                    BillApproveOperateTypeEnum.valueOfByCode(billApplyF.getApproveOperateType()), billApplyF.getOutApproveId(),billApplyF.getExtField1(),billApplyF.getSupCpUnitId())
                    .initDetail(billApplyF.getDetail()));
        } finally {
            // 收款单解锁
            BusinessLockHelper.unLock(MultiLocker.of(LockerEnum.GATHER_LOCK, strBillIds));
        }
    }

    /**
     * 批量发起审核申请
     *
     * @param billApplyBatchF 申请参数
     * @return Boolean
     */
    public List<Long> applyBatch(BillApplyBatchF billApplyBatchF) {
        if (billApplyBatchF.getApproveOperateType().compareTo(7) == 0){
            log.info("收款单退款调用参数:{}", JSONObject.toJSONString(billApplyBatchF));
        }
        List<String> strBillIds = billApplyBatchF.getBillIds().stream().map(String::valueOf).distinct().collect(Collectors.toList());
        try {
            BusinessLockHelper.tryMultiLock(MultiLocker.of(LockerEnum.GATHER_LOCK, strBillIds,
                    LockerEnum.GATHER_LOCK.defLeaseTime()));
        } catch (LockException e) {
            ErrorAssertUtil.throw400(ErrorMessage.PRE_GATHER_OPERATING);
        }
        try {
            return gatherBillDomainService.applyBatch(new BillApplyBatchCommand(billApplyBatchF.getBillIds(), billApplyBatchF.getReason(),billApplyBatchF.getExtField1(),
                    BillApproveOperateTypeEnum.valueOfByCode(billApplyBatchF.getApproveOperateType()), billApplyBatchF.getOutApproveId(),billApplyBatchF.getSupCpUnitId(),
                    billApplyBatchF.getOperationId(), billApplyBatchF.getOperationRemark())
                    .initDetail(billApplyBatchF.getDetail()));
        } finally {
            // 收款单解锁
            BusinessLockHelper.unLock(MultiLocker.of(LockerEnum.GATHER_LOCK, strBillIds));
        }
    }

    /**
     * 审核付款单
     *
     * @param approveGatherBillF 审核信息
     * @return Boolean
     */
    @Transactional
    @EventTransactional
    public Boolean approve(ApproveGatherBillF approveGatherBillF) {
        return gatherBillDomainService.approve(Global.mapperFacade.map(approveGatherBillF, ApproveCommand.class));
    }

    /**
     * 批量审核收款单
     */
    @Transactional
    @EventTransactional
    public Boolean approveBatch(ApproveBatchGatherBillF approveBatchGatherBillF) {
        log.info("批量审核收款单下发参数:{}",JSONObject.toJSONString(approveBatchGatherBillF));
        BatchApproveBillCommand command = new BatchApproveBillCommand();
        command.setApproveState(BillApproveStateEnum.valueOfByCode(approveBatchGatherBillF.getApproveState()));
        command.setRejectReason(approveBatchGatherBillF.getRejectReason());
        command.setQuery(approveBatchGatherBillF.getQuery());
        command.setBillIds(approveBatchGatherBillF.getBillIds());
        command.setSupCpUnitId(approveBatchGatherBillF.getSupCpUnitId());
        command.setGatherMap(approveBatchGatherBillF.getGatherMap());
        return gatherBillDomainService.approveBatch(command, BillTypeEnum.收款单.getCode());
    }

    /**
     * 删除付款单
     *
     * @param payBillId 付款单id
     * @return Boolean
     */
    public Boolean delete(Long payBillId) {
        return gatherBillDomainService.delete(payBillId);
    }

    /**
     * 批量删除应付账单
     *
     * @param deleteBatchPayBillF 删除参数
     * @return BillBatchResultDto
     */
    public BillBatchResultDto deleteBatch(DeleteBatchPayBillF deleteBatchPayBillF) {
        return null;
    }

    /**
     * 查询收款单信息
     *
     * @param gatherBillId 付款单id
     * @return GatherBillV
     */
    public GatherBillV queryById(Long gatherBillId, String supCpUnitId) {
        GatherBillV gatherBillV = Global.mapperFacade.map(gatherBillDomainService.queryById(gatherBillId, supCpUnitId), GatherBillV.class);
        Long gatherId = gatherBillV.getId();
        List<GatherDetailV> gatherDetails = gatherBillV.getGatherDetails();
        //null预收单,不为null，可能是临时或者应收
//        if(Objects.nonNull(gatherId)){
//            gatherDetails.forEach(detailV ->{
//                ReceivableBill receivableBill = receivableBillDomainService.getById(detailV.getRecBillId());
//                if (receivableBill!=null) {
//                    detailV.setUnitPrice(receivableBill.getUnitPrice());
//                    detailV.setChargingCount(receivableBill.getChargingCount());
//                    detailV.setAccountDate(receivableBill.getAccountDate());
//                    detailV.setReceivableAmount(receivableBill.getReceivableAmount());
//                    detailV.setStartTime(receivableBill.getStartTime());
//                    detailV.setEndTime(receivableBill.getEndTime());
//                }
//            });
//        }else{
//            gatherDetails.forEach(detailV ->{
//                AdvanceBill advanceBill = advanceBillDomainService.getById(detailV.getRecBillId());
//                if (advanceBill!=null) {
//                    detailV.setAccountDate(advanceBill.getAccountDate());
//                    detailV.setReceivableAmount(advanceBill.getReceivableAmount());
//                    detailV.setStartTime(advanceBill.getStartTime());
//                    detailV.setEndTime(advanceBill.getEndTime());
//                    detailV.setTotalAmount(advanceBill.getTotalAmount());
//                }
//            });
//        }
        gatherDetails.forEach(detailV ->{
            ReceivableBill receivableBill = receivableBillDomainService.getById(detailV.getRecBillId(), supCpUnitId);
            if (Objects.nonNull(receivableBill)) {
                detailV.setUnitPrice(receivableBill.getUnitPrice());
                detailV.setChargingCount(receivableBill.getChargingCount());
                detailV.setAccountDate(receivableBill.getAccountDate());
                detailV.setReceivableAmount(receivableBill.getReceivableAmount());
                detailV.setStartTime(receivableBill.getStartTime());
                detailV.setEndTime(receivableBill.getEndTime());
                detailV.setTotalAmount(receivableBill.getTotalAmount());
            }
            if (Objects.isNull(receivableBill)){
                AdvanceBill advanceBill = advanceBillDomainService.getById(detailV.getRecBillId(),supCpUnitId);
                if(Objects.nonNull(advanceBill)){
                    detailV.setAccountDate(advanceBill.getAccountDate());
                    detailV.setReceivableAmount(advanceBill.getReceivableAmount());
                    detailV.setStartTime(advanceBill.getStartTime());
                    detailV.setEndTime(advanceBill.getEndTime());
                    detailV.setTotalAmount(advanceBill.getTotalAmount());
                }else {
                    throw BizException.throw400("收款单对应账单不存在");
                }
            }
        });
        return gatherBillV;
    }

    /**
     * 账单结转
     *
     * @param billCarryoverF 结转参数
     * @return Boolean
     */
    public Boolean carryover(GatherBillCarryoverF billCarryoverF) {
        return null;
    }

    /**
     * 分页查询已审核收款单列表
     *
     * @param queryF 分页入参
     * @return PageV
     */
    public PageV<GatherBillV> getApprovedPage(PageF<SearchF<?>> queryF) {
        return gatherBillDomainService.getApprovedPage(queryF);
    }

    /**
     * 分页查询已审核收款单列表
     *
     * @param queryF 分页入参
     * @return PageV
     */
    public PageV<GatherBillV> billQueryPage(PageF<SearchF<?>> queryF) {
        return gatherBillDomainService.billQueryPage(queryF);
    }

    /**
     * 分页查询未审核收款单列表
     *
     * @param queryF 查询条件
     * @return PageV
     */
    public PageV<GatherBillV> queryNotApprovedPage(PageF<SearchF<?>> queryF) {
        return gatherBillDomainService.queryNotApprovedPage(queryF);
    }

    /**
     * 分页查询
     *
     * @param queryF
     * @return
     */
    public PageV<GatherBillV> getPage(PageF<SearchF<?>> queryF) {
        return gatherBillDomainService.getPage(queryF);
    }

    /**
     * 收款单作废
     *
     * @param payBillInvalidF 作废参数
     * @return Boolean
     */
    public Boolean invalid(GatherBillInvalidF payBillInvalidF) {
        return gatherBillDomainService.invalid(Global.mapperFacade.map(payBillInvalidF, InvalidCommand.class));
    }

    /**
     * 冲销付款单
     *
     * @param payBillId 付款单id
     * @return Boolean
     */
    public Boolean reverse(Long payBillId) {
        return null;
    }

    /**
     * 查询收款单详情(包含明细)
     *
     * @param gatherBillId 收款单id
     * @return GatherBillDetailV
     */
    public GatherBillDetailV queryDetailById(Long gatherBillId, String supCpUnitId) {
        return gatherBillDomainService.queryDetailById(gatherBillId, supCpUnitId);
    }

    /**
     * 根据id集合获取收款单
     *
     * @param payBillIdList 收款单id集合
     * @return List
     */
    public List<GatherBillV> queryByIdList(List<Long> payBillIdList, String supCpUnitId) {
        return gatherBillDomainService.queryByIdList(payBillIdList, supCpUnitId);
    }

    /**
     * 查询历史缴费记录
     *
     * @param queryF 查询参数
     * @return PageV
     */
    public PageV<PayListDto> payList(PayListF queryF) {
        Page<PayListDto> page = gatherBillDomainService.payList(queryF);
        return PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
    }

    /**
     * 查询开票收款单列表
     *
     * @param queryF 查询参数
     * @return PageV
     */
    public PageV<PayListDto> payInvoiceList(PayInvoiceListF queryF) {
        Page<PayListDto> page = gatherBillDomainService.payInvoiceList(queryF);
        List<PayListDto> records = page.getRecords();
        if(CollectionUtils.isNotEmpty(records)){
            List<PayListDto> receivableTypeBills = records.stream().filter(detailBill -> BillTypeEnum.预收账单.getCode() != detailBill.getBillType()).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(receivableTypeBills)){
                List<Long> receivableTypeIds = receivableTypeBills.stream().map(PayListDto::getRecBillId).collect(Collectors.toList());
                List<ReceivableBill> receivableBillLists = receivableBillDomainService.getList(receivableTypeIds, queryF.getCommunityId());
                for (PayListDto payItem : records){
                    Integer billType = payItem.getBillType();
                    Long recBillId = payItem.getRecBillId();
                    if(BillTypeEnum.预收账单.getCode() != billType){
                        List<ReceivableBill> bills = receivableBillLists.stream().filter(receivableBill -> recBillId.equals(receivableBill.getId())).collect(Collectors.toList());
                        payItem.setBillType(!CollectionUtils.isEmpty(bills) ? bills.get(0).getBillType() : BillTypeEnum.应收账单.getCode());
                    }
                }
            }
        }

        PageV<PayListDto> of = PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
        return of;
    }


    /**
     * 导出收款单
     *
     * @param queryF   查询参数
     * @param response response
     */
    public void export(PageF<SearchF<?>> queryF, HttpServletResponse response) {
        gatherBillDomainService.export(queryF, response);
    }

    /**
     * 获取推凭用的账单
     *
     * @param form
     * @param billTypeEnum
     * @return
     */
    public PageV<BillInferenceV> pageBillInferenceInfo(ListBillInferenceF form, BillTypeEnum billTypeEnum) {
        SearchF<BillInferenceV> searchF = new SearchF<>();
        searchF.setFields(form.getFieldList());
        PageF<SearchF<BillInferenceV>> page = new PageF<>();
        page.setConditions(searchF);
        page.setPageNum(form.getPageNum());
        page.setPageSize(form.getPageSize());
        Page<BillInferenceV> pageV = gatherBillDomainService.pageBillInferenceInfo(page, form.getEventType(), form.getSupCpUnitId());
        fillTaxRate(pageV.getRecords(), form.getSupCpUnitId());
        return PageV.of(page.getPageNum(), page.getPageSize(), pageV.getTotal(), pageV.getRecords());
    }

    /**
     * 获取账单推凭信息
     *
     * @param billInferenceF
     * @return
     */
    public List<BillInferenceV> listInferenceInfo(BillInferenceF billInferenceF) {
        List<BillInferenceV> list = gatherBillDomainService.listInferenceInfo(billInferenceF);
        fillTaxRate(list, billInferenceF.getSupCpUnitId());
        return list;
    }

    /**
     * 获取账单推凭信息
     *
     * @param batchBillInferenceF
     * @return
     */
    public List<BillInferenceV> listInferenceInfoByIds(BatchBillInferenceF batchBillInferenceF) {
        List<BillInferenceV> list = gatherBillDomainService.listInferenceInfoByIds(batchBillInferenceF);
        fillTaxRate(list, batchBillInferenceF.getSupCpUnitId());
        return list;
    }

    /**
     * 填充收款单的税率
     *
     * @param list
     */
    private void fillTaxRate(List<BillInferenceV> list, String supCpUnitId) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        fillTaxRate(list.stream()
            .filter(billInference -> billInference.getTaxBillType() == 0)
            .collect(Collectors.toList()), 0, supCpUnitId);


        fillTaxRate(list.stream()
            .filter(billInference -> billInference.getTaxBillType() == 1)
            .collect(Collectors.toList()), 1, supCpUnitId);
    }

    /**
     * 根据账单类型查税率
     *
     * @param list
     * @param type 0应收 1预收
     */
    private void fillTaxRate(List<BillInferenceV> list, Integer type, String supCpUnitId) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<Long> billIds = list.stream().map(BillInferenceV::getTaxBillId)
                .collect(Collectors.toList());
        Map<Long, BigDecimal> taxMap = null;
        if (type == 0) {
            // todo 临时账单和应收账单合并使用的逻辑
//            List<ReceivableBill> taxBillList = receivableBillDomainService.getList(billIds);
//            if (CollectionUtils.isEmpty(taxBillList)) {
//                return;
//            }
//            taxMap = taxBillList.stream().collect(Collectors.toMap(ReceivableBill::getId, ReceivableBill::getTaxRate));
            List<ReceivableBill> taxBillList = receivableBillDomainService.getList(billIds,supCpUnitId);
            if (!CollectionUtils.isEmpty(taxBillList)) {
                taxMap = taxBillList.stream().collect(Collectors.toMap(ReceivableBill::getId, item -> Objects.isNull(item.getTaxRate()) ? new BigDecimal(0) : item.getTaxRate()));
                if (Objects.nonNull(taxMap) && !taxMap.isEmpty()) {
                    Map<Long, BigDecimal> finalTaxMap1 = taxMap;
                    billIds = billIds.stream()
                            .filter(billId -> !finalTaxMap1.containsKey(billId))
                            .collect(Collectors.toList());
                }
            }
            if (!CollectionUtils.isEmpty(billIds)) {
                List<TemporaryChargeBill> taxBillList2 = temporaryChargeBillDomainService.getList(billIds,supCpUnitId);
                if (!CollectionUtils.isEmpty(taxBillList2)) {
                    if (taxMap == null) {
                        taxMap = taxBillList2.stream().collect(Collectors.toMap(TemporaryChargeBill::getId, item -> Objects.isNull(item.getTaxRate()) ? new BigDecimal(0) : item.getTaxRate()));
                    } else {
                        taxMap.putAll(taxBillList2.stream().collect(Collectors.toMap(TemporaryChargeBill::getId, item -> Objects.isNull(item.getTaxRate()) ? new BigDecimal(0) : item.getTaxRate())));
                    }
                }
            }
        } else if (type == 1) {
            List<AdvanceBill> taxBillList = advanceBillDomainService.getList(billIds,supCpUnitId);
            if (CollectionUtils.isEmpty(taxBillList)) {
                return;
            }
            taxMap = taxBillList.stream().collect(Collectors.toMap(AdvanceBill::getId, item -> Objects.isNull(item.getTaxRate()) ? new BigDecimal(0) : item.getTaxRate()));
        }
        if (Objects.isNull(taxMap) || taxMap.isEmpty()) {
            return;
        }
        Map<Long, BigDecimal> finalTaxMap = taxMap;
        list.forEach(billInference ->
                billInference.setTaxRate(finalTaxMap.getOrDefault(billInference.getTaxBillId(), new BigDecimal(0))));
    }


    /**
     * 处理数据
     *
     * @return
     */
    public Boolean handleData(String supCpUnitId) {
        return gatherBillDomainService.handleData(supCpUnitId);
    }

    public PageV<GatherBillIgnoreV> queryPageGatherBillIgnore(PageF<SearchF<?>> pageF) {
        Page<GatherDto> gatherDtoPage = gatherBillDomainService.queryPageGatherBillIgnore(pageF);
        return PageV.of(pageF, gatherDtoPage.getTotal(), Global.mapperFacade.mapAsList(gatherDtoPage.getRecords(), GatherBillIgnoreV.class));
    }


    /**
     * 分页查询收款明细记录
     * @param form 分页参数
     * @return {@link GatherDetailInfo}
     */
    public PageV<GatherDetailV> queryPageGatherDetail(PageF<SearchF<?>> form) {
        Page<GatherDetailV> gatherDtoPage = gatherBillDomainService.queryPageGatherDetail(form);
        return PageV.of(form, gatherDtoPage.getTotal(), Global.mapperFacade.mapAsList(gatherDtoPage.getRecords(), GatherDetailV.class));
    }

    /**
     * 收款单其他账户代收
     *
     * @param gatherCollectF
     * @return
     */
    public Boolean collect(GatherCollectF gatherCollectF) {
        return gatherBillDomainService.collect(gatherCollectF);
    }

    public List<GetPaySourceByBillNoV> getPaySourceByBillNo(GetPaySourceByBillNoF getPaySourceByBillNoF) {
        return gatherBillDomainService.getPaySourceByBillNo(getPaySourceByBillNoF);
    }


    /**
     * 收款单审核失败更新状态
     * @param gatherApplyUpdateF gatherApplyUpdateF
     * @return {@link Boolean}
     */
    public Boolean updateApplyInfo(GatherApplyUpdateF gatherApplyUpdateF) {
        return gatherBillDomainService.updateApplyInfo(gatherApplyUpdateF);
    }

    /**
     * 发起开票
     * 根据收款单ids 修改收款单信息为开票中 [invoiceState = 1]
     * @param gatherBillIds
     * @return
     */
    public Boolean gatherBillInvoiceBatch(List<Long> gatherBillIds,String supCpUnitId, Map<Long,Integer> billIdsMap) {
        return gatherBillDomainService.gatherBillInvoiceBatch(gatherBillIds,supCpUnitId, billIdsMap);
    }

    /**
     * 发起开票
     * 根据收款明细ids 修改收款明细信息为开票中 [invoiceState = 1]
     * @param gatherDetailIds
     * @return
     */
    public Boolean gatherDetailInvoiceBatch(List<Long> gatherDetailIds,String supCpUnitId, Map<Long,Integer> billIdsMap) {
        return gatherBillDomainService.gatherDetailInvoiceBatch(gatherDetailIds,supCpUnitId, billIdsMap);
    }


    /**
     * 更新收款单及明细开票状态
     */
    public void updateGatherInvoiceStatus(String supCpUnitId){
        gatherBillDomainService.updateGatherInvoiceStatus(supCpUnitId);
    }

    public void deleteGatherInvoiceStatusCache() {
        RedisHelper.delete("communityIdsToRefreshInvoice");
    }

    public GatherInvoiceRefreshV updateAllGatherInvoiceStatus(){
        String communityIdsToRefreshInvoice = RedisHelper.get("communityIdsToRefreshInvoice");
        GatherInvoiceRefreshV gatherInvoiceRefreshV;
        if (StringUtils.isNotBlank(communityIdsToRefreshInvoice)) {
            gatherInvoiceRefreshV = JSON.parseObject(
                    communityIdsToRefreshInvoice, GatherInvoiceRefreshV.class);
        } else {
            List<String> oldCommunityToRefresh = invoiceDomainService.findOldCommunityToRefresh();
            if (CollectionUtils.isNotEmpty(oldCommunityToRefresh)) {
                gatherInvoiceRefreshV = new GatherInvoiceRefreshV();
                gatherInvoiceRefreshV.setRefreshStates(oldCommunityToRefresh.stream().filter(
                        StringUtils::isNotBlank).map(id -> {
                    RefreshStateV refreshStateV = new RefreshStateV();
                    refreshStateV.setCommunityId(id);
                    return refreshStateV;
                }).collect(Collectors.toList()));
            } else {
                return new GatherInvoiceRefreshV();
            }
        }
        List<RefreshStateV> refreshStates = gatherInvoiceRefreshV.getRefreshStates().stream()
                .filter(state -> state.getState() == 0)
                .collect(Collectors.toList());
        int i = 0;
        for (RefreshStateV refreshState : refreshStates) {
            i++;
            if (i > 2) {
                break;
            }
            try {
                gatherBillDomainService.updateGatherInvoiceStatus(refreshState.getCommunityId());
                refreshState.setState(1);
            } catch (Exception e) {
                log.error("刷新发票账单失败:项目id:{}", refreshState.getCommunityId());
                refreshState.setState(2);
            }
        }
        long successNum = gatherInvoiceRefreshV.getRefreshStates().stream()
                .filter(state -> state.getState() == 1).count();
        gatherInvoiceRefreshV.setResult("项目总数：" + CollectionUtils.size(gatherInvoiceRefreshV.getRefreshStates()) + "成功:" + successNum);
        RedisHelper.set("communityIdsToRefreshInvoice", JSON.toJSONString(gatherInvoiceRefreshV));
        RedisHelper.expire("communityIdsToRefreshInvoice", 60*60*12);
        return gatherInvoiceRefreshV;
        // gatherBillDomainService.updateGatherInvoiceStatus(supCpUnitId);
    }

    /**
     * 根据收款明细id集合获取收款信息列表
     *
     * @param gatherDetailIds 收款明细id集合
     * @return List
     */
    public List<GatherDetailV> gatherDetailList(List<Long> gatherDetailIds, String supCpUnitId) {
        return gatherBillDomainService.gatherDetailList(gatherDetailIds, supCpUnitId);
    }

    /**
     * 查询收款单可开票金额
     *
     * @param gatherInvoiceF
     */
    public BillSimpleInfoV canInvoiceInfo(GatherInvoiceF gatherInvoiceF) {
        return gatherBillDomainService.canInvoiceInfo(gatherInvoiceF);
    }

    /**
     * 根据收款明细id集合获取收款信息列表
     *
     * @param gatherDetailIds 收款明细id集合
     * @return List
     */
    public List<GatherDetailV> getGatherDetails(List<Long> gatherDetailIds, String supCpUnitId) {
        return gatherBillDomainService.getGatherDetails(gatherDetailIds, supCpUnitId);
    }

    /**
     * 根据账单编号id获取收款信息列表
     *
     * @param receivableBillIds 账单编号集合
     * @return List
     */
    public List<GatherDetailV> getGatherDetailsByRecIds(List<Long> receivableBillIds, String supCpUnitId) {
        return gatherBillDomainService.getGatherDetailsByRecIds(receivableBillIds, supCpUnitId);
    }

    /**
     * 获取收款单统计信息
     * @param form
     * @return
     */
    public GatherAndPayStatisticsDto statistics(SearchF<?> form) {
        return gatherBillDomainService.statistics(form);
    }

    /**
     * 根据收款明细id集合删除收款单和明细
     * @param gatherDetailIds supCpUnitId 收款明细id集合
     * @return Boolean
     */
    public Boolean deleteGatherBillDetails(List<Long> gatherDetailIds, String supCpUnitId) {
        return gatherBillDomainService.deleteGatherBillDetails(gatherDetailIds, supCpUnitId);
    }


    /**
     * 查询收款单id
     * @param advanceBillId
     * @param supCpUnitId
     * @return
     */
    public Long getGatherBillId(Long advanceBillId,String supCpUnitId){
        return gatherBillDomainService.getGatherBillId(advanceBillId,supCpUnitId);

    }

    public Boolean editGatherBillFlag(BillFlagF billFlagF){
        return gatherBillDomainService.editGatherBillFlag(billFlagF);
    }

    public List<InvoiceDto> queryInvoiceDetailByGatherBillIds(List<Long> gatherBillIds) {
       return invoiceDomainService.queryInvoiceDetailByGatherBillIds(gatherBillIds);
    }
}
