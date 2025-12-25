package com.wishare.contract.domains.entity.revision;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 结算明细(831新增)
 *
 * @author 龙江锋
 * @date 2023/8/24 18:11
 */
@Data
@Accessors(chain = true)
public class ContractSettlementDetailConcludeE {
    /**
     * 服务类型
     */
    private String serviceType;

    /**
     * 价款方式
     */
    private String costType;

    /**
     * 费项
     */
    private String chargeItem;

    /**
     * 税率
     */
    private String taxRate;

    /**
     * 不含税金额
     */
    private BigDecimal noTaxAmount;

    /**
     * 税额
     */
    private BigDecimal taxAmount;
}
