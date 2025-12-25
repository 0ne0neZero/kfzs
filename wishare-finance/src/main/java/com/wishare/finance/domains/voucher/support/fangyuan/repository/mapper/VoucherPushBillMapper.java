package com.wishare.finance.domains.voucher.support.fangyuan.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.pushbill.vo.VoucherBillMoneyV;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBill;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VoucherPushBillMapper extends BaseMapper<VoucherBill> {

    /**
     * 分页查询凭证明细
     * @param page 分页信息
     * @param queryWrapper 查询条件
     * @return
     */
    Page<VoucherBill> selectBySearch(Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    void delete(@Param("id") Long id);

    VoucherBillMoneyV getMoney(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
}
