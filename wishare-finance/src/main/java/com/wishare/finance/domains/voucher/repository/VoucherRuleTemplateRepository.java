package com.wishare.finance.domains.voucher.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.voucher.entity.VoucherRuleTemplate;
import com.wishare.finance.domains.voucher.repository.mapper.VoucherRuleTemplateMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 凭证规则凭证模板关联表 资源库
 *
 * @author dxclay
 * @since 2023-04-04
 */
@Service
public class VoucherRuleTemplateRepository extends ServiceImpl<VoucherRuleTemplateMapper, VoucherRuleTemplate> {

    public List<VoucherRuleTemplate> getRuleTemps(Long voucherTemplateId) {
        return Objects.nonNull(voucherTemplateId) ? new ArrayList<>() :
                list(new LambdaQueryWrapper<VoucherRuleTemplate>().eq(VoucherRuleTemplate::getVoucherTemplateId, voucherTemplateId));
    }
}
