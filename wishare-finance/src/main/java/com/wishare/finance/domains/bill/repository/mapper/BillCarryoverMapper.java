package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.entity.BillCarryoverE;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.PushBusinessBill;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账单结转明细
 * @Author dxclay
 * @Date 2022/9/13
 * @Version 1.0
 */
@Mapper
public interface BillCarryoverMapper extends BaseMapper<BillCarryoverE> {

    /**
     * 分页查询账单结转明细记录
     * @param page 分页参数
     * @param wrapper wrapper
     * @return {@link Page}
     */
    Page<BillCarryoverE> queryPageBySearch(@Param("page") Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> wrapper);


    List<PushBusinessBill> getBillIdByList(@Param("billId")Long billId , String supCpUnitId);
}
