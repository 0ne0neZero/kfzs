package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.vo.BillCarryV;
import com.wishare.finance.domains.bill.entity.BillCarryoverE;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillCarryMapper
 * @date 2024.05.21  17:09
 * @description:账单结转
 */
@Mapper
public interface BillCarryMapper extends BaseMapper<BillCarryoverE> {


    /**
     * 分页查询结转记录
     * @param page
     * @param wrapper
     * @return
     */
    Page<BillCarryV> queryPageBySearch(@Param("page") Page<SearchF<?>> page,@Param(Constants.WRAPPER) QueryWrapper<?> wrapper);

    Page<BillCarryV> queryCarryoverPage(
            @Param("tableName") String tableName,
            @Param("page") Page<SearchF<?>> page,
            @Param(Constants.WRAPPER) QueryWrapper<?> wrapper
    );

}
