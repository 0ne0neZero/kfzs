package com.wishare.finance.domains.configure.accountbook.command;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author xujian
 * @date 2022/8/22
 * @Description: 账簿金额变更
 */
@Getter
@Setter
public class UpdateAmountCommand {

    /**
     * 账簿id
     */
    private Long accountBookId;

    /**
     * 费项id
     */
    private Long chargeItemId;

    /**
     * 资产金额
     */
    private BigDecimal assetsAmount;

    /**
     * 费用金额
     */
    private BigDecimal costAmount;

    /**
     * 负债金额
     */
    private BigDecimal liabilitiesAmount;

    /**
     * 所有者权益金额
     */
    private BigDecimal equitiesAmount;

    /**
     * 收入金额
     */
    private BigDecimal incomeAmount;

}
