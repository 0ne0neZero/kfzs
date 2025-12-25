package com.wishare.finance.domains.voucher.service;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.finance.apis.common.FinanceConstants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wishare.finance.apis.common.FinanceConstants;
import com.wishare.finance.apps.model.voucher.fo.SyncBatchVoucherF;
import com.wishare.finance.apps.model.voucher.vo.SyncBatchVoucherResultV;
import com.wishare.finance.apps.service.voucher.VoucherAppService;
import com.wishare.finance.domains.configure.chargeitem.consts.enums.ChargeItemAttributeEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.finance.domains.voucher.consts.enums.*;
import com.wishare.finance.domains.voucher.entity.*;
import com.wishare.finance.domains.voucher.facade.VoucherFacade;
import com.wishare.finance.domains.voucher.repository.VoucherRuleRecordRepository;
import com.wishare.finance.domains.voucher.repository.VoucherRuleRepository;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.repository.mapper.VoucherInfoMapper;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.enums.BillSettleStateEnum;
import com.wishare.finance.infrastructure.remote.enums.DeductionMethodEnum;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceCostRv;
import com.wishare.finance.infrastructure.support.thread.AppRunnable;
import com.wishare.finance.infrastructure.support.thread.AppThreadManager;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 凭证规则 领域服务
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/3
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherRuleDomainService {

    private final VoucherFacade voucherFacade;
    private final VoucherRuleRepository voucherRuleRepository;
    private final VoucherInfoMapper voucherInfoMapper;
    private final VoucherAppService voucherAppService;

    /**
     * 新增凭证规则
     *
     * @param voucherRule 凭证规则
     * @return 结果
     */
    public Long addRule(VoucherRule voucherRule) {
        voucherRule.checkParam();
        boolean checkSpecialConditions = checkSpecialConditions(voucherRule);
        if (!checkSpecialConditions){
            log.info(FinanceConstants.CHECK_SPECIAL_CONDITIONS+"条件:{}", JSONObject.toJSONString(voucherRule.getConditions()));
            throw new BizException(300,FinanceConstants.CHECK_SPECIAL_CONDITIONS_MSG);
        }
        //校验名称是否存在相同的数据
        VoucherRule sameRule = voucherRuleRepository.getByName(voucherRule.getRuleName());
        ErrorAssertUtil.isNullThrow301(sameRule, ErrorMessage.VOUCHER_RULE_NAME_EXIST);
        boolean saveRes = voucherRuleRepository.save(voucherRule);
        if (saveRes && voucherRule.registerSchedule()){
            voucherRuleRepository.updateById(voucherRule);
        }
        return voucherRule.getId();
    }



    public static boolean validateSet(Set<String> setA, Set<String> setB) {

        boolean containsElementFromA = false;
        for (String element : setA) {
            if (setB.contains(element)) {
                containsElementFromA = true;
                break;
            }
        }


        if (containsElementFromA) {

            for (String element : setB) {
                if (!setA.contains(element)) {
                    return false;
                }
            }
        }


        return true;
    }

    /**若包含冲销结转，或者结转。不能存在其他类型。
     * @param voucherRule 规则
     * @return false-不符合。
     */
    private boolean checkSpecialConditions(VoucherRule voucherRule){
        AtomicBoolean f = new AtomicBoolean(true);
        voucherRule.getConditions().forEach(v->{
            if (v.getType().equals(VoucherRuleConditionTypeEnum.结算方式.getCode())){
                List<VoucherRuleConditionOptionOBV> list = v.getValues();
                Set<String> set = list.stream().map(VoucherRuleConditionOptionOBV::getCode).collect(Collectors.toSet());
                if (CollUtil.isEmpty(set)){
                    return;
                }


                boolean b = validateSet(CollUtil.newHashSet(VoucherPayWayEnum.结转.getCode(), VoucherPayWayEnum.冲销结转.getCode()), set);
                f.set(b);
            }
        });

        return f.get();
    }
    /**
     * 更新凭证规则
     *
     * @param voucherRule 凭证规则
     * @return 结果
     */
    public boolean updateRule(VoucherRule voucherRule) {
        voucherRule.checkParam();
        boolean checkSpecialConditions = checkSpecialConditions(voucherRule);
        if (!checkSpecialConditions){
            log.info(FinanceConstants.CHECK_SPECIAL_CONDITIONS+"条件:{}", JSONObject.toJSONString(voucherRule.getConditions()));
            throw new BizException(300,FinanceConstants.CHECK_SPECIAL_CONDITIONS_MSG);
        }

        VoucherRule repVoucherRule = getByIdWithCheck(voucherRule.getId());
        repVoucherRule.checkExecuteState();
        //校验名称是否存在相同的数据
        VoucherRule sameRule = voucherRuleRepository.getByName(voucherRule.getRuleName());
        ErrorAssertUtil.isTrueThrow402(Objects.isNull(sameRule) || sameRule.getId().equals(voucherRule.getId()), ErrorMessage.VOUCHER_RULE_NAME_EXIST);
        voucherRule.setTenantId(repVoucherRule.getTenantId());
        voucherRule.updateSchedule(repVoucherRule);
        return voucherRuleRepository.updateById(voucherRule);
    }

    /**
     * 启用规则
     *
     * @param voucherTemplateId 凭证规则id
     * @param disabled          是否启用
     * @return 结果
     */
    public boolean enable(Long voucherTemplateId, int disabled) {
        ErrorAssertUtil.notNullThrow403(DataDisabledEnum.valueOfByCode(disabled), ErrorMessage.DISABLE_PARAM_ERROR);
        VoucherRule voucherRule = getByIdWithCheck(voucherTemplateId);
        voucherRule.enableOrDisabled(disabled);
        return voucherRuleRepository.updateDisabledById(voucherRule.getId(), disabled);
    }

    /**
     * 删除凭证规则
     *
     * @param voucherRuleId 凭证规则id
     * @return
     */
    public boolean delete(Long voucherRuleId) {
        VoucherRule voucherRule = getByIdWithCheck(voucherRuleId);
        voucherRule.delete();
        return voucherRuleRepository.removeById(voucherRule.getId());
    }

    public VoucherRule getByIdWithCheck(Long voucherRuleId) {
        VoucherRule voucherRule = voucherRuleRepository.getById(voucherRuleId);
        ErrorAssertUtil.notNullThrow403(voucherRule, ErrorMessage.VOUCHER_RULE_NOT_EXIST);
        return voucherRule;
    }
    public VoucherRule getRuleCheck(Long voucherRuleId) {
        VoucherRule voucherRule = voucherRuleRepository.getOne(new LambdaQueryWrapper<VoucherRule>()
                .eq(VoucherRule::getId,voucherRuleId)
                .eq(VoucherRule::getDisabled,0));
        ErrorAssertUtil.notNullThrow403(voucherRule, ErrorMessage.VOUCHER_RULE_NOT_EXIST);
        return voucherRule;
    }

    /**
     * 手动运行规则
     *
     * @param voucherRuleId
     * @return
     */
    public boolean manualExecute(Long voucherRuleId) {
        VoucherRule voucherRule = getRuleCheck(voucherRuleId);
        AppThreadManager.execute(new AppRunnable() {
            @Override
            public void execute() {
                VoucherRuleRecord record = null;
                try {
                     record = voucherRule.manualExecute();
                } catch (Exception e) {
                    log.error("凭证规则运行失败:{}", e.getMessage(), e);
                }
                // 若是自动推凭证
                autoPushNoException(record,voucherRule);

            }
        });
        return true;
    }
    private void autoPushNoException(VoucherRuleRecord record,VoucherRule voucherRule){
        try {
            if (ObjectUtil.isNull(record)){
                return;
            }
            Integer rulePushMode = voucherRule.getPushMode();
            if (ObjectUtil.isNull(rulePushMode) || rulePushMode!=VoucherRulePushModeEnum.定时推送.getCode()){
                return;
            }
            VoucherScheduleRuleOBV rule = voucherRule.getScheduleRule();
            if (ObjectUtil.isNull(rule)){
                return;
            }

            // 是定时，并且，开启自动推送
            String autoPush = rule.getAutoPush();
            if (StrUtil.isNotBlank(autoPush) && autoPush.equals("1")){
                List<Long> list = voucherInfoMapper.autoPushList(record.getId());
                if (CollUtil.isEmpty(list)){
                    return;
                }
                SyncBatchVoucherF voucherF = new SyncBatchVoucherF();
                voucherF.setVoucherIds(list);
                voucherF.setVoucherSystem(VoucherSystemEnum.用友NCC.getCode());
                SyncBatchVoucherResultV resultV = voucherAppService.syncBatchVoucher(voucherF);
                log.info(FinanceConstants.auto_sync_ncc_push+"-{}", JSONObject.toJSONString(resultV));

            }



        }
        catch (Exception e) {
            e.printStackTrace();
            log.error(FinanceConstants.auto_sync_ncc_push+"存在异常记录信息record-{},规则信息-{},异常:{}",JSONObject.toJSONString(record), JSONObject.toJSONString(voucherRule),e.getMessage());
        }

    }


    /**
     * 查询过滤条件信息
     * @param conditionType 过滤条件类型
     * @return 过滤条件值列表
     */
    public List<VoucherRuleConditionOptionOBV> getConditionOptions(VoucherRuleConditionTypeEnum conditionType) {
        switch (conditionType) {
            case 费项:
                break;
            case 结算方式:
               return Arrays.stream(VoucherPayWayEnum.values()).map(i -> new VoucherRuleConditionOptionOBV(i.getCode(), i.getCode(), i.getValue())).collect(Collectors.toList());
            case 票据类型:
                return voucherFacade.getInvoiceTypesOptions();
            case 单据来源:
                return Arrays.stream(SysSourceEnum.values()).map(i -> new VoucherRuleConditionOptionOBV(String.valueOf(i.getCode()), String.valueOf(i.getCode()), i.getDes())).collect(Collectors.toList());
            case 税率:
                return voucherFacade.getTaxRateOptions();
            case 客商:
                break;
            case 银行流水:
                return Arrays.stream(VoucherRuleFlowTypeEnum.values()).map(i -> new VoucherRuleConditionOptionOBV(String.valueOf(i.getType()), String.valueOf(i.getType()), i.getDesc())).collect(Collectors.toList());
            case 付款银行账户:
                break;
            case 收款银行账户:
                break;
            case 支付渠道:
                return voucherFacade.getPayWayOptions();
            case 计费周期:
                break;
            case 结算时间:
                break;
            case 归属月:
                break;
            case 业务场景:
                return Arrays.stream(VoucherRuleBusinessScenarioEnum.values()).map(i -> new VoucherRuleConditionOptionOBV(String.valueOf(i.getCode()), String.valueOf(i.getCode()), i.getValue())).collect(Collectors.toList());
            case 调整方式:
                return voucherFacade.getAdjustTypeOptions();
            case 应收类型:
                return List.of(VoucherBusinessBillTypeEnum.应收单, VoucherBusinessBillTypeEnum.临时单).stream().map(i -> new VoucherRuleConditionOptionOBV(String.valueOf(i.getCode()), String.valueOf(i.getCode()), i.getValue())).collect(Collectors.toList());
            case 成本中心:
                break;
            case 费项属性:
                return Stream.of(ChargeItemAttributeEnum.收入, ChargeItemAttributeEnum.支出, ChargeItemAttributeEnum.代收代付及其他).map(i -> new VoucherRuleConditionOptionOBV(String.valueOf(i.getCode()), String.valueOf(i.getCode()), i.getDes())).collect(Collectors.toList());
            case 减免形式:
                return Stream.of(DeductionMethodEnum.应收减免, DeductionMethodEnum.实收减免).map(e -> new VoucherRuleConditionOptionOBV(
                        String.valueOf(e.getCode()), String.valueOf(e.getCode()), String.valueOf(e.getDes())
                )).collect(Collectors.toList());
            case 业务类型:
                return voucherFacade.getAssisteBizTypeOptions();
            case 调整类型:
                return voucherFacade.getAdjustWayOptions();
            case 结算状态:
                return Arrays.stream(BillSettleStateEnum.values())
                        .map(i -> new VoucherRuleConditionOptionOBV(String.valueOf(i.getCode()), String.valueOf(i.getCode()), i.getValue()))
                        .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

}
