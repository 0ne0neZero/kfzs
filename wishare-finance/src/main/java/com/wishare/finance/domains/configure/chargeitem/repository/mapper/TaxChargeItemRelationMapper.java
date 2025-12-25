package com.wishare.finance.domains.configure.chargeitem.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxChargeItemRelationD;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxChargeItemRelationE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yancao
 */
public interface TaxChargeItemRelationMapper extends BaseMapper<TaxChargeItemRelationE> {
    /**
     * 根据商品id获取关联的费项信息
     *
     * @param taxItemIdList 税目id集合
     * @return List
     */
    List<TaxChargeItemRelationD> queryChargeByTaxItemGoodsIdList(@Param(value = "taxItemIdList") List<Long> taxItemIdList);
}
