package com.wishare.finance.domains.configure.chargeitem.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.apps.model.configure.chargeitem.vo.TaxRateV;
import com.wishare.finance.domains.configure.chargeitem.dto.tax.TaxRateDetailD;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxRateE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TaxRateMapper extends BaseMapper<TaxRateE> {


    /**
     * 根据税率id获取税率详情
     *
     * @param id
     * @return
     */
    TaxRateDetailD rateDetail(@Param("id") Long id);

    /**
     *
     * @param taxRates
     * @return
     */
    int saveOrUpdateBatch(@Param("trs") List<TaxRateE> taxRates);

    /**
     * 根据编码和名称查询税率
     * @param code 税率编码
     * @param name 税率名称
     * @return 税率列表
     */
    List<TaxRateE> selectByCodeAndName(@Param("code") String code, @Param("code") String name);


    /**
     * 根据税种编码和税率查询税率信息
     * @param categoryCode 税种编码
     * @param rate 税率值
     * @return 税率信息
     */
    TaxRateE selectByCategoryRate(@Param("categoryCode") String categoryCode, @Param("rate") BigDecimal rate);

    List<TaxRateV> BPMFilterTaxList(@Param("taxCategoryCode") String taxCategoryCode);
}
