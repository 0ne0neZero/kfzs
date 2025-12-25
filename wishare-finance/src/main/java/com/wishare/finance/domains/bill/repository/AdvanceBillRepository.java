package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.fo.AdvanceMaxEndTimeBillF;
import com.wishare.finance.apps.model.bill.fo.ListBillInferenceF;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.domains.bill.command.BillDiscountTotalQuery;
import com.wishare.finance.domains.bill.command.StatisticsBillTotalQuery;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.dto.BillApproveTotalDto;
import com.wishare.finance.domains.bill.dto.BillDiscountTotalDto;
import com.wishare.finance.domains.bill.dto.BillTotalDto;
import com.wishare.finance.domains.bill.dto.RoomBillTotalDto;
import com.wishare.finance.domains.bill.entity.AdvanceBill;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.repository.mapper.AdvanceBillMapper;
import com.wishare.finance.domains.export.service.ExportService;
import com.wishare.finance.domains.voucher.consts.enums.VoucherBusinessBillTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.PushBusinessBill;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.ExportTmpTblTypeEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 预售账单Repository
 *
 * @author yancao
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdvanceBillRepository extends ServiceImpl<AdvanceBillMapper, AdvanceBill> implements BillRepository<AdvanceBill> {

    @Setter(onMethod_ = @Autowired)
    private ExportService exportService;

    @Setter(onMethod_ = @Autowired)
    private SharedBillAppService sharedBillAppService;

    /**
     * 根据房号id和费项id获取预收账单
     *
     * @param roomIdAndChargeIdList 房号id和费项id
     * @return List
     */
    public List<AdvanceBill> queryByRoomIdAndChargeId(ArrayList<List<String>> roomIdAndChargeIdList) {
        LambdaQueryWrapper<AdvanceBill> queryWrapper = new LambdaQueryWrapper<>();
        for (List<String> roomIdAndChargeId : roomIdAndChargeIdList) {
            queryWrapper.or(wrapper -> wrapper.eq(AdvanceBill::getRoomId, roomIdAndChargeId.get(0))
                    .eq(AdvanceBill::getChargeItemId, roomIdAndChargeId.get(1)));
        }
        return list(queryWrapper);
    }

    /**
     * 根据检索条件统计账单退款金额总数
     *
     * @param query
     * @return
     */
    public BillTotalDto statisticsBillRefund(StatisticsBillTotalQuery query) {
        QueryWrapper<?> queryWrapper;
        //添加逻辑删除
        if (CollectionUtils.isNotEmpty(query.getBillIds())){
            queryWrapper = new QueryWrapper<>().in("b.id", query.getBillIds());
        }else {
            queryWrapper = query.getQuery().getConditions().getQueryModel();
        }
        queryWrapper.eq("br.state", RefundStateEnum.已退款.getCode());
        queryWrapper.in("b.refund_state", Lists.newArrayList(BillRefundStateEnum.已退款.getCode(),BillRefundStateEnum.部分退款.getCode()));
        queryWrapper.eq("b.deleted", DataDisabledEnum.启用.getCode()).eq("br.deleted", DataDisabledEnum.启用.getCode());

        BillTotalDto billTotalDto = baseMapper.statisticsBillRefund(queryWrapper);
        //由于一对多特殊处理主表数据
        queryWrapper.groupBy("b.id");
        BillTotalDto billTotalDto2 = baseMapper.statisticsBillRefund2(queryWrapper);
        if (billTotalDto2 != null) {
            billTotalDto.setAmountTotal(billTotalDto2.getAmountTotal());
            billTotalDto.setReceivableAmountTotal(billTotalDto2.getReceivableAmountTotal());
            billTotalDto.setSettleAmountTotal(billTotalDto2.getSettleAmountTotal());
            billTotalDto.setActualPayAmountTotal(billTotalDto2.getActualPayAmountTotal());
        }
        return billTotalDto;
    }

    @Override
    public List<AdvanceBill> getByIdsNoTenant(List<Long> billIds,String supCpUnitId) {
        return baseMapper.getByIdsNoTenant(billIds,supCpUnitId);
    }

    @Override
    public Boolean updateInvoiceState(List<AdvanceBill> billEList) {
        return  baseMapper.updateInvoiceState(billEList);
    }

    @Override
    public BillTotalDto queryTotal(PageF<SearchF<?>> query, List<Long> billIds , Integer billInvalid,Integer billRefund,String supCpUnitId) {
        return baseMapper.queryTotal(getQueryAndIdsWrapper(query, billIds, billInvalid,billRefund));
    }

    @Override
    public List<BillApproveTotalDto> queryApproveTotal(QueryWrapper<?> wrapper, String supCpUnitId) {
        String shareTableName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.BILL_APPROVE);
        return baseMapper.queryApproveTotal(wrapper, shareTableName);
    }

    @Override
    public BillDiscountTotalDto queryDiscountTotal(BillDiscountTotalQuery query) {
        return baseMapper.queryDiscountTotal(query);
    }

    @Override
    public IPage<AdvanceBill> queryBillByPage(PageF<SearchF<?>> query){
        List<Field> fields = query.getConditions().getFields();
        List<Field> billInvalidField = fields.stream().filter(s -> "billInvalid".equals(s.getName())).collect(Collectors.toList());
        fields.removeAll(billInvalidField);
        QueryWrapper<?> wrapper = getWrapper(query);
        if(CollectionUtils.isNotEmpty(billInvalidField) && (Integer) billInvalidField.get(0).getValue() == 1){
            invalidBillCondition(wrapper);
        }else{
            normalBillCondition(wrapper);
        }

        // 导出场合
        IPage<AdvanceBill> queryPage;
        Object exportTaskIdObj = query.getConditions().getSpecialMap().get("exportTaskId");
        if (exportTaskIdObj != null) {
            Object totalObj = query.getConditions().getSpecialMap().get("total");
            Long exportTaskId = Long.parseLong(String.valueOf(exportTaskIdObj));
            long count = 0;
            if (totalObj == null) {
                count = baseMapper.countBill(wrapper);
            } else {
                count = Long.parseLong(String.valueOf(totalObj));
            }
            // 导出条数超过指定条数，则通过临时表方式导出
            if (count > exportService.exportProperties().getTmpTableCount()) {
                String tblName = TableNames.ADVANCE_BILL;
                exportService.createTmpTbl(wrapper, tblName, exportTaskId, ExportTmpTblTypeEnum.ADVANCE);

                // 深分页查询优化
                long tid = (query.getPageNum() - 1) * query.getPageSize();
                queryPage = exportService.queryAdvanceBillByPageOnTempTbl(
                        Page.of(1, query.getPageSize(), false), tblName, exportTaskId, tid);
                queryPage.setTotal(count);
                return queryPage;
            }
        }

        // 原方式查询（非临时表方式）
        queryPage = baseMapper.queryBillByPage(Page.of(query.getPageNum(), query.getPageSize()), wrapper);
        return queryPage;
    }

    @Override
    public List<BillHandV> listBillHand(List<Long> billIds,String supCpUnitId) {
        return baseMapper.listBillHand(billIds,supCpUnitId);
    }

    @Override
    public BillTotalDto queryBillReviewTotal(QueryWrapper<?> wrapper, String supCpUnitId) {
        String shareTableName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.BILL_APPROVE);
        return baseMapper.queryBillReviewTotal(wrapper, shareTableName);
    }

    @Override
    public Page<BillInferenceV> pageBillInferenceInfo(ListBillInferenceF form, List<Long> billIds) {
        SearchF<?> searchF = new SearchF<>();
        searchF.setFields(new ArrayList<>());
        QueryWrapper<?> queryModel = searchF.getQueryModel();
        queryModel.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.eq("b.verify_state", BillVerifyStateEnum.未核销.getCode());
        if (billIds != null && !billIds.isEmpty()) {
            queryModel.notIn("b.id", billIds);
        }
        if (form.getFieldList() != null && !form.getFieldList().isEmpty()) {
            for (Field field : form.getFieldList()) {
                if ("adjust".equals(field.getName())) {
                    continue;
                }
                switch (field.getMethod()) {
                    case 1:
                        queryModel.eq(field.getName(), field.getValue());
                        break;
                    case 6:
                        queryModel.le(field.getName(), field.getValue());
                        break;
                    case 4:
                        queryModel.ge(field.getName(), field.getValue());
                        break;
                    case 15:
                        queryModel.in(field.getName(), (List) field.getValue());
                        break;
                    case 16:
                        queryModel.notIn(field.getName(), (List) field.getValue());
                        break;
                    default:
                        break;
                }
            }
        }
        if (form.getEventType() == EventTypeEnum.应收计提.getEvent() || form.getEventType() == EventTypeEnum.应付计提.getEvent()) {
            return baseMapper.queryAccrualBillInferenceByPage(Page.of(form.getPageNum(), form.getPageSize()), queryModel);
        }
        if (EventTypeEnum.账单调整.getEvent() == form.getEventType()) {
            return baseMapper.queryAdjustBillInferenceByPage(Page.of(form.getPageNum(), form.getPageSize()), queryModel);
        }
        if (EventTypeEnum.冲销作废.getEvent() == form.getEventType()) {
            return baseMapper.queryOffBillInferenceByPage(Page.of(form.getPageNum(), form.getPageSize()), queryModel);
        }
        if (EventTypeEnum.预收应收核销.getEvent() == form.getEventType()) {
            return baseMapper.queryCloseBillInferenceByPage(Page.of(form.getPageNum(), form.getPageSize()), queryModel);
        }
        return baseMapper.queryBillInferenceByPage(Page.of(form.getPageNum(), form.getPageSize()), queryModel);
    }

    @Override
    public IPage<AdvanceBill> queryBillByPageNoTenantLine(PageF<SearchF<?>> queryF) {
        return baseMapper.queryBillByPageNoTenantLine(Page.of(queryF.getPageNum(), queryF.getPageSize()),getWrapper(queryF));
    }

    @Override
    public IPage<AdvanceBill> listByInitialBill(Page<Object> page,QueryWrapper<?> queryWrapper) {
        return baseMapper.listByInitialBill(page, queryWrapper);
    }

    /**
     * 根据id查询应收账单(无租户隔离)
     *
     * @param billIdList 账单id
     * @return List
     */
    public List<AdvanceBill> listByIdsNotTenantId(List<Long> billIdList) {
        return baseMapper.listByIdsNotTenantId(billIdList);
    }

    /**
     * 根据房号统计金额
     *
     * @param roomIdList 房号id
     * @return List
     */
    public List<RoomBillTotalDto> roomBills(List<Long> roomIdList) {
        return baseMapper.roomBills(roomIdList);
    }

    /**
     * 根据房号和费项统计减免总额
     *
     * @param roomIdList 房号id集合
     * @param chargeItemIdList 费项id集合
     * @param currentYear 是否统计当年
     * @return List
     */
    public List<RoomBillTotalDto> roomChargeBills(List<Long> roomIdList, List<Long> chargeItemIdList, boolean currentYear) {
        return baseMapper.roomChargeBills(roomIdList,chargeItemIdList,currentYear);
    }

    /**
     * 更新交账状态
     * @param billId
     * @param handedState
     * @return
     */
    public boolean updateHandState(Long billId, BillAccountHandedStateEnum handedState) {
        return update(new LambdaUpdateWrapper<AdvanceBill>().eq(Bill::getId, billId).set(Bill::getAccountHanded, handedState.getCode()));
    }

    /**
     * 根据账单编号查询账单
     * @param billNo
     * @return
     */
    public AdvanceBill queryByBillNo(String billNo) {
        return baseMapper.selectOne(new LambdaQueryWrapper<AdvanceBill>().eq(AdvanceBill::getBillNo, billNo));
    }

    /**
     * 查询凭证业务单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    public List<VoucherBusinessBill> listVoucherBillByQuery(QueryWrapper<?> wrapper,
            int voucherEventType, boolean special, String tableName) {
        return baseMapper.listVoucherBillByQuery(wrapper,voucherEventType,special,tableName);
    }

    /**
     * 查询凭证业务已银行对账单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    public List<VoucherBusinessBill> listVoucherBankBillByQuery(QueryWrapper<?> wrapper, String tableName) {
        return baseMapper.listVoucherBankBillByQuery(wrapper,tableName);
    }

    /**
     * 查询凭证业务已流水认领单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    public List<VoucherBusinessBill> listVoucherClaimBillByQuery(QueryWrapper<?> wrapper, String tableName) {
        return baseMapper.listVoucherClaimBillByQuery(wrapper,tableName);
    }

    /**
     * 查询凭证业务预收单已退款单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    public List<VoucherBusinessBill> listVoucherRefundBillByQuery(QueryWrapper<?> wrapper, String tableName) {
        return baseMapper.listVoucherRefundBillByQuery(wrapper,
                VoucherEventTypeEnum.收入退款.getCode(),
                VoucherBusinessBillTypeEnum.预收单.getCode(),tableName);
    }

    public List<VoucherBusinessBill> listAdvanceBillByQuery(QueryWrapper<?> gatherWrapper, boolean change, String tableName) {
        return baseMapper.listAdvanceBillByQuery(gatherWrapper,VoucherBusinessBillTypeEnum.账单调整.getCode(),change,tableName);
    }

    /**
     * 查询凭证业务单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    public List<VoucherBusinessBill> listCancellationVoucherBillByQuery(QueryWrapper<?> wrapper, String tableName) {
        return baseMapper.listCancellationVoucherBillByQuery(wrapper,VoucherEventTypeEnum.作废.getCode(), BillTypeEnum.预收账单.getCode(),tableName);
    }

    /**
     * 应收预收冲销 单据信息
     *
     * @param wrapper
     * @param tableName
     */
    public List<VoucherBusinessBill> listVoucherReversedBillByQuery(QueryWrapper<?> wrapper, String tableName) {
        return baseMapper.listVoucherReversedBillByQuery(wrapper,tableName);
    }

    public AdvanceMaxEndTimeV queryMaxEndTime(AdvanceMaxEndTimeBillF maxEndTimeBillF) {
        return baseMapper.queryMaxEndTime(maxEndTimeBillF);
    }

    /**
     * 更新制作凭证成功的预收单状态为已推凭
     * @param idList
     */
    public void updateInferenceState(List<Long> idList) {
        this.baseMapper.updateInferenceState(idList, BillInferStateEnum.已推凭.getCode());
    }

    public List<Long> getAdvanceRoomIds(String communityId, Long chargeItemId) {
        return this.baseMapper.getAdvanceRoomIds(communityId, chargeItemId);
    }

    public List<PushBusinessBill> getAdvanceCarryDownBillList(QueryWrapper<?> wrappers) {
        return this.baseMapper.getAdvanceCarryDownBillList(wrappers);
    }

    public List<PushBusinessBill> getCollectionTransferDownBillList(QueryWrapper<?> wrappers) {
        return this.baseMapper.getCollectionTransferDownBillList(wrappers);
    }

    /**
     * 修改收款账户ID
     * @param sbAccountId
     * @param idList
     */
    public void updateSbAccountId(Long sbAccountId, List<Long> idList) {
        baseMapper.updateSbAccountId(sbAccountId, idList);
    }

    public List<PushBusinessBill> getAdvanceBillList(QueryWrapper<?> queryWrapper) {
        return this.baseMapper.getAdvanceBillList(queryWrapper);
    }

    public void updateBatchApprovedStateById(List<Long> advanceBillIds, int approvedState) {
        baseMapper.updateBatchApprovedStateById(advanceBillIds,approvedState);
    }

    public AdvanceBillTotalMoneyV getAdvanceBillTotalMoney(String payerId,String communityId) {
        return baseMapper.getAdvanceBillTotalMoney(payerId, communityId);
    }

    public List<Long> getAdvanceBillIds(QueryWrapper<?> queryWrapper) {
        return baseMapper.getAdvanceBillIds(queryWrapper);

    }

    public List<PushBusinessBill> reconciliationVerificationBillList(QueryWrapper<?> wrapper) {
        return baseMapper.reconciliationVerificationBillList(wrapper);
    }

    public List<PushBusinessBill> getAdvanceBillCarryoverList(Long billId) {
        return baseMapper.getAdvanceBillCarryoverList(billId);
    }

    public IPage queryPageApprove(PageF<SearchF<?>> queryF, QueryWrapper<?> wrapper, String billTable) {
        return baseMapper.getPageNotApprove(Page.of(queryF.getPageNum(),queryF.getPageSize()), wrapper, billTable);
    }

    public long countPageApprove(QueryWrapper<?> wrapper, String billTable) {
        return baseMapper.countPageNotApprove(wrapper, billTable);
    }

    public List<PushBusinessBill> getPaymentAdjustmentReversedBusinessBills(QueryWrapper<?> wrapper) {
        return baseMapper.getPaymentAdjustmentReversedBusinessBills(wrapper);
    }

    public List<PushBusinessBill> getPaymentAdjustmentInvalidBusinessBills(QueryWrapper<?> wrapper) {
        return baseMapper.getPaymentAdjustmentInvalidBusinessBills(wrapper);
    }


    /**
     * 根据roomIds 修改成本中心信息
     *
     * @param roomIds        房间ids
     * @param supCpUnitId    项目id
     * @param costCenterId   成本中心id
     * @param costCenterName 成本中心名称
     */
    public void updateCostMsgByRoomIds(List<String> roomIds,String supCpUnitId,Long costCenterId,String costCenterName){
        baseMapper.updateCostMsgByRoomIds(roomIds,supCpUnitId,costCenterId,costCenterName);
    }

    /**
     * 根据项目id 修改成本中心信息
     *
     * @param supCpUnitId    项目id
     * @param costCenterId   成本中心id
     * @param costCenterName 成本中心名称
     */
    public void updateCostMsgBySupCpUnitId(String supCpUnitId,Long costCenterId,String costCenterName){
        baseMapper.updateCostMsgBySupCpUnitId(supCpUnitId,costCenterId,costCenterName);
    }


    public List<VoucherBusinessBill> listVoucherReversedTempBillByQuery(QueryWrapper<?> wrapper, String tableName) {
        return baseMapper.listVoucherReversedTempBillByQuery(wrapper,tableName);
    }
}
