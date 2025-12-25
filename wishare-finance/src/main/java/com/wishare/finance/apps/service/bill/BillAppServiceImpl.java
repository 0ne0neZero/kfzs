package com.wishare.finance.apps.service.bill;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.OptionStyle;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.apps.model.bill.fo.*;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.command.*;
import com.wishare.finance.domains.bill.consts.enums.BillApproveOperateTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.*;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.repository.mapper.BillFreezeMapper;
import com.wishare.finance.domains.bill.repository.mapper.ReceivableBillMapper;
import com.wishare.finance.domains.bill.service.BillDomainService;
import com.wishare.finance.domains.voucher.support.fangyuan.service.BillRuleDomainService;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.event.EventTransactional;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 账单应用服务
 *
 * @param <DS> 领域服务
 * @param <B>  账单
 * @Author dxclay
 * @Date 2022/9/23
 * @Version 1.0
 */
public class BillAppServiceImpl<DS extends BillDomainService, B extends Bill> implements BillAppService<DS, B> {

    @Setter(onMethod_ = @Autowired)
    protected DS baseBillDomainService;

    @Autowired
    private ReceivableBillMapper billMapper;

    @Autowired
    private BillFreezeMapper freezeMapper;

    @Override
    public BillTypeEnum getBillType() {
        return null;
    }

    @Override
    @Transactional
    public <T, F> T addBill(F addBillF, Class<T> tClass) {
        AddBillCommand<B> addBillCommand = new AddBillCommand<>();
        addBillCommand.setBill((B) Global.mapperFacade.map(addBillF, baseBillDomainService.getBillClass()));
        Global.mapperFacade.map(addBillF, addBillCommand);
        baseBillDomainService.save(addBillCommand);
        return Global.mapperFacade.map(addBillCommand.getBill(), tClass);
    }

