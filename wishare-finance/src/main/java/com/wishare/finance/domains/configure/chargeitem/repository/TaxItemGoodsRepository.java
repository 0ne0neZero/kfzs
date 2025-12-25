package com.wishare.finance.domains.configure.chargeitem.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxItemGoodsD;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxItemGoodsE;
import com.wishare.finance.domains.configure.chargeitem.repository.mapper.TaxItemGoodsMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * 税目资源库
 *
 * @author yancao
 */
@Repository
public class TaxItemGoodsRepository extends ServiceImpl<TaxItemGoodsMapper, TaxItemGoodsE> {

    /**
     * 根据编码获取税目
     *
     * @param code 编码
     * @return TaxItemE
     */
    public TaxItemGoodsE getByCode(String code, Long taxItemId) {
        LambdaQueryWrapper<TaxItemGoodsE> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(TaxItemGoodsE::getCode, code);
        queryWrapper.ne(Objects.nonNull(taxItemId) , TaxItemGoodsE::getId, taxItemId);
        return getOne(queryWrapper);
    }

    /**
     * 根据编码集合获取税目
     *
     * @param codeList 编码
     * @return TaxItemE
     */
    public List<TaxItemGoodsE> getByCodeList(List<String> codeList) {
        LambdaQueryWrapper<TaxItemGoodsE> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.in(CollectionUtils.isNotEmpty(codeList), TaxItemGoodsE::getCode, codeList);
        return list(queryWrapper);
    }

    /**
     * 分页查询税收商品信息
     *
     * @param page         page
     * @param queryWrapper queryWrapper
     * @return Page
     */
    public Page<TaxItemGoodsD> queryTaxItemGoodsByPage(Page<TaxItemGoodsE> page, Wrapper<TaxItemGoodsE> queryWrapper) {
        return baseMapper.queryTaxItemGoodsByPage(page, queryWrapper);
    }
}
