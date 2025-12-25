package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.domains.bill.command.ReconcileDimensionRuleQuery;
import com.wishare.finance.domains.bill.command.ReconcilePreconditionsQuery;
import com.wishare.finance.domains.bill.command.ReconcileQuery;
import com.wishare.finance.domains.bill.command.ReconcileGatherRefundQuery;
import com.wishare.finance.domains.bill.command.ReconcileRefundQuery;
import com.wishare.finance.domains.bill.command.ReconciliationBillQuery;
import com.wishare.finance.domains.bill.command.UpdateReconcileCommand;
import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillSharedingColumn;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.ReconciliationBillDto;
import com.wishare.finance.domains.bill.dto.ReconciliationGroupDto;
import com.wishare.finance.domains.bill.dto.ReconciliationRefundDto;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.repository.mapper.BillReconciliationMapper;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationRecBillDetailOBV;
import com.wishare.finance.domains.reconciliation.enums.ReconcileModeEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.support.mutiltable.MutilTableParam;
import com.wishare.starter.beans.PageV;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.util.StringUtils;

/**
 * 对账资源库
 *
 * @Author dxclay
 * @Date 2022/10/16
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class BillReconciliationRepository {

    private final BillReconciliationMapper billReconciliationMapper;

    private final ReceivableBillRepository receivableBillRepository;
    private final GatherDetailRepository gatherDetailRepository;

    private final SharedBillAppService sharedBillAppService;

    /**
     * 查询所需对账的维度分组
     * @return
     */
    public List<ReconciliationGroupDto> getReconciliationGroup(ReconcileQuery reconcileQuery){
        ReconcileDimensionRuleQuery dimensionRule = reconcileQuery.getDimensionRuleQuery();
        List<String> groupFields = new ArrayList<>();
        if (Objects.nonNull(dimensionRule.getStatutoryBody()) && dimensionRule.getStatutoryBody().isGroup()){
            groupFields.add("statutory_body_id");
        }
        if (Objects.nonNull(dimensionRule.getCostCenter()) && dimensionRule.getCostCenter().isGroup()) {
            groupFields.add("cost_center_id");
        }
        if (Objects.nonNull(dimensionRule.getPayChannel()) && dimensionRule.getPayChannel().isGroup()){
            groupFields.add("pay_channel");
        }
        if(Objects.nonNull(dimensionRule.getStatutoryBodyAccount()) && dimensionRule.getStatutoryBodyAccount().isGroup()){
            groupFields.add("sb_account_id");
        }
        groupFields.add("sup_cp_unit_id");
        String supCpUnitId = MutilTableParam.supCpUnitId.get();
        if(!StringUtils.hasText(supCpUnitId)) {
            throw new IllegalArgumentException("上级收费单元ID参数不能为空!");
        }
        String gatherBillName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_BILL);
        String gatherDetailName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_DETAIL);
        if (ReconcileModeEnum.商户清分对账.equalsByCode(reconcileQuery.getReconcileMode())){
            return billReconciliationMapper.queryMerchantClearingReconciliationGroup(groupFields
                , getReconcileQueryWrapper(reconcileQuery)
                , reconcileQuery.getReconcileMode()
                ,gatherBillName
                ,gatherDetailName
                , supCpUnitId);
        }else if (ReconcileModeEnum.账票流水对账.equalsByCode(reconcileQuery.getReconcileMode())){
            //添加账单类型默认分组
//            groupFields.add("bill_type");
            log.info("账票流水对账获取账单分组");
            return billReconciliationMapper.queryReconciliationGroup(groupFields
                , getReconcileQueryWrapper(reconcileQuery)
                , reconcileQuery.getReconcileMode()
                , gatherBillName
                , gatherDetailName
                , supCpUnitId);
        }
        return null;
    }


    /**
     * 更新对账账单信息
     * @param reconcileCommands
     * @return
     */
    public boolean updateReconcileBill(List<UpdateReconcileCommand> reconcileCommands){
        String supCpUnitId = MutilTableParam.supCpUnitId.get();
        for (UpdateReconcileCommand reconcileCommand : reconcileCommands) {
            switch (BillTypeEnum.valueOfByCode(reconcileCommand.getBillType())){
                case 预收账单:
                    billReconciliationMapper.updateAdvance(reconcileCommand.getBillIds(), reconcileCommand.getReconcileState(), reconcileCommand.getReconcileMode());
                    break;
                case 收款单:
                    billReconciliationMapper.updateGather(reconcileCommand.getBillIds(), reconcileCommand.getReconcileState(),
                            reconcileCommand.getReconcileMode(), supCpUnitId);
                    List<GatherDetail> gatherBillIds = gatherDetailRepository.getByGatherBillIdsForReconciliation(reconcileCommand.getBillIds(), supCpUnitId);
                    ArrayList<Long> longs = new ArrayList<>();
                    for (GatherDetail gatherBillId : gatherBillIds) {
                        longs.add(gatherBillId.getRecBillId());
                    }
                    if (null != longs && longs.size() > 0){
                        billReconciliationMapper.updateReceivableBill(longs,reconcileCommand.getReconcileState(),
                                reconcileCommand.getReconcileMode(),supCpUnitId);
                    }
                    String receivableBillName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.RECEIVABLE_BILL);
                    String gatherDetailName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_DETAIL);
                    break;
                case 付款单:
                    billReconciliationMapper.updatePay(reconcileCommand.getBillIds(), reconcileCommand.getReconcileState(), reconcileCommand.getReconcileMode());
                    break;
            }
        }
        return true;
    }


    /**
     * 查询所需清分对账的维度分组
     * @return
     */
    public List<ReconciliationGroupDto> getReconcileGroupsClear(List<String> groupFields){
        String supCpUnitId = MutilTableParam.supCpUnitId.get();
        if(!StringUtils.hasText(supCpUnitId)) {
            throw new IllegalArgumentException("上级收费单元ID参数不能为空!");
        }
        String gatherBillName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_BILL);
        String gatherDetailName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_DETAIL);
        return billReconciliationMapper.getReconcileGroupsClear(groupFields, gatherBillName, gatherDetailName);
    }

    /**
     * 查询对账账单信息
     * @param reconcileQuery
     * @return
     */
    public PageV<ReconciliationBillDto> getReconciliationInvoiceBill(ReconcileQuery reconcileQuery, String gatherBillName, String gatherDetailName
                                                                     , String supCpUnitId) {
        Page<ReconciliationBillDto> reconciliationBill = billReconciliationMapper.getReconciliationInvoiceBill(
                Page.of(reconcileQuery.getPageNum(), reconcileQuery.getPageSize()), getReconcileQueryWrapper(reconcileQuery), reconcileQuery.getReconcileMode()
                , gatherBillName, gatherDetailName, supCpUnitId, reconcileQuery.getInvoiceIdList());
        return PageV.of(reconcileQuery, reconciliationBill.getRecords());
    }

    public PageV<ReconciliationBillDto> getReconciliationPayBill(ReconcileQuery reconcileQuery, String gatherBillName, String gatherDetailName
                                                                , String supCpUnitId) {
        Page<ReconciliationBillDto> reconciliationBill = billReconciliationMapper.getReconciliationPayBill(
                Page.of(reconcileQuery.getPageNum(), reconcileQuery.getPageSize()), getReconcileQueryWrapper(reconcileQuery), reconcileQuery.getReconcileMode()
                , gatherBillName, gatherDetailName , supCpUnitId, reconcileQuery.getPayBillIdList());
        return PageV.of(reconcileQuery, reconciliationBill.getRecords());
    }

    public PageV<ReconciliationBillDto> getReconciliationGatherBill(ReconcileQuery reconcileQuery, String gatherBillName, String receivableBillName,String gatherDetailName
                                                                    , String supCpUnitId) {
        Page<ReconciliationBillDto> reconciliationBill = billReconciliationMapper.getReconciliationGatherBill(
                Page.of(reconcileQuery.getPageNum(), reconcileQuery.getPageSize()), getReconcileQueryWrapper(reconcileQuery), reconcileQuery.getReconcileMode()
                , gatherBillName, gatherDetailName , supCpUnitId, receivableBillName);
        return PageV.of(reconcileQuery, reconciliationBill.getRecords());
    }
    public List<ReconciliationBillDto> getGatherBillForOffLine(String supCpUnitId, String startTime, String endTime, Long costCenterId){
        return billReconciliationMapper.getGatherBillForOffLine(supCpUnitId, startTime, endTime ,costCenterId );
    }

    public PageV<ReconciliationBillDto> getFYReconciliationGatherBill(ReconcileQuery reconcileQuery, String gatherBillName, String gatherDetailName
            , String supCpUnitId) {
        Page<ReconciliationBillDto> reconciliationBill = billReconciliationMapper.getFYReconciliationGatherBill(
                Page.of(reconcileQuery.getPageNum(), reconcileQuery.getPageSize()), getReconcileQueryWrapper(reconcileQuery), reconcileQuery.getReconcileMode()
                , gatherBillName, gatherDetailName , supCpUnitId, reconcileQuery.getGatherBillIdList());
        return PageV.of(reconcileQuery, reconciliationBill.getRecords());
    }

    public PageV<ReconciliationBillDto> getMerchantClearingReconciliationBillPage(ReconcileQuery reconcileQuery, String endDate, String beginDate) {
        String supCpUnitId = MutilTableParam.supCpUnitId.get();
        if(!StringUtils.hasText(supCpUnitId)) {
            throw new IllegalArgumentException("上级收费单元ID参数不能为空!");
        }
        String gatherBillName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_BILL);
        String gatherDetailName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_DETAIL);
        // MutilTableParam.supCpUnitId.remove();
        Page<ReconciliationBillDto> clearingReconciliationBill = billReconciliationMapper.getMerchantClearingReconciliationBill(
                Page.of(reconcileQuery.getPageNum(), reconcileQuery.getPageSize()), getReconcileQueryWrapper(reconcileQuery),
                reconcileQuery.getReconcileMode()
            ,gatherBillName
            ,gatherDetailName
            , endDate
            , beginDate);
        return PageV.of(reconcileQuery, clearingReconciliationBill.getRecords());
    }

    /**
     * 查询账单退款信息
     * @param reconcileRefundQuery 查询账单退款信息
     * @return
     */
    public List<ReconciliationRefundDto> getReconciliationBillGatherRefunds(ReconcileGatherRefundQuery reconcileRefundQuery){
        String supCpUnitId = MutilTableParam.supCpUnitId.get();
        if(!StringUtils.hasText(supCpUnitId)) {
            throw new IllegalArgumentException("上级收费单元ID参数不能为空!");
        }
        String gatherBillName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_BILL);
        String gatherDetailName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_DETAIL);
        return billReconciliationMapper.getReconciliationBillGatherRefunds(reconcileRefundQuery, gatherDetailName);
    }

    /**
     * 查询账单付款退款信息
     * @param refundQuery
     * @return
     */
    public List<ReconciliationRefundDto> getRefundInfos(ReconcileRefundQuery refundQuery){
        if (CollectionUtils.isEmpty(refundQuery.getSettleIds())){
            return new ArrayList<>();
        }
        String supCpUnitId = MutilTableParam.supCpUnitId.get();
        if(!StringUtils.hasText(supCpUnitId)) {
            throw new IllegalArgumentException("上级收费单元ID参数不能为空!");
        }
        String gatherBillName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_BILL);
        String gatherDetailName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_DETAIL);
        return billReconciliationMapper.getRefundInfos(refundQuery, gatherDetailName);
    }

    /**
     * 获取账单检索条件
     * @param reconcileQuery 对账查询条件
     * @return 查询结果
     */
    private QueryWrapper<?> getReconcileQueryWrapper(ReconcileQuery reconcileQuery){
        QueryWrapper<?> wrapper = null;
        //账单查询条件
        ReconciliationBillQuery billQuery = reconcileQuery.getReconciliationBillQuery();
        if (Objects.nonNull(billQuery)){
            wrapper = new QueryWrapper<>();
            ReconcileDimensionRuleQuery dimensionRule = reconcileQuery.getDimensionRuleQuery();
            if (Objects.nonNull(dimensionRule.getStatutoryBody()) && dimensionRule.getStatutoryBody().notEmpty()){
                wrapper.eq("b.statutory_body_id", billQuery.getStatutoryBodyId());
            }
            if (Objects.nonNull(dimensionRule.getCommunity())&& dimensionRule.getCommunity().notEmpty()){
                wrapper.eq("gd.sup_cp_unit_id", billQuery.getCommunityId());
                wrapper.eq("b.sup_cp_unit_id", billQuery.getCommunityId());
            }else {
//                throw new IllegalArgumentException("项目ID不存在 !");
            }
            if (Objects.nonNull(dimensionRule.getCostCenter()) && dimensionRule.getCostCenter().notEmpty()) {
                wrapper.eq("gd.cost_center_id", billQuery.getCostCenterId());
            }
            if (Objects.nonNull(dimensionRule.getPayChannel()) && dimensionRule.getPayChannel().notEmpty()){
                wrapper.eq("gd.pay_way", billQuery.getPayWay());
                wrapper.eq("gd.pay_channel", billQuery.getPayChannel());
            }
            if(Objects.nonNull(dimensionRule.getStatutoryBodyAccount()) && dimensionRule.getStatutoryBodyAccount().notEmpty()){
                wrapper.eq("b.sb_account_id", billQuery.getSbAccountId());
            }
            if (Objects.nonNull(billQuery.getBillType())){
                wrapper.eq("ird.bill_type", billQuery.getBillType());
            }
            if (Objects.nonNull(billQuery.getPayWay())){
                wrapper.eq("gd.pay_way", billQuery.getPayWay());
            }

        }
        //前置条件
        List<ReconcilePreconditionsQuery> preconditionsQueries = reconcileQuery.getPreconditionsQueries();
        if (CollectionUtils.isNotEmpty(preconditionsQueries)){
            wrapper = Objects.isNull(wrapper) ? new QueryWrapper<>() : wrapper;
            for (ReconcilePreconditionsQuery preconditionsQuery : preconditionsQueries) {
                wrapper.in("b.sys_source", preconditionsQuery.getSysSource());
                if (Objects.isNull(preconditionsQuery.getHanded()) || preconditionsQuery.getHanded() == 0){
                    wrapper.in("b.account_handed", List.of(1, 2));
                }
                wrapper.in("b.pay_way", preconditionsQuery.getPayWay());
            }
        }
        // 添加新逻辑  所有收款单状态都要是已审核状态
        // wrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode());


        return wrapper;
    }


    /**
     * 根据收款获取应收账单
     * @param gatherId
     * @return
     */
    public List<ReconciliationRecBillDetailOBV> getRecBillDetails(Long gatherId,String supCpUnitId) {
        String gatherDetailName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_DETAIL);
        return billReconciliationMapper.getRecBillDetailsById(gatherId, supCpUnitId, gatherDetailName);
    }


    public List<ReconciliationRecBillDetailOBV> getRecBillDetailsByBillNoList(List<String> billNoList, String supCpUnitId) {
        String gatherDetailName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.GATHER_DETAIL);
        return billReconciliationMapper.getRecBillDetailsByBillNoList(billNoList, supCpUnitId, gatherDetailName);
    }
}
