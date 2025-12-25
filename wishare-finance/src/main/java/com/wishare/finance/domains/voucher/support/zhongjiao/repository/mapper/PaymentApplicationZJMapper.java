package com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.pushbill.vo.PaymentApplicationFormZJV;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.PaymentApplicationFormZJ;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaymentApplicationZJMapper extends BaseMapper<PaymentApplicationFormZJ> {


    Page<PaymentApplicationFormZJ> selectBySearch(Page<SearchF<?>> searchFPage, QueryWrapper<?> ew);

    PaymentApplicationFormZJV getDetailById(@Param("id") String id);

    PaymentApplicationFormZJ selectByCertainSettlementId(@Param("settlementId") String settlementId);

    //根据信息台ID获取数据
    PaymentApplicationFormZJ getDetailByBpmProcessId(@Param("bpmProcessId") String bpmProcessId);
}
