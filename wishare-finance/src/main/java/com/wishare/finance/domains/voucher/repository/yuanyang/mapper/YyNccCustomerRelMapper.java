package com.wishare.finance.domains.voucher.repository.yuanyang.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.voucher.entity.yuanyang.YyNccCustomerRelE;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface YyNccCustomerRelMapper extends BaseMapper<YyNccCustomerRelE> {

    String getNccCustomerCode(@Param("creditCode") String creditCode);

    Page<YyNccCustomerRelE> selectBySearch(Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
}
