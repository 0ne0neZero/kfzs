package com.wishare.finance.domains.voucher.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.voucher.entity.VoucherTemplate;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 凭证模板表 Mapper 接口
 * </p>
 *
 * @author dxclay
 * @since 2023-04-04
 */
@Mapper
public interface VoucherTemplateMapper extends BaseMapper<VoucherTemplate> {

    Page<VoucherTemplate> selectPageBySearch(Page<?> page, @Param(Constants.WRAPPER) QueryWrapper<?> putLogicDeleted);
}
