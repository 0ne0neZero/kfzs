package com.wishare.finance.domains.configure.chargeitem.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxItemE;
import com.wishare.finance.domains.configure.chargeitem.repository.mapper.TaxItemMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * 税目资源库
 *
 * @author yancao
 */
@Repository
public class TaxItemRepository extends ServiceImpl<TaxItemMapper, TaxItemE> {

    /**
     * 根据编码获取税目
     *
     * @param code 编码
     * @return TaxItemE
     */
    public TaxItemE getByCode(String code, Long taxItemId) {
        LambdaQueryWrapper<TaxItemE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TaxItemE::getCode, code);
        queryWrapper.ne(Objects.nonNull(taxItemId) , TaxItemE::getId, taxItemId);
        return getOne(queryWrapper);
    }

    /**
     * 根据编码集合获取税目
     *
     * @param codeList 编码
     * @return TaxItemE
     */
    public List<TaxItemE> getByCodeList(List<String> codeList) {
        LambdaQueryWrapper<TaxItemE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(codeList), TaxItemE::getCode, codeList);
        return list(queryWrapper);
    }
}
