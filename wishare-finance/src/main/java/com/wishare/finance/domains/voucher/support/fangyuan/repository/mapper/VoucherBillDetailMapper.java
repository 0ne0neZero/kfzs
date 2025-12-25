package com.wishare.finance.domains.voucher.support.fangyuan.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.pushbill.vo.VoucherBillDetailMoneyV;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherPushBillDetail;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VoucherBillDetailMapper extends BaseMapper<VoucherPushBillDetail> {
    Page<VoucherPushBillDetail> selectBySearch(Page<SearchF<?>> page, @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);

    List<VoucherPushBillDetail> queryVoucherPushBillDetailInfo(@Param("billIdList")List<Long> billIdList);

    void deleteBatch(@Param("ids")List<Long> ids);

    VoucherBillDetailMoneyV queryMoney(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
}
