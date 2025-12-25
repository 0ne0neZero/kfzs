package com.wishare.finance.domains.configure.chargeitem.dto.tax;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TaxRateD {

    /**
     * 税率主键id
     */
    private Long taxRateId;

    /**
     * 税率
     */
    private BigDecimal rate;

    @ApiModelProperty("税的类型")
    private String taxTypeStr = "税率";
}
