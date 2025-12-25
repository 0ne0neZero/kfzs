package com.wishare.finance.domains.configure.chargeitem.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * 税种税率聚合
 *
 * @author makejava
 * @since 2022-07-19 16:37:59
 */
@Getter
@Setter
public class TaxRateA extends TaxCategoryE {

    /**
     * 税率信息
     */
    private List<TaxRateE> taxRates;

    public void updateOrInsert(){
        super.updateOrInsert();
        if (CollectionUtils.isNotEmpty(taxRates)){
            taxRates.forEach(TaxRateE::updateOrInsert);
        }
    }

}

