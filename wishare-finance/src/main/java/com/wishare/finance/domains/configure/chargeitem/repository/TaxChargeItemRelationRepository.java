package com.wishare.finance.domains.configure.chargeitem.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxChargeItemRelationD;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxChargeItemRelationE;
import com.wishare.finance.domains.configure.chargeitem.repository.mapper.TaxChargeItemRelationMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * 税目资源库
 *
 * @author yancao
 */
@Repository
public class TaxChargeItemRelationRepository extends ServiceImpl<TaxChargeItemRelationMapper, TaxChargeItemRelationE> {

    /**
     * 根据费项id集合获取关联关系
     *
     * @param chargeItemIdList 费项id集合
     * @param taxItemId 税收商品id
     * @return List
     */
    public List<TaxChargeItemRelationE> queryByChargeItemIdList(List<Long> chargeItemIdList, Long taxItemId) {
        LambdaQueryWrapper<TaxChargeItemRelationE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TaxChargeItemRelationE::getChargeItemId, chargeItemIdList);
        queryWrapper.ne(Objects.nonNull(taxItemId), TaxChargeItemRelationE::getTaxItemId , taxItemId );
        return list(queryWrapper);
    }

    /**
     * 根据费项id集合获取关联关系
     *
     * @param chargeItemIdList 费项id集合
     * @param taxItemGoodsId 税收商品id
     * @return List
     */
    public List<TaxChargeItemRelationE> queryByGoodsList(List<Long> chargeItemIdList, Long taxItemGoodsId) {
        LambdaQueryWrapper<TaxChargeItemRelationE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TaxChargeItemRelationE::getChargeItemId, chargeItemIdList);
        queryWrapper.ne(Objects.nonNull(taxItemGoodsId), TaxChargeItemRelationE::getTaxItemGoodsId , taxItemGoodsId );
        return list(queryWrapper);
    }

    /**
     * 根据商品id获取关联关系
     *
     * @param taxItemGoodsIdList 商品id集合
     * @return List
     */
    public List<TaxChargeItemRelationE> queryByTaxItemIdList(List<Long> taxItemGoodsIdList) {
        LambdaQueryWrapper<TaxChargeItemRelationE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TaxChargeItemRelationE::getTaxItemGoodsId, taxItemGoodsIdList);
        return list(queryWrapper);
    }

    /**
     * 根据商品id获取关联的费项信息
     *
     * @param taxItemGoodsIdList 商品id集合
     * @return List
     */
    public List<TaxChargeItemRelationD> queryChargeByTaxItemGoodsIdList(List<Long> taxItemGoodsIdList) {
        return baseMapper.queryChargeByTaxItemGoodsIdList(taxItemGoodsIdList);
    }

    /**
     * 根据税收商品id删除关联关系
     *
     * @param id 税收商品id
     * @return Boolean
     */
    public Boolean removeByTaxItemId(Long id) {
        LambdaQueryWrapper<TaxChargeItemRelationE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TaxChargeItemRelationE::getTaxItemGoodsId, id);
        return remove(queryWrapper);
    }
}
