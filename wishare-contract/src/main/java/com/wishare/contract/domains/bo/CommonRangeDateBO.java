package com.wishare.contract.domains.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author hhb
 * @describe
 * @date 2025/12/3 14:51
 */
@Data
public class CommonRangeDateBO {

    private String id;
    //费用开始日期
    private LocalDate costStartTime;
    //费用结束日期
    private LocalDate costEndTime;
    //计算比例
    private BigDecimal proportion;
}
