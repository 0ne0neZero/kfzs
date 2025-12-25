package com.wishare.finance.domains.configure.chargeitem.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxItemGoodsD;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxItemGoodsE;
import org.apache.ibatis.annotations.Param;

/**
 * @author yancao
 */
public interface TaxItemGoodsMapper extends BaseMapper<TaxItemGoodsE> {


    /**
     * 分页查询税收商品信息
     *
     * @param page         page
     * @param queryWrapper queryWrapper
     * @return Page
     */
    Page<TaxItemGoodsD> queryTaxItemGoodsByPage(Page<TaxItemGoodsE> page, @Param(Constants.WRAPPER) Wrapper<TaxItemGoodsE> queryWrapper);
}