    @Override
    @Transactional
    public <T, F extends AddBillF> List<T> addBatchBill(List<F> addBillFS, Class<T> tClass) {
//        List<B> billList = Global.mapperFacade.mapAsList(addBillFS, baseBillDomainService.getBillClass());
        List<AddBillCommand<B>> addBillCommands = addBillFS.stream().map(addBillF -> {
            AddBillCommand<B> addBillCommand = new AddBillCommand<>();
            B bill = (B) Global.mapperFacade.map(addBillF, baseBillDomainService.getBillClass());
            if (Objects.nonNull(addBillF.getApprovedFlag()) && addBillF.getApprovedFlag()) {
                bill.setApprovedState(BillApproveStateEnum.已审核.getCode());
                if (bill instanceof ReceivableBill) {
                    ((ReceivableBill)bill).setIsInit(false);
                }else if (bill instanceof TemporaryChargeBill) {
                    ((TemporaryChargeBill)bill).setIsInit(false);
                    ((TemporaryChargeBill)bill).generalTaxAmount();
                }
            }
            addBillCommand.setBill(bill);
            Global.mapperFacade.map(addBillF, addBillCommand);
            return addBillCommand;
        }).collect(Collectors.toList());

        boolean saveBatchFlag = baseBillDomainService.saveBatch(addBillCommands);
        if (saveBatchFlag) {
            List<B> billList = addBillCommands.stream().map(AddBillCommand::getBill).collect(Collectors.toList());

            return Global.mapperFacade.mapAsList(billList, tClass);
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <F extends UpdateBillF> boolean updateBatchBill(List<F> updateBillFS) {
        List<UpdateBillCommand<B>> updateBillCommands = updateBillFS.stream().map(updateBillF -> {
            UpdateBillCommand<B> updateBillCommand = new UpdateBillCommand<>();
            B bill = (B) Global.mapperFacade.map(updateBillF, baseBillDomainService.getBillClass());
            updateBillCommand.setBill(bill);
            Global.mapperFacade.map(updateBillF, updateBillCommand);
            return updateBillCommand;
        }).collect(Collectors.toList());
        return baseBillDomainService.updateBatch(updateBillCommands);
    }

    @Override
    @Transactional
    public Boolean syncBatchByCommunity(List<ReceivableBill> receivableBillList) {
        return baseBillDomainService.syncBatchByCommunity(receivableBillList);
    }

    @Override
    @Transactional
    public Boolean addReceivableBatch(List<ReceivableBill> receivableBillList) {
        return baseBillDomainService.addReceivableBatch(receivableBillList);
    }

    @Override
    public Boolean syncBatchUpdateByCommunity(List<ReceivableBill> receivableBillList) {
        return baseBillDomainService.syncBatchUpdateByCommunity(receivableBillList);
    }

    @Override
    public Boolean updateBillSettleStatusInfoByIds(List<Long> billIds,String supCpUnitId,Integer status) {
        return baseBillDomainService.updateBillInfoByIds(billIds,supCpUnitId,status);
    }


    @Override
    public Boolean addAdvanceBill(AdvanceBill advanceBill) {
        return baseBillDomainService.addAdvanceBill(advanceBill);
    }

    @Override
    @Transactional
    public <T extends ImportBillDto, F> List<T> importBill(List<F> addBillFS, Class<T> tClass) {
        List<ImportBillCommand<B>> importBillCommands = addBillFS.stream().map(addBillF -> {
            ImportBillCommand<B> importBillCommand = new ImportBillCommand<>();
            importBillCommand.setBill((B) Global.mapperFacade.map(addBillF, baseBillDomainService.getBillClass()));
            Global.mapperFacade.map(addBillF, importBillCommand);
            return importBillCommand;
        }).collect(Collectors.toList());
        List results = baseBillDomainService.importBill(importBillCommands);
        return Global.mapperFacade.mapAsList(results, tClass);
    }

    @Override
    @Transactional
    public <T extends ImportBillDto, F> List<T> importRecordBill(List<F> addBillFS, Class<T> tClass) {
        List<ImportBillCommand<B>> importBillCommands = addBillFS.stream().map(addBillF -> {
            ImportBillCommand<B> importBillCommand = new ImportBillCommand<>();
            importBillCommand.setBill((B) Global.mapperFacade.map(addBillF, baseBillDomainService.getBillClass()));
            Global.mapperFacade.map(addBillF, importBillCommand);
            return importBillCommand;
        }).collect(Collectors.toList());
        List results = baseBillDomainService.importRecordBill(importBillCommands);
        return Global.mapperFacade.mapAsList(results, tClass);
    }


    @Transactional
    @Override
    @EventTransactional
    public boolean approve(ApproveBillF approveBillF) {
        return baseBillDomainService.approve(Global.mapperFacade.map(approveBillF, ApproveCommand.class));
    }

    @Transactional
    @Override
    @EventTransactional
    public boolean approveBatch(ApproveBatchBillF approveBatchF, Integer billType) {
        BatchApproveBillCommand command = new BatchApproveBillCommand();
        command.setApproveState(BillApproveStateEnum.valueOfByCode(approveBatchF.getApproveState()));
        command.setRejectReason(approveBatchF.getRejectReason());
        command.setQuery(approveBatchF.getQuery());
        command.setBillIds(approveBatchF.getBillIds());
        command.setSupCpUnitId(approveBatchF.getSupCpUnitId());
        List<Long> list = baseBillDomainService.approveBatch(command, billType);
        return CollectionUtils.isNotEmpty(list);
    }

    @Transactional
    @EventTransactional
    public List<Long> approveBatchReturnIds(ApproveBatchBillF approveBatchF, Integer billType) {
        BatchApproveBillCommand command = new BatchApproveBillCommand();
        command.setApproveState(BillApproveStateEnum.valueOfByCode(approveBatchF.getApproveState()));
        command.setRejectReason(approveBatchF.getRejectReason());
        command.setQuery(approveBatchF.getQuery());
        command.setBillIds(approveBatchF.getBillIds());
        command.setSupCpUnitId(approveBatchF.getSupCpUnitId());
        return baseBillDomainService.approveBatch(command, billType);
    }

    @Transactional
    @Override
    public boolean delete(Long billId, String supCpUnitId) {
        return baseBillDomainService.delete(billId, supCpUnitId);
    }

    @Transactional
    @Override
    public BillBatchResultDto deleteBatch(DeleteBatchBillF deleteBatchBillF, Integer billType) {
        return baseBillDomainService.deleteBatch(new DeleteBatchBillCommand(deleteBatchBillF.getQuery(), deleteBatchBillF.getBillIds(), deleteBatchBillF.getSupCpUnitId()), billType);
    }

    /**
     * 获取审核记录
     * @param billApplyInfoF
     * @return
     */
    @Override
    public List<BillApproveV> getApplyInfo(BillApplyInfoF billApplyInfoF) {
       return baseBillDomainService.getApplyInfo(billApplyInfoF);
    }

    @Transactional
    @Override
    public Long apply(BillApplyF billApplyF) {
        BillApplyCommand billApplyCommand = new BillApplyCommand(billApplyF.getBillId(),
                billApplyF.getReason(),
                BillApproveOperateTypeEnum.valueOfByCode(billApplyF.getApproveOperateType()),
                billApplyF.getOutApproveId(), billApplyF.getExtField1());
        billApplyCommand.setSupCpUnitId(billApplyF.getSupCpUnitId());
        billApplyCommand
                .initDetail(billApplyF.getDetail());
        billApplyCommand.setOperationId(billApplyF.getOperationId());
        billApplyCommand.setOperationRemark(billApplyF.getOperationRemark());
        return baseBillDomainService.apply(billApplyCommand);
    }



    @Override
    public Long updateApply(BillApplyUpdateF billApplyF) {
        return baseBillDomainService.updateApply(
                billApplyF.getBillApproveId(), billApplyF.getOutApproveId(), billApplyF.getSupCpUnitId()
        );
    }

    @Override
    public Boolean updateBatchApplyByIds(BillBatchApplyUpdateF billApplyF) {
        return baseBillDomainService.updateBatchApplyByIds(
                billApplyF.getBillApproveIds(), billApplyF.getOutApproveId(), billApplyF.getSupCpUnitId()
        );
    }

    @Override
    public List<BillApproveV> approveHistory(ApproveHistoryF approveHistoryF) {
        List<Long> billIds = Optional.ofNullable(approveHistoryF.getBillIds()).orElse(Lists.newArrayList());
        if (Objects.nonNull(approveHistoryF.getBillId())) {
            billIds.add(approveHistoryF.getBillId());
        }
        List<BillApproveE> billApproveEList = baseBillDomainService.approveHistory(
                billIds,
                approveHistoryF.getOutApproveId(),
                approveHistoryF.getSupCpUnitId()
        );
        return Global.mapperFacade.mapAsList(billApproveEList, BillApproveV.class);
    }

    @Override
    public boolean applyBatch(BillApplyBatchF billApplyBatchF) {
        return baseBillDomainService.applyBatch(
                new BillApplyBatchCommand<>(billApplyBatchF.getBillIds(),
                        billApplyBatchF.getReason(),
                        BillApproveOperateTypeEnum.valueOfByCode(billApplyBatchF.getApproveOperateType()),
                        billApplyBatchF.getOutApproveId(),
                        billApplyBatchF.getSupCpUnitId(),
                        billApplyBatchF.getOperationId(),billApplyBatchF.getOperationRemark()).initDetail(billApplyBatchF.getDetail())
        );
    }

    @Override
    public boolean deapprove(Long billId, String supCpUnitId) {
        return baseBillDomainService.deapprove(billId, supCpUnitId);
    }

    @Override
    public boolean freeze(Long billId, String supCpUnitId) {
        return baseBillDomainService.freeze(billId, supCpUnitId);
    }

    @Override
    public boolean freezeBatch(FreezeBatchF freezeBatchF, Integer billType) {
        return baseBillDomainService.freezeBatch(new FreezeBatchBillCommand(freezeBatchF.getQuery(), freezeBatchF.getBillIds(), freezeBatchF.getSupCpUnitId()), billType);
    }

    public Boolean unfreezeBatch(UnFreezeBatchF unFreezeBatchF) {
        return baseBillDomainService.unfreezeBatch(unFreezeBatchF);
    }

    @Override
    public boolean freezeBatchAddReason(FreezeBatchF freezeBatchF, Integer billType, Integer freezeType) {
        return baseBillDomainService.freezeBatchAddReason(
                new FreezeBatchBillCommand(freezeBatchF.getQuery(), freezeBatchF.getBillIds(),
                        freezeBatchF.getSupCpUnitId()), billType, freezeType);
    }

    @Override
    public <T extends BillRefundF> boolean refund(T billRefundF) {
        return baseBillDomainService.refund(Global.mapperFacade.map(billRefundF, RefundCommand.class));
    }

    @Override
    public <T extends BillPageV> PageV<T> getPage(PageF<SearchF<?>> queryF, Class<T> tClass) {
        IPage page = baseBillDomainService.getPage(queryF);
        return PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), tClass));
    }


    @Override
    public <T extends BillPageV> PageV<T> getChangeApprovePage(PageF<SearchF<?>> queryF, Class<T> tClass) {
        IPage page = baseBillDomainService.getPage(queryF);
        List<T> list = Global.mapperFacade.mapAsList(page.getRecords(), tClass);
        //只有慧享云才设置对应减免的几个字段
        if (EnvConst.HUIXIANGYUN.equals(EnvData.config) && CollectionUtils.isNotEmpty(list)) {
            baseBillDomainService.setAdjustInfo(list);
        }
        return PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), list);
    }


    @Override
    public <T extends BillPageV> PageV<T> getPageWithApprove(PageF<SearchF<?>> queryF, Class<T> tClass) {
        IPage page = baseBillDomainService.getPageWithApprove(queryF);
        return PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), tClass));
    }

    @Override
    public <T extends BillDetailV> T getById(Long billId, Class<T> tClass, String supCpUnitId) {
        return Global.mapperFacade.map(baseBillDomainService.getById(billId, supCpUnitId), tClass);
    }

    @Override
    public <T extends BillApplyDetailV> T getWithApproving(Long billId, Class<T> tClass, String supCpUnitId) {
        BillApproveDetailDto approveDetailDto = baseBillDomainService.getWithApproving(billId, supCpUnitId);
        if (Objects.nonNull(approveDetailDto)) {
            T detail = Global.mapperFacade.map(approveDetailDto.getBill(), tClass);
            Global.mapperFacade.map(approveDetailDto, detail);
            return detail;
        }
        return null;
    }

    @Override
    public <T extends BillGroupDetailDto> PageV<T> getGroupPage(PageF<SearchF<?>> queryF, int type, boolean loadChildren) {
        return baseBillDomainService.getGroupPage(queryF, type, loadChildren);
    }

    /**
     * 账单表(receivable_bill)
     * -调整信息记录(bill_adjust)
     * -申请信息记录(bill_approve)
     *  ----收款单明细(gather_detail)
     *  ----收款单表(gather_bill)
     * -退款单表(bill_refund)
     * -账单结转记录表(bill_carryover)
     * @param billId
     * @param tClass
     * @param supCpUnitId
     * @param <T>
     * @return
     */
    @Override
    public <T extends BillAllDetailV> T getAllDetail(Long billId, Class<T> tClass, String supCpUnitId) {
        BillDetailDto detail = baseBillDomainService.getDetailById(billId, supCpUnitId);
        if (Objects.nonNull(detail)) {
            T detailV = Global.mapperFacade.map(detail.getBill(), tClass);
            Global.mapperFacade.map(detail, detailV);
            detailV.setPayInfos(generatePayInfos(detail.getBillSettleDtos()));
            return detailV;
        }
        return null;
    }

    private List<PayInfo> generatePayInfos(List<BillSettleDto> billSettleDtos) {
        List<PayInfo> payInfos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(billSettleDtos)) {
            billSettleDtos.forEach(settle -> {
                PayInfo payInfo = new PayInfo();
                payInfo.setPayChannel(settle.getSettleChannel());
                payInfo.setPayWay(settle.getSettleWay());
                payInfo.setAmount(settle.getSettleAmount());
                payInfos.add(payInfo);
            });
        }
        return payInfos;
    }

    @Transactional
    @Override
    public Boolean handBatch(BillHandBatchF billHandBatchF, Integer billType) {
        return baseBillDomainService.handBatch(Global.mapperFacade.map(billHandBatchF, BatchHandBillCommand.class), billType);
    }

    @Override
    public List<BillHandV> listBillHand(List<Long> billIds, String supCpUnitId) {
        return baseBillDomainService.listBillHand(billIds, supCpUnitId);
    }

    @Transactional
    @Override
    public Boolean handReversal(Long billId, String supCpUnitId) {
        return baseBillDomainService.handReversal(billId, supCpUnitId);
    }

    /**
     * 分页获取应收账单导出明细数据
     *
     * @param queryF queryF
     * @param <T>    <T>
     * @return PageV
     */
    @Override
    public <T extends BillGroupDetailDto> PageV<T> queryDetailData(PageF<SearchF<?>> queryF) {
        List<Integer> operateTypeValue = (List<Integer>) queryF.getConditions().getSpecialMap().get("operate_type");
        PageV<T> groupPage;
        if (CollectionUtils.isNotEmpty(operateTypeValue)) {
            groupPage = baseBillDomainService.getGroupPage(queryF, 1, true);
        } else {
            groupPage = baseBillDomainService.getGroupPage(queryF, 0, true);
        }
        List<T> records = groupPage.getRecords();
        List<T> realList = new ArrayList<>();
        for (T record : records) {
            realList.addAll(record.getChildren());
        }
        return PageV.of(groupPage.getPageNum(), groupPage.getPageSize(), groupPage.getTotal(), realList);
    }

    /**
     * 分页获取应收账单导出明细数据
     *
     * @param queryF queryF
     * @param <T>    <T>
     * @return PageV
     */
    @Override
    public <T extends BillGroupDetailDto> PageV<T> queryParentData(PageF<SearchF<?>> queryF) {
        List<Integer> operateTypeValue = (List<Integer>) queryF.getConditions().getSpecialMap().get("operate_type");
        if (CollectionUtils.isNotEmpty(operateTypeValue)) {
            return baseBillDomainService.getGroupPage(queryF, 1, false);
        } else {
            return baseBillDomainService.getGroupPage(queryF, 0, false);
        }
    }

    /**
     * 获取账单推凭信息
     *
     * @param billInferenceF
     * @return
     */
    @Override
    public List<BillInferenceV> listInferenceInfo(BillInferenceF billInferenceF) {
        return baseBillDomainService.listInferenceInfo(Global.mapperFacade.map(billInferenceF, BillInferenceQuery.class));
    }

    /**
     * 获取账单推凭信息
     *
     * @param batchBillInferenceF
     * @return
     */
    @Override
    public List<BillInferenceV> listInferenceInfoByIds(BatchBillInferenceF batchBillInferenceF) {
        return baseBillDomainService.listInferenceInfoByIds(Global.mapperFacade.map(batchBillInferenceF, BatchBillInferenceQuery.class));
    }

    /**
     * 根据条件批量获取账单推凭信息（分页）
     *
     * @param form
     * @return
     */
    @Override
    public PageV<BillInferenceV> pageBillInferenceInfo(ListBillInferenceF form, BillTypeEnum billTypeEnum) {
        return baseBillDomainService.pageBillInferenceInfo(form, billTypeEnum);
    }

    @Override
    public boolean reconcileBatch(List<ReconcileBatchF> reconcileBatchFS) {
        return baseBillDomainService.reconcileBatch(Global.mapperFacade.mapAsList(reconcileBatchFS, ReconcileBatchCommand.class));
    }

    @Override
    public <T extends BillPageV> PageV<T> getPageNoTenantLine(PageF<SearchF<?>> queryF, Class<T> tClass) {
        IPage page = baseBillDomainService.getPageNoTenantLine(queryF);
        return PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), tClass));
    }

    @Override
    public boolean inferBatch(List<Long> billIds) {
        return baseBillDomainService.inferBatch(billIds);
    }

    @Override
    public <T> List<T> queryByIdList(List<Long> billIds, Class<T> tClass, String supCpUnitId) {
        List<B> billApplyDetailList = baseBillDomainService.getList(billIds, supCpUnitId);
        return Global.mapperFacade.mapAsList(billApplyDetailList, tClass);
    }

    /**
     * 账单结转
     *
     * @param billCarryoverF 结转参数
     * @return Boolean
     */
    @Override
    @Transactional
    public <T extends BillCarryoverF> Boolean carryover(T billCarryoverF) {
        return baseBillDomainService.carryover(Global.mapperFacade.map(billCarryoverF, CarryoverCommand.class));
    }

    /**
     * 账单调整
     *
     * @param billAdjustF 调整参数
     * @return Boolean
     */
    @Override
    @Transactional
    public <T extends BillAdjustF> Boolean adjust(T billAdjustF) {
        return baseBillDomainService.adjust(Global.mapperFacade.map(billAdjustF, AdjustCommand.class));
    }

    /**
     * 根据账单id集合获取账单信息
     *
     * @param billIds     账单id集合
     * @param tClass      返回的类class
     * @param supCpUnitId 上级收费单元id
     * @return List
     */
    @Override
    public <T> List<T> getBillInfoByIds(List<Long> billIds, Class<T> tClass, String supCpUnitId) {
        List<B> list = baseBillDomainService.getList(billIds, supCpUnitId);
        return Global.mapperFacade.mapAsList(list, tClass);
    }


    @Override
    public <T> List<T> getConditionList(TemporaryBillF temporaryBillF, Class<T> tClass, String supCpUnitId) {
        List<B> list = baseBillDomainService.getConditionList(temporaryBillF, supCpUnitId);
        return Global.mapperFacade.mapAsList(list, tClass);
    }

    @Transactional
    @Override
    public <T extends BillInvalidF> Boolean invalid(T billInvalidF) {
        return baseBillDomainService.invalid(Global.mapperFacade.map(billInvalidF, InvalidCommand.class));
    }

    @Transactional
    @Override
    public BillBatchResultDto invalidBatch(List<Long> billIdList, String supCpUnitId) {
        return baseBillDomainService.invalidBatch(billIdList, supCpUnitId);
    }

    /**
     * 根据账单编号查询账单信息
     *
     * @param billNo 账单编号
     * @return {@link BillOjV}
     */
    @Override
    public BillOjV getBillInfoByBillNo(String billNo, String supCpUnitId) {
        return baseBillDomainService.getBillInfoByBillNo(billNo, supCpUnitId);
    }

    /**
     * 获取账单的状态信息
     *
     * @param billDetailF 查询参数
     * @return {@link BillStatusDetailVo}
     */
    public BillStatusDetailVo statusDetailBill(BillDetailF billDetailF) {
        BillStatusDetailVo billStatusDetailVo = baseBillDomainService.statusDetailBill(billDetailF);
        return billStatusDetailVo;
    }


    /**
     * 批量冻结功能(跳收)
     *
     * @param f
     * @return
     */
    @Transactional
    public Boolean batchJump(BillFreezeF f, String tenantId) {
        List<ReceivableBillDetailV> detailvs = getBillInfoByIds(f.getBillIds(), ReceivableBillDetailV.class, f.getSupCpUnitId());
        List<String> communityIds = new ArrayList<>();
        if (CollectionUtils.isEmpty(detailvs)) {
            throw BizException.throw300("找不到账单详情!");
        }
        long actualUnpayAmount = getFreezeAmount(f.getBillIds(), f.getSupCpUnitId());
        BillFreezeE freezeE = new BillFreezeE();
        JSONArray jumpRecordsExtJson = new JSONArray();
        JSONArray roomNameArray = new JSONArray();
        JSONArray chargeItemNameArray = new JSONArray();
        JSONArray roomIdArray = new JSONArray();
        JSONArray chargeItemIdArray = new JSONArray();
        for (ReceivableBillDetailV v : detailvs) {
            ReceivableBill b = Global.mapperFacade.map(v, ReceivableBill.class);
            b.setId(v.getBillId());
            if (!b.getState().equals(0))
                throw BizException.throw300("请选择正常状态的账单");
            if ((!b.getApprovedState().equals(2)) || (b.getSettleState().equals(2)) || (!b.getReversed().equals(0)) || (b.getBillLabel() != null))
                throw BizException.throw300("请选择已审核、未结算/部分结算的账单");
            b.setState(1);
//            billMapper.updateById(b);
            billMapper.update(null, new UpdateWrapper<ReceivableBill>().set("state", 1).eq("id", b.getId()).eq("sup_cp_unit_id", f.getSupCpUnitId()));

//            添加批量冻结日志
            BizLog.normal(String.valueOf(v.getBillId()), LogContext.getOperator(),
                    LogObject.账单, LogAction.冻结, new Content().option(new ContentOption(new PlainTextDataItem("批量冻结", true)))
                            .option(new ContentOption(new PlainTextDataItem("冻结金额为：", false)))
                            .option(new ContentOption(new PlainTextDataItem(new BigDecimal(b.getActualUnpayAmount()).movePointLeft(2).toString(), false),
                                    OptionStyle.normal()))
                            .option(new ContentOption(new PlainTextDataItem("元", false))));
            communityIds.add(v.getCommunityId());
            JSONObject json = new JSONObject();
            json.put("id", v.getBillId());
            json.put("state", 1);
            jumpRecordsExtJson.add(json);
            if (StringUtils.isNotBlank(b.getRoomName()))
                roomNameArray.add(b.getRoomName());
            if (StringUtils.isNotBlank(b.getChargeItemName()))
                chargeItemNameArray.add(b.getChargeItemName());
            if (b.getChargeItemId() != null)
                chargeItemIdArray.add(b.getChargeItemId());
            if (StringUtils.isNotBlank(b.getRoomId()))
                roomIdArray.add(b.getRoomId());
            freezeE.setCommunityName(b.getCommunityName());
            freezeE.setCommunityId(b.getCommunityId());
        }
        freezeE.setState(1);
        freezeE.setFreezeType(f.getFreezeType());
        freezeE.setIsRefreeze(1);
        freezeE.setReason(f.getReason());
        freezeE.setChargeItemName(chargeItemNameArray.size() == 0 ? null : chargeItemNameArray.toJSONString());
        if (CollectionUtils.isNotEmpty(f.getFileVos()))
            freezeE.setFileList(JSONArray.parseArray(JSON.toJSONString(f.getFileVos())).toJSONString());
        freezeE.setRoomName(roomNameArray.size() == 0 ? null : roomNameArray.toJSONString());
        freezeE.setRoomId(roomIdArray.size() == 0 ? null : roomIdArray.toJSONString());
        freezeE.setChargeItemId(chargeItemIdArray.size() == 0 ? null : chargeItemIdArray.toJSONString());
        freezeE.setJumpRecordsExtJson(jumpRecordsExtJson.toJSONString());
        freezeE.setFreezeAmount(actualUnpayAmount);
        freezeE.setTenantId(tenantId);

//        freezeE.setGmtCreate(DateUtil.date().toString("yyyy-MM-dd"));
        if ((CollectionUtils.isEmpty(communityIds)) || (communityIds.stream().map(e -> e).distinct().count() > 1))//说明项目名称有不重复的
            throw BizException.throw300("项目不能为空并且不能重复!");
        return freezeMapper.insert(freezeE) > 0;
    }

    /**
     * 批量冻结功能(银行代扣)
     *
     * @param f
     * @return
     */
    public Boolean batchBankWithHold(BillFreezeF f) {
        List<ReceivableBillDetailV> detailvs = getBillInfoByIds(f.getBillIds(), ReceivableBillDetailV.class, f.getSupCpUnitId());
        if (CollectionUtils.isEmpty(detailvs)) {
            throw BizException.throw300("找不到账单详情!");
        }
        long actualUnpayAmount = getFreezeAmount(f.getBillIds(), f.getSupCpUnitId());
        BillFreezeE freezeE = new BillFreezeE();
        JSONArray jumpRecordsExtJson = new JSONArray();

        Set<String> roomNameArray = new HashSet<>();
        Set<String> chargeItemNameArray = new HashSet<>();
        Set<String> roomIdArray = new HashSet<>();
        Set<Long> chargeItemIdArray = new HashSet<>();
        for (ReceivableBillDetailV v : detailvs) {
            ReceivableBill b = Global.mapperFacade.map(v, ReceivableBill.class);
            JSONObject json = new JSONObject();
            json.put("id", v.getBillId());
            json.put("state", 1);
            jumpRecordsExtJson.add(json);
            roomNameArray.add(b.getRoomName());
            chargeItemNameArray.add(b.getChargeItemName());
            chargeItemIdArray.add(b.getChargeItemId());
            roomIdArray.add(b.getRoomId());
        }

        freezeE.setState(1);
        freezeE.setIsRefreeze(1);
        freezeE.setReason(f.getReason());
        freezeE.setChargeItemName(chargeItemNameArray.size() == 0 ? null : JSON.toJSONString(chargeItemNameArray));
        freezeE.setRoomName(roomNameArray.size() == 0 ? null : JSON.toJSONString(roomNameArray));
        freezeE.setRoomId(roomIdArray.size() == 0 ? null : JSON.toJSONString(roomIdArray));
        freezeE.setChargeItemId(chargeItemIdArray.size() == 0 ? null : JSON.toJSONString(chargeItemIdArray));
        freezeE.setJumpRecordsExtJson(jumpRecordsExtJson.toJSONString());
        freezeE.setFreezeAmount(actualUnpayAmount);
        freezeE.setCommunityName(detailvs.get(0).getCommunityName());
        freezeE.setCommunityId(detailvs.get(0).getCommunityId());
        freezeE.setFreezeType(f.getFreezeType());
        return freezeMapper.insert(freezeE) > 0;
    }

    /**
     * 查看冻结记录
     *
     * @param f
     * @return
     */
    public PageV<BillFreezeV> getJump(PageF<SearchF<?>> f) {
        List<Field> fields = f.getConditions().getFields();
        QueryWrapper<BillFreezeE> wrap = f.getConditions().getQueryModel(BillFreezeE.class).orderByDesc("gmt_create");
        Page<BillFreezeE> p = freezeMapper.selectPage(Page.of(f.getPageNum(), f.getPageSize()),
                wrap);
        List<BillFreezeE> records = p.getRecords();
        List<BillFreezeV> vos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(records)) {
            records.forEach(rd -> {
                String jumpRecordsExtJson = rd.getJumpRecordsExtJson();
                List<ReceivableBill> bills = JSONArray.parseArray(jumpRecordsExtJson).toJavaList(ReceivableBill.class);
                List<Long> longs = JSONArray.parseArray(JSON.toJSONString(bills.stream().map(e -> e.getId()).collect(Collectors.toList())), Long.class);
                JSONArray array = JSONArray.parseArray(JSON.toJSONString(billMapper.selectList(new QueryWrapper<ReceivableBill>()
                        .in("id", longs)
                        .eq("sup_cp_unit_id", rd.getCommunityId()))));
                for (int i = 0; i < array.size(); i++) {
                    array.getJSONObject(i).put("state", bills.get(i).getState());
                }
                rd.setJumpRecordsExtJson(array.toJSONString());
            });

            vos = records.stream().map(info -> {
                BillFreezeV teach = new BillFreezeV();
                BeanUtils.copyProperties(info, teach);
                return teach;
            }).collect(Collectors.toList());
        }
        return PageV.of(f, p.getTotal(), vos);
    }


    /**
     * 计算冻结金额
     *
     * @param billIds
     * @return
     */
    public Long getFreezeAmount(List<Long> billIds, String supCpUnitId) {
        List<ReceivableBill> billInfoByIds = getBillInfoByIds(billIds, ReceivableBill.class, supCpUnitId);
        long freeAmount = 0L;
        for (ReceivableBill b : billInfoByIds) {
            freeAmount += b.getActualUnpayAmount();
        }
        return freeAmount;
    }

    /**
     * 解除冻结
     *
     * @param billNos
     * @return
     */
    @Transactional
    public Boolean relieveFreeze(Long id, List<String> billNos, String supCpUnitId) {
        if (id == null)
            throw BizException.throw400("请从未收款冻结的明细列表进入(银行托收暂不支持解冻)");

        List<ReceivableBill> bills = billMapper.selectList(new QueryWrapper<ReceivableBill>().in("bill_no", billNos));
        BillFreezeE freezeE = freezeMapper.selectOne(new QueryWrapper<BillFreezeE>().eq("id", id));
        String bs = JSON.toJSONString(bills.stream().map(e -> e.getId()).collect(Collectors.toList()));
        for (ReceivableBill b : bills) {
            if (!b.getState().equals(1)) {
                billNos.remove(b.getBillNo());
                continue;
            }

//        添加批量解冻日志
            BizLog.normal(String.valueOf(b.getId()), LogContext.getOperator(),
                    LogObject.账单, LogAction.解冻, new Content()
                            .option(new ContentOption(new PlainTextDataItem("解冻", true)))
                            .option(new ContentOption(new PlainTextDataItem("解冻金额为：", false)))
                            .option(new ContentOption(new PlainTextDataItem(new BigDecimal(b.getActualUnpayAmount()).movePointLeft(2).toString(), false),
                                    OptionStyle.normal()))
                            .option(new ContentOption(new PlainTextDataItem("元", false))));

        }

        if (CollectionUtils.isNotEmpty(billNos)) {
            UpdateWrapper<ReceivableBill> update = new UpdateWrapper();
            billMapper.update(null, update.set("gmt_modify", LocalDateTime.now()).set("state", 0).in("bill_no", billNos).eq("sup_cp_unit_id", supCpUnitId));
        }

        JSONArray array = JSONArray.parseArray(freezeE.getJumpRecordsExtJson());
        for (int i = 0; i < array.size(); i++) {
            JSONObject json = array.getJSONObject(i);
            if (bs.contains(json.getString("id"))) {
                array.getJSONObject(i).put("state", 0);
            }
        }
        UpdateWrapper<BillFreezeE> u = new UpdateWrapper<>();
        u.set("jump_records_extJson", array.toJSONString()).set("gmt_modify", LocalDateTime.now()).eq("id", id);

        freezeMapper.update(null, !array.toJavaList(ReceivableBill.class).stream().map(e -> e.getState()).collect(Collectors.toList()).contains(1)
                && freezeE.getIsRefreeze().equals(1) ?
                u.set("is_refreeze", 0) : u);

        return true;

    }


    @Override
    public ApplyBatchDeductionV applyBatchDeduction(BillApplyBatchF billApplyBatchF) {
        return baseBillDomainService.applyBatchDeduction(
                new BillApplyBatchCommand<>(billApplyBatchF.getBillIds(),
                        billApplyBatchF.getReason(),
                        BillApproveOperateTypeEnum.valueOfByCode(billApplyBatchF.getApproveOperateType()),
                        billApplyBatchF.getOutApproveId(),
                        billApplyBatchF.getSupCpUnitId(),
                        billApplyBatchF.getOperationId(),
                        billApplyBatchF.getOperationRemark()).initDetail(billApplyBatchF.getDetail())
        );
    }

    public List<ReceivableBill> getByChargeNcId(String chargeNcId,String communityId) {
        return billMapper.getByChargeNcId(chargeNcId, communityId);
    }
    public Boolean deleteBillById(String communityId,List<ReceivableBill> list) {
        billMapper.deleteBillById(communityId,list);
        return true;
    }


}