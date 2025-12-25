package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.fo.FinanceSearchF;
import com.wishare.finance.apps.model.bill.fo.ListBillInferenceF;
import com.wishare.finance.apps.model.bill.vo.BillHandV;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.command.BillDiscountTotalQuery;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.dto.BillApproveTotalDto;
import com.wishare.finance.domains.bill.dto.BillDiscountTotalDto;
import com.wishare.finance.domains.bill.dto.BillTotalDto;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBillRuleConditionOBV;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.PushZJBusinessBillForSettlement;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.enums.YesNoEnum;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 账单资源库接口
 *
 * @Author dxclay
 * @Date 2022/9/21
 * @Version 1.0
 */
public interface BillRepository<B extends Bill> extends IService<B> {

    final String FANG_YUAN = "fangyuan";

    /**
     * 申请审核
     * @param bill
     * @return
     */
    default boolean apply(B bill){
        LambdaUpdateWrapper<B> billWrapper = new LambdaUpdateWrapper<>();
        billWrapper.eq(B::getId, bill.getId());
        billWrapper.eq(B::getSupCpUnitId, bill.getSupCpUnitId());
        billWrapper.set(B::getApprovedState, bill.getApprovedState());
        return update(billWrapper);
    }

    default boolean batchFreezeBillByIds(List<B> billList, Integer freezeType, String supCpUnitId) {
        for (B b : billList) {
            update(
                    b,
                    new UpdateWrapper<B>()
                            .eq("id",b.getId())
                            .eq("sup_cp_unit_id",supCpUnitId));
        }
        return true;
    }

    default boolean batchUnFreezeBillByIds(List<B> billList, String supCpUnitId) {
        for (B b : billList) {
            b.unfreeze();
            update(b,new UpdateWrapper<B>()
                    .eq("id",b.getId())
                    .eq(StringUtils.isNotBlank(supCpUnitId),"sup_cp_unit_id",supCpUnitId));
        }
        return true;
    }

    /**
     * 根据检索条件查询账单列表
     * @param query
     * @return
     */
    default List<B> listByPageSearch(PageF<SearchF<?>> query, Integer billType) {
        QueryWrapper queryWrapper = getWrapperNoneAlias(query);
        if(BillTypeEnum.应收账单.equalsByCode(billType)){
            queryWrapper.eq("bill_type", BillTypeEnum.应收账单.getCode());
        }else if(BillTypeEnum.临时收费账单.equalsByCode(billType)){
            queryWrapper.eq("bill_type", BillTypeEnum.临时收费账单.getCode());
        }
        return list(queryWrapper);
    }

    /**
     * 根据检索条件获取指定查询的类
     * @param wrapper
     * @param tClass
     * @return
     * @param <T>
     */
    default <T> List<T> listToObjs(QueryWrapper<B> wrapper, Class<T> tClass){
        List<Object> objects = listObjs(wrapper);
        return CollectionUtils.isNotEmpty(objects) ? Global.mapperFacade.mapAsList(objects, tClass) : new ArrayList<>();
    }

