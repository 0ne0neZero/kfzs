package com.wishare.contract.apps.fo.revision.pay;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Author dengjie03
 * @Description
 * @Date 2024-11-18
 */
@Data
public class ExpectedSettlementPlan {

    private LocalDate costStartTime;
    private LocalDate costEndTime;
}
