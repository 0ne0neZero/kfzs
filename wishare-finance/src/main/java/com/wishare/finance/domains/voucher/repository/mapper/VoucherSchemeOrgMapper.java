package com.wishare.finance.domains.voucher.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.voucher.entity.VoucherScheme;
import com.wishare.finance.domains.voucher.entity.VoucherSchemeOrg;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 凭证核算方案财务组织关联表 Mapper 接口
 * </p>
 *
 * @author dxclay
 * @since 2023-04-03
 */
@Mapper
public interface VoucherSchemeOrgMapper extends BaseMapper<VoucherSchemeOrg> {

    Page<VoucherSchemeOrg> selectPageBySearch(Page<SearchF<?>> convertMPPage, @Param(Constants.WRAPPER)QueryWrapper<?> queryModel);

    List<VoucherScheme> schemeListByOrgIds(@Param("orgIds") List<Long> orgIds);
}
