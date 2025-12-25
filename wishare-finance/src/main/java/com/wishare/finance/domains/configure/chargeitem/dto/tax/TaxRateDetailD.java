package com.wishare.finance.domains.configure.chargeitem.dto.tax;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author xujian
 * @date 2022/8/4
 * @Description:
 */
@Getter
@Setter
public class TaxRateDetailD {

    /**
     * 税率主键id
     */
    private Long taxRateId;

    /**
     * 税率
     */
    private BigDecimal rate;

    /**
     * 税种id
     */
    private Long categoryId;

    /**
     * 税种名称
     */
    private String categoryName;

    /**
     * 税种编码
     */
    private String categoryCode;

    /**
     * 税种启用状态
     */
    private Integer categoryDisabled;

    /**
     * 计税方式 1:一般计税 2:简易计税
     */
    private String taxType;
}
