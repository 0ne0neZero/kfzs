package com.wishare.contract.domains.service.revision.income;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.service.contractset.ContractPayIncomePlanService;
import com.wishare.contract.domains.entity.contractset.PayIncomePlanE;
import com.wishare.contract.domains.entity.contractset.PayIncomePlanLogE;
import com.wishare.contract.domains.enums.revision.PlanTypeEnum;
import com.wishare.contract.domains.mapper.revision.income.PayIncomePlanLogMapper;
import com.wishare.contract.domains.vo.revision.income.PayIncomePlanV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.helpers.UidHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 收入合同服务 不使用@Transactional注解 统一使用编程式事务TransactionTemplate
 *
 * @author zhangfuyu
 * @mender 龙江锋
 * @Date 2023/7/6/13:57
 */
@Service
@Slf4j
public class PayIncomePlanLogService extends ServiceImpl<PayIncomePlanLogMapper, PayIncomePlanLogE> implements IOwlApiBase {

    @Autowired
    private ContractPayIncomePlanService contractPayIncomePlanService;

    /**
     * 记录收入计划的调整日志
     * @param payIncomePlanV
     */
    public void saveIncomePlanLogType0(PayIncomePlanV payIncomePlanV) {
        PayIncomePlanLogE payIncomePlanLogE = new PayIncomePlanLogE();
        payIncomePlanLogE.setId(UidHelper.nextIdStr("pay_income_plan_log"));
        payIncomePlanLogE.setPlanId(payIncomePlanV.getPlanId());
        payIncomePlanLogE.setIncomePayPlanId(payIncomePlanV.getIncomePlanId());
        payIncomePlanLogE.setContractId(payIncomePlanV.getContractId());
        PayIncomePlanE payIncomePlanE = contractPayIncomePlanService.getById(payIncomePlanV.getPlanId());
        payIncomePlanLogE.setOldConfirmedAmountReceivedTemp(payIncomePlanE.getConfirmedAmountReceivedTemp());
        payIncomePlanLogE.setNewConfirmedAmountReceivedTemp(payIncomePlanV.getConfirmedAmountReceivedTemp());
        payIncomePlanLogE.setOldTaxAmount(payIncomePlanE.getTaxAmount());
        payIncomePlanLogE.setNewTaxAmount(payIncomePlanV.getTaxAmount());
        payIncomePlanLogE.setOldAdjustment(payIncomePlanE.getAdjustment());
        payIncomePlanLogE.setNewAdjustment(payIncomePlanV.getAdjustment());
        payIncomePlanLogE.setPlanType(PlanTypeEnum.INCOME.getCode());
        this.save(payIncomePlanLogE);
    }

}
