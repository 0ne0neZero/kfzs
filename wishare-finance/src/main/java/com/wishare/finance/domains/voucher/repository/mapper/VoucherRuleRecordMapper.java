package com.wishare.finance.domains.voucher.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.voucher.entity.VoucherRuleRecord;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 凭证规则运行记录表 Mapper 接口
 * </p>
 *
 * @author dxclay
 * @since 2023-03-10
 */
@Mapper
public interface VoucherRuleRecordMapper extends BaseMapper<VoucherRuleRecord> {

    /**
     * 分页查询运行记录
     * @param page 分页信息
     * @param queryModel 查询条件
     * @return 分页信息
     */
    Page<VoucherRuleRecord> selectPageBySearch(Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryModel);
}
