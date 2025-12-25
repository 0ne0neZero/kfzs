package com.wishare.finance.domains.voucher.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.voucher.entity.VoucherRule;
import com.wishare.finance.domains.voucher.repository.mapper.VoucherRuleMapper;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 凭证规则表 服务实现类
 * </p>
 *
 * @author dxclay
 * @since 2023-04-04
 */
@Service
public class VoucherRuleRepository extends ServiceImpl<VoucherRuleMapper, VoucherRule> {

    public Page<VoucherRule> getPage(PageF<SearchF<?>> searchFPageF) {
        return baseMapper.selectPageBySearch(RepositoryUtil.convertMPPage(searchFPageF), RepositoryUtil.putLogicDeleted(searchFPageF.getConditions().getQueryModel()));
    }

    public Page<VoucherRule> getPageByScheme(PageF<SearchF<?>> searchFPageF) {
        return baseMapper.selectPageBySchemeSearch(RepositoryUtil.convertMPPage(searchFPageF),
                RepositoryUtil.putLogicDeleted(searchFPageF.getConditions().getQueryModel(), "vr"));
    }

    public boolean updateDisabledById(Long id, Integer disabled) {
        return update(new LambdaUpdateWrapper<VoucherRule>().set(VoucherRule::getDisabled, disabled).eq(VoucherRule::getId, id));
    }

    public boolean updateExecuteStateById(Long id, Integer executeState) {
        return update(new LambdaUpdateWrapper<VoucherRule>().set(VoucherRule::getDisabled, executeState).eq(VoucherRule::getId, id));
    }

    public VoucherRule getByName(String name) {
        return getOne(new LambdaQueryWrapper<VoucherRule>().eq(VoucherRule::getRuleName, name));
    }
}