    /**
     * 获取 wrapper
     * @param query
     * @return
     */
    default QueryWrapper getWrapper(PageF<SearchF<?>> query){
        List<Field> fields = Optional.ofNullable(query.getConditions().getFields()).orElse(new ArrayList<>());
        List<Field> collect = fields.stream().filter(s -> "b.payer_id".equals(s.getName())).collect(Collectors.toList());
        Object getPayerIdIsNullValue = query.getConditions().getSpecialMap().get("getPayerIdIsNull");
        boolean flag = !Objects.isNull(getPayerIdIsNullValue) && (boolean) getPayerIdIsNullValue;
        if(CollectionUtils.isNotEmpty(collect) && flag){
            fields.removeAll(collect);
        }
//        List<Field> notInField = fields.stream().filter(e -> 16 == e.getMethod()).collect(Collectors.toList());
//        fields.removeAll(notInField);
        FinanceSearchF<?> financeSearchF = new FinanceSearchF<>();
        financeSearchF.setFields(fields);
        QueryWrapper<?> queryWrapper = financeSearchF.getQueryModel().eq("b.deleted", DataDisabledEnum.启用.getCode());
//        for (Field field : notInField) {
//            queryWrapper.notIn(field.getName(), (List)field.getValue());
//        }
        if (CollectionUtils.isEmpty(query.getOrderBy())){
            queryWrapper.orderByDesc("b.gmt_create").orderByAsc("b.id");
        }else {
            for (OrderBy item: query.getOrderBy()) {
                if (item.isAsc()) {
                    queryWrapper.orderByAsc(item.getField());
                }else {
                    queryWrapper.orderByDesc(item.getField());
                }
            }
        }
        Map<String, Object> specialMap = Optional.ofNullable(query.getConditions().getSpecialMap()).orElse(new HashMap<>());
        if (Objects.nonNull(specialMap.get("payerTypeIsNotDeveloper"))) {
            queryWrapper.and(wrapper -> wrapper.ne("b.payer_type", 99) .or().isNull("b.payer_type"));
        }
        if(CollectionUtils.isNotEmpty(collect) && Objects.nonNull(collect.get(0).getValue()) && flag){
            queryWrapper.and(wrapper -> FinanceSearchF.createQueryWrapperF(collect.get(0),wrapper)
                    .or()
                    .eq("b.payer_id", StringUtils.EMPTY)
                    .or()
                    .isNull("b.payer_id")
            );
        }else if (flag) {
            queryWrapper.isNull("b.payer_id");
        }
        return queryWrapper;
    }
    /**
     *  临时，预收wrapper
     * @param query
     * @return
     */
    default QueryWrapper getWrappers(PageF<SearchF<?>> query){
        List<Field> fields = new ArrayList<>();
        if (Objects.nonNull(query)) {
            fields = Optional.ofNullable(query.getConditions().getFields()).orElse(new ArrayList<>());
        }
        List<Field> collect = fields.stream().filter(s -> "b.payer_id".equals(s.getName())).collect(Collectors.toList());
        List<Field> notInField = fields.stream().filter(e -> 16 == e.getMethod()).collect(Collectors.toList());
        fields.removeAll(notInField);
        Optional<Field> lockedQuery = fields.stream().filter(s -> "locked".equals(s.getName())).findFirst();
        lockedQuery.ifPresent(field -> query.getConditions().getFields().remove(field));
        Object getPayerIdIsNullValue = query.getConditions().getSpecialMap().get("getPayerIdIsNull");
        boolean flag = !Objects.isNull(getPayerIdIsNullValue) && (boolean) getPayerIdIsNullValue;
        if(CollectionUtils.isNotEmpty(collect) && flag){
            fields.removeAll(collect);
        }
        QueryWrapper<?> queryWrapper = query.getConditions().getQueryModel().eq("b.deleted", DataDisabledEnum.启用.getCode());
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
        for (Field field : notInField) {
            queryWrapper.notIn(field.getName(), (List)field.getValue());
        }
        if (CollectionUtils.isEmpty(query.getOrderBy())){
            if (FANG_YUAN.equals(EnvData.config)) {
                queryWrapper.orderByDesc("b.start_time").orderByDesc("b.end_time").orderByDesc("b.id");
            }else {
                queryWrapper.orderByAsc("b.start_time").orderByAsc("b.end_time").orderByAsc("b.id");
            }
        }else {
            for (OrderBy item: query.getOrderBy()) {
                if (item.isAsc()) {
                    queryWrapper.orderByAsc(item.getField());
                }else {
                    queryWrapper.orderByDesc(item.getField());
                }
            }
        }
        if(CollectionUtils.isNotEmpty(collect) && Objects.nonNull(collect.get(0).getValue()) && flag){
            queryWrapper.and(wrapper -> FinanceSearchF.createQueryWrapperF(collect.get(0),wrapper)
                    .or()
                    .eq("b.payer_id", StringUtils.EMPTY)
                    .or()
                    .isNull("b.payer_id")
            );
        }else if (flag) {
            queryWrapper.isNull("b.payer_id");
        }
        return queryWrapper;
    }

	default String getSupCpUnitId(PageF<SearchF<?>> query){
        String supCpUnitId = "";
        List<Field> collect = query.getConditions().getFields().stream().filter(field -> "b.sup_cp_unit_id".equals(field.getName())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(collect)){
            return collect.get(0).getValue().toString();
        }
        return supCpUnitId;
    }


    default List<B> listByIdsQuery(PageF<SearchF<?>> query,List<?> ids) {
        QueryWrapper<B> queryWrapper = new QueryWrapper<>();
        String supCpUnitId = "";
        List<Field> collect = query.getConditions().getFields().stream().filter(field -> "b.sup_cp_unit_id".equals(field.getName())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(collect)){
            Object value = collect.get(0).getValue();
            if(value instanceof List){
                List list = (List) value;
                queryWrapper.in("sup_cp_unit_id",list);
            }else {
                supCpUnitId = collect.get(0).getValue().toString();
                queryWrapper.eq("sup_cp_unit_id",supCpUnitId);
            }

        }
        queryWrapper.in("id",ids);
        return list(queryWrapper);
    }

