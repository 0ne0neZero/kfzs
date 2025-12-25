package com.wishare.finance.domains.voucher.support.fangyuan.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.BillRule;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 推单规则表 Mapper 接口
 */
@Mapper
public interface BillRuleMapper extends BaseMapper<BillRule> {
    Page<BillRule> selectPageBySearch(Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> putLogicDeleted);

    List<BillRule> selectBySearch(@Param(Constants.WRAPPER) QueryWrapper<?> putLogicDeleted);

}
