package com.wishare.finance.domains.voucher.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.finance.domains.voucher.command.UpdateVoucherSchemeCommand;
import com.wishare.finance.domains.voucher.entity.VoucherScheme;
import com.wishare.finance.domains.voucher.entity.VoucherSchemeOrg;
import com.wishare.finance.domains.voucher.entity.VoucherSchemeOrgOBV;
import com.wishare.finance.domains.voucher.entity.VoucherSchemeRule;
import com.wishare.finance.domains.voucher.entity.VoucherSchemeRuleOBV;
import com.wishare.finance.domains.voucher.repository.VoucherSchemeOrgRepository;
import com.wishare.finance.domains.voucher.repository.VoucherSchemeRepository;
import com.wishare.finance.domains.voucher.repository.VoucherSchemeRuleRepository;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.Global;
import com.wishare.starter.utils.ErrorAssertUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 凭证规则应用id
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/3
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherSchemeDomainService {

    private final VoucherSchemeRepository voucherSchemeRepository;

    private final VoucherSchemeOrgRepository voucherSchemeOrgRepository;
    private final VoucherSchemeRuleRepository voucherSchemeRuleRepository;

    /**
     * 新增核算方案
     * @param voucherScheme
     * @return
     */
    public Long addVoucherScheme(VoucherScheme voucherScheme){
        getVoucherSchemeName( voucherScheme.getName());
        boolean save = voucherSchemeRepository.save(voucherScheme);
        ErrorAssertUtil.isTrueThrow402(save, ErrorMessage.VOUCHER_SCHEME_ADD_ERROR);
        Long voucherSchemeId = voucherScheme.getId();
        List<VoucherSchemeOrgOBV> orgs = voucherScheme.getOrgs();
        if(CollectionUtils.isNotEmpty(orgs)){
            orgs.forEach(org -> org.setVoucherSchemeId(voucherSchemeId));
            voucherSchemeOrgRepository.saveBatch(Global.mapperFacade.mapAsList(orgs, VoucherSchemeOrg.class));
        }
        List<VoucherSchemeRuleOBV> rules = voucherScheme.getRules();
        if(CollectionUtils.isNotEmpty(rules)){
            rules.forEach(rule -> rule.setVoucherSchemeId(voucherSchemeId));
            voucherSchemeRuleRepository.saveBatch(Global.mapperFacade.mapAsList(rules, VoucherSchemeRule.class));
        }
        return voucherSchemeId;
    }
    public void getVoucherSchemeName(String voucherSchemeName) {
        long count = voucherSchemeRepository.count(new LambdaQueryWrapper<VoucherScheme>()
                .eq(VoucherScheme::getName, voucherSchemeName)
                .eq(VoucherScheme::getDeleted, 0));
        ErrorAssertUtil.isTrueThrow403(count <= 0, ErrorMessage.VOUCHER_SCHEME_NAME_EXIST, count);
    }
    /**
     * 更新核算方案
     * @param command
     * @return
     */
    public boolean updateVoucherScheme(UpdateVoucherSchemeCommand command) {
        //查询是否存在
        VoucherScheme voucherScheme = getByIdWithCheck(command.getId());
        if(!command.getName().equals(voucherScheme.getName())){
            getVoucherSchemeName(command.getName());
        }
        Global.mapperFacade.map(command, voucherScheme); //更新数据
        //更新核算方案
        boolean result = voucherSchemeRepository.updateById(voucherScheme);
        List<VoucherSchemeOrgOBV> orgs = voucherScheme.getOrgs();
        if(CollectionUtils.isNotEmpty(orgs)){
            orgs.forEach(org -> org.setVoucherSchemeId(voucherScheme.getId()));
            voucherSchemeOrgRepository.sudOrg(Global.mapperFacade.mapAsList(orgs, VoucherSchemeOrg.class));
        }else {
            voucherSchemeOrgRepository.removeBySchemeId(voucherScheme.getId());
        }
        List<VoucherSchemeRuleOBV> rules = voucherScheme.getRules();
        if(CollectionUtils.isNotEmpty(rules)){
            rules.forEach(rule -> rule.setVoucherSchemeId(voucherScheme.getId()));
            voucherSchemeRuleRepository.sudRule(Global.mapperFacade.mapAsList(rules, VoucherSchemeRule.class));
        }else {
            voucherSchemeRuleRepository.removeBySchemeId(voucherScheme.getId());
        }
        return result;
    }

    public boolean enableScheme(Long voucherSchemeId, Integer disabled) {
        VoucherScheme voucherScheme = getByIdWithCheck(voucherSchemeId);
        return voucherSchemeRepository.updateByDisabled(voucherScheme.getId(), disabled);
    }

    public boolean deleteScheme(Long voucherSchemeId) {
        VoucherScheme voucherScheme = getByIdWithCheck(voucherSchemeId);
        return voucherSchemeRepository.removeById(voucherScheme.getId());
    }

    public VoucherScheme getByIdWithCheck(Long voucherSchemeId){
        VoucherScheme voucherScheme = voucherSchemeRepository.getById(voucherSchemeId);
        ErrorAssertUtil.notNullThrow403(voucherScheme, ErrorMessage.VOUCHER_SCHEME_NOT_EXIST);
        return voucherScheme;
    }

    public boolean relateRule(Long voucherRuleId, List<Long> voucherSchemeIds) {
        //为空则删除
        if (CollectionUtils.isEmpty(voucherSchemeIds)){
            return voucherSchemeRuleRepository.removeByRuleId(voucherRuleId);
        }

        List<VoucherSchemeRule> voucherSchemeRules = new ArrayList<>();
        for (Long voucherSchemeId : voucherSchemeIds) {
            voucherSchemeRules.add(new VoucherSchemeRule().setVoucherRuleId(voucherRuleId).setVoucherSchemeId(voucherSchemeId));
        }
        return voucherSchemeRuleRepository.sudScheme(voucherSchemeRules);
    }
}