    /**
     * 获取 无效账单 wrapper
     * @param query query
     * @return QueryWrapper
     */
    default QueryWrapper<?> getInvalidBillWrapper(PageF<SearchF<?>> query){
        QueryWrapper<?> wrapper = getWrapper(query);
        //拼接无效账单条件
        wrapper.and(queryWrapper -> queryWrapper.eq("b.state", BillStateEnum.作废.getCode())
                .or().eq("b.reversed", BillReverseStateEnum.已冲销.getCode())
                .or().eq("b.carried_state", BillCarriedStateEnum.已结转.getCode())
                .or().eq("b.refund_state", BillRefundStateEnum.已退款.getCode()));
        wrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        return wrapper;
    }

    /**
     * 无效账单条件
     */
    default void invalidBillCondition(QueryWrapper<?> queryWrapper) {
        queryWrapper.and(wrapper -> wrapper.eq("b.state", BillStateEnum.作废.getCode())
                .or().eq("b.reversed", BillReverseStateEnum.已冲销.getCode())
                .or().eq("b.carried_state", BillCarriedStateEnum.已结转.getCode())
                .or().eq("b.refund_state", BillRefundStateEnum.已退款.getCode()));
    }

    /**
     * 正常账单条件
     */
    default void normalBillCondition(QueryWrapper<?> queryWrapper) {
        queryWrapper
                .ne("b.state", BillStateEnum.作废.getCode())
                .ne("b.reversed", BillReverseStateEnum.已冲销.getCode())
                .ne("b.carried_state", BillCarriedStateEnum.已结转.getCode())
                .ne("b.refund_state", BillRefundStateEnum.已退款.getCode())
                .ne("b.settle_state", BillSettleStateEnum.结算中.getCode())
                .isNull("b.bill_label");
    }

    default void normalFieldBillCondition(PageF<SearchF<?>> query){
        List<Field> fields = Optional.ofNullable(query.getConditions().getFields()).orElse(new ArrayList<>());
        Set<String> fieldName = fields.stream().map(Field::getName).collect(Collectors.toSet());
        if (!fieldName.contains("b.state")) {
            Field stateField = new Field("b.state", BillStateEnum.作废.getCode(),2);
            fields.add(stateField);
        }
        if (!fieldName.contains("b.reversed")) {
            Field reversedField = new Field("b.reversed", BillReverseStateEnum.未冲销.getCode(),1);
            fields.add(reversedField);
        }
        if (!fieldName.contains("b.carried_state")) {
            Field carriedStateField = new Field("b.carried_state", BillCarriedStateEnum.已结转.getCode(),2);
            fields.add(carriedStateField);
        }
        if (!fieldName.contains("b.refund_state")) {
            Field refundStateField = new Field("b.refund_state", BillRefundStateEnum.已退款.getCode(),2);
            fields.add(refundStateField);
        }
        if (!fieldName.contains("b.settle_state")) {
            Field settleStateField = new Field("b.settle_state", BillSettleStateEnum.结算中.getCode(),2);
            fields.add(settleStateField);
        }
        if (!fieldName.contains("b.bill_label")) {
            Field billLabelField = new Field("b.bill_label", 1 , 11);
            fields.add(billLabelField);
        }
        if (!fieldName.contains("b.overdue")) {
            Field fieldOverdue = new Field("b.overdue", DataDisabledEnum.启用.getCode() , 1);
            fields.add(fieldOverdue);
        }
    }

    /**
     * 正常账单条件
     */
    default void normalCommonBillCondition(QueryWrapper<?> queryWrapper) {
        queryWrapper
                .ne("b.state", BillStateEnum.作废.getCode())
                .ne("b.reversed", BillReverseStateEnum.已冲销.getCode())
                .ne("b.carried_state", BillCarriedStateEnum.已结转.getCode())
                .ne("b.refund_state", BillRefundStateEnum.已退款.getCode())
                .isNull("b.bill_label");
    }

