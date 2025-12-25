package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.fo.*;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.command.BillDiscountTotalQuery;
import com.wishare.finance.domains.bill.command.StatisticsBillTotalQuery;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.dto.*;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.repository.mapper.ReceivableBillMapper;
import com.wishare.finance.domains.export.service.ExportService;
import com.wishare.finance.domains.voucher.consts.enums.VoucherBusinessBillTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.PushBusinessBill;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.PushZJBusinessBill;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.PushZJBusinessBillForSettlement;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.ExportTmpTblTypeEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.finance.infrastructure.utils.SearchFieldSortUtils;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReceivableBillRepository extends ServiceImpl<ReceivableBillMapper, ReceivableBill> implements BillRepository<ReceivableBill> {

    private  final String FANG_YUAN = "fangyuan";

    @Autowired
    private SharedBillAppService sharedBillAppService;

    @Autowired
    private ExportService exportService;

    /**
     * 查询周期性账单
     * @param receivableBill
     * @return
     */
    public List<ReceivableBill> listByPeriodic(ReceivableBill receivableBill){
		QueryWrapper<ReceivableBill> wrapper = new QueryWrapper<ReceivableBill>().eq("community_id", receivableBill.getCommunityId())
                .eq("charge_item_id", receivableBill.getChargeItemId())
                .eq("room_id", receivableBill.getRoomId())
                .ge("start_time", receivableBill.getStartTime())
                .le("end_time", receivableBill.getEndTime());
        wrapper.eq("bill_type", BillTypeEnum.应收账单.getCode());
		wrapper.eq("sup_cp_unit_id", receivableBill.getSupCpUnitId());
        return list(wrapper);
    }

    @Override
    public boolean batchFreezeBillByIds(List<ReceivableBill> billList, Integer freezeType, String supCpUnitId) {
        List<Long> collect = billList.stream().map(ReceivableBill::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(billList)) {
            return false;
        }
        String shareTableName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.RECEIVABLE_BILL);
        return baseMapper.batchFreezeBillByIds(collect, freezeType, shareTableName);
    }

    @Override
    public boolean batchUnFreezeBillByIds(List<ReceivableBill> billList, String supCpUnitId) {
        List<Long> collect = billList.stream().map(ReceivableBill::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(billList)) {
            return false;
        }
        String shareTableName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.RECEIVABLE_BILL);
        return baseMapper.batchUnFreezeBillByIds(collect, shareTableName);
    }

    @Override
    public IPage<ReceivableBill> getPageWithApprove(PageF<SearchF<?>> query) {
        QueryWrapper<?> wrapper = getWrapper(query);
        wrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        PageQueryUtils.validQueryContainsFieldAndValue(query, "b." + BillSharedingColumn.应收账单.getColumnName());
        return baseMapper.getPageWithApprove(Page.of(query.getPageNum(), query.getPageSize()), wrapper);
    }

    @Override
    public List<ReceivableBill> getByIdsNoTenant(List<Long> billIds,String supCpUnitId) {
        return baseMapper.getByIdsNoTenant(billIds,supCpUnitId);
    }

    @Override
    public Boolean updateInvoiceState(List<ReceivableBill> billEList) {
        return  baseMapper.updateInvoiceState(billEList);
    }

    @Override
    public BillTotalDto queryTotal(PageF<SearchF<?>> query, List<Long> billIds, Integer billInvalid, Integer billRefund,String supCpUnitId) {
        boolean all=true;
        Optional<Field> lockedQuery = null;
        Boolean isApproveUnion = Boolean.FALSE;
        if (query!=null&&query.getConditions()!=null&&query.getConditions().getFields()!=null){
            List<Field> fields = query.getConditions().getFields();
            lockedQuery = fields.stream().filter(s -> "locked".equals(s.getName())).findFirst();
            List<Field> operTypecontain = fields.stream().filter(s -> "ba.sup_cp_unit_id".equals(s.getName())).collect(Collectors.toList());
            isApproveUnion = CollectionUtils.isNotEmpty(operTypecontain);
            lockedQuery.ifPresent(field -> query.getConditions().getFields().remove(field));
            List<Field> billcontain = fields.stream().filter(s -> "billcontain".equals(s.getName())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(billcontain)){
                fields.removeAll(billcontain);
                all=false;
            }
        }
        QueryWrapper<?> wrapper = getQueryAndIdsWrapper(query, billIds, billInvalid, billRefund);
        if (Objects.nonNull(lockedQuery) &&lockedQuery.isPresent()) {
            Field field = lockedQuery.get();
            String value = (String) field.getValue();
            if ("1".equals(value)) {
                wrapper.eq("b.state", BillStateEnum.冻结.getCode())
                        .in("b.freeze_type", FreezeTypeEnum.LOCK_TYPE);
            }else {
                wrapper.and(e -> e
                        .ne("b.state", BillStateEnum.冻结.getCode())
                        .or( w -> w.eq("b.state", BillStateEnum.冻结.getCode())
                                .notIn("b.freeze_type", FreezeTypeEnum.LOCK_TYPE))
                ) ;
            }
        }
        if (all) {
//            wrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
            if (query!=null&&query.getConditions()!=null&&query.getConditions().getFields()!=null){
                Field fieldBillType = new Field("b.bill_type",BillTypeEnum.应收账单.getCode(),1);
                query.getConditions().getFields().add(fieldBillType);
            }else{
                wrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
            }
        }
        //有效账单条件
        if(Objects.isNull(billInvalid) || billInvalid != 1){
//            wrapper.ne("b.overdue",DataDisabledEnum.禁用.getCode());
            if (query != null && query.getConditions() != null && CollectionUtils.isNotEmpty(query.getConditions().getFields())) {
                List<Field> fields = query.getConditions().getFields();
                List<Field> isInits = fields.stream().filter(s -> "b.is_init".equals(s.getName())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(isInits) || !Objects.equals(isInits.get(0).getValue(), 0)) {
                    Field fieldOverdue = new Field("b.overdue", DataDisabledEnum.禁用.getCode(), 2);
                    query.getConditions().getFields().add(fieldOverdue);
                }

            } else {
                wrapper.ne("b.overdue", DataDisabledEnum.禁用.getCode());
            }
        }
        if(StringUtils.isNotBlank(supCpUnitId)){
            if (query!=null&&query.getConditions()!=null&&query.getConditions().getFields()!=null){
                Field field1 = new Field("b.sup_cp_unit_id",supCpUnitId,1);
                query.getConditions().getFields().add(field1);
            }else {
                wrapper.eq(StringUtils.isNotBlank(supCpUnitId),"b.sup_cp_unit_id", supCpUnitId);
            }
        }
//        wrapper.eq(StringUtils.isNotBlank(supCpUnitId),"b.sup_cp_unit_id", supCpUnitId);
        if (query!=null&&query.getConditions()!=null&&query.getConditions().getFields()!=null){
            wrapper = SearchFieldSortUtils.sortField(query);
        }
        wrapper.eq("b.deleted", 0);

        if (!isApproveUnion){
            return baseMapper.queryTotal(wrapper);
        }else {
            String receivableBillName = sharedBillAppService.getShareTableName(query, TableNames.RECEIVABLE_BILL, "b." + BillSharedingColumn.应收账单.getColumnName());
            String billApproveName = sharedBillAppService.getShareTableName(query, TableNames.BILL_APPROVE, "ba." + BillSharedingColumn.审核信息.getColumnName());
            wrapper.eq("ba.deleted", 0);
            wrapper.in("ba.approved_state", List.of(0,1));
            return baseMapper.queryApproveInfoTotal(wrapper,receivableBillName,billApproveName);
        }



    }

    @Override
    public List<BillApproveTotalDto> queryApproveTotal(QueryWrapper<?> wrapper, String supCpUnitId) {
        wrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        return baseMapper.queryApproveTotal(wrapper);
    }
    public List<BillApproveTotalDto> queryApproveTotalNew(QueryWrapper<?> wrapper) {
        return baseMapper.queryApproveTotalNew(wrapper);
    }

    @Override
    public BillDiscountTotalDto queryDiscountTotal(BillDiscountTotalQuery query) {
        if(StringUtils.isBlank(query.getSupCpUnitId())) {
            throw new IllegalArgumentException("查询应收账单：上级收费单元ID supCpUnitId为必传!");
        }
        return baseMapper.queryDiscountTotal(query);
    }

    @Override
    public IPage<ReceivableBill> queryBillByPage(PageF<SearchF<?>> query){
        PageQueryUtils.validQueryContainsFieldAndValue(query, "b." + BillSharedingColumn.应收账单.getColumnName());
        List<Field> fields = query.getConditions().getFields();
        // List<Field> collect = fields.stream().filter(s -> "b.payer_id".equals(s.getName())).collect(Collectors.toList());
        Object getPayerIdIsNullValue = query.getConditions().getSpecialMap().get("getPayerIdIsNull");
        boolean flag = !Objects.isNull(getPayerIdIsNullValue) && (boolean) getPayerIdIsNullValue;
        // if(CollectionUtils.isNotEmpty(collect) && flag){
        //     fields.removeAll(collect);
        // }
        boolean all=true;
        List<Field> billcontain = fields.stream().filter(s -> "billcontain".equals(s.getName())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(billcontain)){
            fields.removeAll(billcontain);
            all=false;
        }
        //分页获取应收账单，单独处理结转应收时同时获取收费对象为空或为该房号收费对象的账单
        QueryWrapper<?> wrapper = getWrapper(query);
        List<Field> billInvalidField = fields.stream().filter(s -> "billInvalid".equals(s.getName())).collect(Collectors.toList());
        fields.removeAll(billInvalidField);
        // if(CollectionUtils.isNotEmpty(collect) && flag){
        //     wrapper.and(queryWrapper -> queryWrapper.eq("b.payer_id", collect.get(0).getValue()).or().isNull("b.payer_id"));
        // }else
            if(CollectionUtils.isNotEmpty(billInvalidField) && (Integer) billInvalidField.get(0).getValue() == 1){
            invalidBillCondition(wrapper);
        }else{
            normalBillCondition(wrapper);
            List<Field> isInits = fields.stream().filter(s -> "b.is_init".equals(s.getName())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(isInits) || !Objects.equals(isInits.get(0).getValue(),0)){
                wrapper.ne("b.overdue", DataDisabledEnum.禁用.getCode());
            }
        }
        if (all) {
            wrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        }
        List<Field> operTypecontain = fields.stream().filter(s -> "ba.sup_cp_unit_id".equals(s.getName())).collect(Collectors.toList());
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

    /**
     *
     * 绿城poc查询应收账单分页接口
     **/

    public IPage<ReceivableBill> queryCommonBillByPage(PageF<SearchF<?>> query){
        Field field1 = new Field("b.bill_type",BillTypeEnum.应收账单.getCode(),1);
        Field field2 = new Field("b.overdue",DataDisabledEnum.禁用.getCode(),2);
        Field field3 = new Field("b.approved_state",BillApproveStateEnum.已审核.getCode(),1);
        query.getConditions().getFields().add(field1);
        query.getConditions().getFields().add(field2);
        query.getConditions().getFields().add(field3);
        List<Field> fields = query.getConditions().getFields();
        String supCpUnitId = "";
        List<Field> supCpUnitIdFields = fields.stream().filter(s -> "b.sup_cp_unit_id".equals(s.getName())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(supCpUnitIdFields)){
            supCpUnitId = supCpUnitIdFields.get(0).getValue().toString();
        }
        Optional<Field> lockedQuery = fields.stream().filter(s -> "locked".equals(s.getName())).findFirst();
        lockedQuery.ifPresent(field -> query.getConditions().getFields().remove(field));
        QueryWrapper<?> wrapper = SearchFieldSortUtils.sortField(query);
        // 锁定账单查询
        if (lockedQuery.isPresent()) {
            Field field = lockedQuery.get();
            String value = (String) field.getValue();
            if ("1".equals(value)) {
                wrapper.eq("b.state", BillStateEnum.冻结.getCode())
                        .in("b.freeze_type", FreezeTypeEnum.LOCK_TYPE);
            }else {
                wrapper.and(e -> e
                                .ne("b.state", BillStateEnum.冻结.getCode())
                                .or( w -> w.eq("b.state", BillStateEnum.冻结.getCode())
                                        .notIn("b.freeze_type", FreezeTypeEnum.LOCK_TYPE))
                ) ;
            }
        }
        //分页获取应收账单，单独处理结转应收时同时获取收费对象为空或为该房号收费对象的账单
//         wrapper = query.getConditions().getQueryModel();
        Boolean flag = (Boolean) query.getConditions().getSpecialMap().get("getPayerIdIsNull");
        List<Field> collect = fields.stream().filter(s -> "b.payer_id".equals(s.getName())).collect(Collectors.toList());
        List<Field> billInvalidField = fields.stream().filter(s -> "billInvalid".equals(s.getName())).collect(Collectors.toList());
        fields.removeAll(billInvalidField);
//        if(CollectionUtils.isNotEmpty(collect) && flag){
        if(CollectionUtils.isNotEmpty(collect) && Objects.nonNull(flag) && flag){
            fields.removeAll(collect);
            wrapper.and(queryWrapper -> queryWrapper.eq("b.payer_id", collect.get(0).getValue()).or().isNull("b.payer_id"));
        }else if(CollectionUtils.isNotEmpty(billInvalidField) && (Integer) billInvalidField.get(0).getValue() == 1){
            invalidBillCondition(wrapper);
        }else{
//            normalCommonBillCondition(wrapper);
        }
        wrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
//        wrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
//        wrapper.ne("b.overdue",DataDisabledEnum.禁用.getCode());
//        wrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode());
        List<OrderBy> orderBy = query.getOrderBy();
        if (CollectionUtils.isEmpty(orderBy)) {
            if (FANG_YUAN.equals(EnvData.config)) {
                wrapper.orderByDesc("b.start_time").orderByDesc("b.end_time").orderByDesc("b.id");
            }else {
                wrapper.orderByAsc("b.start_time").orderByAsc("b.end_time").orderByAsc("b.id");
            }
        }else {
            for (OrderBy item : orderBy) {
                if (item.isAsc()) {
                    wrapper.orderByAsc(item.getField());
                }else {
                    wrapper.orderByDesc(item.getField());
                }
            }
        }
        PageQueryUtils.validQueryContainsFieldAndValue(query, "b." + BillSharedingColumn.应收账单.getColumnName());

        // 导出场合
        IPage<ReceivableBill> queryPage;
        Object exportTaskIdObj = query.getConditions().getSpecialMap().get("exportTaskId");
        if (exportTaskIdObj != null) {
            Object totalObj = query.getConditions().getSpecialMap().get("total");
            Long exportTaskId = Long.parseLong(String.valueOf(exportTaskIdObj));
            long count = 0;
            if (totalObj == null) {
                count = baseMapper.queryBillCountByPage(wrapper);
            } else {
                count = Long.parseLong(String.valueOf(totalObj));
            }
            // 导出条数超过指定条数，则通过临时表方式导出
            if (count > exportService.exportProperties().getTmpTableCount()) {
                String tblName = TableNames.RECEIVABLE_BILL;
                if (StringUtils.isNotBlank(supCpUnitId)) {
                    tblName = sharedBillAppService.getShareTableName(supCpUnitId, tblName);
                }
                exportService.createTmpTbl(wrapper, tblName, exportTaskId, ExportTmpTblTypeEnum.RECEIVABLE);

                // 深分页查询优化
                long tid = (query.getPageNum() - 1) * query.getPageSize();
                queryPage = exportService.queryReceivableBillByPageOnTempTbl(
                        Page.of(1, query.getPageSize(), false), tblName, exportTaskId, tid);
                queryPage.setTotal(count);
                return queryPage;
            }
        }
        Map<String, Object> specialMap = Optional.ofNullable(query.getConditions().getSpecialMap()).orElse(new HashMap<>());
        if (Objects.nonNull(specialMap.get("payerTypeIsNotDeveloper"))) {
            wrapper.and(e -> e.ne("b.payer_type", 99) .or().isNull("b.payer_type"));
        }
        // 原方式查询（非临时表方式）
        queryPage = baseMapper.queryBillByPage(Page.of(query.getPageNum(), query.getPageSize(), query.isCount()), wrapper);
        return queryPage;
    }

    public Integer queryBillCountByPage(PageF<SearchF<?>> query){
        List<Field> fields = query.getConditions().getFields();
        //分页获取应收账单，单独处理结转应收时同时获取收费对象为空或为该房号收费对象的账单
        QueryWrapper<?> wrapper = getWrapper(query);
        Boolean flag = Optional.ofNullable((Boolean) query.getConditions().getSpecialMap().get("getPayerIdIsNull")).orElse(false);
        List<Field> collect = fields.stream().filter(s -> "b.payer_id".equals(s.getName())).collect(Collectors.toList());
        List<Field> billInvalidField = fields.stream().filter(s -> "billInvalid".equals(s.getName())).collect(Collectors.toList());
        fields.removeAll(billInvalidField);
        if(CollectionUtils.isNotEmpty(collect) && flag){
            fields.removeAll(collect);
            wrapper.and(queryWrapper -> queryWrapper.eq("b.payer_id", collect.get(0).getValue()).or().isNull("b.payer_id"));
        }else if(CollectionUtils.isNotEmpty(billInvalidField) && (Integer) billInvalidField.get(0).getValue() == 1){
            invalidBillCondition(wrapper);
        }else{
            normalBillCondition(wrapper);
            wrapper.ne("b.overdue", DataDisabledEnum.禁用.getCode());
        }
        wrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        wrapper.isNull("b.bill_label");
        PageQueryUtils.validQueryContainsFieldAndValue(query, "b." + BillSharedingColumn.应收账单.getColumnName());
        return baseMapper.queryBillCountByPage(wrapper);
    }

    public List<ReceivableMaxEndTimeV> queryMaxEndTime(ReceivableMaxEndTimeBillF maxEndTimeBillF){
        return baseMapper.queryMaxEndTime(maxEndTimeBillF);
    }


    public List<ReceivableIntervalBillV> queryIntervalBill(ReceivableIntervalBillF query){
        return baseMapper.queryIntervalBill(query);
    }


    @Override
    public List<BillHandV> listBillHand(List<Long> billIds,String supCpUnitId) {
        if(StringUtils.isBlank(supCpUnitId)) {
            throw new RuntimeException("查询应收账单必须传入参数:上级收费单元 supCpUnitId !");
        }
        return baseMapper.listBillHand(billIds,supCpUnitId);
    }

    @Override
    public BillTotalDto queryBillReviewTotal(QueryWrapper<?> wrapper, String supCpUnitId) {
        wrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        return baseMapper.queryBillReviewTotal(wrapper);
    }

    @Override
    public Page<BillInferenceV> pageBillInferenceInfo(ListBillInferenceF form, List<Long> billIds) {
        SearchF<?> searchF = new SearchF<>();
        searchF.setFields(new ArrayList<>());
        QueryWrapper<?> queryModel = searchF.getQueryModel();
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
                    case 15:
                        queryModel.in(field.getName(), (List) field.getValue());
                        break;
                    case 6:
                        queryModel.le(field.getName(), field.getValue());
                        break;
                    case 4:
                        queryModel.ge(field.getName(), field.getValue());
                        break;
                    case 16:
                        queryModel.notIn(field.getName(), (List) field.getValue());
                        break;
                    default:
                        break;
                }
            }
        }
        queryModel.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
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
    public IPage<ReceivableBill> queryBillByPageNoTenantLine(PageF<SearchF<?>> queryF) {
        QueryWrapper<?> wrapper = getWrapper(queryF);
        wrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        PageQueryUtils.validQueryContainsFieldAndValue(queryF, "b." + BillSharedingColumn.应收账单.getColumnName());
        return baseMapper.queryBillByPageNoTenantLine(Page.of(queryF.getPageNum(), queryF.getPageSize()), wrapper);
    }

    @Override
    public IPage<ReceivableBill> listByInitialBill(Page<Object> page,QueryWrapper<?> queryWrapper) {
        queryWrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        return baseMapper.listByInitialBill(page, queryWrapper);
    }

    /**
     * 批量获取应收账单信息
     *
     * @param billIds
     * @return
     */
    public List<ReceivableBill> queryByIds(List<Long> billIds, String supCpUnitId) {
        QueryWrapper<ReceivableBill> wrapper = new QueryWrapper<>();
        wrapper.in("id", billIds);
        wrapper.eq("sup_cp_unit_id", supCpUnitId);
        return baseMapper.selectList(wrapper);
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
        queryWrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        if(StringUtils.isBlank(query.getSupCpUnitId())) {
            throw new RuntimeException("必须传入上级收费单元ID!");
        }
        queryWrapper.eq("b." + BillSharedingColumn.应收账单.getColumnName(), query.getSupCpUnitId());
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

    /**
     * 催缴欠缴账单统计
     *
     * @param form
     * @return
     */
    public BillTotalDto call(StatisticsBillAmountF form) {
        return baseMapper.call(this.callQueryWrapper(form));
    }

    private QueryWrapper<?> callQueryWrapper(StatisticsBillAmountF form){
        QueryWrapper<?> queryWrapper;
        //添加逻辑删除
        if (CollectionUtils.isNotEmpty(form.getBillIds())){
            queryWrapper = new QueryWrapper<>().in("b.id", form.getBillIds()).in("b.sup_cp_unit_id", form.getSupCpUnitIdList());
        }else {
            queryWrapper = form.getQuery().getConditions().getQueryModel();
        }
        queryWrapper.eq("b.deleted", DataDisabledEnum.启用.getCode()).eq("b.deleted", DataDisabledEnum.启用.getCode());
        queryWrapper.eq(Objects.nonNull(form.getBillType()),"b.bill_type", BillTypeEnum.应收账单.getCode());
        return queryWrapper;
    }


    /**
     *
     * @param form
     * @return
     */
    public List<BillTotalDto> callGroupByRoomAndItem(StatisticsBillAmountF form) {
        final QueryWrapper<?> queryWrapper = this.callQueryWrapper(form);
        queryWrapper.groupBy("b.room_id","b.charge_item_id");
        return baseMapper.callGroupByRoomAndItem(queryWrapper);
    }






    /**
     * 根据id查询应收账单(无租户隔离)
     *
     * @param billIdList 账单id
     * @return List
     */
    public List<ReceivableBill> listByIdsNotTenantId(List<Long> billIdList, String supCpUnitId) {
        return baseMapper.listByIdsNotTenantId(billIdList, supCpUnitId);
    }


    /**
     * 根据id查询应收账单(无租户隔离)
     *
     * @param billIdList 账单id
     * @return List
     */
    public List<ReceivableBill> listBillsByIdsNotTenantId(List<Long> billIdList, String supCpUnitId) {
        return baseMapper.listBillsByIdsNotTenantId(billIdList, supCpUnitId);
    }


    /**
     * 根据房号统计金额
     *
     * @param roomIdList 房号id
     * @return List
     */
    public List<RoomBillTotalDto> roomBills(List<Long> roomIdList, String supCpUnitId) {
        return baseMapper.roomBills(roomIdList,supCpUnitId);
    }

    /**
     * 根据房号和费项统计减免总额
     *
     * @param roomIdList 房号id集合
     * @param chargeItemIdList 费项id集合
     * @param currentYear 是否统计当年
     * @return List
     */
    public List<RoomBillTotalDto> roomChargeBills(List<Long> roomIdList, List<Long> chargeItemIdList, boolean currentYear, String supCpUnitId) {
        return baseMapper.roomChargeBills(roomIdList,chargeItemIdList,currentYear, supCpUnitId);
    }

    /**
     * 查询应收收费信息
     *
     * @param page 分页参数
     * @param queryWrapper 查询参数
     * @return ReceivableBillApplyDetailV
     */
    public Page<ReceivableRoomsDto> receivableRooms(Page<Object> page, QueryWrapper<?> queryWrapper) {
//        queryWrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        return baseMapper.receivableRooms(page, queryWrapper);
    }
    public Integer receivableRoomsCount(QueryWrapper<?> queryWrapper) {
        return baseMapper.receivableRoomsCount(queryWrapper);
    }

    public Page<ReceivableRoomsDto> queryCanAdvanceRooms(Page<Object> page, QueryWrapper<?> queryWrapper, QueryWrapper<?> queryWrapper1,String tenantId) {
        return baseMapper.queryCanAdvanceRooms(page, queryWrapper,queryWrapper1,tenantId);
    }


    /**
     * 查询房间应收账单列表
     *
     * @param state 账单状态
     * @param roomId 房号id
     * @param communityId 项目id
     * @param payState 缴费状态
     * @param targetObjIds 收费对象id
     * @param chargeItemIds 费项id
     * @return List
     */
    public List<ReceivableBill> getReceivableBills(Integer state, String roomId, String communityId, List<Integer> payState, List<String> targetObjIds, List<String> chargeItemIds) {
        QueryWrapper<ReceivableBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", state);
        queryWrapper.eq("room_id",roomId);
        queryWrapper.eq(StringUtils.isNotBlank(communityId),"community_id",communityId);
        queryWrapper.in(CollectionUtils.isNotEmpty(payState), "settle_state", payState);
        queryWrapper.in(CollectionUtils.isNotEmpty(targetObjIds), "payer_id", targetObjIds);
        queryWrapper.in(CollectionUtils.isNotEmpty(chargeItemIds), "charge_item_id", chargeItemIds);
        queryWrapper.eq("bill_type", BillTypeEnum.应收账单.getCode());
        return list(queryWrapper);
    }

    /**
     * 查询房间应收账单列表
     *
     * @param state 账单状态
     * @param roomId 房号id
     * @param communityId 项目id
     * @param payState 缴费状态
     * @param targetObjIds 收费对象id
     * @param chargeItemIds 费项id
     * @return List
     */
    public List<ReceivableBillsDto> receivableBills(Integer state, String roomId, String communityId,
                                                    List<Integer> payState, List<String> targetObjIds, List<String> chargeItemIds,List<String> roomIds) {
        return baseMapper.receivableBills(state,roomId,communityId,payState,targetObjIds,chargeItemIds,roomIds);
    }

    /**
     * 分页查询账单
     *
     * @param page 分页参数
     * @param queryWrapper 查询参数
     * @return Page
     */
    public IPage<ReceivableBill> history(Page<Object> page, QueryWrapper<?> queryWrapper) {
        queryWrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        return baseMapper.queryBillByPage(page, queryWrapper);
    }

    /**
     * 根据ID集合获取账单并根据字段排序
     *
     * @param billIds 账单id
     * @param field 排序字段
     * @param isAsc 是否降序
     * @return List
     */
    public List<ReceivableBill> queryByIdsOrderByCloum(ArrayList<String> billIds, List<String> field, Boolean isAsc, String supCpUnitId) {
        QueryWrapper<ReceivableBill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sup_cp_unit_id", supCpUnitId);
        queryWrapper.in("id", billIds);
        queryWrapper.orderBy(Objects.nonNull(isAsc),isAsc,field);
//        queryWrapper.eq("bill_type", BillTypeEnum.应收账单.getCode());
        return list(queryWrapper);
    }

    /**
     * 获取应收单导出数据
     *
     * @param queryWrapper 查询条件
     * @return List
     */
    public List<ReceivableBill> queryExportData(QueryWrapper<?> queryWrapper) {
        queryWrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        return baseMapper.queryExportData(queryWrapper);
    }

    /**
     * 更新交账状态
     * @param billId
     * @param handedState
     * @return
     */
    public boolean updateHandState(Long billId, BillAccountHandedStateEnum handedState, String supCpUnitId) {
        return update(new UpdateWrapper<ReceivableBill>().eq("id", billId).eq("sup_cp_unit_id", supCpUnitId).set("account_handed", handedState.getCode()));
    }

    /**
     * 根据账单编号查询账单
     * @param billNo
     * @return
     */
    public ReceivableBill queryByBillNo(String billNo) {
        return baseMapper.selectOne(new QueryWrapper<ReceivableBill>().eq("bill_no", billNo));
    }

    public Page<ReceivableBill> pageByWrapper(Page page, QueryWrapper<?> queryWrapper){
        return baseMapper.pageByWrapper(page, queryWrapper);
    }

    public List<VoucherBusinessBill> listByQuery(QueryWrapper<?> wrapper, Integer sceneType, String tableName) {
        return baseMapper.selectListByQuery(wrapper, VoucherBusinessBillTypeEnum.应收单.getCode(), sceneType,tableName);
    }

    public void receivableModifyInferenceStatus(List<Long> idList, String supCpUnitId) {
        baseMapper.updateInferenceState(idList, BillInferStateEnum.已推凭.getCode(), supCpUnitId);
    }

    /**
     * 查询减免过的账单信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    public List<VoucherBusinessBill> reducedListByQuery(QueryWrapper<?> wrapper, String tableName) {
        return baseMapper.reducedListByQuery(wrapper, VoucherBusinessBillTypeEnum.应收单.getCode(), VoucherEventTypeEnum.账单减免.getCode(),tableName);
    }



    /**
     * 应收调整账单的查询
     *
     * @param advanceWrapper
     * @param tableName
     * @return
     */
    public List<VoucherBusinessBill> listReceivableBillByQuery(QueryWrapper<?> advanceWrapper, boolean change, String tableName) {
        return baseMapper.receivableListByQuery(advanceWrapper,VoucherBusinessBillTypeEnum.账单调整.getCode(),change,tableName);
    }


    /**
     * 查询应收账单和临时应收业务单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    public List<VoucherBusinessBill> queryCancellationBillList(QueryWrapper<?> wrapper, String tableName) {
        return baseMapper.queryCancellationBillList(wrapper,VoucherEventTypeEnum.作废.getCode(),BillTypeEnum.应收账单.getCode(),tableName);
    }

    /**
     * 查询凭证应收业务账单开票单据信息
     * @return
     */
    public List<VoucherBusinessBill> listVoucherInvoiceReceiptBillByQuery(QueryWrapper<?> wrapper, String tableName) {
        return baseMapper.listVoucherInvoiceReceiptBillByQuery(wrapper,
                VoucherEventTypeEnum.账单开票.getCode(),
                VoucherBusinessBillTypeEnum.应收单.getCode(),tableName);
    }

    /**
     * 查询凭证业务应收单已退款单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    public List<VoucherBusinessBill> listVoucherReceivableRefundBillByQuery(QueryWrapper<?> wrapper, String tableName) {
        return baseMapper.listVoucherRefundBillByQuery(wrapper,
                VoucherEventTypeEnum.收入退款.getCode(),
                VoucherBusinessBillTypeEnum.应收单.getCode(),tableName);
    }

    /**
     * 查询凭证业务临时单已退款单据信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    public List<VoucherBusinessBill> listVoucherTemporaryRefundBillByQuery(QueryWrapper<?> wrapper, String tableName) {
        return baseMapper.listVoucherRefundBillByQuery(wrapper,
                VoucherEventTypeEnum.收入退款.getCode(),
                VoucherBusinessBillTypeEnum.临时单.getCode(), tableName);
    }

    /**
     * 查询结算过的临时单
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    public List<VoucherBusinessBill> tempBillListByQuery(QueryWrapper<?> wrapper, Integer sceneType, String tableName) {
        return baseMapper.tempBillListByQuery(wrapper, VoucherBusinessBillTypeEnum.临时单.getCode(), sceneType,tableName);
    }

    public void getBillCostType() {
        List<ReceivableBill> receivableBills = baseMapper.selectList(new QueryWrapper<ReceivableBill>()
                .eq("settle_state", 0)
                .eq("approved_state", 2)
                .isNull("bill_cost_type"));
        List<ReceivableBill> receivableBillsNew =Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(receivableBills)){
            for (ReceivableBill receivableBill : receivableBills) {
                isDate(receivableBill,receivableBillsNew);
            }
            List<Long> idList = receivableBillsNew.stream().map(ReceivableBill::getId).collect(Collectors.toList());
            baseMapper.updateBillCostType(idList,BillCostTypeEnum.当期应收.getCode());
        }
    }

    private void isDate(ReceivableBill receivableBill,List<ReceivableBill> receivableBillsNew) {
        if( null != receivableBill.getAccountDate()){
        int quarterNow = (LocalDate.now().getMonthValue() - 1) / 3 + 1;
        int quarterBill = (receivableBill.getAccountDate().getMonthValue() - 1) / 3 + 1;
            if (quarterBill == quarterNow) {
                receivableBillsNew.add(receivableBill);
            }
        }
    }

    public List<PushBusinessBill> listPushReceivableBillByQuery(QueryWrapper<?> wrapper) {
        List<PushBusinessBill> list= baseMapper.listPushReceivableBillByQuery(wrapper);
        return list;
    }

    public void updateInferenceStateByIds(List<Long> billIds, Integer inferenceState,List<String> communityIds) {

        update(new LambdaUpdateWrapper<ReceivableBill>().set(ReceivableBill::getInferenceState, inferenceState)
                .in(ReceivableBill::getId, billIds)
                .in(ReceivableBill::getCommunityId,communityIds)
                .in(ReceivableBill::getSupCpUnitId ,communityIds));
    }

    public List<PushBusinessBill> getReceivableCarryDownBillList(QueryWrapper<?> wrapper) {
        return baseMapper.getReceivableCarryDownBillList(wrapper);
    }

    /**
     * 收款结转 结转应收账单查询
     * @param wrapper
     * @return
     */
    public List<PushBusinessBill> getCollectionTransferReceivableBillList(QueryWrapper<?> wrapper) {
        return baseMapper.getCollectionTransferReceivableBillList(wrapper);
    }

    /**
     * 修改收款账户ID
     * @param sbAccountId
     * @param idList
     */
    public void updateSbAccountId(Long sbAccountId, List<Long> idList,  String supCpUnitId) {
        baseMapper.updateSbAccountId(sbAccountId, idList,supCpUnitId);
    }

    public List<PushBusinessBill> getCollectionTransferBillQuery(QueryWrapper<?> wrapper) {
        return baseMapper.getCollectionTransferBillQuery(wrapper);
    }

    public List<PushBusinessBill> arrearsProvisionBillList(QueryWrapper<?> wrapper) {
        return baseMapper.arrearsProvisionBillList(wrapper);

    }

    public List<ReceivableBill> getlistByIds(List<Long> targetBillIds, String supCpUnitId) {
        return baseMapper.selectList(new QueryWrapper<ReceivableBill>().eq("sup_cp_unit_id",supCpUnitId)
            .in("id",targetBillIds));
    }

    public List<PushBusinessBill> badBillConfirmBilList(QueryWrapper<?> wrappers) {
        return baseMapper.badBillConfirmBilList(wrappers);
    }

    public void updateBatchApprovedStateById(List<Long> receivableBillIds, int approvedState) {
        baseMapper.updateBatchApprovedStateById(receivableBillIds,approvedState);
    }

    public void updateProvisionVoucherPushingStatusById(List<Long> receivableBillIds,
                                                        String supCpUnitId,
                                                        int provisionVoucherPushingStatus) {
        baseMapper.updateProvisionVoucherPushingStatusById(receivableBillIds,supCpUnitId,provisionVoucherPushingStatus);
    }

    public void updateRelatedBillStatusOnPayCost(List<Long> receivableBillIds,
                                                 String supCpUnitId,
                                                 Integer status) {
        baseMapper.updateRelatedBillStatusOnPayCost(receivableBillIds,supCpUnitId,status);
    }

    public void updateRelatedBillStatusOnPayIncome(List<Long> receivableBillIds,
                                                 String supCpUnitId,
                                                 Integer status) {
        baseMapper.updateRelatedBillStatusOnPayIncome(receivableBillIds,supCpUnitId,status);
    }

    /**
     * 分页查询跳收记录
     *
     * @param page 分页参数
     * @param queryWrapper 查询参数
     * @return Page
     */
    public IPage<JumpRecordDto> jumpRecordPage(Page<Object> page, QueryWrapper<?> queryWrapper) {
        IPage<JumpRecordDto> jumpRecordDtoIPage = baseMapper.jumpRecordPage(page, queryWrapper);
        List<JumpRecordDto> records = jumpRecordDtoIPage.getRecords();
        if(CollectionUtils.isNotEmpty(records)){
            StringJoiner roomJoiner = new StringJoiner(",");
            StringJoiner itemJoiner = new StringJoiner(",");
            List<String> roomNameList = Lists.newArrayList();
            List<String> itemsList = Lists.newArrayList();
            for(JumpRecordDto rd : records){
                String bills = rd.getBills();
                String communityId = rd.getCommunityId();
                String[] split = bills.split(",");
                List<String> billIdList = Arrays.asList(split);
                LambdaQueryWrapper<ReceivableBill> wrapper = new LambdaQueryWrapper<ReceivableBill>()
                        .eq(Bill::getSupCpUnitId,communityId)
                        .eq(ReceivableBill::getBillType, BillTypeEnum.应收账单.getCode())
                        .in(Bill::getId,billIdList);
                List<ReceivableBill> billList = list(wrapper);
                Long jumpAmount = billList.stream()
                        .map(ReceivableBill::getActualUnpayAmount)
                        .reduce(Long::sum)
                        .orElse(0L);
                rd.setJumpAccount(jumpAmount);
                rd.setCommunityName(billList.get(0).getCommunityName());
                List<ReceivableBillDetailV> billDetailVList = Global.mapperFacade.mapAsList(billList,ReceivableBillDetailV.class);
                rd.setBillDetailVList(billDetailVList);
                for (ReceivableBill bill : billList) {
                    roomNameList.add(bill.getRoomName());
                    itemsList.add(bill.getChargeItemName());
                }
                roomNameList = roomNameList.stream().distinct().collect(Collectors.toList());
                itemsList = itemsList.stream().distinct().collect(Collectors.toList());
                for (String roomName : roomNameList) {
                    roomJoiner.add(roomName);
                }
                for (String itemName : itemsList) {
                    itemJoiner.add(itemName);
                }
                rd.setRoomNames(roomJoiner.toString());
                rd.setItemNames(itemJoiner.toString());
            }

        }
        return jumpRecordDtoIPage;
    }

    public ReceivableAndTemporaryBillTotalV queryTotalMoney(PageF<SearchF<?>> queryF) {
        return baseMapper.queryTotalMoney(RepositoryUtil.putLogicDeleted(queryF.getConditions().getQueryModel()));
    }

    public ReceivableAndTemporaryBillTotalV queryTotalMoney(TemporaryBillF temporaryBillF) {
        return baseMapper.queryTotalMoneyInfo(temporaryBillF.getBillIds(),temporaryBillF.getCommunityId()
                ,temporaryBillF.getCutTime(),temporaryBillF.getCondition());
    }

    /**
     * 查询对账账单信息
     * @param wrappers
     * @return
     */
    public List<PushBusinessBill> reconciliationVerificationBillList(QueryWrapper<?> wrappers) {
        return baseMapper.reconciliationVerificationBillList(wrappers);
    }

    public List<PushBusinessBill> getPaymentAdjustmentReversedBusinessBills(QueryWrapper<?> wrappers) {
        return baseMapper.getPaymentAdjustmentReversedBusinessBills(wrappers);
    }

    public List<PushBusinessBill> getPaymentAdjustmentInvalidBusinessBills(QueryWrapper<?> wrappers) {
        return baseMapper.getPaymentAdjustmentInvalidBusinessBills(wrappers);
    }

    public List<ReceivableBill> getByChargeNcId(String chargeNcId,String communityId) {
       return baseMapper.getByChargeNcId(chargeNcId,communityId);
    }

    public int batchUpdateGatherBillById(List<UpdateGatherBillF> updateGatherBillFList) {
        return baseMapper.batchUpdateGatherBillById(updateGatherBillFList);
    }

    public Boolean deleteBillById(String communityId,List<ReceivableBill> list) {
         baseMapper.deleteBillById(communityId,list);
        return true;
    }

    public List<PushBusinessBill> voidInvoiceReceivableBillByQuery(QueryWrapper<?> wrapper) {
        return baseMapper.voidInvoiceReceivableBillByQuery(wrapper);
    }

    public List<PushBusinessBill> getReceivableCarryDownTwoBillList(QueryWrapper<?> wrapper) {
        return baseMapper.getReceivableCarryDownTwoBillList(wrapper);
    }

    public List<PushBusinessBill> getVoucherBillDetail(QueryWrapper<?> wrapper) {
        return baseMapper.getVoucherBillDetail(wrapper);
    }

    public List<PushZJBusinessBill> revenueRecognitionBillList(QueryWrapper<?> wrappers) {
        return baseMapper.revenueRecognitionBillList(wrappers);
    }

    public List<PushZJBusinessBillForSettlement> revenueRecognitionBillListForSettlement(QueryWrapper<?> wrappers) {
        return baseMapper.revenueRecognitionBillListForSettlement(wrappers);
    }

    public List<PushZJBusinessBillForSettlement> revenueRecognitionBillListForSettlementOnReverse(QueryWrapper<?> wrappers) {
        return baseMapper.revenueRecognitionBillListForSettlementOnReverse(wrappers);
    }

    public List<PushZJBusinessBillForSettlement> revenueRecognitionBillListForPayIncome(QueryWrapper<?> wrappers) {
        return baseMapper.revenueRecognitionBillListForPayIncome(wrappers);
    }

    public List<PushZJBusinessBillForSettlement> revenueRecognitionBillListForPayIncomeOnReverse(QueryWrapper<?> wrappers) {
        return baseMapper.revenueRecognitionBillListForPayIncomeOnReverse(wrappers);
    }

    public List<PushZJBusinessBill> billAdjustBillList(QueryWrapper<?> wrapper){
        return baseMapper.billAdjustBillList(wrapper);
    }

    public List<PushZJBusinessBill> billAdjustBillListOnOldPayer(QueryWrapper<?> wrapper){
        return baseMapper.billAdjustBillListOnOldPayer(wrapper);
    }

    public List<PushZJBusinessBill> billAdjustBillListOnNewPayer(QueryWrapper<?> wrapper){
        return baseMapper.billAdjustBillListOnNewPayer(wrapper);
    }

    public List<PushZJBusinessBill> reverseBillList(QueryWrapper<?> wrapper){
        return baseMapper.reverseBillList(wrapper);
    }

    public IPage<ReceivableBill> queryPageApprove(PageF<SearchF<?>> queryF, QueryWrapper<?> wrapper) {
        wrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        PageQueryUtils.validQueryContainsFieldAndValue(queryF, "b." + BillSharedingColumn.应收账单.getColumnName());

        // 导出场合
        IPage<ReceivableBill> billPage;
        Object exportTaskIdObj = queryF.getConditions().getSpecialMap().get("exportTaskId");
        if (exportTaskIdObj != null) {
            long total = 0;
            Object totalObj = queryF.getConditions().getSpecialMap().get("total");
            Long exportTaskId = Long.parseLong(String.valueOf(exportTaskIdObj));
            if (totalObj == null) {
                total = baseMapper.countPageNotApprove(wrapper);
            } else {
                total = Long.parseLong(String.valueOf(totalObj));
            }
            // 导出条数超过指定条数，则通过临时表方式导出
            if (total > exportService.exportProperties().getTmpTableCount()) {
                String tblName = TableNames.RECEIVABLE_BILL;
                List<Field> fields = queryF.getConditions().getFields();
                List<Field> supCpUnitIds = fields.stream().filter(s -> "b.sup_cp_unit_id".equals(s.getName())).collect(Collectors.toList());
                if (supCpUnitIds != null && supCpUnitIds.size() > 0) {
                    tblName = sharedBillAppService.getShareTableName(supCpUnitIds.get(0).getValue().toString(), tblName);
                }
                exportService.createTmpTbl(wrapper, tblName, exportTaskId, ExportTmpTblTypeEnum.RECEIVABLE_APPROVE);

                // 深分页查询优化
                long tid = (queryF.getPageNum() - 1) * queryF.getPageSize();
                billPage = exportService.queryReceivableBillByPageOnTempTbl(
                        Page.of(1, queryF.getPageSize(), false), tblName, exportTaskId, tid);
                billPage.setTotal(total);
                return billPage;
            }
        }

        return baseMapper.getPageNotApprove(Page.of(queryF.getPageNum(),queryF.getPageSize()), wrapper);
    }

    public List<BillApproveTotalNewDto> queryApproveBillTotal(QueryWrapper<?> wrapper, String supCpUnitId) {
        String shareTableName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.RECEIVABLE_BILL);
        return baseMapper.queryApproveBillTotal(wrapper, shareTableName);
    }

    public List<BillApproveTotalNewDto> queryApproveUnionBillTotal(QueryWrapper<?> wrapper, String supCpUnitId) {
        String shareTableName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.RECEIVABLE_BILL);
        String shareTableName2 = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.BILL_APPROVE);
        return baseMapper.queryApproveUnionBillTotal(wrapper, shareTableName,shareTableName2);
    }

    public List<ReceivableBill> getBillOverdueDetail(List<Long> billIds,String supCpUnitId) {
        return baseMapper.getBillOverdueDetail(billIds, supCpUnitId);
    }

    public IPage<ReceivableBill> queryCommonBillByPageField(PageF<SearchF<?>> query){
        QueryWrapper<?> wrapper = SearchFieldSortUtils.sortField(query);
        if (FANG_YUAN.equals(EnvData.config)) {
            wrapper.orderByDesc("b.start_time").orderByDesc("b.end_time").orderByDesc("b.id");
        }else {
            wrapper.orderByAsc("b.start_time").orderByAsc("b.end_time").orderByAsc("b.id");
        }
        PageQueryUtils.validQueryContainsFieldAndValue(query, "b." + BillSharedingColumn.应收账单.getColumnName());
        // 原方式查询（非临时表方式）
        IPage<ReceivableBill> queryPage = baseMapper.queryBillByPage(Page.of(query.getPageNum(), query.getPageSize(), query.isCount()), wrapper);
        return queryPage;
    }


    public IPage<UnInvoiceReceivableBillDto> unInvoiceBillPage(Page<Object> page, QueryWrapper<?> queryWrapper) {
        return baseMapper.unInvoiceBillPage(page, queryWrapper);
    }

    public boolean updateReconcileState(List<Long> billId,  String supCpUnitId, Integer reconcileState) {
        return update(new UpdateWrapper<ReceivableBill>().in("id", billId).eq("sup_cp_unit_id", supCpUnitId).set("reconcile_state", reconcileState));
    }

    public Boolean updateSignByRoomIds(
            @NotNull List<String> roomIds,
            Integer sign,
            String tableName) {
        return baseMapper.updateSignByRoomIds(roomIds, sign, tableName);
    }

    public Integer getVoucherStatus(String billNo) {
        return baseMapper.getVoucherStatus(billNo);
    }


    /**
     * 根据roomIds 修改成本中心信息
     * @param roomIds
     * @param supCpUnitId
     * @param costCenterId
     * @param costCenterName
     */
    public void updateCostMsgByRoomIds(Integer billType,List<String> roomIds,String supCpUnitId,Long costCenterId,String costCenterName,String receivableBillName){
        baseMapper.updateCostMsgByRoomIds(billType,roomIds,supCpUnitId,costCenterId,costCenterName, receivableBillName);
    }


    /**
     * 根据项目+账单类型 修改成本中心
     *
     * @param billType           账单类型
     * @param supCpUnitId        项目id
     * @param costCenterId       成本中心id
     * @param costCenterName     成本中心名称
     * @param receivableBillName 分表表名
     */
    public void updateCostMsgBySupCpUnitId(Integer billType,String supCpUnitId,Long costCenterId,String costCenterName,String receivableBillName){
        baseMapper.updateCostMsgBySupCpUnitId(billType,supCpUnitId,costCenterId,costCenterName, receivableBillName);
    }


    public List<PushZJBusinessBillForSettlement> revenueRecognitionBillListForSQ(QueryWrapper<?> wrappers) {
        return baseMapper.revenueRecognitionBillListForSQ(wrappers);
    }

    public List<PushZJBusinessBillForSettlement> revenueRecognitionBillListForPaySettlement(QueryWrapper<?> wrappers) {
        return baseMapper.revenueRecognitionBillListForPaySettlement(wrappers);
    }

    public String getBusinessTypeCode(Long billId, String projectId) {
        return baseMapper.getBusinessTypeCode(billId, projectId);
    }

    public void updateCertainStatusOnVoucherBillApprovedV2(String communityId,
                                                           List<Long> billIdList,
                                                           Integer eventType,
                                                           Integer status) {
        baseMapper.updateCertainStatusOnVoucherBillApprovedV2(communityId, billIdList, eventType, status);
    }


    public List<PushZJBusinessBillForSettlement> queryMxBysettlementIdList(QueryWrapper<?> ew) {
        return baseMapper.queryMxBysettlementIdList(ew);
    }

    public void updatePayAppVoucherPushingStatusById(List<Long> receivableBillIds,
                                                     String supCpUnitId,
                                                     int provisionVoucherPushingStatus) {
        baseMapper.updatePayAppVoucherPushingStatusById(receivableBillIds,supCpUnitId,provisionVoucherPushingStatus);
    }
}
