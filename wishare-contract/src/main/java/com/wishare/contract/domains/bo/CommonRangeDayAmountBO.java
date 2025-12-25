package com.wishare.contract.domains.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author hhb
 * @describe
 * @date 2025/6/11 18:40
 */
@Data
public class CommonRangeDayAmountBO {
    private String id;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal reductionAmount;
}
