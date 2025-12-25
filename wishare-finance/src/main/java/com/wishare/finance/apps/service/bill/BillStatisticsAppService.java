package com.wishare.finance.apps.service.bill;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apis.common.DeveloperPayFilterCondition;
import com.wishare.finance.apps.model.bill.fo.*;
import com.wishare.finance.apps.model.bill.vo.BillApproveStatisticsCountV;
import com.wishare.finance.apps.model.bill.vo.BillApproveTotalV;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.command.BillDiscountTotalQuery;
import com.wishare.finance.domains.bill.command.StatisticsBillTotalQuery;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.dto.*;
import com.wishare.finance.domains.bill.service.*;
import com.wishare.finance.domains.configure.arrears.service.ArrearsReasonDomainService;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.remote.enums.OperateTypeEnum;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 账单统计领域服务
 * @Author dxclay
 * @Date 2022/8/26
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillStatisticsAppService {

    private final BillStatisticsDomainService billStatisticsDomainService;
    private final SharedBillAppService sharedBillAppService;
    private final ReceivableBillDomainService receivableBillDomainService;
    private final TemporaryChargeBillDomainService temporaryChargeBillDomainService;
    private final AdvanceBillDomainService advanceBillDomainService;
    private final PayableBillDomainService payableBillDomainService;
    private final PayBillDomainService payBillDomainService;
    private final GatherBillDomainService gatherBillDomainService;
    private final ArrearsReasonDomainService arrearsReasonDomainService;

    /**
     * 根据检索条件统计账单金额总数
     * @param statisticsBillTotalF 统计入参
     * @return BillTotalV
     */
    public BillTotalDto bills(StatisticsBillTotalByConditionF statisticsBillTotalF) {
        StatisticsBillAmountF command = new StatisticsBillAmountF();
        command.setBillIds(statisticsBillTotalF.getBillIds());
        command.setBillState(statisticsBillTotalF.getBillState());
        command.setBillInvalid(statisticsBillTotalF.getBillInvalid());
        command.setQuery(statisticsBillTotalF.getQuery());
        command.setBillType(statisticsBillTotalF.getBillType());
        command.setSupCpUnitId(statisticsBillTotalF.getSupCpUnitId());
        PageF<SearchF<?>> query = command.getQuery();

        if(Objects.nonNull(command.getQuery())){
            List<Field> fields = query.getConditions().getFields();
            List<Field> billInvalidField = fields.stream().filter(s -> "billInvalid".equals(s.getName())).collect(Collectors.toList());
            List<Field> billTypeField = fields.stream().filter(s -> "billType".equals(s.getName())).collect(Collectors.toList());
            fields.removeAll(billTypeField);
            fields.removeAll(billInvalidField);

            if(CollectionUtils.isNotEmpty(billTypeField)){
                Integer value = (Integer)billTypeField.get(0).getValue();
                command.setBillType(value);
            }
            if(CollectionUtils.isNotEmpty(billInvalidField) && (Integer) billInvalidField.get(0).getValue() == 1){
                command.setBillInvalid(1);
            }else{
                if(Objects.isNull(command.getBillInvalid()) || command.getBillInvalid() == 0 ){
                    fields.add(new Field("b.state", BillStateEnum.正常.getCode(),1));
                    fields.add(new Field("b.reversed", BillReverseStateEnum.已冲销.getCode(),2));
                    fields.add(new Field("b.carried_state", BillCarryoverStateEnum.已结转.getCode(),2));
                    fields.add(new Field("b.refund_state", BillRefundStateEnum.已退款.getCode(),2));
                }
            }
        }
        return queryTotal(command);
    }

    /**
     * 统计减免合计金额
     * @param discountTotalF
     * @return
     */
    public BillDiscountTotalDto queryDiscountTotal(DiscountTotalF discountTotalF) {
        BillDiscountTotalDto billDiscountTotalDto = billStatisticsDomainService.queryDiscountTotal(Global.mapperFacade.map(discountTotalF, BillDiscountTotalQuery.class));
        return Objects.isNull(billDiscountTotalDto) ? new BillDiscountTotalDto() : billDiscountTotalDto;
    }

    /**
     * 根据检索条件统计账单金额总数
     * @param statisticsBillAmountF
     * @return
     */
    public BillTotalDto queryTotal(StatisticsBillAmountF statisticsBillAmountF) {
        BillTypeEnum billTypeEnum = BillTypeEnum.valueOfByCode(statisticsBillAmountF.getBillType());

        PageF<SearchF<?>> query = statisticsBillAmountF.getQuery();

        if(statisticsBillAmountF.getBillState() != null && statisticsBillAmountF.getBillState() == 0){
            //初始审核操作类型
            query.getConditions().getSpecialMap().put("operate_type",List.of(0));
        }else if(statisticsBillAmountF.getBillState() != null && statisticsBillAmountF.getBillState() == 1){
            //变更审核操作类型
            List<Field> operTypecontain = query.getConditions().getFields().stream().filter(s -> "ba.operate_type".equals(s.getName())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(operTypecontain)) {
                query.getConditions().getSpecialMap()
                        .put("operate_type", Arrays.asList(OperateTypeEnum.APPROVEING_TYPE));
            }
        }
        if(Objects.nonNull(query)) {
            //过滤只有应收和收款单才需要的参数
            query.getConditions().setFields(query.getConditions().getFields().stream().filter(v ->
                !(("b." + BillSharedingColumn.收款账单.getColumnName()).equals(v.getName()) || ("gd." + BillSharedingColumn.收款明细.getColumnName()).equals(v.getName()))).collect(
                Collectors.toList()));
        }

        switch (billTypeEnum){
            case 应收账单:
                if(StringUtils.isBlank(statisticsBillAmountF.getSupCpUnitId())) {
                    throw new IllegalArgumentException("请传入supCpUnitId!");
                }
                if (EnvConst.YUANYANG.equals(EnvData.config)) {
                    DeveloperPayFilterCondition.handleDeveloperPay(query);
                }
                if(statisticsBillAmountF.getBillState() != null){
                    query.getConditions().getFields().add(new Field("b." + BillSharedingColumn.应收账单.getColumnName(), statisticsBillAmountF.getSupCpUnitId(), 1));
                    return receivableBillDomainService.queryBillReviewTotal(query, statisticsBillAmountF.getBillIds(), statisticsBillAmountF.getSupCpUnitId());
                }else{
                    return receivableBillDomainService.queryTotal(new StatisticsBillTotalQuery(statisticsBillAmountF.getQuery(), statisticsBillAmountF.getBillIds(), statisticsBillAmountF.getBillInvalid(), statisticsBillAmountF.getBillRefund(),statisticsBillAmountF.getSupCpUnitId()));
                }
            case 预收账单:
                if(statisticsBillAmountF.getBillState() != null){
                    //变更审核统计
                    return advanceBillDomainService.queryBillReviewTotal(query, statisticsBillAmountF.getBillIds(), statisticsBillAmountF.getSupCpUnitId());
                }else{
                    return advanceBillDomainService.queryTotal(new StatisticsBillTotalQuery(statisticsBillAmountF.getQuery(), statisticsBillAmountF.getBillIds(), statisticsBillAmountF.getBillInvalid(), statisticsBillAmountF.getBillRefund(),statisticsBillAmountF.getSupCpUnitId()));
                }
            case 临时收费账单:
                if (EnvConst.YUANYANG.equals(EnvData.config)) {
                    DeveloperPayFilterCondition.handleDeveloperPay(query);
                }
                if(statisticsBillAmountF.getBillState() != null ){
                    //变更审核统计
                    query.getConditions().getFields().add(new Field("b." + BillSharedingColumn.应收账单.getColumnName(), statisticsBillAmountF.getSupCpUnitId(), 1));

                    return temporaryChargeBillDomainService.queryBillReviewTotal(query, statisticsBillAmountF.getBillIds(), statisticsBillAmountF.getSupCpUnitId());
                }else{
                    return temporaryChargeBillDomainService.queryTotal(new StatisticsBillTotalQuery(statisticsBillAmountF.getQuery(), statisticsBillAmountF.getBillIds(), statisticsBillAmountF.getBillInvalid(), statisticsBillAmountF.getBillRefund(),statisticsBillAmountF.getSupCpUnitId()));
                }
            case 应付账单:
                if(statisticsBillAmountF.getBillState() != null ){
                    //变更审核统计
                    return payableBillDomainService.queryBillReviewTotal(query, statisticsBillAmountF.getBillIds(), statisticsBillAmountF.getSupCpUnitId());
                }else{
                    return payableBillDomainService.queryTotal(new StatisticsBillTotalQuery(statisticsBillAmountF.getQuery(), statisticsBillAmountF.getBillIds(), statisticsBillAmountF.getBillInvalid(), statisticsBillAmountF.getBillRefund()));
                }
            case 付款单:
                query.getConditions().getFields().removeIf(a-> "b.room_id".equals(a.getName()));
                return payBillDomainService.queryTotal(new StatisticsBillTotalQuery(statisticsBillAmountF.getQuery(), statisticsBillAmountF.getBillIds(), statisticsBillAmountF.getBillInvalid(), statisticsBillAmountF.getBillRefund()));
            case 收款单:
                query.getConditions().getFields().removeIf(a-> "b.room_id".equals(a.getName()));
                return gatherBillDomainService.queryTotal(new StatisticsBillTotalQuery(statisticsBillAmountF.getQuery(), statisticsBillAmountF.getBillIds(), statisticsBillAmountF.getBillInvalid(), statisticsBillAmountF.getBillRefund(), statisticsBillAmountF.getSupCpUnitId()));
            default:
                throw BizException.throw400(ErrorMessage.BILL_TYPE_NOT_SUPPORT.msg());
        }
    }

    public BillTotalDto queryReceivableTotal(StatisticsBillAmountF statisticsBillAmountF) {
        BillTypeEnum billTypeEnum = BillTypeEnum.valueOfByCode(statisticsBillAmountF.getBillType());
        PageF<SearchF<?>> query = statisticsBillAmountF.getQuery();
        if(statisticsBillAmountF.getBillState() != null && statisticsBillAmountF.getBillState() == 0){
            //初始审核操作类型
            query.getConditions().getFields().add(new Field("b.approved_state", List.of(0,1), 15));
            query.getConditions().getFields().add(new Field("b.is_init", 1, 1));
        }else if(statisticsBillAmountF.getBillState() != null && statisticsBillAmountF.getBillState() == 1){
            //变更审核操作类型
            query.getConditions().getFields().add(new Field("b.approved_state", List.of(0,1), 15));
            query.getConditions().getFields().add(new Field("b.is_init", 0, 1));
        }

        switch (billTypeEnum){
            case 应收账单:
                if(StringUtils.isBlank(statisticsBillAmountF.getSupCpUnitId())) {
                    throw new IllegalArgumentException("请传入supCpUnitId!");
                }
                return receivableBillDomainService
                        .queryTotal(
                                new StatisticsBillTotalQuery(
                                        statisticsBillAmountF.getQuery(),
                                        statisticsBillAmountF.getBillIds(),
                                        statisticsBillAmountF.getBillInvalid(),
                                        statisticsBillAmountF.getBillRefund(),
                                        statisticsBillAmountF.getSupCpUnitId()
                                )
                        );

            case 临时收费账单:
                if(StringUtils.isBlank(statisticsBillAmountF.getSupCpUnitId())) {
                    throw new IllegalArgumentException("请传入supCpUnitId!");
                }
                return temporaryChargeBillDomainService.queryTotal(
                        new StatisticsBillTotalQuery(
                                statisticsBillAmountF.getQuery(),
                                statisticsBillAmountF.getBillIds(),
                                statisticsBillAmountF.getBillInvalid(),
                                statisticsBillAmountF.getBillRefund(),
                                statisticsBillAmountF.getSupCpUnitId()
                        )
                );
            default:
                throw BizException.throw400(ErrorMessage.BILL_TYPE_NOT_SUPPORT.msg());
        }
    }

    /**
     * 根据检索条件统计账单退款金额总数
     *
     * @param statisticsBillAmountF
     * @return
     */
    public BillTotalDto queryRefundTotal(StatisticsBillAmountF statisticsBillAmountF) {
        BillTypeEnum billTypeEnum = BillTypeEnum.valueOfByCode(statisticsBillAmountF.getBillType());
        switch (billTypeEnum){
            case 应收账单:
                return receivableBillDomainService.statisticsBillRefund(new StatisticsBillTotalQuery(statisticsBillAmountF.getQuery()
                    , statisticsBillAmountF.getBillIds(), statisticsBillAmountF.getBillInvalid()
                    , statisticsBillAmountF.getBillRefund(), statisticsBillAmountF.getSupCpUnitId()));
            case 预收账单:
                return advanceBillDomainService.statisticsBillRefund(new StatisticsBillTotalQuery(statisticsBillAmountF.getQuery()
                    , statisticsBillAmountF.getBillIds(), statisticsBillAmountF.getBillInvalid(), statisticsBillAmountF.getBillRefund()
                    , statisticsBillAmountF.getSupCpUnitId()));
            case 临时收费账单:
                return temporaryChargeBillDomainService.statisticsBillRefund(new StatisticsBillTotalQuery(statisticsBillAmountF.getQuery()
                    , statisticsBillAmountF.getBillIds(), statisticsBillAmountF.getBillInvalid(), statisticsBillAmountF.getBillRefund()
                    , statisticsBillAmountF.getSupCpUnitId()));
            default:
                throw BizException.throw400(ErrorMessage.BILL_TYPE_NOT_SUPPORT.msg());
        }
    }

    /**
     * 根据状态查询账单数合计
     *
     * @param statisticsBillTotalF 账单统计入参
     * @return List
     */
    public List<BillApproveTotalV> queryApproveTotal(StatisticsBillTotalF statisticsBillTotalF) {
        PageF<SearchF<?>> query = statisticsBillTotalF.getQuery();
        List<BillApproveTotalDto> billApproveTotalDtoS = billStatisticsDomainService.queryApproveTotal(BillApproveStateEnum.valueOfByCodes(statisticsBillTotalF.getApproveState()) , query);
        List<BillApproveTotalV> approveTotalVS = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(billApproveTotalDtoS)) {
            Map<String, List<BillApproveTotalDto>> approveMap = billApproveTotalDtoS.stream().collect(Collectors.groupingBy(item -> item.getApprovedState() + "&" + item.getOperateType()));
            approveTotalVS = approveMap.values().stream().map(list -> {
                BillApproveTotalV billApproveTotalV = new BillApproveTotalV();
                long total = 0;
                for (BillApproveTotalDto approveTotalDto : list) {
                    total += approveTotalDto.getTotal();
                    billApproveTotalV.setApproveState(approveTotalDto.getApprovedState());
                    billApproveTotalV.setOperateType(approveTotalDto.getOperateType());
                    if (BillTypeEnum.应收账单.equalsByCode(approveTotalDto.getType())) {
                        billApproveTotalV.setReceivableTotal(approveTotalDto.getTotal());
                    } else if (BillTypeEnum.预收账单.equalsByCode(approveTotalDto.getType())) {
                        billApproveTotalV.setAdvanceTotal(approveTotalDto.getTotal());
                    } else if (BillTypeEnum.临时收费账单.equalsByCode(approveTotalDto.getType())) {
                        billApproveTotalV.setTemporaryChargeTotal(approveTotalDto.getTotal());
                    } else if (BillTypeEnum.应付账单.equalsByCode(approveTotalDto.getType())) {
                        billApproveTotalV.setPaymentTotal(approveTotalDto.getTotal());
                    } else if (BillTypeEnum.退款账单.equalsByCode(approveTotalDto.getType())) {
                        billApproveTotalV.setRefundTotal(approveTotalDto.getTotal());
                    }
                }
                billApproveTotalV.setTotal(total);
                return billApproveTotalV;
            }).collect(Collectors.toList());
        }
        return approveTotalVS;
    }

    public BillApproveStatisticsCountV queryApproveTotalNew(StatisticsBillTotalF statisticsBillTotalF){
        PageF<SearchF<?>> query = statisticsBillTotalF.getQuery();
        QueryWrapper<?> wrapper;
        if(Objects.nonNull(query)){
            wrapper = query.getConditions().getQueryModel();
        }else{
            wrapper = new QueryWrapper<>();
        }
        BillApproveStatisticsCountV result = new BillApproveStatisticsCountV();
        List<BillApproveTotalNewDto> billApproveTotalNews =
                billStatisticsDomainService.queryReceivableBillApproveTotal(statisticsBillTotalF.getApproveState(),
                        wrapper,
                        statisticsBillTotalF.getSupCpUnitId());
        result.dealReceivableBillApproveTotal(billApproveTotalNews,0);
        List<BillApproveTotalNewDto> billApproveUnionTotalNews =
                billStatisticsDomainService.queryReceivableBillUnionApproveTotal(statisticsBillTotalF.getApproveState(),
                        wrapper,
                        statisticsBillTotalF.getSupCpUnitId());
        result.dealReceivableBillApproveTotal(billApproveUnionTotalNews,1);

        List<BillApproveTotalDto> advanceBillApproveTotals =
                billStatisticsDomainService.queryAdvanceApproveTotal(
                        statisticsBillTotalF.getApproveState(),
                        wrapper, statisticsBillTotalF.getSupCpUnitId());
        result.dealAdvanceTotal(advanceBillApproveTotals);
        return result;
    }




    /**
     * 催缴欠缴账单统计
     *
     * @param form
     * @return
     */
    public BillTotalDto call(StatisticsBillAmountF form) {
        return receivableBillDomainService.call(form);
    }

    /**
     * 催缴欠缴账单统计
     *
     * @param form
     * @return
     */
    public List<BillTotalDto> callGroupByRoomAndItem(StatisticsBillAmountF form) {
        return receivableBillDomainService.callGroupByRoomAndItem(form);
    }




    /**
     * 根据房号统计账单
     *
     * @param form 入参
     * @return List
     */
    public List<RoomBillTotalDto> roomBills(StatisticsRoomBillAmountF form) {
        BillTypeEnum billTypeEnum = BillTypeEnum.valueOfByCode(form.getBillType());
        switch (billTypeEnum){
            case 应收账单:
                List<RoomBillTotalDto> receivableRoomBillTotalDtoList = receivableBillDomainService.roomBills(form.getRoomIdList(), form.getSupCpUnitId());
                List<RoomBillTotalDto> receivableChargeItemBillTotalDtoList = receivableBillDomainService.roomChargeBills(form.getRoomIdList(),form.getChargeItemIdList(),true, form.getSupCpUnitId());
                setCurrentYearChargeItemDeductAmount(receivableRoomBillTotalDtoList, receivableChargeItemBillTotalDtoList);
                return receivableRoomBillTotalDtoList;
            case 预收账单:
                List<RoomBillTotalDto> advanceRoomBillTotalDtoList = advanceBillDomainService.roomBills(form.getRoomIdList());
                List<RoomBillTotalDto> advanceChargeItemBillTotalDtoList = advanceBillDomainService.roomChargeBills(form.getRoomIdList(),form.getChargeItemIdList(),true);
                setCurrentYearChargeItemDeductAmount(advanceRoomBillTotalDtoList, advanceChargeItemBillTotalDtoList);
                return advanceRoomBillTotalDtoList;
            case 临时收费账单:
                List<RoomBillTotalDto> temporaryChargeRoomBillTotalDtoList = temporaryChargeBillDomainService.roomBills(form.getRoomIdList());
                List<RoomBillTotalDto> temporaryChargeChargeItemBillTotalDtoList = temporaryChargeBillDomainService.roomChargeBills(form.getRoomIdList(),form.getChargeItemIdList(),true);
                setCurrentYearChargeItemDeductAmount(temporaryChargeRoomBillTotalDtoList, temporaryChargeChargeItemBillTotalDtoList);
                return temporaryChargeRoomBillTotalDtoList;
            default:
                throw BizException.throw400(ErrorMessage.BILL_TYPE_NOT_SUPPORT.msg());
        }
    }

    /**
     * 设置今年所有费项减免总额
     */
    private void setCurrentYearChargeItemDeductAmount(List<RoomBillTotalDto> roomBillTotalDtoList, List<RoomBillTotalDto> chargeItemBillTotalDtoList) {
        Map<Long, List<RoomBillTotalDto>> collect = chargeItemBillTotalDtoList.stream().collect(Collectors.groupingBy(RoomBillTotalDto::getRoomId));
        for (RoomBillTotalDto roomBillTotalDto : roomBillTotalDtoList) {
            List<RoomBillTotalDto> roomBillTotalList = collect.get(roomBillTotalDto.getRoomId());
            if (CollectionUtils.isNotEmpty(roomBillTotalList)) {
                roomBillTotalDto.setChargeItemDeductAmountTotal(roomBillTotalList.get(0).getChargeItemDeductAmountTotal());
            } else {
                roomBillTotalDto.setChargeItemDeductAmountTotal(0L);
            }
        }
    }

    public ReasonBillTotalDto batchReasonBillTotal(PageF<SearchF<?>> queryF) {
        return arrearsReasonDomainService.batchReasonBillTotal(queryF);
    }
}
