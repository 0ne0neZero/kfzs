package com.wishare.finance.apps.model.configure.chargeitem.fo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TaxRateInfoF implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long taxCategoryId;

    private String taxCategoryName;

    private Long taxRateId;

    private BigDecimal rate;

}
