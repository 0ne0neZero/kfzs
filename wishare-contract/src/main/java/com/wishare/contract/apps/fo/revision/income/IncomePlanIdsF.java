package com.wishare.contract.apps.fo.revision.income;

import com.wishare.contract.domains.enums.revision.PlanFxmPushType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class IncomePlanIdsF implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> planIds;

    private PlanFxmPushType pushType;

}
