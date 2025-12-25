package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.entity.BillPrepayInfoE;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillPrepayInfoMapper
 * @date 2023.11.08  10:22
 * @description:账单预支付信息 mapper
 */
@Mapper
public interface BillPrepayInfoMapper extends BaseMapper<BillPrepayInfoE> {


    /**
     * 分页查询预支付查询记录
     * @param page 分页参数
     * @param wrapper wrapper
     * @return {@link Page}<>{@link BillPrepayInfoE}</>
     */
    @InterceptorIgnore(tenantLine = "on")
    Page<BillPrepayInfoE> queryPageBySearch(@Param("page") Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);
}
