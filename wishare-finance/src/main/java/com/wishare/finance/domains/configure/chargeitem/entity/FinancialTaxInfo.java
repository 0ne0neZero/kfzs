package com.wishare.finance.domains.configure.chargeitem.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author longhuadmin
 */
@Getter
@Setter
@TableName("financial_tax_info")
public class FinancialTaxInfo {

    /**
     * 主键id
     */
    @TableId
    private String id;

    /**
     * 税种id
     **/
    private String taxTypeId;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 租户id
     **/
    private String tenantId;

    /**
     * 税率
     **/
    private BigDecimal inputTaxRate;

    /**
     * 税率2
     **/
    private BigDecimal applicableTaxRate;

    /**
     * 固定税率
     **/
    private String fixedRate;

    /**
     * 启用
     **/
    private Integer enable;
}
