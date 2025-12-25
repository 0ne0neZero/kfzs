package com.wishare.finance.domains.voucher.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.voucher.dto.VoucherSchemeRuleDto;
import com.wishare.finance.domains.voucher.entity.VoucherScheme;
import com.wishare.finance.domains.voucher.entity.VoucherSchemeRule;
import com.wishare.finance.domains.voucher.entity.VoucherSchemeRuleOBV;
import com.wishare.finance.domains.voucher.repository.mapper.VoucherSchemeRuleMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 凭证核算方案凭证规则关联表 服务实现类
 * </p>
 *
 * @author dxclay
 * @since 2023-04-03
 */
@Service
public class VoucherSchemeRuleRepository extends ServiceImpl<VoucherSchemeRuleMapper, VoucherSchemeRule> {


    /**
     * 根据规则新增、删除、或修改更新
     * @param rules 组织信息
     * @return 结果
     */
    public boolean sudScheme(List<VoucherSchemeRule> rules){
        Long voucherRuleId = rules.get(0).getVoucherRuleId();
        List<VoucherSchemeRule> voucherRuleOrgs = listByRuleIds(List.of(voucherRuleId));
        Map<Long, VoucherSchemeRule> schemeRuleMap = new HashMap<>();
        for (VoucherSchemeRule voucherSchemeRule : voucherRuleOrgs) {
            schemeRuleMap.put(voucherSchemeRule.getVoucherSchemeId(), voucherSchemeRule);
        }
        List<VoucherSchemeRule> saveOrUpdateRules = new ArrayList<>();
        for (VoucherSchemeRule schemeRule : rules) {
            if (schemeRuleMap.containsKey(schemeRule.getVoucherSchemeId())){
                schemeRule.setId(schemeRuleMap.get(schemeRule.getVoucherSchemeId()).getId());
                schemeRuleMap.remove(schemeRule.getVoucherSchemeId());
            }
            saveOrUpdateRules.add(schemeRule);
        }
        boolean result = saveOrUpdateBatch(saveOrUpdateRules);
        if (!schemeRuleMap.isEmpty()){
            result = remove(new LambdaUpdateWrapper<VoucherSchemeRule>().eq(VoucherSchemeRule::getVoucherRuleId, voucherRuleId)
                    .in(VoucherSchemeRule::getVoucherSchemeId, schemeRuleMap.values().stream().map(VoucherSchemeRule::getVoucherSchemeId).collect(Collectors.toList())));
        }
        return result;
    }

    /**
     * 根据规则新增、删除、或修改更新
     * @param rules 组织信息
     * @return 结果
     */
    public boolean sudRule(List<VoucherSchemeRule> rules){
        Long voucherSchemeId = rules.get(0).getVoucherSchemeId();
        List<VoucherSchemeRule> voucherRuleOrgs = listBySchemeId(voucherSchemeId);
        Map<Long, VoucherSchemeRule> schemeRuleMap = new HashMap<>();
        for (VoucherSchemeRule voucherSchemeRule : voucherRuleOrgs) {
            schemeRuleMap.put(voucherSchemeRule.getVoucherRuleId(), voucherSchemeRule);
        }
        List<VoucherSchemeRule> saveOrUpdateRules = new ArrayList<>();
        for (VoucherSchemeRule schemeRule : rules) {
            if (schemeRuleMap.containsKey(schemeRule.getVoucherRuleId())){
                schemeRule.setId(schemeRuleMap.get(schemeRule.getVoucherRuleId()).getId());
                schemeRuleMap.remove(schemeRule.getVoucherRuleId());
            }
            saveOrUpdateRules.add(schemeRule);
        }
        boolean result = saveOrUpdateBatch(saveOrUpdateRules);
        if (!schemeRuleMap.isEmpty()){
            result = remove(new LambdaUpdateWrapper<VoucherSchemeRule>().eq(VoucherSchemeRule::getVoucherSchemeId, voucherSchemeId)
                    .in(VoucherSchemeRule::getVoucherRuleId, schemeRuleMap.values().stream().map(VoucherSchemeRule::getVoucherRuleId).collect(Collectors.toList())));
        }
        return result;
    }

    public boolean removeByRuleIds(Long voucherSchemeId, List<Long> ruleIds) {
        if (CollectionUtils.isEmpty(ruleIds)){
            return false;
        }
        return remove(new LambdaUpdateWrapper<VoucherSchemeRule>().eq(VoucherSchemeRule::getVoucherSchemeId, voucherSchemeId).in(VoucherSchemeRule::getVoucherRuleId,ruleIds));
    }

    public boolean removeByRuleId(Long voucherRuleId) {
        return remove(new LambdaUpdateWrapper<VoucherSchemeRule>().eq(VoucherSchemeRule::getVoucherRuleId, voucherRuleId));
    }

    public List<VoucherSchemeRule> listByRuleIds(List<Long> ruleIds) {
        return CollectionUtils.isEmpty(ruleIds) ? new ArrayList<>() : list(new LambdaQueryWrapper<VoucherSchemeRule>().in(VoucherSchemeRule::getVoucherRuleId, ruleIds));
    }

    public List<VoucherSchemeRuleDto> dtoLListByRuleIds(List<Long> ruleIds) {
        return CollectionUtils.isEmpty(ruleIds) ? new ArrayList<>() : baseMapper.selectByRuleIds(ruleIds);
    }

    public List<VoucherSchemeRule> listBySchemeId(Long voucherSchemeId) {
        return list(new LambdaQueryWrapper<VoucherSchemeRule>().eq(VoucherSchemeRule::getVoucherSchemeId, voucherSchemeId));
    }
    public List<VoucherSchemeRuleOBV> obvListBySchemeId(Long voucherSchemeId) {
        return baseMapper.selectOBVSBySchemeId(voucherSchemeId);
    }

    public boolean removeBySchemeId(Long voucherSchemeId) {
        return remove(new LambdaUpdateWrapper<VoucherSchemeRule>().eq(VoucherSchemeRule::getVoucherSchemeId, voucherSchemeId));
    }

    public List<VoucherScheme> getSchemeByRuleId(Long ruleId){
        return baseMapper.selectSchemesByRuleId(ruleId);
    }

}
