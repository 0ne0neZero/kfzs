package com.wishare.contract.domains.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author longhuadmin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementPlanBO {

    private LocalDate startTime;

    private LocalDate endTime;

    private Integer rangeUnsettledTermDate;

    public SettlementPlanBO(LocalDate startTime, LocalDate endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
