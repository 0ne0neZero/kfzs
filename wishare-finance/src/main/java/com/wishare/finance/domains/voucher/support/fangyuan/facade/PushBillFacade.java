package com.wishare.finance.domains.voucher.support.fangyuan.facade;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.repository.AdvanceBillRepository;
import com.wishare.finance.domains.bill.repository.BillCarryoverRepository;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptDetailRepository;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimDetailE;
import com.wishare.finance.domains.reconciliation.entity.FlowDetailE;
import com.wishare.finance.domains.reconciliation.enums.ReconcileResultEnum;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimDetailRepository;
import com.wishare.finance.domains.reconciliation.repository.FlowDetailRepository;
import com.wishare.finance.domains.voucher.consts.enums.VoucherBillCustomerTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBillRuleConditionOBV;
import com.wishare.finance.domains.voucher.consts.enums.InferenceStateEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.TriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.PushBusinessBill;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.VoucherBillRuleConditionUtils;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.PushBillStateEnum;
import com.wishare.finance.infrastructure.conts.CarryoverConst;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.utils.WrapperUtils;
import com.wishare.starter.Global;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PushBillFacade {

    private final ReceivableBillRepository receivableBillRepository;
    private final AdvanceBillRepository advanceBillRepository;
    private final GatherDetailRepository gatherDetailRepository;
    private final BillCarryoverRepository billCarryoverRepository;
    private final FlowClaimDetailRepository flowClaimDetailRepository;
    private final FlowDetailRepository flowDetailRepository;
    private final InvoiceReceiptDetailRepository invoiceReceiptDetailRepository;



    public List<PushBusinessBill> getReceivableBillList(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        List<PushBusinessBill> businessBills = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(communityIds)) {
            communityIds.forEach(communityId -> {
                QueryWrapper<?> wrapper = VoucherBillRuleConditionUtils.parseConditionToQuery(conditions);
                wrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
//                        .eq("b.bill_type", BillTypeEnum.应收账单.getCode()) //FYWY-954 加上临时账单类型
                        //.eq("inference_state", InferenceStateEnum.未推凭.getCode())
                        .eq("b.invoice_state", InvoiceStateEnum.已开票.getCode())
                        .eq("i.invoice_type", 1)
                        .in("ir.state", InvoiceReceiptStateEnum.开票成功.getCode())
                        .in("b.settle_state", BillSettleStateEnum.未结算.getCode(),BillSettleStateEnum.部分结算.getCode())
                        .eq("b.sup_cp_unit_id", communityId)
                        .isNull("vbd.invoice_id")
                        .isNull("vbd.invoice_red_type")
                        .isNull("b.bill_label")
                        .isNotNull("b.bill_cost_type");
                WrapperUtils.logWrapper(wrapper);
                List<PushBusinessBill> advanceVoucherBusinessBills = receivableBillRepository.listPushReceivableBillByQuery(wrapper);
                QueryWrapper<?> voidInvoiceWrapper = VoucherBillRuleConditionUtils.parseConditionToQuery(conditions);
                voidInvoiceWrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
//                        .eq("b.bill_type", BillTypeEnum.应收账单.getCode()) //FYWY-954 加上临时账单类型
                        .eq("vb.invoice_red_type", 0)
                        .isNull("b.bill_label")
                        .in("b.settle_state", BillSettleStateEnum.未结算.getCode(), BillSettleStateEnum.部分结算.getCode())
                        .in("ir.state", InvoiceReceiptStateEnum.已作废.getCode(), InvoiceReceiptStateEnum.已红冲.getCode(),InvoiceReceiptStateEnum.部分红冲.getCode())
                        .eq("b.sup_cp_unit_id", communityId)
                        .isNotNull("b.bill_cost_type");
                WrapperUtils.logWrapper(voidInvoiceWrapper);
                List<PushBusinessBill> voidInvoiceBusinessBills = receivableBillRepository.voidInvoiceReceivableBillByQuery(voidInvoiceWrapper);
                businessBills.addAll(advanceVoucherBusinessBills);
                businessBills.addAll(voidInvoiceBusinessBills);
            });
        }
        return businessBills;
    }

    /**
     * 预收结转
     * 查询打了‘预收款项’标签的应收账单
     * 部分结算或已结算
     *
     * @param conditions
     * @return
     */
    public List<PushBusinessBill> getAdvanceCarryDownBillList(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        List<PushBusinessBill> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(communityIds)) {
            communityIds.forEach(communityId -> {
                Map<String, String> map = checkDate();
                QueryWrapper<?> wrappers = VoucherBillRuleConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.应收账单);
                wrappers.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                        .eq("b.bill_type", BillTypeEnum.应收账单.getCode())
                        .in("b.settle_state", BillSettleStateEnum.部分结算.getCode(), BillSettleStateEnum.已结算.getCode())
                        .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                        .eq("b.sup_cp_unit_id", communityId)
                        .eq("gd.sup_cp_unit_id",communityId)
                        .isNull("vbd.id")
                        .eq("b.bill_cost_type",BillCostTypeEnum.预收款项.getCode())
                        //归属月为推送的北京时间所在季度的账单
                        .between("b.account_date", map.get("startTime"), map.get("endTime"));
                WrapperUtils.logWrapper(wrappers);
                List<PushBusinessBill> receivableCarryDownBillList = receivableBillRepository.getReceivableCarryDownBillList(wrappers);
                //过滤出收款方式为结转，同时去反向查出结转的账单信息
                List<PushBusinessBill> carryover = receivableCarryDownBillList.stream()
                        .filter(iter -> StringUtils.isNotBlank(iter.getPayChannel()) && iter.getPayChannelCarryover().equals("CARRYOVER")).collect(Collectors.toList());

                List<PushBusinessBill> billIdByList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(carryover)){
                    List<Long> billIds = carryover.stream().map(PushBusinessBill::getBillId).distinct().collect(Collectors.toList());
                    for (Long billId : billIds) {
                        billIdByList = billCarryoverRepository.getBillIdByList(billId, carryover.get(0).getSupCpUnitId());
                        for (PushBusinessBill pushBusinessBill : billIdByList) {
                            String payChannel = pushBusinessBill.getPayChannel();
                            JSONArray objects = JSON.parseArray(payChannel);
                            String payChannels = objects.getJSONObject(0).getString("payChannel");
                            pushBusinessBill.setPayChannel(payChannels);
                        }
                        if (!CollectionUtils.isNotEmpty(billIdByList)) {
                            List<PushBusinessBill> advanceBillCarryoverList = advanceBillRepository.getAdvanceBillCarryoverList(billId);
                            result.addAll(advanceBillCarryoverList);
                        }
                        result.addAll(billIdByList);
                    }
                    result.addAll(carryover);
                    receivableCarryDownBillList.removeAll(carryover);
                }
                //如果支付方式不是结转的生成一条相同的账单金额为负数的
                List<PushBusinessBill> pushBusinessBill = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(receivableCarryDownBillList)){
                    for (PushBusinessBill businessBill : receivableCarryDownBillList) {
                        PushBusinessBill pushBusinessBill1 = new PushBusinessBill();
                        BeanUtils.copyProperties(businessBill,pushBusinessBill1);
                        pushBusinessBill1.setCarriedAmount( - businessBill.getCarriedAmount());
                        businessBill.setPayChannel(businessBill.getPayChannelCarryover());
                        pushBusinessBill1.setPayChannel(businessBill.getPayChannelCarryover());
                        pushBusinessBill.add(pushBusinessBill1);
                    }
                    result.addAll(receivableCarryDownBillList);
                    result.addAll(pushBusinessBill);
                }
            });
        }

        return result;
    }


    /**
     * 预收结转特殊场景
     * @param conditions
     * @param communityIds
     * @return
     */
    public List<PushBusinessBill> getAdvanceCarryDownTwoBillList(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        List<PushBusinessBill> businessBills = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(communityIds)) {
            communityIds.forEach(communityId -> {
                QueryWrapper<?> wrapper = VoucherBillRuleConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.应收账单);
                wrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                        .eq("b.bill_type", BillTypeEnum.应收账单.getCode())
                        .in("b.settle_state", BillSettleStateEnum.部分结算.getCode(), BillSettleStateEnum.已结算.getCode())
                        .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                        .eq("b.sup_cp_unit_id", communityId)
                        .eq("gd.sup_cp_unit_id",communityId)
                        .isNull("vbd.id")
                        .eq("b.bill_cost_type",BillCostTypeEnum.当期应收.getCode())
                        .eq("gd.pay_channel", "CARRYOVER");
                ;
                WrapperUtils.logWrapper(wrapper);
                List<PushBusinessBill> receivableCarryDownBillList = receivableBillRepository.getReceivableCarryDownTwoBillList(wrapper);
                log.info("结转到的应收账单:{}", JSON.toJSONString(receivableCarryDownBillList));
                if (CollectionUtils.isNotEmpty(receivableCarryDownBillList)){
                    Map<Long, List<PushBusinessBill>> businessBillMap = receivableCarryDownBillList.stream().collect(Collectors.groupingBy(PushBusinessBill::getBillId));
                    businessBillMap.forEach((billId, billList) -> {
                        List<PushBusinessBill> advanceBillCarryoverList = advanceBillRepository.getAdvanceBillCarryoverList(billId);
                        log.info("查询到的预收账单:{}", JSON.toJSONString(advanceBillCarryoverList));
                        if (CollectionUtils.isNotEmpty(advanceBillCarryoverList)) {
                            businessBills.addAll(advanceBillCarryoverList);
                            advanceBillCarryoverList.forEach(carryBill -> {
                                PushBusinessBill pushBusinessBill = billList.stream()
                                        .filter(bill -> bill.getCarriedAmount().compareTo(-carryBill.getCarriedAmount()) == 0
                                        && LocalDateTimeUtil.between(bill.getGmtCreate(), carryBill.getGmtCreate(), ChronoUnit.MINUTES) < 3).findAny().get();
                                businessBills.add(pushBusinessBill);
                            });
                        }
                    });
                }
            });
        }
        return businessBills;

    }




    /**
     * 获取当前时间的所在季度的开始和结束
     */
    private Map<String, String> checkDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> map = new HashMap();
        //获取当前时间所属季度开始月第一天
        Date dBegin = new Date();
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        int remainder = calBegin.get(Calendar.MONTH) % 3;
        int month = remainder != 0 ? calBegin.get(Calendar.MONTH) - remainder : calBegin.get(Calendar.MONTH);
        calBegin.set(Calendar.MONTH, month);
        calBegin.set(Calendar.DAY_OF_MONTH, calBegin.getActualMinimum(Calendar.DAY_OF_MONTH));
        calBegin.setTime(calBegin.getTime());
        map.put("startTime", sdf.format(calBegin.getTime()));

        //获取当前时间所属季度结束d月最后一天
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dBegin);
        int remainders = (calendar.get(Calendar.MONTH) + 1) % 3;
        int months = remainders != 0 ? calendar.get(Calendar.MONTH) + (3 - remainders) : calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, months);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.setTime(calendar.getTime());
        map.put("endTime", sdf.format(calendar.getTime()));

        //获取当前日上个季度最大月份
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.MONTH, ((int) endCalendar.get(Calendar.MONTH) / 3 - 1) * 3 + 2);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        endCalendar.set(Calendar.HOUR_OF_DAY, endCalendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        endCalendar.set(Calendar.MINUTE, endCalendar.getActualMaximum(Calendar.MINUTE));
        endCalendar.set(Calendar.SECOND, endCalendar.getActualMaximum(Calendar.SECOND));
        endCalendar.set(Calendar.MILLISECOND, endCalendar.getActualMaximum(Calendar.MILLISECOND));
        map.put("maxMonth",sdf.format(endCalendar.getTime()));
        return map;
    }

    /**
     * 推单成功后，修改对应收款明细账单状态
     *
     * @param businessBills
     */
    public void getModifyInferenceStatus(List<PushBusinessBill> businessBills) {
        List<Long> billIds = businessBills.stream().map(PushBusinessBill::getGatherDetailId).collect(Collectors.toList());
        List<String> collect = businessBills.stream().map(PushBusinessBill::getCommunityId).collect(Collectors.toList());
        gatherDetailRepository.updateInferenceStateByIds(billIds, InferenceStateEnum.已推凭.getCode(),collect);
    }

    public List<PushBusinessBill> getCollectionTransferBillList(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        List<PushBusinessBill> businessBills = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(communityIds)) {
            for (String communityId : communityIds) {
                // 查询临时账单结转出去的原账单信息
                QueryWrapper<?> wrapper = VoucherBillRuleConditionUtils.parseConditionToQuery(conditions);
                wrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.verify_state", BillReverseStateEnum.未冲销.getCode())
                        .eq("b.bill_type", BillTypeEnum.临时收费账单.getCode())
                        .eq("b.sup_cp_unit_id", communityId)
                        .isNull("vb.id")
                        .isNull("b.ext_field4")
                        .isNotNull("b.bill_cost_type");
                WrapperUtils.logWrapper(wrapper);
                List<PushBusinessBill> list = receivableBillRepository.getCollectionTransferBillQuery(wrapper);
                Map<Long, PushBusinessBill> pushBusinessBillMap = list.stream().collect(Collectors.toMap(PushBusinessBill::getBillCarryoverId, Function.identity()));
                Set<Long> billIds = Sets.newHashSet();
                if (CollectionUtils.isNotEmpty(list)) {
                    //获取结转记录信息
                    List<BillCarryoverE> billCarryoverES = billCarryoverRepository.listByIds(list.stream().map(PushBusinessBill::getBillCarryoverId).collect(Collectors.toList()));
                    for (BillCarryoverE listById : billCarryoverES) {
                        billIds.addAll(listById.getCarryoverDetail().stream().map(CarryoverDetail::getTargetBillId).collect(Collectors.toSet()));
                        PushBusinessBill pushBusinessBill = pushBusinessBillMap.get(listById.getId());
                        for (CarryoverDetail carryoverDetail : listById.getCarryoverDetail()) {
                            if (Objects.nonNull(carryoverDetail.getCarryoverAmount())){
                                carryoverDetail.setCarryoverAmount(- carryoverDetail.getCarryoverAmount());
                            }
                            if (Objects.nonNull(carryoverDetail.getActualCarryoverAmount())){
                                carryoverDetail.setActualCarryoverAmount(- carryoverDetail.getActualCarryoverAmount());
                            }
                            businessBills.add(this.enterBusinessBill(pushBusinessBill, carryoverDetail));
                        }
                    }
                    QueryWrapper<?> wrappers = VoucherBillRuleConditionUtils.parseConditionToQuery(conditions);
                    wrappers.in("b.id", billIds)
                            .eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                            .eq("b.state", BillStateEnum.正常.getCode())
                            .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                            .in("b.settle_state", BillSettleStateEnum.部分结算.getCode(), BillSettleStateEnum.已结算.getCode())
                            .eq("b.sup_cp_unit_id", communityId)
                            .eq("gd.sup_cp_unit_id", communityId)
                            .isNotNull("gd.id")
                            .isNull("vbd.id")
                            .eq("gd.pay_way", SettleWayEnum.结转.getCode());
                    WrapperUtils.logWrapper(wrappers);
                    List<PushBusinessBill> receivableCarryDownBillList = receivableBillRepository.getCollectionTransferReceivableBillList(wrappers);

                    QueryWrapper<?> queryWrapper = VoucherBillRuleConditionUtils.parseConditionToQuery(conditions);
                    queryWrapper.in("b.id", billIds)
                            .eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                            .eq("b.state", BillStateEnum.正常.getCode())
                            .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                            .eq("b.sup_cp_unit_id", communityId)
                            .in("b.settle_state", BillSettleStateEnum.部分结算.getCode(), BillSettleStateEnum.已结算.getCode())
                            .eq("gd.sup_cp_unit_id", communityId)
                            .isNotNull("gd.id")
                            .eq("gd.pay_way", SettleWayEnum.结转.getCode());
                    WrapperUtils.logWrapper(queryWrapper);
                    List<PushBusinessBill> advanceBillList = advanceBillRepository.getCollectionTransferDownBillList(queryWrapper);
                    businessBills.addAll(receivableCarryDownBillList);
                    businessBills.addAll(advanceBillList);
                }
            }
        }

        return businessBills;
    }

    private PushBusinessBill enterBusinessBill(PushBusinessBill pushBusinessBill, CarryoverDetail carryoverDetail) {
        PushBusinessBill businessBill = Global.mapperFacade.map(pushBusinessBill, PushBusinessBill.class);
        businessBill.setCarriedAmount(carryoverDetail.getActualCarryoverAmount() == null ? carryoverDetail.getCarryoverAmount() : carryoverDetail.getActualCarryoverAmount());
        return businessBill;
    }


    public List<PushBusinessBill> getArrearsProvisionBillList(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        List<PushBusinessBill> businessBills = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(communityIds)) {
            communityIds.forEach(communityId -> {
                QueryWrapper<?> wrapper = VoucherBillRuleConditionUtils.parseConditionToQuery(conditions,BillTypeEnum.应收账单);
                Map<String, String> map = checkDate();
                wrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                        .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                        .eq("b.bill_type", BillTypeEnum.应收账单.getCode())
                        .isNull("b.bill_label")
                        .isNull("vbd.id")
                        .in("b.settle_state", BillSettleStateEnum.未结算.getCode(), BillSettleStateEnum.部分结算.getCode())
                        .in("b.invoice_state", BillInvoiceStateEnum.未开票.getCode(), BillInvoiceStateEnum.部分开票.getCode())
                        .eq("b.sup_cp_unit_id", communityId)
                        .le("b.account_date", map.get("endTime"));
                WrapperUtils.logWrapper(wrapper);
                List<PushBusinessBill> advanceVoucherBusinessBills = receivableBillRepository.arrearsProvisionBillList(wrapper);
                businessBills.addAll(advanceVoucherBusinessBills);
            });
        }
        return businessBills;
    }

    public void updateReceivableBillInferenceStater(List<PushBusinessBill> businessBills) {
        List<Long> billIds = businessBills.stream().map(PushBusinessBill::getBillId).collect(Collectors.toList());
        List<String> collect = businessBills.stream().map(PushBusinessBill::getCommunityId).collect(Collectors.toList());
        receivableBillRepository.updateInferenceStateByIds(billIds, InferenceStateEnum.已推凭.getCode(),collect);
    }


    public List<PushBusinessBill> getBadBillConfirmBilList(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        List<PushBusinessBill> businessBills = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(communityIds)) {
            communityIds.forEach(communityId -> {
                //查询出推过欠费计提的账单
                QueryWrapper<?> wrapper = VoucherBillRuleConditionUtils.parseConditionToQuery(conditions,BillTypeEnum.应收账单);
                wrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                        .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                        .eq("b.bill_type", BillTypeEnum.应收账单.getCode())
                        .eq("vbd.push_bill_state", PushBillStateEnum.已推送.getCode())
                        .gt("b.deductible_amount", 0)
                        .eq("b.sup_cp_unit_id", communityId)
                        .eq("vbd.bill_event_type",TriggerEventBillTypeEnum.欠费计提.getCode());

                WrapperUtils.logWrapper(wrapper);
                List<PushBusinessBill> advanceVoucherBusinessBill = receivableBillRepository.getVoucherBillDetail(wrapper);
                List<Long> ids = advanceVoucherBusinessBill.stream().map(PushBusinessBill::getBillId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(ids)) {
                    QueryWrapper<?> wrappers = VoucherBillRuleConditionUtils.parseConditionToQuery(conditions,BillTypeEnum.应收账单);
                    Map<String, String> map = checkDate();
                    wrappers.in("b.id", ids)
                            .eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                            .eq("b.state", BillStateEnum.正常.getCode())
                            .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                            .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                            .eq("b.bill_type", BillTypeEnum.应收账单.getCode())
                            .eq("ba.adjust_type",BillAdjustTypeEnum.减免.getCode())
                            .ne("b.payee_type", VoucherBillCustomerTypeEnum.开发商.getCode())
                            .gt("ba.actual_lost_amount", 0) // 减免且未结转的金额大于 0
//                        .in("b.settle_state", BillSettleStateEnum.未结算.getCode(), BillSettleStateEnum.部分结算.getCode())
                            .eq("b.sup_cp_unit_id", communityId)
                            .le("b.account_date", map.get("maxMonth"))
                            .isNull("vbd.id");

                    WrapperUtils.logWrapper(wrappers);
                    List<PushBusinessBill> advanceVoucherBusinessBills = receivableBillRepository.badBillConfirmBilList(wrappers);
                    businessBills.addAll(advanceVoucherBusinessBills);
                }
            });
        }
        return businessBills;
    }

    /**
     *  对账核销
     * @param conditions
     * @param communityIds
     * @return
     */
    public List<PushBusinessBill> getReconciliationVerificationBillList(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        List<PushBusinessBill> businessBills = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(communityIds)) {
            communityIds.forEach(communityId -> {
                QueryWrapper<?> wrappers = VoucherBillRuleConditionUtils.parseConditionToQuery(conditions,BillTypeEnum.应收账单);
                wrappers.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                        .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                        .in("b.settle_state", BillSettleStateEnum.部分结算.getCode(), BillSettleStateEnum.已结算.getCode())
                        .eq("b.sup_cp_unit_id", communityId)
                        .eq("gd.sup_cp_unit_id",communityId)
                        .eq("gb.sup_cp_unit_id",communityId)
                        .isNull("vbd.id")
                        .ne("gd.pay_channel",SettleChannelEnum.结转.getCode())
                        .and(
                                QueryWrapper -> QueryWrapper.eq("gb.reconcile_state", ReconcileResultEnum.已核对.getCode()).or()
                                        .eq("gb.mc_reconcile_state",ReconcileResultEnum.已核对.getCode())
                        );
                WrapperUtils.logWrapper(wrappers);
                List<PushBusinessBill> receivableVoucherBusinessBills = receivableBillRepository.reconciliationVerificationBillList(wrappers);

                QueryWrapper<?> wrapper = VoucherBillRuleConditionUtils.parseConditionToQuery(conditions,BillTypeEnum.预收账单);
                wrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                        .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                        .in("b.settle_state", BillSettleStateEnum.部分结算.getCode(), BillSettleStateEnum.已结算.getCode())
                        .eq("b.sup_cp_unit_id", communityId)
                        .eq("gd.sup_cp_unit_id",communityId)
                        .eq("gb.sup_cp_unit_id",communityId)
                        .isNull("vbd.id")
                        .ne("gd.pay_channel",SettleChannelEnum.结转.getCode())
                        .and(
                                QueryWrapper -> QueryWrapper.eq("gb.reconcile_state", ReconcileResultEnum.已核对.getCode()).or()
                                        .eq("gb.mc_reconcile_state",ReconcileResultEnum.已核对.getCode())
                        );
                WrapperUtils.logWrapper(wrapper);
                List<PushBusinessBill> advanceVoucherBusinessBills = advanceBillRepository.reconciliationVerificationBillList(wrapper);
                receivableVoucherBusinessBills.addAll(advanceVoucherBusinessBills);
                if (CollectionUtils.isNotEmpty(receivableVoucherBusinessBills)) {
                    for (PushBusinessBill bill : receivableVoucherBusinessBills) {
                        //账票流水对账,获取流水号-根据收款单id或者账单id去匹配对应的流水号
                        if (bill.getReconcileState().equals(ReconcileResultEnum.已核对.getCode())) {
                            ArrayList<Long> ids = Lists.newArrayList();
                            Long sceneId = bill.getSceneId();
                            Long id = bill.getBillId();
                            Long invoiceReceiptId = null;
                            List<InvoiceReceiptDetailE> byBillId = invoiceReceiptDetailRepository.getByBillId(null, id);
                            if (CollectionUtils.isNotEmpty(byBillId)){
                                invoiceReceiptId = byBillId.get(0).getInvoiceReceiptId();
                                ids.add(invoiceReceiptId);
                            }
                            ids.add(sceneId);
                            List<FlowClaimDetailE> list = flowClaimDetailRepository.list(new LambdaQueryWrapper<FlowClaimDetailE>().in(FlowClaimDetailE::getInvoiceId, ids).eq(FlowClaimDetailE::getState ,BillStateEnum.正常.getCode()));
                            if (CollectionUtils.isNotEmpty(list)){
                                List<Long> flowIds = list.stream().map(FlowClaimDetailE::getFlowId).collect(Collectors.toList());
                                List<FlowDetailE> flowDetailES = flowDetailRepository.listByIds(flowIds);
                                String collect = flowDetailES.stream().map(FlowDetailE::getSerialNumber).collect(Collectors.joining("/"));
                                bill.setBankSerialNumber(collect);
                            }
                        }
                    }
                }
                businessBills.addAll(receivableVoucherBusinessBills);
            });
        }
        return businessBills;
    }

    public List<PushBusinessBill> getPaymentAdjustmentBusinessBills(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        List<PushBusinessBill> businessBills = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(communityIds)) {
            communityIds.forEach(communityId -> {
                // 冲销账单处理，做过对账核销的账单冲销后需要推
                QueryWrapper<?> receivableWrapper = VoucherBillRuleConditionUtils.parseConditionToQuery(conditions);
                receivableWrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                        .eq("vbd.push_bill_state", PushBillStateEnum.已推送.getCode())
                        .isNull("vbdd.id")
                        .eq("b.sup_cp_unit_id", communityId);
                WrapperUtils.logWrapper(receivableWrapper);
                List<PushBusinessBill> receivableVoucherBusinessBills =
                        receivableBillRepository.getPaymentAdjustmentReversedBusinessBills(receivableWrapper);
                businessBills.addAll(receivableVoucherBusinessBills);
                QueryWrapper<?> wrapper = VoucherBillRuleConditionUtils.parseConditionToQuery(conditions);
                wrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                        .eq("vbd.push_bill_state", PushBillStateEnum.已推送.getCode())
                        .isNull("vbdd.id")
                        .eq("b.sup_cp_unit_id", communityId);
                WrapperUtils.logWrapper(wrapper);
                List<PushBusinessBill> advanceVoucherBusinessBills =
                        advanceBillRepository.getPaymentAdjustmentReversedBusinessBills(wrapper);
                businessBills.addAll(advanceVoucherBusinessBills);
                // 作废账单处理
                QueryWrapper<?> invalidReceivableWrapper = VoucherBillRuleConditionUtils.parseConditionToQuery(conditions);
                invalidReceivableWrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.作废.getCode())
                        .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                        .eq("vbd.push_bill_state", PushBillStateEnum.已推送.getCode())
                        .isNull("vbdd.id")
                        .eq("b.sup_cp_unit_id", communityId);
                WrapperUtils.logWrapper(invalidReceivableWrapper);
                List<PushBusinessBill> invalidReceivableVoucherBusinessBills =
                        receivableBillRepository.getPaymentAdjustmentInvalidBusinessBills(invalidReceivableWrapper);
                businessBills.addAll(invalidReceivableVoucherBusinessBills);
                QueryWrapper<?> invalidAdvanceWrapper = VoucherBillRuleConditionUtils.parseConditionToQuery(conditions);
                invalidAdvanceWrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.作废.getCode())
                        .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                        .eq("vbd.push_bill_state", PushBillStateEnum.已推送.getCode())
                        .isNull("vbdd.id")
                        .eq("b.sup_cp_unit_id", communityId);
                WrapperUtils.logWrapper(invalidAdvanceWrapper);
                List<PushBusinessBill> invalidAdvanceVoucherBusinessBills = advanceBillRepository.getPaymentAdjustmentInvalidBusinessBills(invalidAdvanceWrapper);
                businessBills.addAll(invalidAdvanceVoucherBusinessBills);
            });
        }
        return businessBills;
    }
}
