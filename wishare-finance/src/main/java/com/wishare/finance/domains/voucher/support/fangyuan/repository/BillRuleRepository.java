package com.wishare.finance.domains.voucher.support.fangyuan.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.BillRule;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.mapper.BillRuleMapper;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillRuleRepository extends ServiceImpl<BillRuleMapper, BillRule> {
    public BillRule getByName(String ruleName) {
        return getOne(new LambdaQueryWrapper<BillRule>().eq(BillRule::getRuleName, ruleName));
    }
    public Page<BillRule> getPage(PageF<SearchF<?>> searchFPageF) {
        return baseMapper.selectPageBySearch(RepositoryUtil.convertMPPage(searchFPageF), RepositoryUtil.putLogicDeleted(searchFPageF.getConditions().getQueryModel()));
    }

    public List<BillRule> selectBySearch(PageF<SearchF<?>> searchFPageF) {
        return baseMapper.selectBySearch( RepositoryUtil.putLogicDeleted(searchFPageF.getConditions().getQueryModel()));
    }

    public boolean updateExecuteStateById(Long id, int executeState) {
        return update(new LambdaUpdateWrapper<BillRule>().set(BillRule::getExecuteState, executeState).eq(BillRule::getId, id));
    }
}
