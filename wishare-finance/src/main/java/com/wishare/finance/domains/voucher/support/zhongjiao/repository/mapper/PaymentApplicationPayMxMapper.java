package com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.pushbill.vo.PaymentApplicationZFDetailV;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.PaymentApplicationFormPayMx;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaymentApplicationPayMxMapper extends BaseMapper<PaymentApplicationFormPayMx> {


    Page<PaymentApplicationZFDetailV> selectZFBySearch(Page<SearchF<?>> searchFPage,@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper);
}
