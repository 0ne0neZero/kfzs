package com.wishare.finance.domains.configure.chargeitem.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.finance.domains.configure.chargeitem.entity.FinancialTaxInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * @author longhuadmin
 */
public interface FinancialTaxInfoMapper extends BaseMapper<FinancialTaxInfo> {


    List<FinancialTaxInfo> getListByTaxRateAndTaxTypeId(@Param("taxRateList") Collection<BigDecimal> taxRateList,
                                                        @Param("taxTypeId") String taxTypeId);

}
