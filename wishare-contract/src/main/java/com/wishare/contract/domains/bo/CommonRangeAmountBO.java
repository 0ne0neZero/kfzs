package com.wishare.contract.domains.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hhb
 * @describe
 * @date 2025/6/9 17:13
 */
@Data
public class CommonRangeAmountBO {

    //起始月金额
    private BigDecimal startMonthAmount;
    //平均月金额
    private BigDecimal avgMonthAmount;
    //结束月金额
    private BigDecimal endMonthAmount;
}
