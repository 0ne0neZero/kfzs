package com.wishare.finance.domains.voucher.strategy;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
 * 手动触发冲销作废即时触发策略
 */
@Service
@Slf4j
public class CancellationAndCancellationStrategy extends ManualVoucherStrategy<ManualVoucherStrategyCommand>{


    public CancellationAndCancellationStrategy() {
        super(VoucherEventTypeEnum.作废);
    }

    @Override
    public List<Voucher> makeVoucher(VoucherRule rule, VoucherTemplate template, VoucherMakeError voucherMakeError,
                                     List<VoucherBusinessBill> businessBills, List<VoucherBusinessDetail> voucherBusinessDetails,
                                     VoucherRuleRecord voucherRuleRecord) {
        //返回结果集
        List<Voucher> resultVouchers = new ArrayList<>();
        for (VoucherBusinessBill businessBill : businessBills) {

            List<VoucherBusinessDetail> voucherBusinessDetailsList = voucherBusinessDetailRepository.getBaseMapper()
                    .selectList(Wrappers.<VoucherBusinessDetail>lambdaQuery()
                            .eq(VoucherBusinessDetail::getDeleted,0)
                            .eq(VoucherBusinessDetail::getAccountBookId,businessBill.getAccountBookId())
                            .eq(VoucherBusinessDetail::getBusinessBillId,businessBill.getBusinessBillId())
                            .eq(VoucherBusinessDetail::getSceneType,VoucherEventTypeEnum.应收计提.getCode())
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
                voucherBusinessBill.setSceneType(VoucherEventTypeEnum.作废.getCode());
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
                details.forEach(detail -> detail.setSummary("作废冲回收入-" + detail.getSummary()));
            });
        }
        return vouchers;
    }

    @Override
    public List<VoucherBusinessBill> businessBills(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        return voucherFacade.getCancellationBillList(command,conditions);
    }
}
