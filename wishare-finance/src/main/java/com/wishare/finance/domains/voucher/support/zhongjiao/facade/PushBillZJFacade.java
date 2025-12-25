package com.wishare.finance.domains.voucher.support.zhongjiao.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillAdjustE;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.repository.BillAdjustRepository;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.report.enums.ChargeItemAttributeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOptionOBV;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBillRuleConditionOBV;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ReverseFlagEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.VoucherBillRuleConditionZJTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillDetailZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.PushZJBusinessBill;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.PushZJBusinessBillForSettlement;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.VoucherBillRuleZJConditionUtils;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.remote.enums.DeductionMethodEnum;
import com.wishare.finance.infrastructure.utils.WrapperUtils;
import com.wishare.starter.beans.IdentityInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.wishare.starter.utils.ThreadLocalUtil.curIdentityInfo;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PushBillZJFacade {

    private final ReceivableBillRepository receivableBillRepository;
    private final VoucherBillDetailZJRepository voucherBillDetailZJRepository;
    private final BillAdjustRepository billAdjustRepository;


    //报账汇总规则-收入确认-小业主
    public List<PushZJBusinessBill> getReceivableBillZJList(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        List<PushZJBusinessBill> pushZJBusinessBills = Lists.newArrayList();
        for (String communityId : communityIds) {
            QueryWrapper<?> wrappers = VoucherBillRuleZJConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.应收账单);
            wrappers.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                    .in("b.state", BillStateEnum.正常.getCode(), BillStateEnum.冻结.getCode())
                    .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                    .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                    .in("b.bill_type", BillTypeEnum.应收账单.getCode(), BillTypeEnum.临时收费账单.getCode())
                    .le("b.account_date", LocalDate.now().withDayOfMonth(LocalDate.now().getMonth().length(LocalDate.now().isLeapYear())))
                    .eq("b.sup_cp_unit_id", communityId)
                    .ne("b.refund_state", BillRefundStateEnum.已退款.getCode())
                    .eq("ci.attribute", ChargeItemAttributeEnum.收入.getCode())
                    .eq("b.inference_state", 0)
                    .apply("(b.contract_no is null or b.contract_no = '')")
                    .isNull("b.bill_label")
                    .isNotNull("b.cost_center_name")
                    .isNull("vbdz.bill_id");
            WrapperUtils.logWrapper(wrappers);
            List<PushZJBusinessBill> receivableVoucherBusinessBills = receivableBillRepository.revenueRecognitionBillList(wrappers);
            pushZJBusinessBills.addAll(receivableVoucherBusinessBills);

            // 查询该账单是否进行调整、减免、作废、冲销
            // 查询该项目下 已推送的 应收账单  临时账单
           QueryWrapper<ReceivableBill> receivableBillStateWrapper = new QueryWrapper<ReceivableBill>()
                    .eq("sup_cp_unit_id", communityId)
                    .eq("inference_state", 1);


            for (VoucherBillRuleConditionOBV condition : conditions) {
                VoucherBillRuleConditionZJTypeEnum ruleConditionTypeEnum = VoucherBillRuleConditionZJTypeEnum.valueOfByCode(condition.getType());
                switch (ruleConditionTypeEnum) {
                    case 费项:
                        VoucherBillRuleZJConditionUtils.putField(condition.getMethod(), receivableBillStateWrapper,  "charge_item_id" ,
                                condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                        break;
                    case 单据来源:
                        VoucherBillRuleZJConditionUtils.putField(condition.getMethod(), receivableBillStateWrapper, "sys_source",
                                condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                        break;
                    case 归属月:
                        List<Object> dates = new ArrayList<>();
                        condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()).forEach(s ->{
                            dates.add(s);
                        });
                        VoucherBillRuleZJConditionUtils.putField(condition.getMethod(), receivableBillStateWrapper,  "DATE_FORMAT(account_date, '%Y-%m')",
                                dates, true);
                        break;
                }
            }

            List<ReceivableBill> list1 = receivableBillRepository.list(receivableBillStateWrapper);
            if (CollectionUtils.isNotEmpty(list1)) {
                // 查询减免记录

                QueryWrapper<?> queryWrapperJM = new QueryWrapper<>();
                queryWrapperJM.eq("deduction_method", DeductionMethodEnum.应收减免.getCode())
                        .in("bill_id", list1.stream().map(Bill::getId).collect(Collectors.toList()))
                        .eq("inference_state", 0)
                        .eq("state", 2);
                List<BillAdjustE> billAdjustEListJM = billAdjustRepository.queryList(queryWrapperJM);
                if (CollectionUtils.isNotEmpty(billAdjustEListJM)){
                    QueryWrapper<?> billAdjustQueryWrapper = new QueryWrapper<>();
                    billAdjustQueryWrapper.eq("b.sup_cp_unit_id", communityId)
                            .eq("ba.deduction_method",  DeductionMethodEnum.应收减免.getCode())
                            .eq("ba.inference_state",  0)
                            .eq("ba.state", 2)
                            .in("b.id", billAdjustEListJM.stream().map(BillAdjustE::getBillId).collect(Collectors.toList()));
                    List<PushZJBusinessBill> pushZJBusinessBills1 = receivableBillRepository.billAdjustBillList(billAdjustQueryWrapper);
                    for (PushZJBusinessBill pushZJBusinessBill : pushZJBusinessBills1) {
                        pushZJBusinessBill.setReverseFlag(ReverseFlagEnum.减免.getCode());
                        pushZJBusinessBills.add(pushZJBusinessBill);
                    }
                    billAdjustRepository.updateBillInferenceState(pushZJBusinessBills1.stream().map(PushZJBusinessBill::getSceneId).collect(Collectors.toList()), 1);
                }

                // 查询调整记录
                QueryWrapper<?> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("deduction_method", DeductionMethodEnum.不减免.getCode())
                        .ne("reason", BillAdjustReasonEnum.收费对象变更.getCode())
                        .eq("adjust_way", BillAdjustWayEnum.RECEIVABLE_AMOUNT.getCode())
                        .in("bill_id", list1.stream().map(Bill::getId).collect(Collectors.toList()))
                        .eq("inference_state", 0)
                        .eq("state", 2);
                List<BillAdjustE> billAdjustEList = billAdjustRepository.queryList(queryWrapper);

                if (CollectionUtils.isNotEmpty(billAdjustEList)){
                    QueryWrapper<?> billAdjustQueryWrapper = new QueryWrapper<>();
                    billAdjustQueryWrapper.eq("b.sup_cp_unit_id", communityId)
                            .eq("ba.adjust_way", BillAdjustWayEnum.RECEIVABLE_AMOUNT.getCode())
                            .ne("ba.reason", BillAdjustReasonEnum.收费对象变更.getCode())
                            .eq("ba.deduction_method",  DeductionMethodEnum.不减免.getCode())
                            .eq("ba.inference_state",  0)
                            .eq("ba.state", 2)
                            .in("b.id", billAdjustEList.stream().map(BillAdjustE::getBillId).collect(Collectors.toList()));
                    List<PushZJBusinessBill> pushZJBusinessBills1 = receivableBillRepository.billAdjustBillList(billAdjustQueryWrapper);
                    for (PushZJBusinessBill pushZJBusinessBill : pushZJBusinessBills1) {
                        pushZJBusinessBill.setReverseFlag(ReverseFlagEnum.调整.getCode());
                        pushZJBusinessBills.add(pushZJBusinessBill);
                    }
                    billAdjustRepository.updateBillInferenceState(pushZJBusinessBills1.stream().map(PushZJBusinessBill::getSceneId).collect(Collectors.toList()), 1);
                }

                QueryWrapper<?> payerChangeQueryWrapper = new QueryWrapper<>();
                payerChangeQueryWrapper.eq("deduction_method", DeductionMethodEnum.不减免.getCode())
                        .eq("reason", BillAdjustReasonEnum.收费对象变更.getCode())
                        .in("bill_id", list1.stream().map(Bill::getId).collect(Collectors.toList()))
                        .eq("inference_state", 0)
                        .eq("state", 2);
                List<BillAdjustE> billAdjustEListPayerChange = billAdjustRepository.queryList(payerChangeQueryWrapper);

                // 仅针对 大小业主变更的场景进行处理 大业主 payerType/originPayerType=99, 小业主 非99(可能为空)
                if (CollectionUtils.isNotEmpty(billAdjustEListPayerChange)){
                    List<BillAdjustE> realChangeList = Lists.newArrayList();
                    for (BillAdjustE billAdjustE : billAdjustEListPayerChange) {
                        // 原小业主 新小业主 的都不处理
                        boolean originJudge = Objects.isNull(billAdjustE.getOriginalPayerType()) ||  billAdjustE.getOriginalPayerType() != 99;
                        boolean newJudge = Objects.isNull(billAdjustE.getPayerType()) || billAdjustE.getPayerType() != 99;
                        if (originJudge && newJudge){
                            continue;
                        }
                        realChangeList.add(billAdjustE);
                    }
                    if (CollectionUtils.isNotEmpty(realChangeList)){
                        // 变动信息 payerId 新收费对象, originalPayerId 老收费对象 adjustAmount 调整金额(带正负值)
                        // 老收费对象 金额=-billAmount
                        QueryWrapper<?> payerChangeWrapper = new QueryWrapper<>();
                        payerChangeWrapper.eq("b.sup_cp_unit_id", communityId)
                                .eq("ba.reason", BillAdjustReasonEnum.收费对象变更.getCode())
                                .eq("ba.inference_state",  0)
                                .eq("ba.state", 2)
                                .in("ba.id", realChangeList.stream().map(BillAdjustE::getId).collect(Collectors.toList()));
                        List<PushZJBusinessBill> oldPayerBusinessBills = receivableBillRepository.billAdjustBillListOnOldPayer(payerChangeWrapper);
                        // 新收费对象 金额=billAmount+adjustAmount
                        List<PushZJBusinessBill> newPayerBusinessBills1 = receivableBillRepository.billAdjustBillListOnNewPayer(payerChangeWrapper);

                        List<PushZJBusinessBill> changePayerBusinessBills = Lists.newArrayList();
                        if (CollectionUtils.isNotEmpty(oldPayerBusinessBills)){
                            changePayerBusinessBills.addAll(oldPayerBusinessBills);
                        }
                        if (CollectionUtils.isNotEmpty(newPayerBusinessBills1)){
                            changePayerBusinessBills.addAll(newPayerBusinessBills1);
                        }
                        if (CollectionUtils.isNotEmpty(changePayerBusinessBills)){
                            for (PushZJBusinessBill pushZJBusinessBill : changePayerBusinessBills) {
                                pushZJBusinessBill.setReverseFlag(ReverseFlagEnum.调整.getCode());
                                pushZJBusinessBills.add(pushZJBusinessBill);
                            }
                            billAdjustRepository.updateBillInferenceState(changePayerBusinessBills.stream().map(PushZJBusinessBill::getSceneId).collect(Collectors.toList()), 1);
                        }
                    }
                }


            }
            // 查询冲销记录
           QueryWrapper<ReceivableBill> receivableBillWrapper = new QueryWrapper<ReceivableBill>()
                    .eq("reversed", BillReverseStateEnum.已冲销.getCode())
                    .eq("sup_cp_unit_id", communityId)
                    .eq("inference_state", 1);
            for (VoucherBillRuleConditionOBV condition : conditions) {
                VoucherBillRuleConditionZJTypeEnum ruleConditionTypeEnum = VoucherBillRuleConditionZJTypeEnum.valueOfByCode(condition.getType());
                switch (ruleConditionTypeEnum) {
                    case 费项:
                        VoucherBillRuleZJConditionUtils.putField(condition.getMethod(), receivableBillWrapper,  "charge_item_id" ,
                                condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                        break;
                    case 单据来源:
                        VoucherBillRuleZJConditionUtils.putField(condition.getMethod(), receivableBillWrapper, "sys_source",
                                condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()), false);
                        break;
                    case 归属月:
                        List<Object> dates = new ArrayList<>();
                        condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList()).forEach(s ->{
                            dates.add(s);
                        });
                        VoucherBillRuleZJConditionUtils.putField(condition.getMethod(), receivableBillWrapper,  "DATE_FORMAT(account_date, '%Y-%m')",
                                dates, true);
                        break;
                }
            }
            List<ReceivableBill> receivableBillList = receivableBillRepository.list(receivableBillWrapper);
            if (CollectionUtils.isNotEmpty(receivableBillList)) {
                QueryWrapper<?> reversedQueryWrapper = new QueryWrapper<>();
                reversedQueryWrapper.eq("b.sup_cp_unit_id", communityId)
                         .in("b.id", receivableBillList.stream().map(Bill::getId).collect(Collectors.toList()));
                List<PushZJBusinessBill> pushZJBusinessBills2 = receivableBillRepository.reverseBillList(reversedQueryWrapper);
                // 查询该应收账单 是否已经推送， 若是已经推送， 则不在继续操作
                for (PushZJBusinessBill pushZJBusinessBill : pushZJBusinessBills2) {
                    LambdaQueryWrapper<VoucherPushBillDetailZJ> detailWrapper = new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                            .eq(VoucherPushBillDetailZJ::getBillId, pushZJBusinessBill.getBillId())
                            .eq(VoucherPushBillDetailZJ::getReverseFlag, ReverseFlagEnum.冲销.getCode());
                    List<VoucherPushBillDetailZJ> pushBillDetailZJS1 = voucherBillDetailZJRepository.list(detailWrapper);
                    if (CollectionUtils.isEmpty(pushBillDetailZJS1)){
                        List<Long> collect = pushZJBusinessBills.stream().filter(s -> s.getReverseFlag() == 1 || s.getReverseFlag() == 2).map(PushZJBusinessBill::getBillId).collect(Collectors.toList());
                        if (!collect.contains(pushZJBusinessBill.getBillId())){
                            pushZJBusinessBill.setReverseFlag(ReverseFlagEnum.冲销.getCode());
                            pushZJBusinessBills.add(pushZJBusinessBill);
                        }
                    }
                }

            }
            // 查询作废记录 生成报账单，过滤作废账单
            /*List<ReceivableBill> receivableBillStateList = receivableBillRepository.list(receivableBillStateWrapper);
            if (CollectionUtils.isNotEmpty(receivableBillStateList)) {
                QueryWrapper<?> reversedQueryWrapper = new QueryWrapper<>();
                reversedQueryWrapper.eq("b.sup_cp_unit_id", communityId)
                        .eq("b.state", BillStateEnum.作废.getCode())
                        .in("b.id", receivableBillStateList.stream().map(Bill::getId).collect(Collectors.toList()));
                List<PushZJBusinessBill> pushZJBusinessBills2 = receivableBillRepository.reverseBillList(reversedQueryWrapper);
                // 查询该应收账单 是否已经推送， 若是已经推送， 则不在继续操作
                for (PushZJBusinessBill pushZJBusinessBill : pushZJBusinessBills2) {
                    LambdaQueryWrapper<VoucherPushBillDetailZJ> detailWrapper = new LambdaQueryWrapper<VoucherPushBillDetailZJ>()
                            .eq(VoucherPushBillDetailZJ::getBillId, pushZJBusinessBill.getBillId())
                            .eq(VoucherPushBillDetailZJ::getReverseFlag, ReverseFlagEnum.作废.getCode());
                    List<VoucherPushBillDetailZJ> pushBillDetailZJS1 = voucherBillDetailZJRepository.list(detailWrapper);
                    if (CollectionUtils.isEmpty(pushBillDetailZJS1)){
                        List<Long> collect = pushZJBusinessBills.stream().filter(s -> s.getReverseFlag() == 1 || s.getReverseFlag() == 2).map(PushZJBusinessBill::getBillId).collect(Collectors.toList());
                        if (!collect.contains(pushZJBusinessBill.getBillId())){
                            pushZJBusinessBill.setReverseFlag(ReverseFlagEnum.作废.getCode());
                            pushZJBusinessBills.add(pushZJBusinessBill);
                        }
                    }
                }

            }*/
        }
        return pushZJBusinessBills;
    }


    public List<PushZJBusinessBillForSettlement> getReceivableBillZJListForSettlement(List<VoucherBillRuleConditionOBV> conditions,
                                                                                      List<String> communityIds,List<String> billIdList,Integer eventType) {
        List<PushZJBusinessBillForSettlement> pushZJBusinessBills = Lists.newArrayList();
        for (String communityId : communityIds) {
            QueryWrapper<?> wrappers = VoucherBillRuleZJConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.应收账单);
            wrappers.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                    .in("b.state", BillStateEnum.正常.getCode())
                    .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                    .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                    .eq("b.bill_type", BillTypeEnum.临时收费账单.getCode())
                    .le("b.account_date", LocalDate.now().withDayOfMonth(LocalDate.now().getMonth().length(LocalDate.now().isLeapYear())))
                    .eq("b.sup_cp_unit_id", communityId)
                    .ne("b.refund_state", BillRefundStateEnum.已退款.getCode())
                    .eq("ci.attribute", ChargeItemAttributeEnum.支出.getCode())
                    .eq("b.provision_status",0)
                    .eq("b.settlement_status",0)
                    .eq("b.provision_voucher_pushing_status", 0)
                    .isNull("b.bill_label")
                    .isNotNull("b.contract_no")
                    .isNotNull("b.cost_center_name")
                    .ne("b.contract_no","")
                    .isNull("vbdz.bill_id")
                    .in(CollectionUtils.isNotEmpty(billIdList),"b.id",billIdList);
            ;
            WrapperUtils.logWrapper(wrappers);
            // 计提税额都是0
            List<PushZJBusinessBillForSettlement> receivableVoucherBusinessBills =
                    receivableBillRepository.revenueRecognitionBillListForSettlement(wrappers);
            pushZJBusinessBills.addAll(receivableVoucherBusinessBills);

            // 减免、作废、冲销 不做 20241218 15:03 by 沈宝存
        }
        return pushZJBusinessBills;
    }

    public List<PushZJBusinessBillForSettlement> getReceivableBillZJListForSettlementOnReverse(List<VoucherBillRuleConditionOBV> conditions,
                                                                                      List<String> communityIds,List<String> billIdList,Integer eventType) {
        List<PushZJBusinessBillForSettlement> pushZJBusinessBills = Lists.newArrayList();
        for (String communityId : communityIds) {
            QueryWrapper<?> wrappers = VoucherBillRuleZJConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.应收账单);
            wrappers.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                    .in("b.state", BillStateEnum.正常.getCode())
                    .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                    .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                    .eq("b.bill_type", BillTypeEnum.临时收费账单.getCode())
//                    .le("b.account_date", LocalDate.now().withDayOfMonth(LocalDate.now().getMonth().length(LocalDate.now().isLeapYear())))
                    .eq("b.sup_cp_unit_id", communityId)
                    .ne("b.refund_state", BillRefundStateEnum.已退款.getCode())
                    .eq("ci.attribute", ChargeItemAttributeEnum.支出.getCode())
                    .ne("b.provision_status",0)
                    .isNull("b.bill_label")
                    .isNotNull("b.contract_no")
                    .isNotNull("b.cost_center_name")
                    .ne("b.contract_no","")
                    .isNull("vbdz.bill_id")
                    .in(CollectionUtils.isNotEmpty(billIdList),"b.id",billIdList);
            ;
            WrapperUtils.logWrapper(wrappers);
            // 计提税额都是0
            List<PushZJBusinessBillForSettlement> receivableVoucherBusinessBills =
                    receivableBillRepository.revenueRecognitionBillListForSettlementOnReverse(wrappers);
            pushZJBusinessBills.addAll(receivableVoucherBusinessBills);

            // 减免、作废、冲销 不做 20241218 15:03 by 沈宝存
        }
        return pushZJBusinessBills;
    }


    public List<PushZJBusinessBillForSettlement> getReceivableBillZJListForPayIncome(List<VoucherBillRuleConditionOBV> conditions,
                                                                                      List<String> communityIds,List<String> billIdList,Integer eventType) {
        IdentityInfo identityInfo = curIdentityInfo();
        identityInfo.setTenantId("13554968497211");
        List<PushZJBusinessBillForSettlement> pushZJBusinessBills = Lists.newArrayList();
        for (String communityId : communityIds) {
            QueryWrapper<?> wrappers = VoucherBillRuleZJConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.应收账单);
            wrappers.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                    .in("b.state", BillStateEnum.正常.getCode())
                    .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                    .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                    .eq("b.bill_type", BillTypeEnum.临时收费账单.getCode())
                    .le("b.account_date", LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()))
                    .eq("b.sup_cp_unit_id", communityId)
                    .ne("b.refund_state", BillRefundStateEnum.已退款.getCode())
                    .eq("ci.attribute", ChargeItemAttributeEnum.收入.getCode())
                    .eq("b.provision_status",0)
                    .eq("b.receipt_confirmation_status", 0)
                    .eq("b.provision_voucher_pushing_status", 0)
                    .isNull("b.bill_label")
                    .isNotNull("b.contract_no")
                    .ne("b.contract_no","")
                    .isNotNull("b.cost_center_name")
                    .isNull("vbdz.bill_id")
                    .in(CollectionUtils.isNotEmpty(billIdList),"b.id",billIdList);
            ;
            WrapperUtils.logWrapper(wrappers);
            List<PushZJBusinessBillForSettlement> receivableVoucherBusinessBills =
                    receivableBillRepository.revenueRecognitionBillListForPayIncome(wrappers);
            pushZJBusinessBills.addAll(receivableVoucherBusinessBills);

            // 减免、作废、冲销 不做 20241218 15:03 by 沈宝存
        }
        return pushZJBusinessBills;
    }

    public List<PushZJBusinessBillForSettlement> getReceivableBillZJListForPayIncomeOnReverse(List<VoucherBillRuleConditionOBV> conditions,
                                                                                     List<String> communityIds,List<String> billIdList,Integer eventType) {
        List<PushZJBusinessBillForSettlement> pushZJBusinessBills = Lists.newArrayList();
        for (String communityId : communityIds) {
            QueryWrapper<?> wrappers = VoucherBillRuleZJConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.应收账单);
            wrappers.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                    .in("b.state", BillStateEnum.正常.getCode())
                    .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                    .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                    .eq("b.bill_type", BillTypeEnum.临时收费账单.getCode())
//                    .le("b.account_date", LocalDate.now().withDayOfMonth(LocalDate.now().getMonth().length(LocalDate.now().isLeapYear())))
                    .eq("b.sup_cp_unit_id", communityId)
                    .ne("b.refund_state", BillRefundStateEnum.已退款.getCode())
                    .eq("ci.attribute", ChargeItemAttributeEnum.收入.getCode())
                    .ne("b.provision_status",0)
                    .isNull("b.bill_label")
                    .isNotNull("b.contract_no")
                    .ne("b.contract_no","")
                    .isNotNull("b.cost_center_name")
                    .isNull("vbdz.bill_id")
                    .in(CollectionUtils.isNotEmpty(billIdList),"b.id",billIdList);
            ;
            WrapperUtils.logWrapper(wrappers);
            List<PushZJBusinessBillForSettlement> receivableVoucherBusinessBills =
                    receivableBillRepository.revenueRecognitionBillListForPayIncomeOnReverse(wrappers);
            pushZJBusinessBills.addAll(receivableVoucherBusinessBills);

            // 减免、作废、冲销 不做 20241218 15:03 by 沈宝存
        }
        return pushZJBusinessBills;
    }


    public List<PushZJBusinessBillForSettlement> getReceivableBillZJListForSQ(List<VoucherBillRuleConditionOBV> conditions,
                                                                                      List<String> communityIds,List<String> billIdList,Integer eventType) {


        IdentityInfo identityInfo = curIdentityInfo();
        identityInfo.setTenantId("13554968497211");
        List<PushZJBusinessBillForSettlement> pushZJBusinessBills = Lists.newArrayList();
        for (String communityId : communityIds) {
            QueryWrapper<?> wrappers = VoucherBillRuleZJConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.应收账单);
            wrappers.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                    .in("b.state", BillStateEnum.正常.getCode())
                    .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                    .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                    .eq("b.bill_type", BillTypeEnum.临时收费账单.getCode())
//                    .le("b.account_date", LocalDate.now().withDayOfMonth(LocalDate.now().getMonth().length(LocalDate.now().isLeapYear())))
                    .eq("b.sup_cp_unit_id", communityId)
                    .ne("b.refund_state", BillRefundStateEnum.已退款.getCode())
                    .eq("ci.attribute", ChargeItemAttributeEnum.收入.getCode())
                    .eq("b.receipt_confirmation_status", 0)
                    .eq("b.receipt_confirmation_voucher_pushing_status", 0)
                    .ne("b.contract_no","")
                    .isNotNull("b.contract_no")
                    .in(CollectionUtils.isNotEmpty(billIdList), "b.id", billIdList);
            ;
            WrapperUtils.logWrapper(wrappers);
            List<PushZJBusinessBillForSettlement> receivableVoucherBusinessBills =
                    receivableBillRepository.revenueRecognitionBillListForSQ(wrappers);
            pushZJBusinessBills.addAll(receivableVoucherBusinessBills);

            // 减免、作废、冲销 不做 20241218 15:03 by 沈宝存
        }
        return pushZJBusinessBills;
    }

    public List<PushZJBusinessBillForSettlement> getReceivableBillZJListForSettlementDx(List<VoucherBillRuleConditionOBV> conditions,
                                                                                        List<String> communityIds,
                                                                                        List<String> billIdList) {
        if (CollectionUtils.isEmpty(billIdList)){
            return Collections.emptyList();
        }
        IdentityInfo identityInfo = curIdentityInfo();
        identityInfo.setTenantId("13554968497211");
        List<PushZJBusinessBillForSettlement> pushZJBusinessBills = Lists.newArrayList();
        for (String communityId : communityIds) {
            QueryWrapper<?> wrappers = VoucherBillRuleZJConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.应收账单);
            wrappers.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                    .in("b.state", BillStateEnum.正常.getCode())
                    .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                    .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                    .eq("b.bill_type", BillTypeEnum.临时收费账单.getCode())
                    //优化，因生成合同报账单缺失数据，去除归属月判断
                    //.le("b.account_date", LocalDate.now().withDayOfMonth(LocalDate.now().getMonth().length(LocalDate.now().isLeapYear())))
                    .eq("b.sup_cp_unit_id", communityId)
                    .ne("b.refund_state", BillRefundStateEnum.已退款.getCode())
                    .eq("ci.attribute", ChargeItemAttributeEnum.支出.getCode())
                    .eq("b.settlement_status", 0)
                    .eq("b.settlement_voucher_pushing_status", 0)
                    .in( "b.id", billIdList);
            ;
            WrapperUtils.logWrapper(wrappers);
            List<PushZJBusinessBillForSettlement> receivableVoucherBusinessBills =
                    receivableBillRepository.revenueRecognitionBillListForPaySettlement(wrappers);
            pushZJBusinessBills.addAll(receivableVoucherBusinessBills);

            // 减免、作废、冲销 不做 20241218 15:03 by 沈宝存
        }
        return pushZJBusinessBills;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProvisionVoucherPushingStatus(List<Long> billIdList, List<String> communityIds) {
        for (String communityId : communityIds) {
            receivableBillRepository.updateProvisionVoucherPushingStatusById(billIdList, communityId,1);
        }
    }


    public String getBusinessTypeCode(Long billId, String projectId) {
        return receivableBillRepository.getBusinessTypeCode(billId, projectId);
    }

    public void updateRelatedBillStatusOnPayCost(List<Long> billIdList, List<String> communityIds) {
        for (String communityId : communityIds) {
            receivableBillRepository.updateRelatedBillStatusOnPayCost(billIdList, communityId,1);
        }
    }

    public void updateRelatedBillStatusOnPayIncome(List<Long> billIdList, List<String> communityIds) {
        for (String communityId : communityIds) {
            receivableBillRepository.updateRelatedBillStatusOnPayIncome(billIdList, communityId,1);
        }
    }

    public List<PushZJBusinessBillForSettlement> businessZJBillsForPaymentApplicationForm(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds, List<String> settlementIdList) {
        QueryWrapper<?> queryWrapper = new QueryWrapper<>();
        if(CollectionUtils.isNotEmpty(conditions) && conditions.get(0).getType().equals(1)){
            queryWrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                    .in("b.state", BillStateEnum.正常.getCode(), BillStateEnum.冻结.getCode())
                    .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                    .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                    .eq("b.bill_type", BillTypeEnum.临时收费账单.getCode())
                    .le("b.account_date", LocalDate.now().withDayOfMonth(LocalDate.now().getMonth().length(LocalDate.now().isLeapYear())))
                    .eq("b.sup_cp_unit_id", communityIds.get(0))
                    .ne("b.refund_state", BillRefundStateEnum.已退款.getCode())
                    .eq("ci.attribute", ChargeItemAttributeEnum.代收代付及其他.getCode())
                    .eq("b.settlement_status", 0)
                    .eq("b.settlement_voucher_pushing_status", 0)
                    .eq("b.pay_app_push_status",0)
                    .eq("b.pay_app_status",0)
                    .in( "b.id", settlementIdList);
        }else{
            queryWrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                    .in("b.state", BillStateEnum.正常.getCode(), BillStateEnum.冻结.getCode())
                    .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                    .eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                    .eq("b.bill_type", BillTypeEnum.临时收费账单.getCode())
                    .le("b.account_date", LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()))
                    .eq("b.sup_cp_unit_id", communityIds.get(0))
                    .ne("b.refund_state", BillRefundStateEnum.已退款.getCode())
                    .eq("ci.attribute", ChargeItemAttributeEnum.支出.getCode())
                    .eq("b.settlement_voucher_pushing_status",1)
                    .eq("b.pay_app_push_status",0)
                    .eq("b.pay_app_status",0)
                    .isNotNull("b.contract_no")
                    .ne("b.contract_no","")
                    .isNotNull("b.cost_center_name")
                    .in("b.ext_field7", settlementIdList);
        }
        WrapperUtils.logWrapper(queryWrapper);
        return receivableBillRepository.queryMxBysettlementIdList(queryWrapper);
    }

    public void updatePayAppVoucherPushingStatus(List<Long> billIdList, List<String> communityIds) {
        for (String communityId : communityIds) {
            receivableBillRepository.updatePayAppVoucherPushingStatusById(billIdList, communityId,1);
        }
    }
}
