package com.wishare.finance.domains.voucher.strategy;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.*;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategy;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 冲销-触发策略
 */
@Service
@Slf4j
public class ChargeAgainstStrategy extends ManualVoucherStrategy<ManualVoucherStrategyCommand>{


    public ChargeAgainstStrategy() {
        super(VoucherEventTypeEnum.冲销);
    }

    @Override
    public List<Voucher> makeVoucher(VoucherRule rule, VoucherTemplate template, VoucherMakeError voucherMakeError,
                                     List<VoucherBusinessBill> businessBills, List<VoucherBusinessDetail> voucherBusinessDetails,
                                     VoucherRuleRecord voucherRuleRecord) {
        //返回结果集
        List<Voucher> resultVouchers = new ArrayList<>();
        for (VoucherBusinessBill businessBill : businessBills) {

            if (StrUtil.isBlank(businessBill.getSceneId())){
                log.error(VoucherEventTypeEnum.冲销.getValue()+"异常，sceneId为空,入参:{}", JSONObject.toJSONString(businessBill));
                continue;
            }

            List<VoucherBusinessDetail> voucherBusinessDetailsList = voucherBusinessDetailRepository.getBaseMapper()
                    .selectList(Wrappers.<VoucherBusinessDetail>lambdaQuery()
                            .eq(VoucherBusinessDetail::getDeleted,0)
                            .eq(VoucherBusinessDetail::getSceneId, Long.valueOf(businessBill.getSceneId()))
                            .eq(VoucherBusinessDetail::getAccountBookId,businessBill.getAccountBookId())
                            .eq(VoucherBusinessDetail::getSceneType,VoucherEventTypeEnum.收款结算.getCode())
                            .groupBy(VoucherBusinessDetail::getSceneId)
                    );

            //查出每个凭证明细所对应的凭证
            for (int i = 0; i < voucherBusinessDetailsList.size(); i++) {
                VoucherBusinessDetail detail = voucherBusinessDetailsList.get(i);
                Long voucherId = detail.getVoucherId();
                LambdaQueryWrapper<Voucher> voucherLambdaQueryWrapper = new LambdaQueryWrapper<>();
                voucherLambdaQueryWrapper.eq(Voucher::getId,voucherId);
                //得到凭证
                Voucher voucher = voucherInfoRepository.getBaseMapper().selectOne(voucherLambdaQueryWrapper);
                //根据凭证推凭记录id查出相应的规则
                LambdaQueryWrapper<VoucherRuleRecord> voucherRuleRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
                voucherRuleRecordLambdaQueryWrapper.eq(VoucherRuleRecord::getId,voucher.getVoucherRuleRecordId());
                //得到对应的推凭记录
                VoucherRuleRecord voucherRuleRecords = voucherRuleRecordRepository.getBaseMapper().selectOne(voucherRuleRecordLambdaQueryWrapper);
                LambdaQueryWrapper<VoucherRule> voucherRuleLambdaQueryWrapper = new LambdaQueryWrapper<>();
                voucherRuleLambdaQueryWrapper.eq(VoucherRule::getId,voucherRuleRecords.getVoucherRuleId());
                //得到规则
                VoucherRule voucherRule = voucherRuleRepository.getBaseMapper().selectOne(voucherRuleLambdaQueryWrapper);
                //查询凭证关联的模板信息
                LambdaQueryWrapper<VoucherTemplate> voucherTemplateLambdaQueryWrapper = new LambdaQueryWrapper<>();
                voucherTemplateLambdaQueryWrapper.eq(VoucherTemplate::getId,voucherRule.getVoucherTemplateId());
                VoucherTemplate voucherTemplate = voucherTemplateRepository.getBaseMapper().selectOne(voucherTemplateLambdaQueryWrapper);
                voucherTemplate.setRedVoucher(1);

                List<VoucherBusinessBill> details = new ArrayList<>();
                VoucherBusinessBill voucherBusinessBill = JSON.parseObject(detail.getBusinessBillDetails(), VoucherBusinessBill.class);
                voucherBusinessBill.setSceneType(VoucherEventTypeEnum.冲销.getCode());
                details.add(voucherBusinessBill);
                resultVouchers.addAll(super.makeVoucher(voucherRule, voucherTemplate, voucherMakeError, details, voucherBusinessDetails, voucherRuleRecord));
            }
        }
        return addSummary(mergeVoucher(resultVouchers, voucherBusinessDetails));
    }

    public List<Voucher> addSummary(List<Voucher> vouchers) {
        if (CollectionUtils.isNotEmpty(vouchers)) {
            vouchers.forEach(voucher -> {
                List<VoucherDetailOBV> details = voucher.getDetails();
                details.forEach(detail -> detail.setSummary("冲销收款-" + detail.getSummary()));
            });
        }
        return vouchers;
    }

    @Override
    public List<VoucherBusinessBill> businessBills(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        return voucherFacade.getChargeAgainstBillList(command,conditions,VoucherEventTypeEnum.收款结算.getCode(),VoucherEventTypeEnum.冲销.getCode());
    }
}