    /**
     * 正常账单条件
     */
    default void payAndGatherBillNormalConditions(QueryWrapper<?> queryWrapper) {
        queryWrapper
                .ne("b.state", BillStateEnum.作废.getCode())
                .ne("b.reversed", BillReverseStateEnum.已冲销.getCode())
                // .ne("b.carried_state", BillCarriedStateEnum.已结转.getCode())
                .ne("b.refund_state", BillRefundStateEnum.已退款.getCode())
                .eq("b.approved_state",BillApproveStateEnum.已审核.getCode());
    }

    /**
     * 获取 wrapper (无别名)
     * @param query
     * @return
     */
    default QueryWrapper<?> getWrapperNoneAlias(PageF<SearchF<?>> query){
        QueryWrapper<?> queryWrapper = query.getConditions().getQueryModel().eq("deleted", DataDisabledEnum.启用.getCode());
        if (CollectionUtils.isEmpty(query.getOrderBy())){
            queryWrapper.orderByDesc("gmt_create").orderByAsc("id");
        }
        return queryWrapper;
    }

    /**
     * 分页查询审核账单
     * @param query
     * @return
     */
    default IPage<B> getPageWithApprove(PageF<SearchF<?>> query){
        return new Page<>(query.getPageNum(), query.getPageSize());
    }


    /**
     * 根据账单ids批量获取账单信息（忽略租户隔离）
     * @param billIds
     * @return
     */
    List<B> getByIdsNoTenant(List<Long> billIds,String supCpUnitId);

    Boolean updateInvoiceState(List<B> updateBills);

    /**
     * 账单的数据统计（暂时方法，后期优化）
     * @param query
     * @return
     */
    BillTotalDto queryTotal(PageF<SearchF<?>> query, List<Long> billIds, Integer billInvalid,Integer billRefund,String supCpUnitId);

    /**
     * 根据审核状态查询账单合计
     * @return
     */
    List<BillApproveTotalDto> queryApproveTotal(QueryWrapper<?> wrapper, String supCpUnitId);

    /**
     * 统计账单合计查询
     * @param query
     * @return
     */
    BillDiscountTotalDto queryDiscountTotal(BillDiscountTotalQuery query);

    /**
     * 获取查询的wrapper
     * @param query
     * @param billIds
     * @return
     */
    default QueryWrapper<?> getQueryAndIdsWrapper(PageF<SearchF<?>> query, List<Long> billIds, Integer billInvalid,Integer billRefund){
        QueryWrapper<?> wrapper;
        //添加逻辑删除
        if (CollectionUtils.isNotEmpty(billIds)){
            wrapper = new QueryWrapper<>().in("b.id", billIds);
        }else {
            wrapper = query.getConditions().getQueryModel();
        }
        //无效账单条件
        if(Objects.nonNull(billInvalid) && billInvalid == 1){
            invalidBillCondition(wrapper);
        }else{
            normalCommonBillCondition(wrapper);
        }
        if (Objects.nonNull(billRefund) && billRefund == 1) {
            wrapper.and(queryWrapper -> queryWrapper.in("b.refund_state", Lists.newArrayList(BillRefundStateEnum.已退款.getCode(),BillRefundStateEnum.部分退款.getCode())));
        }
        wrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        return wrapper;
    }

    /**
     * 分页查询账单
     * 【收款账单-临时账单】TemporaryChargeBillRepository.queryBillByPage
     * 【收费工作台】ReceivableAndTemporaryBillRepository.queryBillByPage
     * @param query query
     * @return IPage
     */
    IPage<B> queryBillByPage(PageF<SearchF<?>> query);

    /**
     * 根据条件获取初始审核账单
     *
     * @param queryWrapper queryWrapper
     * @return List
     */
    IPage<B> listByInitialBill(Page<Object> page, QueryWrapper<?> queryWrapper);

    /**
     * 查询获取
     * @param billIds
     * @return
     */
    List<BillHandV> listBillHand(List<Long> billIds,String supCpUnitId);

    /**
     * 获取账单审核统计
     *
     * @param wrapper wrapper
     * @return BillTotalDto
     */
    BillTotalDto queryBillReviewTotal(QueryWrapper<?> wrapper, String supCpUnitId);

    /**
     * 批量获取
     * @param form
     * @param billIds
     * @return
     */
    Page<BillInferenceV> pageBillInferenceInfo(ListBillInferenceF form, List<Long> billIds);

    /**
     * 分页查询 （去除租户隔离）
     * @param queryF
     * @return
     */
    IPage<B> queryBillByPageNoTenantLine(PageF<SearchF<?>> queryF);

}
