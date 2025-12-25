package com.wishare.contract.domains.vo.revision.income;

import com.wishare.contract.apps.fo.revision.income.PreIncomePlanDataV;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author dengjie03
 * @Description
 * @Date 2024-11-13
 */
@Data
public class PreIncomePlanV {

    @ApiModelProperty(value = "结算周期")
    private Integer splitMode;

    @ApiModelProperty("成本预估金额合计")
    private BigDecimal totalPlannedCollectionAmount;

    @ApiModelProperty(value = "成本预估分组数据")
    private List<PreIncomePlanDataV> preSettlePlanDataVList;
}
