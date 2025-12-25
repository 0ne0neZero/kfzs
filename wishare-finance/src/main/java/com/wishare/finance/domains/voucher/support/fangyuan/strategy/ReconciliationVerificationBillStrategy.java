package com.wishare.finance.domains.voucher.support.fangyuan.strategy;

import com.google.common.collect.Lists;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.repository.ChargeItemRepository;
import com.wishare.finance.domains.reconciliation.enums.ReconcileResultEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBillRuleConditionOBV;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.TriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.ManualBillStrategy;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.ManualBillStrategyCommand;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.PushBusinessBill;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 对账核销
 */
@Service
public class ReconciliationVerificationBillStrategy extends ManualBillStrategy<ManualBillStrategyCommand> {

    @Autowired
    private ChargeItemRepository chargeItemRepository;

    public ReconciliationVerificationBillStrategy() {
        super(TriggerEventBillTypeEnum.对账核销);
    }

    @Override
    public List<PushBusinessBill> businessBills(ManualBillStrategyCommand command, List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        return pushBillFacade.getReconciliationVerificationBillList(conditions,communityIds);
    }

    /**
     * 分组时如果保证sceneId(gather_bill_id)相同的都在同一组
     * @param list
     * @param groupSize
     * @return
     */
    public List<List<PushBusinessBill>> grouping(List<PushBusinessBill> list, int groupSize) {
        List<List<PushBusinessBill>> groups = new ArrayList<>();
        int startIndex = 0;
        while (startIndex < list.size()) {
            int endIndex = Math.min(startIndex + groupSize, list.size() - 1);
            // 检查并扩展分组
            while (endIndex < list.size() - 1 && Objects.equals(list.get(endIndex).getSceneId(), list.get(endIndex + 1).getSceneId())) {
                endIndex++;
            }
            // 添加当前分组
            groups.add(new ArrayList<>(list.subList(startIndex, endIndex + 1)));
            startIndex = endIndex + 1;
        }
        return groups;
    }


    /**
     * 添加合并的手续费和违约金，并且隐藏原违约金的报账明细（用于数据留存标记）
     * @param partition
     */
    @Override
    public void middleHandle(List<List<PushBusinessBill>> partition) {
        String liquidatedDamagesChargeItemCode = "0701"; // 违约金费项code
        ChargeItemE liquidatedDamages = chargeItemRepository.getChargeItemByCode(liquidatedDamagesChargeItemCode);
        String commissionChargeItemCode = "0702";  // 银行手续费code
        ChargeItemE commission = chargeItemRepository.getChargeItemByCode(commissionChargeItemCode);
        for (List<PushBusinessBill> businessBills : partition) {
            if (CollectionUtils.isNotEmpty(businessBills)) {
                PushBusinessBill commissionBill = null;
                PushBusinessBill liquidatedDamagesBill = null;
                Set<String> bankSerialNumberSet = new HashSet<>();
                for (PushBusinessBill bill : businessBills) {
                    // 商户清分对账情况下，手续费汇聚成一条明细
                    if (bill.getMcReconcileState() != null && bill.getMcReconcileState().equals(ReconcileResultEnum.已核对.getCode())) {
                        if (Objects.isNull(commissionBill)) {
                            commissionBill = Global.mapperFacade.map(bill, PushBusinessBill.class);
                            commissionBill.setChargeItemId(commission.getId());
                            commissionBill.setChargeItemName(commission.getName());
                            commissionBill.setBillId(null);
                            commissionBill.setBillNo(IdentifierFactory.getInstance().serialNumber("commission_bill", "SXF", 20));
                            commissionBill.setRoomName(null);
                            commissionBill.setRoomId(null);
                            commissionBill.setAccountDate(null);
                            commissionBill.setTaxAmount(0L);
                            commissionBill.setCommission(null);
                            commissionBill.setPayChannel(null);
                            commissionBill.setTaxRateId(null);
                            commissionBill.setTaxRate(BigDecimal.ZERO);
                            bankSerialNumberSet.add(bill.getBankSerialNumber());
                            BigDecimal commissionAmount = Objects.isNull(bill.getCommission()) ? BigDecimal.ZERO : bill.getCommission();
                            long commissionAmountL = -commissionAmount.multiply(new BigDecimal("100")).longValue();
                            commissionBill.setReceivableAmount(0L);
                            commissionBill.setOverdueAmount(0L);
                            commissionBill.setDiscountAmount(0L);
                            commissionBill.setRefundAmount(0L);
                            commissionBill.setCarriedAmount(0L);
                            commissionBill.setSettleAmount(0L);
                            commissionBill.setTaxIncludAmount(commissionAmountL);
                            commissionBill.setTaxExcludAmount(0L);
                        } else {
                            if (!bankSerialNumberSet.contains(bill.getBankSerialNumber())) {
                                BigDecimal commissionAmount = Objects.isNull(bill.getCommission()) ? BigDecimal.ZERO : bill.getCommission();
                                long commissionAmountL = -commissionAmount.multiply(new BigDecimal("100")).longValue();
                                commissionBill.setTaxIncludAmount(commissionBill.getTaxIncludAmount() + commissionAmountL);
                                bankSerialNumberSet.add(bill.getBankSerialNumber());
                            }
                        }
                    }
                    // 账单为违约金时，汇聚成一条明细，原来的明细仍然存在，不过不可见不会推送过去，用于记录状态
                    if (bill.getOverdue() != null && bill.getOverdue() == 1) {
                        bill.setVisible(1); // 该笔明细只做状态留存数据库，不展示也不参与计算
                        if (Objects.isNull(liquidatedDamagesBill)) {
                            liquidatedDamagesBill = Global.mapperFacade.map(bill, PushBusinessBill.class);
                            liquidatedDamagesBill.setVisible(0);
                            liquidatedDamagesBill.setBillId(null);
                            liquidatedDamagesBill.setBillNo(IdentifierFactory.getInstance().serialNumber("voucher_liquidated_damages", "WYJ", 20));
                            liquidatedDamagesBill.setSceneId(null);
                            liquidatedDamagesBill.setRoomName(null);
                            liquidatedDamagesBill.setRoomId(null);
                            liquidatedDamagesBill.setAccountDate(null);
                            liquidatedDamagesBill.setPayChannel(null);
                            liquidatedDamagesBill.setCommission(null);
                            liquidatedDamagesBill.setChargeItemId(liquidatedDamages.getId());
                            liquidatedDamagesBill.setChargeItemName(liquidatedDamages.getName());
                        } else {
                            liquidatedDamagesBill.setTaxIncludAmount(liquidatedDamagesBill.getTaxIncludAmount()
                                    + bill.getTaxIncludAmount());
                        }
                    }
                }
                if (Objects.nonNull(commissionBill)) {
                    this.amount(commissionBill, TriggerEventBillTypeEnum.对账核销.getCode());
                    businessBills.add(commissionBill);
                }
                if (Objects.nonNull(liquidatedDamagesBill)) {
                    this.amount(liquidatedDamagesBill, TriggerEventBillTypeEnum.对账核销.getCode());
                    businessBills.add(liquidatedDamagesBill);
                }
            }
        }

    }
}
