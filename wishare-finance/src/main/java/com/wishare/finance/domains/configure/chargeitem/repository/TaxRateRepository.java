package com.wishare.finance.domains.configure.chargeitem.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.configure.chargeitem.vo.TaxRateV;
import com.wishare.finance.domains.configure.chargeitem.dto.tax.TaxRateDetailD;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxRateE;
import com.wishare.finance.domains.configure.chargeitem.repository.mapper.TaxRateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Service
public class TaxRateRepository extends ServiceImpl<TaxRateMapper, TaxRateE> {

    @Autowired
    private TaxRateMapper taxRateMapper;

    /**
     * 根据税率id获取税率详情
     *
     * @param id
     * @return
     */
    public TaxRateDetailD rateDetail(Long id) {
        return taxRateMapper.rateDetail(id);
    }


    /**
     * 批量新增或更新
     * @param taxRates
     * @return
     */
    public boolean saveOrUpdateBatchByCode(List<TaxRateE> taxRates){
        return taxRateMapper.saveOrUpdateBatch(taxRates) > 0;
    }

    /**
     * 根据编码和名称查询税率
     * @param code 税率编码
     * @param name 税率名称
     * @return 税率列表
     */
    public List<TaxRateE> getByCodeAndName(String code, String name) {
        return taxRateMapper.selectByCodeAndName(code, name);
    }

    /**
     * 根据税率获取增值税税率
     * @param rate 税率值
     * @return 税率
     */
    public TaxRateE getByRate(BigDecimal rate) {
        return getOne(new LambdaQueryWrapper<TaxRateE>().eq(TaxRateE::getRate, rate), false);
    }

    public List<TaxRateE> listWithSize(int size) {
        return list(new LambdaQueryWrapper<TaxRateE>().last("limit " + size));
    }

    /**
     * 根据税种编码和税率查询税率信息
     * @param categoryCode 税种编码
     * @param rate 税率值
     * @return 税率信息
     */
    public TaxRateE getByCategoryRate(String categoryCode, BigDecimal rate) {
        return baseMapper.selectByCategoryRate(categoryCode, rate);
    }

    public List<TaxRateV> BPMFilterTaxList(String taxCategoryCode) {
        return baseMapper.BPMFilterTaxList(taxCategoryCode);
    }
}
