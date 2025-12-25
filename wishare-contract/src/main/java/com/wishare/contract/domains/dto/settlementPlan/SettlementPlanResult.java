package com.wishare.contract.domains.dto.settlementPlan;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author dengjie03
 * @Description
 * @Date 2024-11-13
 */
@Data
public class SettlementPlanResult {

    private Boolean success = true;

    private List<String> errorMessage = new ArrayList<>();
}
