package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.fo.ListBillInferenceF;
import com.wishare.finance.apps.model.bill.vo.BillHandV;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.domains.bill.command.BillDiscountTotalQuery;
import com.wishare.finance.domains.bill.command.StatisticsBillTotalQuery;
import com.wishare.finance.domains.bill.consts.enums.BillRefundStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillSharedingColumn;
import com.wishare.finance.domains.bill.consts.enums.BillStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.FreezeTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.RefundStateEnum;
import com.wishare.finance.domains.bill.dto.BillApproveTotalDto;
import com.wishare.finance.domains.bill.dto.BillDiscountTotalDto;
import com.wishare.finance.domains.bill.dto.BillTotalDto;
import com.wishare.finance.domains.bill.dto.RoomBillTotalDto;
import com.wishare.finance.domains.bill.dto.TempChargeBillExportDto;
import com.wishare.finance.domains.bill.entity.TemporaryChargeBill;
import com.wishare.finance.domains.bill.repository.mapper.TemporaryChargeBillMapper;
import com.wishare.finance.domains.export.service.ExportService;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.ExportTmpTblTypeEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.remote.clients.base.ChargeClient;
import com.wishare.finance.infrastructure.remote.vo.payment.DeductionBillF;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TemporaryChargeBillRepository extends ServiceImpl<TemporaryChargeBillMapper, TemporaryChargeBill> implements BillRepository<TemporaryChargeBill> {

    @Setter(onMethod_ = @Autowired)
    private ExportService exportService;

    @Setter(onMethod_ = @Autowired)
    private SharedBillAppService sharedBillAppService;
    private final ChargeClient chargeClient;

    /**
     * 分页查询临时收费导出账单列表
     *
     * @param query query
     * @return IPage
     */
    public IPage<TempChargeBillExportDto> queryExportDataPage(PageF<SearchF<?>> query, QueryWrapper<?> wrapper) {
        wrapper.groupBy("b.id");
        wrapper.eq("b.bill_type", BillTypeEnum.临时收费账单.getCode());
        wrapper.ne("b.overdue", DataDisabledEnum.禁用.getCode());
        normalBillCondition(wrapper);
        return baseMapper.queryExportDataPage(Page.of(query.getPageNum(),query.getPageSize()), wrapper);
    }

    /**
     * 分页查询临时收费初始审核导出账单列表
     *
     * @param query query
     * @return IPage
     */
    public IPage<TempChargeBillExportDto> queryInitialApprovedExportDataPage(PageF<SearchF<?>> query,QueryWrapper<?> wrapper) {
        wrapper.eq("b.bill_type", BillTypeEnum.临时收费账单.getCode());
        wrapper.ne("b.overdue", DataDisabledEnum.禁用.getCode());
        normalBillCondition(wrapper);
        return baseMapper.queryInitialApprovedExportDataPage(Page.of(query.getPageNum(),query.getPageSize()), wrapper);
    }


    @Override
    public IPage<TemporaryChargeBill> getPageWithApprove(PageF<SearchF<?>> query) {
        QueryWrapper<?> wrapper = getWrapper(query);
        wrapper.eq("b.bill_type", BillTypeEnum.临时收费账单.getCode());
        PageQueryUtils.validQueryContainsFieldAndValue(query, "b." + BillSharedingColumn.应收账单.getColumnName());

        // 导出场合
        IPage<TemporaryChargeBill> queryPage;
        Object exportTaskIdObj = query.getConditions().getSpecialMap().get("exportTaskId");
        if (exportTaskIdObj != null) {
            Object totalObj = query.getConditions().getSpecialMap().get("total");
            Long exportTaskId = Long.parseLong(String.valueOf(exportTaskIdObj));
            long count = 0;
            if (totalObj == null) {
                count = baseMapper.countWithApprove(wrapper);
            } else {
                count = Long.parseLong(String.valueOf(totalObj));
            }
            // 导出条数超过指定条数，则通过临时表方式导出
            if (count > exportService.exportProperties().getTmpTableCount()) {
                queryPage = getTemporaryChargeBillBytTmpTbl(query, wrapper, ExportTmpTblTypeEnum.TEMPORARY_GROUP, exportTaskId);
                queryPage.setTotal(count);
                return queryPage;
            }
        }

        // 原方式查询（非临时表方式）
        queryPage = baseMapper.getPageWithApprove(Page.of(query.getPageNum(),query.getPageSize()), wrapper);
        return queryPage;
    }

    /**
     * 分页获取临时账单审核列表
     * @return IPage
     */
    public IPage<TemporaryChargeBill> getPageNotApprove(PageF<SearchF<?>> queryF, QueryWrapper<?> wrapper) {
        wrapper.eq("b.bill_type", BillTypeEnum.临时收费账单.getCode());
        PageQueryUtils.validQueryContainsFieldAndValue(queryF, "b." + BillSharedingColumn.应收账单.getColumnName());

        // 导出场合
        IPage<TemporaryChargeBill> queryPage;
        Object exportTaskIdObj = queryF.getConditions().getSpecialMap().get("exportTaskId");
        if (exportTaskIdObj != null) {
            Object totalObj = queryF.getConditions().getSpecialMap().get("total");
            Long exportTaskId = Long.parseLong(String.valueOf(exportTaskIdObj));
            long count = 0;
            if (totalObj == null) {
                count = baseMapper.countNotApprove(wrapper);
            } else {
                count = Long.parseLong(String.valueOf(totalObj));
            }
            // 导出条数超过指定条数，则通过临时表方式导出
            if (count > exportService.exportProperties().getTmpTableCount()) {
                queryPage = getTemporaryChargeBillBytTmpTbl(queryF, wrapper, ExportTmpTblTypeEnum.TEMPORARY_GROUP_APPROVE, exportTaskId);
                queryPage.setTotal(count);
                return queryPage;
            }
        }

        // 原方式查询（非临时表方式）
        queryPage = baseMapper.getPageNotApprove(Page.of(queryF.getPageNum(),queryF.getPageSize()), wrapper);
        return queryPage;
    }

    @Override
    public List<TemporaryChargeBill> getByIdsNoTenant(List<Long> billIds,String supCpUnitId) {
        return baseMapper.getByIdsNoTenant(billIds,supCpUnitId);
    }

    @Override
    public Boolean updateInvoiceState(List<TemporaryChargeBill> billEList) {
        return baseMapper.updateInvoiceState(billEList);
    }

    @Override
    public BillTotalDto queryTotal(PageF<SearchF<?>> query, List<Long> billIds, Integer billInvalid, Integer billRefund,String supCpUnitId) {
        List<Field> fields = new ArrayList<>();
        if (Objects.nonNull(query)) {
            fields = query.getConditions().getFields();
        }
        Boolean isApproveUnion = Boolean.FALSE;
        Optional<Field> lockedQuery = fields.stream().filter(s -> "locked".equals(s.getName())).findFirst();
        lockedQuery.ifPresent(field -> query.getConditions().getFields().remove(field));
        List<Field> operTypecontain = fields.stream().filter(s -> "ba.sup_cp_unit_id".equals(s.getName())).collect(Collectors.toList());
        isApproveUnion = CollectionUtils.isNotEmpty(operTypecontain);
        QueryWrapper<?> queryWrapper = getQueryAndIdsWrapper(query, billIds, billInvalid, billRefund);
        if (lockedQuery.isPresent()) {
            Field field = lockedQuery.get();
            String value = (String) field.getValue();
            if ("1".equals(value)) {
                queryWrapper.eq("b.state", BillStateEnum.冻结.getCode())
                        .in("b.freeze_type", FreezeTypeEnum.LOCK_TYPE);
            }else {
                queryWrapper.and(e -> e
                        .ne("b.state", BillStateEnum.冻结.getCode())
                        .or( w -> w.eq("b.state", BillStateEnum.冻结.getCode())
                                .notIn("b.freeze_type", FreezeTypeEnum.LOCK_TYPE))
                ) ;
            }
        }
        //有效账单条件
        if(Objects.isNull(billInvalid) || billInvalid != 1){
            queryWrapper.ne("b.overdue", DataDisabledEnum.禁用.getCode());
        }
        queryWrapper.eq("b.bill_type", BillTypeEnum.临时收费账单.getCode());
        if(StringUtils.isBlank(supCpUnitId)) {
            throw new RuntimeException("上级收费单元ID为必传字段!");
        }
        queryWrapper.eq(StringUtils.isNotBlank(supCpUnitId),"b.sup_cp_unit_id", supCpUnitId);
        if (!isApproveUnion){
            return baseMapper.queryTotal(queryWrapper);
        }else {
            String receivableBillName = sharedBillAppService.getShareTableName(query, TableNames.RECEIVABLE_BILL, "b." + BillSharedingColumn.应收账单.getColumnName());
            String billApproveName = sharedBillAppService.getShareTableName(query, TableNames.BILL_APPROVE, "ba." + BillSharedingColumn.审核信息.getColumnName());
            queryWrapper.eq("ba.deleted", 0);
            queryWrapper.in("ba.approved_state", List.of(0,1));
            return baseMapper.queryApproveInfoTotal(queryWrapper,receivableBillName,billApproveName);
        }
    }

    @Override
    public List<BillApproveTotalDto> queryApproveTotal(QueryWrapper<?> wrapper, String supCpUnitId) {
        wrapper.eq("b.bill_type", BillTypeEnum.临时收费账单.getCode());
        return baseMapper.queryApproveTotal(wrapper);
    }


    public Boolean updateBatch(List<TemporaryChargeBill> billList){
        String supCpUnitId = billList.get(0).getSupCpUnitId();
        String receivableBillName = sharedBillAppService.getShareTableName(supCpUnitId,TableNames.RECEIVABLE_BILL);
        Boolean result = baseMapper.updateBatch(receivableBillName, billList);
        //生成对应减免记录
        /*List<DeductionBillF> deductionBillFList = new ArrayList<>();
        for(TemporaryChargeBill bill : billList){
            DeductionBillF deductionBillF = new DeductionBillF();
            deductionBillF.setBillId(bill.getId());
            deductionBillF.setAdjustAmount(bill.getReductionAmount());
            deductionBillF.setAdjustWay(8);
            deductionBillF.setReason(99);
            deductionBillF.setAdjustType(1);
            deductionBillF.setDeductionMethod(1);
            deductionBillF.setDerateStrategy(1);
            deductionBillF.setSupCpUnitId(bill.getSupCpUnitId());
            if(Objects.nonNull(bill.getReductionAmount())){
                deductionBillFList.add(deductionBillF);
            }
        }
        if(CollectionUtils.isNotEmpty(deductionBillFList)){
            try{
                chargeClient.deductionApplyList(deductionBillFList);
            }catch (Exception e){
                log.error("生成减免记录错误，{}",e);
            }
        }*/
        return result;
    }

    public Boolean updateBatchStatus(List<Long> ids, String communityId, Integer billEventType, int flag){
        String receivableBillName = sharedBillAppService.getShareTableName(communityId, TableNames.RECEIVABLE_BILL);
        return baseMapper.updateBatchStatus(receivableBillName, ids, billEventType, flag);
    }


    @Override
    public BillDiscountTotalDto queryDiscountTotal(BillDiscountTotalQuery query) {
        return baseMapper.queryDiscountTotal(query);
    }

    //收款账单-临时账单
    @Override
    public IPage<TemporaryChargeBill> queryBillByPage(PageF<SearchF<?>> query){
        List<Field> fields = query.getConditions().getFields();
        List<Field> billInvalidField = fields.stream().filter(s -> "billInvalid".equals(s.getName())).collect(Collectors.toList());
        fields.removeAll(billInvalidField);
        QueryWrapper<?> wrapper;
        if(CollectionUtils.isNotEmpty(billInvalidField) && (Integer) billInvalidField.get(0).getValue() == 1){
            wrapper = getInvalidBillWrapper(query);
        }else{
            wrapper = getWrappers(query);
            wrapper.ne("b.overdue", DataDisabledEnum.禁用.getCode());
        }
        wrapper.eq("b.bill_type", BillTypeEnum.临时收费账单.getCode());
        PageQueryUtils.validQueryContainsFieldAndValue(query, "b." + BillSharedingColumn.应收账单.getColumnName());

        // 导出场合
        IPage<TemporaryChargeBill> queryPage;
        Object exportTaskIdObj = query.getConditions().getSpecialMap().get("exportTaskId");
        List<Field> operTypecontain = fields.stream().filter(s -> "ba.sup_cp_unit_id".equals(s.getName())).collect(Collectors.toList());
        if (exportTaskIdObj != null) {
            Object totalObj = query.getConditions().getSpecialMap().get("total");
            Long exportTaskId = Long.parseLong(String.valueOf(exportTaskIdObj));
            long count = 0;
            if (totalObj == null) {
                if (CollectionUtils.isNotEmpty(operTypecontain)){
                    String receivableBillName = sharedBillAppService.getShareTableName(query, TableNames.RECEIVABLE_BILL, "b." + BillSharedingColumn.应收账单.getColumnName());
                    String billApproveName = sharedBillAppService.getShareTableName(query, TableNames.BILL_APPROVE, "ba." + BillSharedingColumn.审核信息.getColumnName());
                    wrapper.eq("ba.deleted", 0);
                    wrapper.in("ba.approved_state", List.of(0,1));
                    count =  baseMapper.countApproveBill(wrapper,receivableBillName,billApproveName);
                }else {
                    count = baseMapper.countBill(wrapper);
                }
            } else {
                count = Long.parseLong(String.valueOf(totalObj));
            }
            // 导出条数超过指定条数，则通过临时表方式导出
            if (count > exportService.exportProperties().getTmpTableCount()) {
                if (CollectionUtils.isEmpty(operTypecontain)){
                    queryPage = getTemporaryChargeBillBytTmpTbl(query, wrapper, ExportTmpTblTypeEnum.TEMPORARY, exportTaskId);
                }else {
                    queryPage = getTemporaryChargeBillBytTmpTbl(query, wrapper, ExportTmpTblTypeEnum.TEMPORARY_GROUP_APPROVE, exportTaskId);
                }
                queryPage.setTotal(count);
                return queryPage;
            }
        }

        if (CollectionUtils.isEmpty(operTypecontain)){
            return baseMapper.queryBillByPage(Page.of(query.getPageNum(), query.getPageSize()), wrapper);
        }else {
            String receivableBillName = sharedBillAppService.getShareTableName(query, TableNames.RECEIVABLE_BILL, "b." + BillSharedingColumn.应收账单.getColumnName());
            String billApproveName = sharedBillAppService.getShareTableName(query, TableNames.BILL_APPROVE, "ba." + BillSharedingColumn.审核信息.getColumnName());
            wrapper.eq("ba.deleted", 0);
            wrapper.in("ba.approved_state", List.of(0,1));
            return baseMapper.queryBillApproveByPage(Page.of(query.getPageNum(), query.getPageSize()), wrapper,receivableBillName,billApproveName);
        }
    }

    @Override
    public List<BillHandV> listBillHand(List<Long> billIds,String supCpUnitId) {
        return baseMapper.listBillHand(billIds,supCpUnitId);
    }

    @Override
    public BillTotalDto queryBillReviewTotal(QueryWrapper<?> wrapper, String supCpUnitId) {
        wrapper.eq("b.bill_type", BillTypeEnum.临时收费账单.getCode());
        return baseMapper.queryBillReviewTotal(wrapper);
    }

    @Override
    public Page<BillInferenceV> pageBillInferenceInfo(ListBillInferenceF form, List<Long> billIds) {
        SearchF<?> searchF = new SearchF<>();
        searchF.setFields(new ArrayList<>());
        QueryWrapper<?> queryModel = searchF.getQueryModel();
//        queryModel.eq("b.charge_item_id", form.getChargeItemId());
        queryModel.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
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
        queryModel.eq("b.bill_type", BillTypeEnum.临时收费账单.getCode());
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
    public IPage<TemporaryChargeBill> queryBillByPageNoTenantLine(PageF<SearchF<?>> queryF) {
        QueryWrapper<?> wrapper = getWrapper(queryF);
        wrapper.eq("b.bill_type", BillTypeEnum.临时收费账单.getCode());
        PageQueryUtils.validQueryContainsFieldAndValue(queryF, "b." + BillSharedingColumn.应收账单.getColumnName());
        return baseMapper.queryBillByPageNoTenantLine(Page.of(queryF.getPageNum(), queryF.getPageSize()),wrapper);
    }

    @Override
    public IPage<TemporaryChargeBill> listByInitialBill(Page<Object> page, QueryWrapper<?> queryWrapper) {
        queryWrapper.eq("b.bill_type", BillTypeEnum.临时收费账单.getCode());
        queryWrapper.in("b.approved_state", List.of(0,1));
        queryWrapper.in("is_init", 1);
        queryWrapper.eq("b.deleted", 0);
        return baseMapper.listByInitialBill(page, queryWrapper);
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
        queryWrapper.eq("b.bill_type", BillTypeEnum.临时收费账单.getCode());
        if(StringUtils.isBlank(query.getSupCpUnitId())) {
            throw new RuntimeException("上级收费单元ID supCpUnitId 不能为空!");
        }
        queryWrapper.eq("b." + BillSharedingColumn.应收账单.getColumnName(), query.getSupCpUnitId());
        BillTotalDto billTotalDto = baseMapper.statisticsBillRefund(queryWrapper, query.getSupCpUnitId());

        //由于一对多特殊处理主表数据
        queryWrapper.groupBy("b.id");
        BillTotalDto billTotalDto2 = baseMapper.statisticsBillRefund2(queryWrapper, query.getSupCpUnitId());
        if (billTotalDto2 != null) {
            billTotalDto.setAmountTotal(billTotalDto2.getAmountTotal());
            billTotalDto.setReceivableAmountTotal(billTotalDto2.getReceivableAmountTotal());
            billTotalDto.setSettleAmountTotal(billTotalDto2.getSettleAmountTotal());
            billTotalDto.setActualPayAmountTotal(billTotalDto2.getActualPayAmountTotal());
        }
        return billTotalDto;
    }

    /**
     * 根据id查询应收账单(无租户隔离)
     *
     * @param billIdList 账单id
     * @return List
     */
    public List<TemporaryChargeBill> listByIdsNotTenantId(List<Long> billIdList,String supCpUnitId) {
        return baseMapper.listByIdsNotTenantId(billIdList,supCpUnitId);
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
     * 获取临时账单数据
     *
     * @return List
     */
    public List<TemporaryChargeBill> getDealDate() {
        return baseMapper.getDealDate();
    }

    public List<Long> getTemporaryBillIds(QueryWrapper<?> temporaryBillQueryWrapper) {
        return baseMapper.getTemporaryBillIds(temporaryBillQueryWrapper);
    }

    private IPage<TemporaryChargeBill> getTemporaryChargeBillBytTmpTbl(PageF<SearchF<?>> queryF, QueryWrapper<?> wrapper, ExportTmpTblTypeEnum exportTmpTblTypeEnum, Long exportTaskId) {
        List<Field> fields = queryF.getConditions().getFields();
        String tblName = TableNames.RECEIVABLE_BILL;
        List<Field> supCpUnitIds = fields.stream().filter(s -> "b.sup_cp_unit_id".equals(s.getName())).collect(Collectors.toList());
        if (supCpUnitIds != null && supCpUnitIds.size() > 0) {
            tblName = sharedBillAppService.getShareTableName(supCpUnitIds.get(0).getValue().toString(), tblName);
        }
        exportService.createTmpTbl(wrapper, tblName, exportTaskId, exportTmpTblTypeEnum);

        // 深分页查询优化
        long tid = (queryF.getPageNum() - 1) * queryF.getPageSize();
        IPage<TemporaryChargeBill> queryPage = exportService.queryTemporaryChargeBillByPageOnTempTbl(
                Page.of(1, queryF.getPageSize(), false), tblName, exportTaskId, tid);
        return queryPage;
    }

    public List<Long> getBillRoomIds(String communityId, Long chargeItemId) {
        return this.baseMapper.getBillRoomIds(communityId, chargeItemId);
    }
}
