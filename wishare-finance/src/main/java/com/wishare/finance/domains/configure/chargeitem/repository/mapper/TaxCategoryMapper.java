package com.wishare.finance.domains.configure.chargeitem.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.configure.chargeitem.command.tax.GetTaxCategoryCommand;
import com.wishare.finance.domains.configure.chargeitem.dto.tax.TaxCategoryD;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxCategoryE;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxRateE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaxCategoryMapper extends BaseMapper<TaxCategoryE> {

    /**
     * 根据id获取税种信息,包含税率
     * @param id
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    TaxCategoryD getTaxCategoryById(@Param("id") Long id);

    /**
     * 获取税种详细信息，包含税率
     * @param command
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<TaxCategoryD> getTaxCategoryList(@Param("command") GetTaxCategoryCommand command);


    /**
     * 根据条件获取税种信息
     *
     * @param command
     * @return
     */
    List<TaxCategoryE> getTaxCategory(@Param("command") GetTaxCategoryCommand command);

    /***
     * 根据税种id获取税率
     *
     * @param taxCategoryId
     * @return
     */
    List<TaxRateE> getTaxRate(@Param("taxCategoryId")Long taxCategoryId);

    /**
     * 批量新增或更新
     * @param taxCategorys
     * @return
     */
    int saveOrUpdateBatch(@Param("tcs") List<TaxCategoryE> taxCategorys);
}
